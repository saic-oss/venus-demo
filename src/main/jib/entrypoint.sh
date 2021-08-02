#!/bin/sh

echo "The application will start in ${JHIPSTER_SLEEP}s..." && sleep "${JHIPSTER_SLEEP}"

# Wrapping ${JAVA_OPTS} in quotes breaks the image such that it won't start properly
# shellcheck disable=SC2086
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "com.saic.demos.VenusApp"  "$@"
