#!/bin/bash
# Push aggregated source code back to git
# This is inspired by https://martinrotter.github.io/it-programming/2016/08/26/pushing-git-travis/

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# Maven executable
MVN="$BASEDIR"/mvnw
# fail on error
set -e

create_source_jar() {
  logi "Aggregate all sources in jar"
  "$MVN" source:aggregate
}

git_clone() {
  logi "Cloning https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
  git clone --depth 1 --branch aggregated-java-sources https://github.com/jflex-de/jflex.git repo
}

update_source() {
  version=$(ls target/jflex-*-sources.jar)
  logi "Updating sources from $version"
  cd repo
  git rm -r META-INF jflex java_cup UnicodeProperties.java.skeleton
  jar -xf ../target/jflex-*-sources.jar
  logi "Remove unrelated sources and compile"
  ./compile.sh
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

current_branch=$(git rev-parse --abbrev-ref HEAD)

if [ -z "$CI" ] || \
    ([ "_$TEST_SUITE" == "_unit" ] && [ "_${TRAVIS_JDK_VERSION}" == "_oraclejdk8" ]); then
  create_source_jar
  git_clone
  update_source
else
  logi "Skipping update in CI for test suite '$TEST_SUITE' (JDK='$TRAVIS_JDK_VERSION')"
fi

# Travis should only push from master ; not from pull requests
# TODO: Introduce a "release"/"stable" branch
if [ "_$TEST_SUITE" == "_unit" ] && [ "_$current_branch" == "_master" ]; then
  git_push
else
  logi "Skipping git push in branch '$current_branch'"
fi
