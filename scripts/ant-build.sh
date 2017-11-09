#!/bin/bash
# Build with ant

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

logi "Compile with ant"
cd "$BASEDIR"/jflex; ant gettools build test; cd ..

cd "$CWD"
