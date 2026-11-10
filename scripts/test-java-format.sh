#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# Check the Java code style with google-java-format. Needs JDK 21+.
#
# To reformat instead of check, run
#   ./mvnw com.spotify.fmt:fmt-maven-plugin:format

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Check java format"
logi "================="
cd "$BASEDIR"
if [[ "$CI" ]]; then
  "$MVN" --batch-mode --quiet com.spotify.fmt:fmt-maven-plugin:check
else
  "$MVN" com.spotify.fmt:fmt-maven-plugin:check
fi

logi "OK 🙌"

cd "$CWD"
