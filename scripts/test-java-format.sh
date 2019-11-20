#!/bin/bash
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
VERSION_GJF=1.7
# Version of Buildifier
VERSION_BZL_BUILDTOOLS=0.29.0

if [[ "_${TRAVIS_JDK_VERSION_GJF}" = "_openjdk7" ]]; then
  logi "Skip google-java-format. Unsupported java version."
  exit
fi

function gjf() {
  directory=$1
  logi "Checking $directory"
  java -jar $TOOLSDIR/google-java-format-${VERSION_GJF}.jar --dry-run --set-exit-if-changed $(find $directory -name '*.java')
}

logi "Download tools"
logi "=============="
echo "TRAVIS_JDK_VERSION_GJF=$TRAVIS_JDK_VERSION_GJF"
mkdir -p $TOOLSDIR
curl -C - -L https://github.com/google/google-java-format/releases/download/google-java-format-${VERSION_GJF}/google-java-format-${VERSION_GJF}-all-deps.jar -o ${TOOLSDIR}/google-java-format-${VERSION_GJF}.jar
curl -C - -L https://github.com/bazelbuild/buildtools/releases/download/${VERSION_BZL_BUILDTOOLS}/buildifier -o ${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS}
chmod u+x ${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS}

logi "Check java format"
logi "================="
java -jar $TOOLSDIR/google-java-format-${VERSION_GJF}.jar --version
gjf cup-maven-plugin
gjf java
gjf javatests
gjf jflex
gjf jflex-maven-plugin
gjf jflex-unicode-maven-plugin
gjf testsuite/jflex-testsuite-maven-plugin

logi "Check Starlark (Bazel) format"
logi "============================="
${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS} -version
${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS} -mode=check -r=true .
logi "OK 🙌"

cd "$CWD"
