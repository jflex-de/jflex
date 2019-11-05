#!/bin/bash
# Run unit tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# Alias for Make or noop
which make
if [[ $? -eq 0 ]]; then
  MAKE=make
else
  MAKE=echo
fi
# Exit with error in case of error (see #242)
set -e

logi "Compile, test and install all"
logi "============================="
"$BASEDIR"/scripts/mvn-install-fastbuild.sh jflex-maven-plugin
# Some tests invoke /bin/jflex which expects the jar in /lib
cp "$BASEDIR"/jflex/target/jflex-full-*.jar "$BASEDIR"/jflex/lib

cd "$BASEDIR"/jflex/examples

logi "Run jflex examples"
logi "=================="

logi "Example: byaccj"
cd byaccj
# Maven not supported
# ant not supported
# don't assume byacc/j is installed, just run lexer
"$MAKE" Yylex.java
cd ..

logi "Example: cup-interpreter"
cd cup-interpreter
"$MVN" test
# TODO(#384) Fix ant test
# ant test
ant build
"$MAKE" test
cd ..

logi "Example: cup-java"
cd cup-java
"$MVN" test
# Fix ant #384
#ant test
ant build
ant test
"$MAKE" test
cd ..

logi "Example: cup-lcalc"
cd cup-lcalc
"$MVN" test
# ant test
# make test
cd ..

logi "Example: simple"
cd simple
"$MVN" test
# Fix ant
#ant test
ant build
# make test
cd ..

logi "Example: standalone"
cd standalone
"$MVN" test
# ant test
# make test
cd ..

logi "Example: zero-reader"
cd zero-reader
"$MVN" test
# ant test
"$MAKE" test
cd ..

cd "$CWD"
