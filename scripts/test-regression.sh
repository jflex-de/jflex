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
# The test cases in testsuite/testcases are run by TestsuiteTest in javatests.
# Needs jflex and jflex-maven-plugin installed.
# Select case directories with -Dtestcases=dot,unicode-scripts
cd "$BASEDIR"
"$MVN" -pl java,javatests install -Dtest=TestsuiteTest -Dsurefire.failIfNoSpecifiedTests=false "$@"
cd "$CWD"
