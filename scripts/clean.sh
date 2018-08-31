#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh

logi "Clean the environment"
logi "====================="

# Clean up all Maven targets
# note that Maven could fail if POM are incorrect
find "$BASEDIR" -name target -type d -exec rm -rf {} \; || true

# Clean up SNAPSHOT in ant lib
find "$BASEDIR/jflex/lib" -name '*-SNAPSHOT.*' -exec rm -rf {} \; || true

# Clean up SNAPSHOT in local maven repo
find ~/.m2/repository/de/jflex -type d -name '*-SNAPSHOT' -exec rm -rf {} \; || true
find ~/.m2/repository/de/jflex -name maven-metadata-local.xml -exec rm -rf {} \; || true
