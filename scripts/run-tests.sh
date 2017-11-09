#!/bin/bash

# Multi-platform hack for:
#SCRIPT_PATH=$(dirname "$(readlink -f $0)")
CWD="$PWD"
SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd -P)"
# Provides the logi function
source "$SCRIPT_PATH"/logger.sh
# Maven executable
MVN="$SCRIPT_PATH"/../mvnw
# fail on error
set -e

# Clean environment
if [[ ! $TRAVIS ]]; then
  cd "$SCRIPT_PATH"/..
  logi "Clean up environment"
  # Cleanup up Maven targets
  # note that mvn could fail if POM are incorrect
  find . -name target -type d -exec rm -rf {} \; || true

  # Clean up local maven repo
  # TODO: This could be changed with ~/.m2/settings.xml
  rm -rf "$HOME"/.m2/repository/de/jfex || true

  logi "Remove jflex.jar in lib directory"
  rm "$SCRIPT_PATH"/../jflex/lib/jflex-*.jar || true
fi

logi "Powered by Maven wrapper"
"$MVN" --version

# Travis then runs _in parallel_ (but we do it in sequence)
"$SCRIPT_PATH"/test-unit.sh
"$SCRIPT_PATH"/test-regression.sh

logi "Success"
cd "$CWD"
