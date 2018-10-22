#!/bin/sh
# Compiles the aggregated-java-sources
# Meant to be exectute in https://github.com/jflex-de/jflex/tree/aggregated-java-sources
# Source: https://github.com/jflex-de/jflex/blob/master/scripts/compile-aggregated-sources.sh
mkdir lib
REPO=https://repo.maven.apache.org/maven2
[ -x lib/ant-1.7.0.jar ] || wget $REPO/org/apache/ant/ant/1.7.0/ant-1.7.0.jar -P lib
[ -x lib/auto-value-1.4.1.jar ] || wget $REPO/com/google/auto/value/auto-value/1.4.1/auto-value-1.4.1.jar -P lib
set -x
javac -cp lib/ant-1.7.0.jar:lib/auto-value-1.4.1.jar $(find . -name '*.java')

