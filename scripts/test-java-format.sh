#!/bin/bash
# Run the java-format code style

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

logi "Download google-java-format"
logi "==========================="
mkdir lib
curl -L https://github.com/google/google-java-format/releases/download/google-java-format-1.6/google-java-format-1.6-all-deps.jar -o lib/google-java-format.jar

logi "Check java format"
logi "================="

function gjf() {
  directory=$1
  java -jar lib/google-java-format.jar --dry-run --set-exit-if-changed $(find $directory -name '*.java')  
}

gjf cup-maven-plugin
gjf jflex
gjf jfkex-maven-plugin
gjf jflex-unicode-maven-plugin
gjf testsuite/jflex-testsuite-maven-plugin
