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

git_clone() {
  if [[ -d repo ]]; then
    backup=$(mktemp -d)
    logi "Move existing repo to $backup"
    mv repo $backup
  fi
  logi "Cloning https://github.com/jflex-de/jflex/tree/aggregated-java-sources"
  git clone --depth 1 --branch aggregated-java-sources "https://github.com/jflex-de/jflex.git" repo
}

# update_source <initial_log_message>
update_source() {
  gittitle="$1"
  gitlog="$2"
  logi "Updating source for $gittitle"
  bazel --bazelrc=.ci.bazelrc build //jflex:jflex_bin_deploy-src.jar //jflex:resources

  logi "Updating sources from jflex_bin_deploy-src.jar"
  cd repo
  git config user.name "Cirrus CI"
  git config user.email "nobody@cirrus-ci.org"
  git rm -r java jflex LICENSE_* *.sh
  mkdir java
  cd java
  jar -xf ../../bazel-bin/jflex/jflex_bin_deploy-src.jar
  # for some reason, it doesn't include the resources
  jar -xf ../../bazel-bin/jflex/libresources.jar
  # cd repo
  cd ..

  logi "Checking licenses"
  mv java/LICENSE_JFLEX .
  if [[ ! $(head -1 LICENSE_JFLEX | cut -f 1 -d " ") == "JFlex" ]]; then
      loge "JFlex license has bad content"
      head LICENSE_JFLEX
  fi

  logi "Copying compile script"
  cp ../scripts/compile-aggregated-java-sources.sh compile.sh

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
          -m "Pseudo-Merge $gittitle" \
          -m "$gitlog" \
          -m "Updated from $version"
  cd ..
}

# N.B. TRAVIS_BRANCH is the name of the branch targeted by the pull request (if PR)
logi "On branch ${TRAVIS_PULL_REQUEST_SLUG}:${TRAVIS_PULL_REQUEST_BRANCH} → ${TRAVIS_BRANCH}"

gittitle=$(git log -1 --pretty=format:'%h %s')
gitlog=$(git log -1 --pretty=fuller)
git_clone
update_source "$gittitle" "$gitlog"

if [[ -z "$CI" ]]; then
  logi "Check the last commit"
  logi "git log -1"
  logi "git diff HEAD^1"
  logi "# git push"
fi

cd "$CWD"
