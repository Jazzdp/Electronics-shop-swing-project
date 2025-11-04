#!/usr/bin/env bash
# Lightweight Gradle runner for Unix-like systems: downloads Gradle distribution if not present and runs it.
set -e
GRADLE_VERSION=8.4.1
WRAPPER_DIR="$HOME/.gradle/wrapper/gradle-$GRADLE_VERSION"
GRADLE_BIN="$WRAPPER_DIR/bin/gradle"

if [ ! -x "$GRADLE_BIN" ]; then
  echo "Gradle not found locally. Downloading Gradle $GRADLE_VERSION to $WRAPPER_DIR ..."
  ZIPFILE="/tmp/gradle-$GRADLE_VERSION-bin.zip"
  if command -v curl >/dev/null 2>&1; then
    curl -fLo "$ZIPFILE" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ZIPFILE" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  else
    echo "curl or wget required to download Gradle. Install one and try again."
    exit 1
  fi
  mkdir -p "$WRAPPER_DIR"
  unzip -q "$ZIPFILE" -d "/tmp/gradle_unpack"
  rsync -a "/tmp/gradle_unpack/gradle-$GRADLE_VERSION/" "$WRAPPER_DIR/"
  rm -rf "/tmp/gradle_unpack" "$ZIPFILE"
fi

exec "$GRADLE_BIN" "$@"
