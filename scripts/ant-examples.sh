#!/bin/bash
# Run unit tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Compile, test and install all"
logi "============================="
# NB: Installs jflex in local repo
# implies: validate, compile, test, package, verify, install
if [[ "$TRAVIS" ]]; then
  # Quiet mode shows errors only.
  "$MVN" install --quiet
else
  "$MVN" install
fi

logi "Run jflex examples with ant"
logi "==========================="
# Some tests invoke /bin/jflex which expects the jar in /lib
ln "$BASEDIR"/jflex/target/jflex-full-*.jar "$BASEDIR"/jflex/lib
# Exit with error in case of error (see #242)
set -x

cd "$BASEDIR"/jflex/examples

logi "Example: byaccj"
cd byaccj
logi "(skipped)"
cd ..

logi "Example: cup-interpreter"
cd cup-interpreter
ant test
cd ..

logi "Example: cup-java"
cd cup-java
ant test
cd ..

logi "Example: simple"
cd simple
ant test
cd ..

logi "Example: standalone-maven"
cd standalone-maven
logi "(skipped)"
cd ..

logi "Example: cup-lcalc"
cd cup-lcalc
logi "(skipped)"
cd ..

logi "Example: zero-reader"
cd zero-reader
logi "(skipped)"
cd ..

set +x
cd "$CWD"
