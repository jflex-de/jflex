#!/bin/sh
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
  git commit -a -m "Update from $version"
  cd ..
}

git_push() {
  cd repo
  logi "Git log"
  git --no-pager log
  git diff --name-only HEAD^1
  logi "Push to https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
  git push
  cd ..
}

if [ -z "$CI" ] || [ "_$TEST_SUITE" -eq "_unit" ]; then
  git_clone
  update_source
fi

# Travis should only push from master ; not from pull requests
if [ "_$TEST_SUITE" -eq "_unit" ] && [ "_$(git rev-parse --abbrev-ref HEAD)" -eq "_master" ]; then
  git_push
fi
