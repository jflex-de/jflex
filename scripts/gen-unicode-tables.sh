#!/bin/bash
#
# Copyright 2026, Gerwin Klein
# SPDX-License-Identifier: BSD-3-Clause
#
# Generates UnicodeProperties.java and data/Unicode_x_y.java in
# jflex/src/main/java/jflex/core/unicode from the Unicode Character Database.
# See tests/generators/src/main/java/de/jflex/ucd_generator/README.md.
#
# Needs JDK 21+, for google-java-format. The generator gives the same result on
# all JDKs.

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw
# fail on error
set -e

GENERATED="$BASEDIR"/tests/unicode/target/generated-unicode-tables
UNICODE="$BASEDIR"/jflex/src/main/java/jflex/core/unicode

# Fail before the sources are overwritten; fmt-maven-plugin's own error is obscure.
JAVA="${JAVA_HOME:+$JAVA_HOME/bin/}java"
if ! "$JAVA" -version 2>&1 | grep -qE 'version "(2[1-9]|[3-9][0-9]|[1-9][0-9]{2})[".]'; then
  echo "Needs JDK 21+ for google-java-format, found:" >&2
  "$JAVA" -version >&2
  exit 1
fi

cd "$BASEDIR"
logi "Build and install JFlex and jflex-maven-plugin (-P fastbuild)"
"$MVN" -Pfastbuild -pl .,cup-maven-plugin,jflex,jflex-maven-plugin install

logi "Generate Unicode tables"
"$MVN" -Pgen-unicode-tables -pl tests/framework,tests/generators,tests/unicode process-classes

logi "Update and format sources"
cp "$GENERATED"/UnicodeProperties.java "$UNICODE"
cp "$GENERATED"/Unicode_*.java "$UNICODE"/data
"$MVN" -pl jflex com.spotify.fmt:fmt-maven-plugin:format
