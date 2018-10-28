#!/bin/bash
# Run the java-format code style
TOOLSDIR=buildtools
CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

if [[ "_${TRAVIS_JDK_VERSION}" = "_openjdk7" ]]; then
  logi "Skip google-java-format. Unsupported java version."
  exit
fi

function gjf() {
  directory=$1
  logi "Checking $directory"
  java -jar $TOOLSDIR/google-java-format.jar --dry-run --set-exit-if-changed $(find $directory -name '*.java')
}

logi "Download google-java-format"
logi "==========================="
echo "TRAVIS_JDK_VERSION=$TRAVIS_JDK_VERSION"
mkdir -p $TOOLSDIR
curl -C - -L https://github.com/google/google-java-format/releases/download/google-java-format-1.6/google-java-format-1.6-all-deps.jar -o $TOOLSDIR/google-java-format.jar

logi "Check java format"
logi "================="
java -jar $TOOLSDIR/google-java-format.jar --version
gjf cup-maven-plugin
gjf java
gjf javatests
gjf jflex
gjf jflex-maven-plugin
gjf jflex-unicode-maven-plugin
gjf testsuite/jflex-testsuite-maven-plugin
cd "$CWD"
