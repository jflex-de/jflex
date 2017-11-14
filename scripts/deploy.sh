#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw

if [[ -z "$CI" || "$TEST_SUITE" == "unit" ]]; then
  logi "Buil Maven site"
  "$MVN" site
  logi "Deploy Maven site"
  "$MVN" site-deploy
fi
