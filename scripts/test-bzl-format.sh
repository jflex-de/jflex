#!/bin/bash
# Run the java-format code style
TOOLSDIR=buildtools
CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

# Version of Buildifier
VERSION_BZL_BUILDTOOLS=0.29.0

if [[ ! -z "$(find . -iname BUILD)" ]]; then
  loge "Found BUILD file"
  loge "$(find . -iname BUILD)"
  echo
  logi "Name the file BUILD.bazel for Bazel build files, and add do not submit build directory"
  exit 1
fi

if [[ ! -f ${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS} ]]; then
    logi "Download tools"
    logi "=============="
    mkdir -p $TOOLSDIR
    curl -C - -L https://github.com/bazelbuild/buildtools/releases/download/${VERSION_BZL_BUILDTOOLS}/buildifier -o ${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS}
    chmod u+x ${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS}
fi

logi "Check Starlark (Bazel) format"
logi "============================="
${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS} -version
${TOOLSDIR}/buildifier-${VERSION_BZL_BUILDTOOLS} -r=true -mode=diff .
logi "OK 🙌"

cd "$CWD"
