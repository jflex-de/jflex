#!/bin/bash
# Run bazel on examples

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

logi "Start Bazel"
logi "==========="
cd jflex/examples
bazel start

logi "Build everything"
bazel build //...

logi "Test everything"
logi "==============="
bazel test //..

cd "$CWD"
