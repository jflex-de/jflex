#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw

if [[ "$TEST_SUITE" == "unit" ]]; then
  "$MVNW" site site-deploy
fi
