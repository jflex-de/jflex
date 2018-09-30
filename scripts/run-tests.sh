#!/bin/bash

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

if [[ $TRAVIS ]]; then
  loge "This script is only for manual invocation"
  exit 1
fi

# Clean environment
"$BASEDIR"/scripts/clean.sh

# Travis then runs _in parallel_ (but we do it in sequence)
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "java-format" ]]; then
  "$BASEDIR"/scripts/test-java-format.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "unit" ]]; then
  "$BASEDIR"/scripts/test-unit.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "regression" ]]; then
  "$BASEDIR"/scripts/test-regression.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "ant" ]]; then
  "$BASEDIR"/scripts/ant-build.sh
  "$BASEDIR"/scripts/test-examples.sh
fi
if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "bazel" ]]; then
  "$BASEDIR"/scripts/bazel.sh
fi

logi "Success"
cd "$CWD"
