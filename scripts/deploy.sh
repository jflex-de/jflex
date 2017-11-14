#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw

if [[ -z "$CI" || "$TEST_SUITE" == "unit" ]]; then
  "$MVN" site site-deploy
fi
