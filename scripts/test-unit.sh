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
if [[ $TRAVIS ]]; then
  # Quiet mode shows errors only.
  "$MVN" install --quiet
else
  "$MVN" install
fi

logi "Run jflex examples"
logi "=================="
# Some tests invoke /bin/jflex which expects the jar in /lib
cp "$BASEDIR"/jflex/target/jflex-full-*.jar "$BASEDIR"/jflex/lib
# Exit with error in case of error (see #242)
set -x

cd "$BASEDIR"/jflex/examples
logi "Example: simple-maven"
cd simple-maven; $MVN test; cd ..
logi "Example: standalone-maven"
# Note that mvn is a likely to be a different and older version of Maven.
cd standalone-maven; $MVN test; cd ..
logi "Example: cup-maven"
cd cup-maven; $MVN test; cd ..

logi "Example: byacc/j"
# don't assume byacc/j is installed, just run lexer
cd byaccj; make clean; make Yylex.java; cd ..
logi "Example: interpreter"
cd interpreter; make clean; make; cd ..
logi "Example: java"
cd java; make clean; make; cd ..
logi "Example: zero-reader"
cd zero-reader; make clean; make; cd ..
set +x

cd "$CWD"
