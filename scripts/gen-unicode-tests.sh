#!/bin/sh
#
# Copyright 2021, Régis Décamps
# SPDX-License-Identifier: BSD-3-Clause
#

set -e

# Regenerates the checked-in Unicode tests of tests/unicode from the Unicode Character
# Database, with the generators in tests/generators. Needs google-java-format.

WS=$(git rev-parse --show-toplevel)
cd "${WS}"

scripts/mvn-install-fastbuild.sh jflex-maven-plugin
./mvnw -pl tests/framework,tests/generators,tests/unicode process-classes
# Sync everything that is generated and checked in to check for changes
rsync --archive -vm \
  --include '*.java' --include='*.output' --include='*/' --exclude='*' \
  "${WS}"/tests/unicode/target/generated-unicode-tests/ "${WS}"/tests/unicode/src/test/java/

# list of java files that changes. git ls-files to include untracked files.
java_files=$(git ls-files --modified --others --exclude-standard \
  tests/unicode/src/test/java/de/jflex/testcase/unicode | grep '\.java$' || true)
# format changed files to ignore pure formatting diff
if [ -n "$java_files" ]; then
  google-java-format -r $java_files
fi
