#!/bin/bash

# fail on error
set -e

VERSION="1.8.1"
JFLEX_JAR="jflex-full-$VERSION.jar"

BASEDIR="$(cd "$(dirname "$0")" && pwd -P)"/..
MVN="$BASEDIR"/mvnw

GPG=gpg
KEY=20A847CE6AB7F5F3

printf "Clean checkout?  "
stat_results=$(git status -s)
if [ ! -z "$stat_results" ] ; then
  printf "NO!\n\n${stat_results}\nAborting.\n"
  exit 1
fi
echo "Yes."

echo "------[ Building docs ]"
cd "$BASEDIR"/docs
make

echo "------[ Cleaning jflex ]"
cd "$BASEDIR"/jflex
# we're on a clean checkout, but there can still be ignored files
git clean -fxd

# work from a clean copy
cd ..
mkdir -p release
rm -rf release/jflex-$VERSION
cp -a jflex release/jflex-$VERSION
cd release/jflex-$VERSION

echo "------[ Packaging jflex ]"
# make parent pom available
cp ../../pom.xml parent.xml
perl -pi -e "s|<relativePath>../pom.xml</relativePath>|<relativePath>parent.xml</relativePath>|" pom.xml

# build things
"$MVN" package

# main jar file
cp target/$JFLEX_JAR lib/
"$GPG" -ba -u $KEY lib/$JFLEX_JAR

# put generated sources into release, so those who want can bootstrap from source
mkdir src/generated
cp -r target/generated-sources/cup src/generated/
cp -r target/generated-sources/jflex src/generated/

# add generated docs
cp ../../docs/manual.html doc/
cp ../../docs/manual.pdf doc/
mkdir doc/fig
cp ../../docs/fig/jflex-black.png doc/fig/

"$MVN" clean
rm -f .gitignore

JFLEX_TAR=jflex-$VERSION.tar.gz
JFLEX_ZIP=jflex-$VERSION.zip

cd ..
rm -f $JFLEX_TAR $JFLEX_TAR.asc $JFLEX_TAR.sha1
tar cvzf $JFLEX_TAR jflex-$VERSION
"$GPG" -ba -u $KEY $JFLEX_TAR
shasum $JFLEX_TAR > $JFLEX_TAR.sha1

rm -f $JFLEX_ZIP $JFLEX_ZIP.asc $JFLEX_ZIP.sha1
zip -r $JFLEX_ZIP jflex-$VERSION
"$GPG" -ba -u $KEY $JFLEX_ZIP
shasum $JFLEX_ZIP > $JFLEX_ZIP.zip.sha1

echo "------[ Release packages in $(PWD) ]"
