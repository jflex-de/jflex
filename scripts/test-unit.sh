#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# Run unit tests

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Compile, test and install all"
logi "============================="
# NB: Installs jflex in local repo
# implies: validate, compile, test, package, verify, install
# The test modules under tests/ have their own CI jobs.
if [[ "$CI" ]]; then
  # Quiet mode shows errors only.
  "$MVN" install --quiet -pl '!tests/framework,!tests/generators,!tests/regression,!tests/unicode'
else
  "$MVN" install -pl '!tests/framework,!tests/generators,!tests/regression,!tests/unicode'
fi

cd "$CWD"
