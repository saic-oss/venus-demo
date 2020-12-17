pipeline {
  agent {
    kubernetes {
      //cloud 'kubernetes'
      label "sif-full-cicd-demo-${UUID.randomUUID().toString()}"
      yaml """
apiVersion: v1
kind: Pod
spec:
  imagePullSecrets:
    - name: gitlab-registry-access
  containers:
    - name: jnlp
      resources:
        requests:
          cpu: "166m"
          memory: "2666Mi"
        limits:
          cpu: "10000m"
          memory: "8000Mi"
    - name: anvil
      image: registry.saicinnovationfactory.com/devsecops/docker-images/anvil:0.4.0
      imagePullPolicy: Always
      command: ['cat']
      tty: true
      resources:
        requests:
          cpu: "166m"
          memory: "2666Mi"
        limits:
          cpu: "10000m"
          memory: "8000Mi"
      env:
        - name: DOCKER_HOST
          value: tcp://localhost:2375
    - name: dind
      image: docker:stable-dind
      imagePullPolicy: Always
      resources:
        requests:
          cpu: "166m"
          memory: "2666Mi"
        limits:
          cpu: "10000m"
          memory: "8000Mi"
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
    gitLabConnection('code.saicinnovationfactory.com')
    timeout(time: 1, unit: 'HOURS')
  }
  triggers {
    gitlab(triggerOnPush: true, triggerOnMergeRequest: true, branchFilterType: 'All')
  }
  environment {
    SHORT_SHA = getShortSha()
    APP_VERSION = getAppVersion()
    SHOULD_DEPLOY_DEV = shouldDeployDev()

    CLUSTER_NAME = 'innovation-factory'
    AWS_DEFAULT_REGION = 'us-east-1'

    // Container settings
    GROUP_NAME = 'Venus'
    IMAGE_NAME = 'Venus-demo'
    FULLY_QUALIFIED_IMAGE_NAME = "${DOCKER_REGISTRY}/${GROUP_NAME}/${IMAGE_NAME}"
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
                (env.BRANCH_NAME ==~ /^master$/)
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
        stage('Set Env Vars') {
          steps {
            withCredentials([usernamePassword(credentialsId: 'sa-sif-jenkins-creds', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
              container('anvil') {
                // Get the AWS account number and set it as an environment variable. We'll need it later
                script {
                  env.AWS_ACCOUNT_ID = sh(script: 'aws sts get-caller-identity | jq -r .Account', returnStdout: true).trim()
                }
              }
            }
          }
        }
        stage('Initialize Codebase') {
          steps {
            withCredentials([usernamePassword(credentialsId: 'sa-sif-jenkins-creds', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
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
        stage('Login Docker') {
          steps {
            withCredentials([usernamePassword(credentialsId: 'gitlab-creds', passwordVariable: 'REGISTRY_PASSWORD', usernameVariable: 'REGISTRY_USERNAME')]) {
              // Get logged into the docker registry that we are using
              container('anvil') {
                sh '''
                  task dockerLogin URL=${DOCKER_REGISTRY} USERNAME=${REGISTRY_USERNAME} PASSWORD=${REGISTRY_PASSWORD}
                '''
              }
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
            withCredentials([usernamePassword(credentialsId: 'gitlab-creds', passwordVariable: 'REGISTRY_PASSWORD', usernameVariable: 'REGISTRY_USERNAME')]) {
              container('anvil') {
                sh '''
                  task build SKIP_NPM_INSTALL=true APP_VERSION=${APP_VERSION} \
                    dockerTag TAG_TO_CREATE=${FULLY_QUALIFIED_IMAGE_NAME}:sca-${SHORT_SHA} \
                    dockerPush TAG_TO_PUSH=${FULLY_QUALIFIED_IMAGE_NAME}:sca-${SHORT_SHA}
                '''
              }
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

    stage('Test/Secure-prep (Parent)') {
      parallel {
        stage('Start E2E Test Env') {
          steps {
            container('anvil') {
              sh '''
                task startSelenium
              '''
            }
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
        stage('Run E2E Tests') {
          steps {
            container('anvil') {
              // The app needs time to spin up, or the first few tests will always fail.
              retry(3) {
                sh '''
                  task cucumberSmokeTest USE_CACHE=true
                '''
              }
              retry(2) {
                sh '''
                  task cucumberE2ETest USE_CACHE=true
                '''
              }
            }
          }
          post {
            always {
              container('anvil') {
                sh '''
                  task stopSelenium
                '''
              }
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

    stage('Secure (Parent)') {
      steps {
        echo "Placeholder stage to make BlueOcean format the parallel stages correctly"
      }
    }

    stage('Deliver (Parent)') {
      steps{
        when {
          anyOf {
            branch 'release/*'
            branch 'hotfix/*'
            branch 'develop'
            branch 'master'
            buildingTag()
            allOf {
              branch 'feature/*'
              environment name: 'SHOULD_DEPLOY_DEV', value: 'true'
            }
          }
        }
        stage('Deliver - zPlaceholder') {
          steps {
            echo "Placeholder stage to make BlueOcean format the parallel stages correctly"
          }
        }
      }
    }

    stage('Deploy (Parent)') {
      steps{
        when {
          anyOf {
            branch 'release/*'
            branch 'hotfix/*'
            branch 'develop'
            branch 'master'
            buildingTag()
            allOf {
              branch 'feature/*'
              environment name: 'SHOULD_DEPLOY_DEV', value: 'true'
            }
          }
        }
        stage('Deploy - zPlaceholder') {
          steps {
            echo "Placeholder stage to make BlueOcean format the parallel stages correctly"
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
