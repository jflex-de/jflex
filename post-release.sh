#!/bin/bash

release=1.5.0
next_release=1.6.0

next_release_snapshot=${next_release}-SNAPSHOT

trunk_url="https://svn.code.sf.net/p/jflex/code/trunk"
echo "svn switch'ing to ${trunk_url}"
svn switch "${trunk_url}"

echo "POM versions s/${release}/${next_release}/ and remove release tag from SCM URLs"
# Change the POMs' versions (skip jflex/examples/**/pom.xml)
# Change scm info to point to the release tag
perl -pi.bak -e "s:\\Q${release}\\E(?=</version>):${next_release_snapshot}:;
                 s:(?<=jflex/code/)[^<]+(?=</developerConnection>):trunk:;
                 s:(?<=jflex/code/)[^<]+(?=</connection>):trunk:;
                 s:(?<=jflex/code/HEAD/tree/)[^<]+(?=</url>:trunk:;"           \
     $(find . -name examples -prune -o -name pom.xml -print)

# Delete the backup files
rm $(find . -name pom.xml.bak)

# Commit the POM changes
echo "Committing the changed POMs"
svn ci -m "next SNAPSHOT version and trunk SCM URLs"

