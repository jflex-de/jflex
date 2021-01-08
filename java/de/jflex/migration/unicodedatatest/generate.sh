#!/bin/sh
set -e

WS=$(bazel info workspace)

# Generate BUILD files, in javatests/de/jflex/testcase/unicode/unicode_x_y/BUILD.bazel
bazel build //java/de/jflex/migration/unicodedatatest:generate
cp -rf "${WS}"/bazel-bin/java/de/jflex/migration/unicodedatatest/javatests "${WS}"
for f in $(find "${WS}"/javatests/de/jflex/testcase/unicode -name BUILD.bazel); do
  chmod u+w "$f"
  buildifier -r "$f"
done

# Generate UnicodeAgeTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testage:generate
# Generate UnicodeBlocksTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testblock:generate
# Generate UnicodeCaselessTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testcaseless:generate
# Generate UnicodeCompatibilityPropertiesTest_alnum_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testcompat:generate

# Exclude the .flex files are they are sourced from the bazel target directly.
rsync --archive --exclude '**/*.flex' \
  "${WS}"/bazel-bin/java/de/jflex/migration/unicodedatatest/*/javatests "${WS}"

for f in $(git diff --name-only | grep '.java$');	do
  chmod u+w "$f"
  google-java-format -r "$f"
done
