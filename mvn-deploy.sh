#!/bin/bash
#
# mvn-deploy.sh
# 
# This script deploys the current working copy to the Sonatype OSS Maven
# staging repository.
#  
# The prepare-release.pl script should be run before this script.
# See HOWTO_release.txt for more information.
#
# This script is more than just "mvn -Psonatype-oss-release deploy"
# because of inter-dependencies: the maven plugin depends on the release
# version of the jflex jar, and since that can't be satisfied until the
# jflex jar has been installed, that has to be done first.  Similarly,
# the jflex module depends on the parent POM, so that has to be installed
# before building the jflex module.
# 

# Fail if any command fails
set -e

# Abort if the checkout is not clean
printf "Clean checkout?  "
stat_results=`git status -s`
if [ ! -z "$stat_results" ] ; then
  printf "NO!\n\n${stat_results}\nAborting.\n"
  exit 1
fi
echo "Yes."

# Install the jflex-parent POM
./mvnw -N install

# Install JFlex
cd jflex
../mvnw clean
../mvnw install

# Install the JFlex Maven Plugin
cd ../jflex-maven-plugin
../mvnw clean
../mvnw install

# Deploy the deployable stuff to the Sonatype OSS Maven repository
cd ..
mvn deploy

