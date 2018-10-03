#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw

QUIET=""
if [[ "$TRAVIS" ]]; then
  # Quiet mode shows errors only.
  QUIET="--quiet"
fi

# TODO(regisd) Deifne the list of packages via the profile
logi "Build and install JFlex only (-P fastbuild)"
"$MVN" ${QUIET} -pl cup/cup,cup/cup_runtime,cup-maven-plugin,.,jflex install
