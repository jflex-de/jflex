#!/bin/sh
# Build with ant

# Multi-platform hack for:
#SCRIPT_PATH=$(dirname "$(readlink -f $0)")
CWD="$PWD"
SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd -P)"
# Provides the logi function
source "$SCRIPT_PATH"/logger.sh
# fail on error
set -e

logi "Compile with ant"
cd "$SCRIPT_PATH"/jflex; ant gettools build test; cd ..

cd "$CWD"
