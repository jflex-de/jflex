#!/bin/sh
# Compiles the aggregated Java sources.
# Meant to be execute in https://github.com/jflex-de/jflex/tree/aggregated-java-sources
# Source: https://github.com/jflex-de/jflex/blob/master/scripts/compile-aggregated-sources.sh
mkdir lib
REPO=https://repo.maven.apache.org/maven2
CP=""
mvnget() {
  dep=$1
  jarfile=$(basename $dep)
  ls lib/$jarfile
  if [ -f lib/$jarfile ]; then
    echo "Using cached $jarfile"
    return "$jarfile"
  fi
  wget $REPO/$dep -P lib
  CP="$CP:lib/$jarfile"
}

mvnget org/apache/ant/ant/1.7.0/ant-1.7.0.jar
mvnget com/google/auto/value/auto-value/1.4.1/auto-value-1.4.1.jar

CP=${CP##:}  # Remove leading ':'
javac -cp "$CP" $(find . -name '*.java')
