#!/bin/bash
mkdir -p tools

if [[ -z "${CODACY_PROJECT_TOKEN}" ]]; then
 echo "Missing \$CODACY_PROJECT_TOKEN"
 exit 1
fi
if [[ "${TRAVIS_PULL_REQUEST_SLUG}:${TRAVIS_BRANCH}" = ":master" ]]; then
  # https://app.codacy.com/project/regisd/jflex/dashboard
  curl -C - -L https://github.com/codacy/codacy-coverage-reporter/releases/download/4.0.3/codacy-coverage-reporter-4.0.3-assembly.jar  -o tools/codacy-coverage-reporter.jar
  echo "Sending code coverage report fo Codacy"
  java -jar tools/codacy-coverage-reporter.jar report -l Java -r report-module/target/site/jacoco-aggregate/jacoco.xml
else
  echo "Only Travis merging in main branch sends code coverage reports"
fi
