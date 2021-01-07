pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes'
      label "venus-demo-${UUID.randomUUID().toString()}"
      yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: jnlp
      resources:
        requests:
          cpu: "250m"
          memory: "512Mi"
        limits:
          cpu: "250m"
          memory: "512Mi"
    - name: anvil
      image: saicoss/anvil:0.5.4
      imagePullPolicy: IfNotPresent
      command: ['cat']
      tty: true
      resources:
        requests:
          cpu: "2"
          memory: "4Gi"
        limits:
          cpu: "2"
          memory: "4Gi"
      env:
        - name: DOCKER_HOST
          value: tcp://localhost:2375
    - name: dind
      image: docker:stable-dind
      imagePullPolicy: IfNotPresent
      resources:
        requests:
          cpu: "250m"
          memory: "512Mi"
        limits:
          cpu: "250m"
          memory: "512Mi"
      securityContext:
        privileged: true
      env:
        - name: DOCKER_TLS_CERTDIR
          value: ''
      volumeMounts:
        - name: dind-storage
          mountPath: /var/lib/docker
  volumes:
    - name: dind-storage
      emptyDir: {}
"""
    }
  }
  post {
    failure {
      updateGitlabCommitStatus name: 'build', state: 'failed'
    }
    success {
      updateGitlabCommitStatus name: 'build', state: 'success'
    }
    aborted {
      updateGitlabCommitStatus name: 'build', state: 'canceled'
    }
  }
  options {
    gitLabConnection('gitlab')
    timeout(time: 1, unit: 'HOURS')
  }
  triggers {
    gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
  }
  environment {
    APP_VERSION = getAppVersion()

    CLUSTER_NAME = 'innovation-factory'
    AWS_DEFAULT_REGION = 'us-east-1'
  }

  stages {

    stage('Init (Parent)') {
      parallel {
        stage('Notify Build Started') {
          steps {
            updateGitlabCommitStatus name: 'build', state: 'running'
          }
        }
        stage('Determine Deploy Env') {
          steps {
            script {
              if (
              (env.BRANCH_NAME ==~ /^feature\/.*$/) ||
                (env.BRANCH_NAME ==~ /^develop$/)
              ) {
                echo "Deploy env is 'dev'"
                env.DEPLOY_ENV = "dev"
              } else if (
              (env.BRANCH_NAME ==~ /^release\/.*$/) ||
                (env.BRANCH_NAME ==~ /^hotfix\/.*$/) ||
                (env.BRANCH_NAME ==~ /^main$/)
              ) {
                echo "Deploy env is 'test'"
                env.DEPLOY_ENV = "test"
              } else if (
              (env.TAG_NAME != null && env.TAG_NAME.length() > 0)
              ) {
                echo "Deploy env is 'prod'"
                env.DEPLOY_ENV = "prod"
              } else {
                error 'The name you used for your branch is not supported by this pipeline. Please use one of the following branch names: "feature/*", "release/*", "hotfix/*"'
              }
            }
          }
        }
        stage('Initialize Codebase') {
          steps {
            container('anvil') {
              // - Change the gradle dist to bin which is a lighter download
              // - Change the max_old_space_size of webpack for faster builds
              // - run installDeps task
              sh '''
                sed -i '/^distributionUrl.*/s/all/bin/' gradle/wrapper/gradle-wrapper.properties \
                  && sed -i 's/--max_old_space_size=[1-9].* /--max_old_space_size=5500 /' package.json \
                  && task installDeps
              '''
            }
          }
        }
      }
    }

    stage('Validate (Parent)') {
      parallel {
        stage('Validate Pre-Commit') {
          steps {
            container('anvil') {
              sh '''
                SKIP=no-commit-to-branch task validatePreCommitHooks
              '''
            }
          }
        }
        stage('Validate Codebase') {
          steps {
            container('anvil') {
              sh '''
                task validateGradle
              '''
            }
          }
        }
      }
    }

    stage('Build (Parent)') {
      parallel {
        stage('Build Container') {
          steps {
            container('anvil') {
              sh '''
                task build SKIP_NPM_INSTALL=true APP_VERSION=${APP_VERSION}
              '''
            }
          }
        }
        stage('zPlaceholder') {
          steps {
            echo "Placeholder stage to make BlueOcean format the parallel stages correctly"
          }
        }
      }
    }

    stage('Test (Parent)') {
      // parallel {
      stages {
        stage('Run Unit Tests') {
          steps {
            container('anvil') {
              sh '''
                task test USE_CACHE=true
              '''
            }
          }
        }
        stage('Run Integration Tests') {
          steps {
            container('anvil') {
              sh '''
                task integrationTest USE_CACHE=true
              '''
            }
          }
        }
      }
      post {
        always {
          junit 'build/test-results/**/*.xml'
        }
      }
    }
  }
}

def getShortSha() {
  return sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
}

def getAppVersion() {

  if (env.TAG_NAME != null && env.TAG_NAME.length() > 0) {
    return env.TAG_NAME
  } else {
    return (env.BRANCH_NAME).replace("/", "_") + "-" + getShortSha()
  }
}

def shouldDeployDev() {
  result = sh(script: "git log -1 | grep '.*\\[deploy-dev\\].*'", returnStatus: true)
  if (result == 0) {
    return "true"
  } else {
    return "false"
  }
}
