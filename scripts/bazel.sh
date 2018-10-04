#!/bin/bash
# Run bazel on examples

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

if [[ $TRAVIS ]]; then
  BAZEL="bazel --bazelrc=$TRAVIS_BUILD_DIR/.travis.bazelrc --output_user_root=${HOME}/__bazel_travis_root__ --output_base=${HOME}/__bazel_output_base__"
else
  BAZEL='bazel'
fi

logi "Copy JFlex jar in //third_party"
logi "==============================="
# NB This will fail if there are multiple versions of the jflex jar.
cp "$BASEDIR"/jflex/target/jflex-1.*.jar  "$BASEDIR"/jflex/examples/third_party/de/jflex/jflex.jar
cp "$BASEDIR"/cup/cup_runtime/target/cup_runtime-*.jar  "$BASEDIR"/jflex/examples/third_party/de/jflex/cup_runtime.jar

logi "Start Bazel"
logi "==========="
cd "$BASEDIR"/jflex/examples
$BAZEL info

logi "Build everything"
logi "================"
$BAZEL build //...

logi "Test everything"
logi "==============="
$BAZEL test //...

if [[ -n "$CI" ]]; then
  logi "Shutdown"
  logi "========"
  $BAZEL shutdown
fi
logi "Done ✅"
cd "$CWD"
