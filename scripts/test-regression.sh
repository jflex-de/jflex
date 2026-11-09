#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# Runs regressions tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Run regression test cases"
logi "========================="
# The hand-written end-to-end tests in tests/regression: JUnit tests, and the .test cases that
# TestsuiteTest runs. Needs jflex and jflex-maven-plugin installed.
# Select .test case directories with -Dtestcases=dot,testloader
cd "$BASEDIR"
"$MVN" -pl tests/framework,tests/regression install "$@"
cd "$CWD"
