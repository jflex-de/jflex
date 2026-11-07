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
# javatests needs java, which is not installed at this point.
"$MVN" site -pl '!java,!javatests'
"$MVN" site:stage -pl '!java,!javatests'
