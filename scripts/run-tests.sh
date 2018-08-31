#!/bin/bash

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

echo '==============================  JAVA VERSION  =============================='
java -version
javac -version
echo '============================================================================'

# Clean environment
if [[ ! $TRAVIS ]]; then
  logi "Clean up environment"
  # Cleanup up Maven targets
  # note that Maven could fail if POM are incorrect
  find "$BASEDIR" -name target -type d -exec rm -rf {} \; || true

  # Clean up local maven repo
  # TODO: This could be changed with ~/.m2/settings.xml
  rm -rf "$HOME"/.m2/repository/de/jfex || true
fi

# Travis then runs _in parallel_ (but we do it in sequence)
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "unit" ]]; then
  "$BASEDIR"/scripts/test-unit.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "regression" ]]; then
  "$BASEDIR"/scripts/test-regression.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "ant" ]]; then
  "$BASEDIR"/scripts/ant-build.sh
fi

logi "Success"
cd "$CWD"
