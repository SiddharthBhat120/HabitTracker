#!/bin/sh
# Gradle wrapper script
DIRNAME=$(dirname "$0")
exec "$DIRNAME/gradle/wrapper/gradle-wrapper.jar" "$@"
