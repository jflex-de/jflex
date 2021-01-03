#!/bin/sh
WS=$(bazel info workspace)

# Generate BUILD files, in javatests/de/jflex/testcase/unicode/unicode_x_y/BUILD.bazel
bazel build //java/de/jflex/migration/unicodedatatest:generate
cp -rf ${WS}/bazel-bin/java/de/jflex/migration/unicodedatatest/javatests ${WS}

# Generate UnicodeAgeTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testage:generate
cp -rf ${WS}/bazel-bin/java/de/jflex/migration/unicodedatatest/testage/javatests ${WS}
# This flex specs are sourced from the bazel generator directly and should not be submitted.
find ${WS}/javatests/de/jflex/testcase/unicode -name 'UnicodeAge*.flex' -exec rm -rf {} \;
