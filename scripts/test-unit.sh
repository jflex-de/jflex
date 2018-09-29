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
  "$MVN" -Pfastbuild install --quiet
else
  "$MVN" -Pfastbuild install
fi

logi "Run jflex examples"
logi "=================="
# Some tests invoke /bin/jflex which expects the jar in /lib
cp "$BASEDIR"/jflex/target/jflex-full-*.jar "$BASEDIR"/jflex/lib
# Exit with error in case of error (see #242)
set -x

cd "$BASEDIR"/jflex/examples

logi "Example: byacc/j"
# don't assume byacc/j is installed, just run lexer
cd byaccj; make clean; make Yylex.java; cd ..

logi "Example: cup-interpreter"
cd interpreter
make clean; make test
ant test
"$MVN" test
cd ..

logi "Example: cup-java"
cd java
make clean; make test
ant test
"$MVN" test
cd ..

logi "Example: simple"
cd simple
ant test
"$MVN" test
cd ..

logi "Example: standalone-maven"
cd standalone-maven
$MVN test
cd ..

logi "Example: cup-lcalc"
cd cup-lcalc
"$MVN" test
cd ..


logi "Example: zero-reader"
cd zero-reader; make clean; make; cd ..
set +x

cd "$CWD"
