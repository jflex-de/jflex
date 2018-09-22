#!/bin/bash
# Build with ant

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

logi "Compile with ant"
cd "$BASEDIR"/jflex
ant gettools build test
cd ..

# There are also a few examples made with ant
cd "$BASEDIR"/jflex/examples/
cd cup-interpreter
ant compile
cd ..

# Return to the user Initial directory
cd "$CWD"
