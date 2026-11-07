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
# java and javatests have their own CI job.
if [[ "$CI" ]]; then
  # Quiet mode shows errors only.
  "$MVN" install --quiet -pl '!java,!javatests'
else
  "$MVN" install -pl '!java,!javatests'
fi

cd "$CWD"
