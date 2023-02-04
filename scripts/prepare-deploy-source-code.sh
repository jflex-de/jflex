#!/bin/bash
#
# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause
#
# Prepare the aggregated source code in the '../aggregated-java-sources'
# directory that is cloned from branch [aggregated-java-sources]. Expects
# this repo to exist.

REPO=../aggregated-java-sources
CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

# update_source <initial_log_message>
update_source() {
  gittitle="$1"
  gitlog="$2"
  logi "In $(pwd)"
  logi "Updating source for $gittitle"
  bazel --bazelrc=.ci.bazelrc build //jflex:jflex_bin_deploy-src.jar //jflex:resources

  logi "Updating sources from jflex_bin_deploy-src.jar"
  cd $REPO
  logi "In $(pwd)"
  git config user.name "JFLex GitHub CI"
  git config user.email "ci-bot@jflex.de"
  # Separate jflex/ dir used to exist in aggregated_java_sources, but no longer does
  # This is here just in case it reappears:
  if [[ -d jflex ]]; then
    git rm -r jflex
  fi
  # -- *.sh in case there are files that start with -
  git rm -r java LICENSE_* -- *.sh
  mkdir java
  cd java
  jar -xf ../../jflex/bazel-bin/jflex/jflex_bin_deploy-src.jar
  # for some reason, it doesn't include the resources
  jar -xf ../../jflex/bazel-bin/jflex/libresources.jar
  cd ..

  logi "Checking licenses"
  mv java/LICENSE_JFLEX .
  if [[ ! $(head -1 LICENSE_JFLEX | cut -f 1 -d " ") == "JFlex" ]]; then
      loge "JFlex license has bad content"
      head LICENSE_JFLEX
  fi

  logi "Copying compile script"
  cp ../jflex/scripts/compile-aggregated-java-sources.sh compile.sh

  logi "Download deps and compile"
  ./compile.sh

  logi "Update git sources"
  git add --all

  logi "Git status"
  git status
  # Don't commit if the diff is empty.
  # git commit fails if the commit is empty, which makes CI build fail.
  git diff-index --quiet HEAD || \
      git commit -a \
          -m "Pseudo-Merge $gittitle" \
          -m "$gitlog" \
          -m "Updated from $version"
  cd ..
}

if [[ ! -d "$REPO" ]]; then
  logi "$REPO does not exit"
  exit 1
fi

gittitle=$(git log -1 --pretty=format:'%h %s')
gitlog=$(git log -1 --pretty=fuller)
update_source "$gittitle" "$gitlog"

if [[ -z "$CI" ]]; then
  logi "Check the last commit"
  logi "git log -1"
  logi "git diff HEAD^1"
  logi "# git push"
fi

cd "$CWD"
