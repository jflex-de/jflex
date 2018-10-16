#!/bin/bash
# Prepare the aggregated source code in the 'repo' directory that is cloned from
# branch [aggregated-java-sources].

# This is inspired by https://martinrotter.github.io/it-programming/2016/08/26/pushing-git-travis/

CWD="$PWD"
BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
# Provides the logi function
source "$BASEDIR"/scripts/logger.sh
# fail on error
set -e
set -x

git_clone() {
  if [[ -d repo ]]; then
    backup=$(mktemp -d)
    logi "Move existing repo to $backup"
    mv repo $backup
  fi
  if [[ -z "$GITHUB_TOKEN" ]]; then
    logi "Cloning ssh://git@github.com:jflex-de/jflex.git (aggregated-java-sources)"
    git clone --depth 1 --branch aggregated-java-sources "git@github.com:jflex-de/jflex.git" repo > /dev/null 2>&1
  else
    logi "Cloning https://[GITHUB_TOKEN]@github.com/jflex-de/jflex/tree/aggregated-java-sources"
    # SECURITY NOTICE: Be sure to send stdout & stderr to /dev/null so that the the ${GITHUB_TOKEN} is never revealed
    git clone --depth 1 --branch aggregated-java-sources "https://${GITHUB_TOKEN}@github.com/jflex-de/jflex.git" repo > /dev/null 2>&1
  fi
}

# update_source <initial_log_message>
update_source() {
  gitlog="$1"
  logi "Updating source for $gitlog"
  version=$(ls target/jflex-*-sources.jar)
  logi "Updating sources from $version"
  cd repo
  git config user.name "Travis CI"
  git config user.email "deploy@travis-ci.org"
  git rm -r META-INF jflex java_cup UnicodeProperties.java.skeleton
  mkdir -p java
  cd java
  jar -xf ../../target/jflex-*-sources.jar
  logi "Remove unrelated sources"
  rm -rf jflex/maven jflextest

  logi "Checking licenses"
  [[ $(head -1 LICENSE_JFLEX | cut -f 1 -d " ") == "JFlex" ]] || \
      loge "JFlex license has bad content" && cat LICENSE_JFLEX
  mv LICENSE_JFLEX ..
  mv LICENSE_CUP ..
  cd ..

  logi "Download deps and Compile"
  ./compile.sh

  logi "Update git sources"
  git add --all

  logi "Git status"
  git status
  # Don't commit if the diff is empty.
  # git commit fails if the commit is empty, which makes Travis build fail.
  git diff-index --quiet HEAD || \
      git commit -a \
          -m "Pseudo-Merge $gitlog" \
          -m "Updated from $version"
  cd ..
}

# N.B. TRAVIS_BRANCH is the name of the branch targeted by the pull request (if PR)
logi "On branch ${TRAVIS_PULL_REQUEST_SLUG}:${TRAVIS_PULL_REQUEST_BRANCH} → ${TRAVIS_BRANCH}"

gitlog=$(git log -1)
git_clone
update_source "$gitlog"

if [[ -z "$CI" ]]; then
  logi "Check the last commit"
  logi "git log -1"
  logi "git diff HEAD^1"
  logi "# git push"
fi

cd "$CWD"
