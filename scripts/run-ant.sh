#!/bin/bash

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "ant" ]]; then
  "$BASEDIR"/scripts/ant-build.sh
  "$BASEDIR"/scripts/test-examples.sh
fi

logi "Success"
cd "$CWD"
