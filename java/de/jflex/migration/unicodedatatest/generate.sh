#!/bin/sh
#
# Copyright 2021, Régis Décamps
# SPDX-License-Identifier: BSD-3-Clause
#

set -e

WS=$(git rev-parse --show-toplevel)
cd "${WS}"

scripts/mvn-install-fastbuild.sh jflex-maven-plugin
./mvnw -pl java,javatests process-classes
# Sync everything that is generated and checked in to check for changes
rsync --archive -vm \
  --include '*.java' --include='*.output' --include='*/' --exclude='*' \
  "${WS}"/javatests/target/generated-unicode-tests/javatests "${WS}"

# list of java files that changes. git ls-files to include untracked files.
java_files=$(git ls-files --modified --others --exclude-standard \
  javatests/de/jflex/testcase/unicode | grep '\.java$' || true)
# format changed files to ignore pure formatting diff
if [ -n "$java_files" ]; then
  google-java-format -r $java_files
fi
