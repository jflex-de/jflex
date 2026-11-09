#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw

logi "Build Maven site"
# The test modules under tests/ depend on each other, and are not installed at this point.
"$MVN" site -pl '!tests/framework,!tests/generators,!tests/regression,!tests/unicode'
"$MVN" site:stage -pl '!tests/framework,!tests/generators,!tests/regression,!tests/unicode'
