#!/bin/bash
CODACY_REPORTER_VERSION=6.1.1
CODACY_JAR=buildtools/codacy-coverage-reporter-$CODACY_REPORTER_VERSION.jar
mkdir -p buildtools

if [[ -z "${CODACY_PROJECT_TOKEN}" ]]; then
 echo "Missing \$CODACY_PROJECT_TOKEN"
 exit 1
fi

if [[ "_${CIRRUS_BRANCH}" == "_${CIRRUS_DEFAULT_BRANCH}" || "_${CIRRUS_BRANCH}" == "_cirrus" ]]; then
  if [[ ! -f "${CODACY_JAR}" ]]; then
    curl -L https://github.com/codacy/codacy-coverage-reporter/releases/download/$CODACY_REPORTER_VERSION/codacy-coverage-reporter-$CODACY_REPORTER_VERSION-assembly.jar  -o ${CODACY_JAR}
  fi
  echo "Sending code coverage report fo Codacy"
  # https://app.codacy.com/project/regisd/jflex/dashboard
  java -jar ${CODACY_JAR} report -l Java -r bazel-out/_coverage/_coverage_report.dat
else
  echo "Cirrus-CI only send coverage report only for commits in master"
fi
