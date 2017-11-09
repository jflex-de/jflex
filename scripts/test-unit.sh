#!/bin/sh
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
# NB: Installs jflex in local repo
# implies: validate, compile, test, package, verify, install
if [[ $TRAVIS ]]; then
  # Quiet mode shows errors only.
  "$MVN" install --quiet
else
  "$MVN" install
fi

logi "Run jflex examples"
# Some scripts invoke jflex/bin
cp "$BASEDIR"/jflex/target/jflex-*.jar "$BASEDIR"/jflex/lib
set -x
# Each line must end with the test command to make the script exit
# in case of error (see #242)
cd "$BASEDIR"/jflex/examples
cd simple-maven; mvn test; cd ..
# Note that mvn is a likely to be a different and older version of Maven.
cd standalone-maven; mvn test; cd ..
# don't assume byacc/j is installed, just run lexer
cd byaccj; make clean; make Yylex.java; cd ..
cd cup; make clean; make; cd ..
cd interpreter; make clean; make; cd ..
cd java; make clean; make; cd ..
cd zero-reader; make clean; make; cd ..
set +x

cd "$CWD"
