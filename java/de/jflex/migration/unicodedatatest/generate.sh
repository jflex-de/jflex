#!/bin/sh
set -e

WS=$(bazel info workspace)

# Generate BUILD files, in javatests/de/jflex/testcase/unicode/unicode_x_y/BUILD.bazel
bazel build //java/de/jflex/migration/unicodedatatest:generate

# Generate UnicodeAgeTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testage:generate
# Generate UnicodeBlocksTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testblock:generate
# Generate UnicodeCaselessTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testcaseless:generate
# Generate UnicodeCompatibilityPropertiesTest_alnum_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testcompat:generate
# Generate UnicodeDigitTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testdigit:generate
# Generate UnicodeEmojiTest_x_y.java
bazel build //java/de/jflex/migration/unicodedatatest/testemoji:generate
# Generate derived core properties
bazel build //java/de/jflex/migration/unicodedatatest/testderivedcoreprop:generate

# Exclude the .flex files are they are sourced from the bazel target directly.
rsync --archive -vm --chmod=Fa=r \
  --include '*.java' --include='*.output' --include='*/' --exclude='*' \
  "${WS}"/bazel-bin/java/de/jflex/migration/unicodedatatest/*/javatests "${WS}"
rsync --archive -vm --chmod=Fa=r \
  --include 'BUILD.bazel' --include='*/' --exclude='*' \
  "${WS}"/bazel-bin/java/de/jflex/migration/unicodedatatest/javatests "${WS}"

for f in $(git diff --name-only | grep '.java$');	do
  chmod u+w "$f"
  google-java-format -r "$f"
done
for f in $(find "${WS}"/javatests/de/jflex/testcase/unicode -name BUILD.bazel); do
  chmod u+w "$f"
  buildifier -r "$f"
  chmod u-w "$f"
done

chmod -R u-wx javatests/de/jflex/testcase/unicode/unicode_*/*.*
