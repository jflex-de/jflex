#!/bin/bash
# Run bazel on examples

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

logi "Install Bazel"
logi "============="
mkdir -p tools
curl -L https://github.com/bazelbuild/bazel/releases/download/0.16.1/bazel_0.16.1-linux-x86_64.deb -o tools/bazel-0.16.1.deb
sudo apt-get install ./tools/bazel-0.16.1.deb

logi "Run Bazel tests on examples"
logi "==========================="
cd jflex/examples
bazel test /..

cd "$CWD"
