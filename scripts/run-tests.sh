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
  "$BASEDIR"/scripts/clean.sh
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
