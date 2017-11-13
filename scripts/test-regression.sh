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

if [[ $TRAVIS ]]; then
  logi "Compile and install all (no tests)"
  # Travis has "`install: true` and hence jflex needs to be install in local repo
  "$MVN" install -Pfastbuild --quiet
else
  # On local dev, ./run-tests has already installed all, so we can skip installation
  echo
fi


logi "Run regression test cases"
# regression test suite must run in its own directory
cd "$BASEDIR"/testsuite/testcases; "$MVN" test
cd "$CWD"
