#!/bin/bash
CODACY_REPORTER_VERSION=6.1.1

mkdir -p buildtools

if [[ -z "${CODACY_PROJECT_TOKEN}" ]]; then
 echo "Missing \$CODACY_PROJECT_TOKEN"
 exit 1
fi

if [[ "$CIRRUS_CI" && "$CIRRUS_BRANCH" = "$CIRRUS_DEFAULT_BRANCH" ]]; then
  # https://app.codacy.com/project/regisd/jflex/dashboard
  curl -L https://github.com/codacy/codacy-coverage-reporter/releases/download/$CODACY_REPORTER_VERSION/codacy-coverage-reporter-$CODACY_REPORTER_VERSION-assembly.jar  -o buildtools/codacy-coverage-reporter-$CODACY_REPORTER_VERSION.jar
  echo "Sending code coverage report fo Codacy"
  java -jar buildtools/codacy-coverage-reporter-$CODACY_REPORTER_VERSION.jar report -l Java -r bazel-out/_coverage/_coverage_report.dat
else
  echo "Cirrus-CI only send coverage report only for commits in master"
fi