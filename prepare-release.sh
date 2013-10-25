#!/bin/sh

release=1.5.0
next_release=1.6.0

snapshot=${release}-SNAPSHOT
next_release_snapshot=${next_release}-SNAPSHOT

echo "Running 'mvn clean'..."
mvn clean >/dev/null 2>/dev/null
if [ ! -z "`svn stat`" ]; then
    echo "There are uncommitted changes.  Aborting."
    exit 1;
fi

echo "POM versions s/${snapshot}/${release}/ and append release tag to SCM URLs"
# Change the POMs' versions (skip jflex/examples/**/pom.xml)
# Change scm info to point to the release tag
perl -pi.bak -e "BEGIN { \$tag = 'tags/release_${release}'; \$tag =~ s/\./_/g; }
                 s:\\Q${snapshot}\\E(?=</version>):${release}:;
                 s:(?<=jflex/code/)trunk(?=</developerConnection>):\$tag:;
                 s:(?<=jflex/code/)trunk(?=</connection>):\$tag:;
                 s:(?<=jflex/code/HEAD/tree/)trunk(?=</url>):\$tag:;
				 END { print qq/tag: \$tag\\n/; }"             \
     $(find . -name examples -prune -o -name pom.xml -print)
	 
# Delete the backup files
rm $(find . -name pom.xml.bak)

# Commit the POM changes
echo "Committing the changed POMs"
svn ci -m "release versions and tagged SCM URLs"

# Tag the release
echo "Tagging the release"
svn copy . https://svn.code.sf.net/p/jflex/code/\$tag

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

