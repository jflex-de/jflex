#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw

if [[ -z "$TEST_SUITE" || "$TEST_SUITE" == "unit" ]]; then
  logi "Build Maven site"
  "$MVN" site
  "$MVN" site:stage
fi
