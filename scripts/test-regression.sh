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

if [[ $CI ]]; then
  "$BASEDIR"/scripts/mvn-install-fastbuild.sh
  # TODO(regisd) Why do we need cup? Isn't cup_runtime sufficient?
  "$MVN" -Pfastbuild -pl cup,jflex-maven-plugin,testsuite/jflex-testsuite-maven-plugin install
else
  # On local dev, ./run-tests has already installed all, so we can skip installation
  echo
fi


logi "Run regression test cases"
logi "========================="
# regression test suite must run in its own directory
cd "$BASEDIR"/testsuite/testcases; "$MVN" test
cd "$CWD"
