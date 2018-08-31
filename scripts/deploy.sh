#!/bin/bash

echo "Starting deploy script"

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
echo "BASEDIR=$BASEDIR"
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw

logi "Deploying..."
if [[ -z "$CI" || "$TEST_SUITE" == "unit" ]]; then
  logi "Build Maven site"
  echo "$MVN" site
  logi "Deploy Maven site"
  echo "$MVN" site-deploy
fi
