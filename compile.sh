#!/bin/sh
xargs rm < unrelated-sources.txt
mkdir lib
REPO=https://repo.maven.apache.org/maven2
[ -x lib/ant.jar ] || curl -L $REPO/org/apache/ant/ant/1.7.0/ant-1.7.0.jar -o lib/ant.jar
set -x
javac -cp lib/ant.jar $(find . -name '*.java')
