#!/bin/bash

EXTRA_PROJECTS=$1

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw

QUIET=""
if [[ "$TRAVIS" ]]; then
  # Quiet mode shows errors only.
  QUIET="--quiet"
fi

# TODO(regisd) Define the list of packages via the profile
logi "Build and install JFlex only (-P fastbuild)"
"$MVN" ${QUIET} -Pfastbuild -pl cup,cup/cup,cup/cup_runtime,cup-maven-plugin,.,jflex install

if [[ -n "$EXTRA_PROJECTS" ]]; then
  "$MVN" ${QUIET} -Pfastbuild -pl "$EXTRA_PROJECTS" install
fi
