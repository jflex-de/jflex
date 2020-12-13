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

which ant
if [[ $? -eq 0 ]]; then
  ANT=ant
else
  ANT=echo
fi

# Exit with error in case of error (see #242)
set -e

logi "Compile, test and install all"
logi "============================="
"$BASEDIR"/scripts/mvn-install-fastbuild.sh jflex-maven-plugin

logi "Downloading ivy"
logi "==============="
cd "$BASEDIR"/jflex/examples/simple
"$ANT" download-ivy
cd ..

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
"$ANT" test
"$MAKE" test
cd ..

logi "Example: cup-java"
cd cup-java
"$MVN" test
"$ANT" test
"$MAKE" test
cd ..

logi "Example: cup-java-minijava"
cd cup-java-minijava
"$MVN" test
"$ANT" test
# make not supported
cd ..

logi "Example: cup-lcalc"
cd cup-lcalc
"$MVN" test
"$ANT" test
"$MAKE" test
cd ..

logi "Example: simple"
cd simple
"$MVN" test
"$ANT" test
"$MAKE" test
cd ..

logi "Example: standalone"
cd standalone
"$MVN" test
"$ANT" test
"$MAKE" test
cd ..

logi "Example: zero-reader"
cd zero-reader
"$MVN" test
"$ANT" test
"$MAKE" test
cd ..

cd "$CWD"
