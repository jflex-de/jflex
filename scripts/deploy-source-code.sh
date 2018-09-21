#!/bin/bash
# Push aggregated source code back to git
# This is inspired by https://martinrotter.github.io/it-programming/2016/08/26/pushing-git-travis/

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e

git_clone() {
  logi "Cloning https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
  git clone --depth 1 --branch aggregated-java-sources git@github.com:jflex-de/jflex.git repo
}

update_source() {
  version=$(ls target/jflex-*-sources.jar)
  logi "Updating sources from $version"
  cd repo
  git config user.name "Travis CI"
  git config user.email "deploy@travis-ci.org"
  git rm -r META-INF jflex java_cup UnicodeProperties.java.skeleton
  jar -xf ../target/jflex-*-sources.jar
  logi "Remove unrelated sources"
  logi "Download deps and Compile"
  ./compile.sh

  logi "Add license files"
  cp ../LICENSE.md .
  cp ../cup/LICENSE LICENSE_CUP
  logi "Update git sources"
  git add --all

  logi "Git status"
  git status
  git commit -a -m "Update from $version"
  cd ..
}

git_push() {
  cd repo
  logi "Push to https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
  git log -1
  git push
  cd ..
}

# N.B. TRAVIS_BRANCH is the name of the branch targeted by the pull request (if PR)
logi "On branch ${TRAVIS_PULL_REQUEST_SLUG}:${TRAVIS_PULL_REQUEST_BRANCH} → ${TRAVIS_BRANCH}"

git_clone
update_source

if [[ -z "$CI" ]]; then
  logi "Check the last commit"
  logi "git log -1"
  logi "git diff HEAD^1"
  logi "And run:"
  logi "git push"
else
  git_push
fi
