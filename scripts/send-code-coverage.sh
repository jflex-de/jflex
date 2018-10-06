#!/bin/bash

mkdir -p tools

# https://app.codacy.com/project/regisd/jflex/dashboard
curl -C - -L https://github.com/codacy/codacy-coverage-reporter/releases/download/4.0.3/codacy-coverage-reporter-4.0.3-assembly.jar  -o tools/codacy-coverage-reporter.jar
java -jar tools/codacy-coverage-reporter.jar report -l Java -r report-module/target/site/jacoco-aggregate/jacoco.xml

