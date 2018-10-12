#!/bin/bash
# Runs regressions tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Run regression test cases"
logi "========================="
# regression test suite must run in its own directory
cd "$BASEDIR"/testsuite/testcases; "$MVN" test
cd "$CWD"
