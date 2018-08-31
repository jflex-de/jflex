#!/bin/bash

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh

logi "Clean the environment"
logi "====================="

# Cleanup up Maven targets
# note that Maven could fail if POM are incorrect
find "$BASEDIR" -name target -type d -exec rm -rf {} \; || true

# Clean up local maven repo
find ~/.m2/repository/de/jflex -name '*-SNAPSHOT.*' -exec rm -rf {} \; || true
