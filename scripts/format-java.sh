#!/bin/bash
#
# Copyright 2026, Gerwin Klein
# SPDX-License-Identifier: BSD-3-Clause
#
# Format all Java sources with google-java-format. Needs JDK 21+.
#
# Extra arguments go to Maven, e.g. to format one module only:
#   scripts/format-java.sh -pl jflex
#
# To check instead of reformat, run scripts/test-java-format.sh

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Format java sources"
cd "$BASEDIR"
"$MVN" --quiet com.spotify.fmt:fmt-maven-plugin:format "$@"

logi "OK"
