#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
MVN="$BASEDIR"/mvnw

logi "Build sources jar"
"$MVN" source:aggregate
