#!/usr/bin/env sh
# Gradle wrapper launcher for Unix-like systems
# This script expects gradle/wrapper/gradle-wrapper.jar to exist in the repository.
set -e
DIRNAME="$(cd "$(dirname "$0")" && pwd)"
WRAPPER_JAR="$DIRNAME/gradle/wrapper/gradle-wrapper.jar"
if [ -f "$WRAPPER_JAR" ]; then
  exec java -jar "$WRAPPER_JAR" "$@"
else
  echo "Gradle wrapper JAR not found at $WRAPPER_JAR"
  echo "If you have Gradle installed, run: gradle wrapper"
  echo "Or generate the wrapper on another machine and add gradle/wrapper/gradle-wrapper.jar to the repo."
  exit 1
fi
