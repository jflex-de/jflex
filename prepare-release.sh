#!/bin/bash

release=1.5.0

snapshot=${release}-SNAPSHOT
tag="release_${release//./_}"

echo "Running 'mvn clean'..."
mvn clean >/dev/null 2>/dev/null
if [[ ! -z "`svn stat`" ]]; then
    echo "There are uncommitted changes.  Aborting."
    exit 1;
fi

echo "POM versions s/${snapshot}/${release}/ and append release tag to SCM URLs"
# Change the POMs' versions (skip jflex/examples/**/pom.xml)
# Change scm info to point to the release tag
perl -pi.bak -e "s:\\Q${snapshot}\\E(?=</version>):${release}:;
                 s:(?<=jflex/code/)trunk(?=</developerConnection>):tags/${tag}:;
                 s:(?<=jflex/code/)trunk(?=</connection>):tags/${tag}:;
                 s:(?<=jflex/code/HEAD/tree/)trunk(?=</url>):\${tag}:;"        \
     $(find . -name examples -prune -o -name pom.xml -print)
	 
# Delete the backup files
rm $(find . -name pom.xml.bak)

echo "Committing the changed POMs"
svn ci -m "release versions and tagged SCM URLs"

echo "Tagging the release"
tag_url="https://svn.code.sf.net/p/jflex/code/tags/${tag}" 
svn copy . "${tag_url}"

# Switching to the tag
echo "svn switch'ing to ${tag_url}"
svn switch "${tag_url}"

