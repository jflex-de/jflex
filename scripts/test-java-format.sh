#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# Run the java-format code style

TOOLSDIR=buildtools
CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

# Version of Google java format
# https://github.com/google/google-java-format/releases
VERSION_GJF=1.15.0

function gjf() {
  directory=$1
  logi "Checking $directory"
  java -jar $TOOLSDIR/google-java-format-${VERSION_GJF}.jar --dry-run --set-exit-if-changed $(find $directory -type f \( -name '*.java' -and -not -name 'Unicode_*.java' -and -not -name 'UnicodeProperties.java' \) )
}

if [[ ! -f ${TOOLSDIR}/google-java-format-${VERSION_GJF}.jar ]]; then
    logi "Download tools"
    logi "=============="
    mkdir -p $TOOLSDIR
    curl -C - -L https://github.com/google/google-java-format/releases/download/v${VERSION_GJF}/google-java-format-${VERSION_GJF}-all-deps.jar -o ${TOOLSDIR}/google-java-format-${VERSION_GJF}.jar
fi

logi "Check java format"
logi "================="
java -jar $TOOLSDIR/google-java-format-${VERSION_GJF}.jar --version
gjf cup-maven-plugin
gjf java
gjf javatests
gjf jflex
gjf jflex-maven-plugin
gjf testsuite/jflex-testsuite-maven-plugin

logi "OK 🙌"

cd "$CWD"
