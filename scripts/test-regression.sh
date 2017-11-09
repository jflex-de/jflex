#!/bin/sh
# Runs regressions tests

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


if [[ $TRAVIS ]]; then
  # We have already installed all, so we can pass this step
  logi "Compile and install all (no tests)"
  # Travis has "`install: true` and hence jflex needs to be install in local repo
  "$MVN" install -DskipTests=true -Dmaven.javadoc.skip=true --quiet
fi

logi "Run regression test cases"
# regression test suite must run in its own directory
cd "$SCRIPT_PATH"/..
cd testsuite/testcases; "$MVN" test
cd ../..
