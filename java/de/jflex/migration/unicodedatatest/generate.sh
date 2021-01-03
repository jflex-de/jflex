#!/bin/sh
WS=$(bazel info workspace)
bazel build //java/de/jflex/migration/unicodedatatest:generate
cp -rf ${WS}/bazel-bin/java/de/jflex/migration/unicodedatatest/javatests ${WS}

bazel build //java/de/jflex/migration/unicodedatatest/testage:generate
cp -rf ${WS}/bazel-bin/java/de/jflex/migration/unicodedatatest/testage/javatests ${WS}
