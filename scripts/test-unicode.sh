#!/bin/bash
#
# Copyright 2026, Gerwin Klein
# SPDX-License-Identifier: BSD-3-Clause
#
# Runs the Unicode tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Run Unicode tests"
logi "================="
# Tests for Unicode support and of the Unicode generators in tests/unicode. Downloads the
# Unicode Character Database (cached in ~/.m2). Needs jflex and jflex-maven-plugin installed. Select
# .test case directories with e.g. -Dtestcases=unicode-scripts,unicode-word-break
cd "$BASEDIR"
"$MVN" -pl tests/framework,tests/generators,tests/unicode install "$@"
cd "$CWD"
