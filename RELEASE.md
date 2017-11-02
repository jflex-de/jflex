# How to release JFlex

This document describes how the release manager of the JFlex team can release 
JFlex and the JFlex Maven Plugin.

JFlex and the JFlex Maven Plugin will be deployed to the Sonatype OSS Maven
repository at [oss.sonatype.org][sonatype].
The Maven Central repository is synchronized with this repository.
For more information, see [Sonatype respository usage guide][sonatype-repo-usage]. 


## Preparing to release

1. First, create a JIRA account on issues.sonatype.org, then make sure that your
   account is authorized to deploy to the Sonatype OSS Maven repository - see
   the above-linked usage guide for more information.

2. In your `~/.m2/settings.xml`, create `<server>`-s for snapshots and releases on
   the Sonatype OSS Maven repository, as well as for uploading the JFlex Maven
   Plugin to SourceForge.  In the sonatype entries, fill in the username and
   password for your Sonatype JIRA account, which is also the account for the
   Sonatype Nexus instance that serves the OSS Maven repository: 

    ```xml
       <?xml version="1.0" encoding="UTF-8"?>
       <settings xmlns="http://maven.apache.org/POM/4.0.0">
         <!-- ... -->
         <servers>
           <!-- ... -->
           <server>
             <id>sonatype-nexus-snapshots</id>
             <username> ... </username>
             <password> ... </password>
           </server>
           <server>
             <id>sonatype-nexus-staging</id>
             <username> ... </username>
             <password> ... </password>
           </server>
           <server>
             <id>sourceforge</id>
             <username>decamps</username>
             <!-- full qualified path must be provided -->
             <privateKey>/home/regis/.ssh/id_rsa</privateKey>
             <filePermissions>774</filePermissions>
             <directoryPermissions>775</directoryPermissions>
           </server>
           <!-- ... -->
         </servers>
         <!-- ... -->
       </settings>
    ```

3. (Optional) Create an SSH key and set SourceForge to use it on
   [Sourceforge SSH keys][sf-ssh].
   
4. Make sure you have installed Perl and the XML::LibXSLT module (required
   by the `prepare-release.pl` and `post-release.pl` scripts).

5. Get the source:
   ```sh
   git clone git@github.com:jflex-de/jflex.git 
   ```

6. Make sure all changes are committed
   ```sh
   cd jflex
   git status
   ```


Perform the release
-------------------

1. Run the `prepare-release.pl` script, which does the following:

   - Creates a release branch
   - Changes the version in all POMs by removing all -SNAPSHOT suffixes
   - Changes the versions in java comments and @version tags
   - Commits the changes
     ```sh
     ./prepare-release.pl
     ```

   If you are happy with the changes, you can tag and push:
     ```sh
     git tag X.Y.Z
     git push --tags
     ```

   If something goes wrong with one of the steps performed by
   the script, it will halt.  You can return to the state before
   it ran by checking out the master branch and deleting the
   release branch:

   ```sh
   git checkout master
   git reset --hard
   git branch -D branch-X.Y.Z
   ```

2. Stage the release to the Sonatype OSS Maven repository:

   ```sh
   ./mvn-deploy
   ```

3. After staging the release, you have to perform several manual steps
   on the Sonatype OSS Maven repository website <http://oss.sonatype.org>
   after logging into the site:
   
   1. Click the "Staging Repositories" link in the left-hand navigation bar.
   2. Select "de.jflex" from the Filter combobox on the top right
   3. Click the "Refresh" button on top just to the right of the left navbar.
   4. Click on the repository you created when you ran "mvn-deploy".
   5. Click the "Close" button just to the right of the "Refresh" button.
     This process may take a while - once the artifacts have been uploaded,
     automated quality checks are performed to insure everything meets
     the advertized standards.
   6. Click the "Refresh" button again...
   7. Click the "Release" button, to the right of the "Refresh" button -
     this is the final step to release the artifacts.  Maven Central
     will then sync within less than one day.

3. Build the user manual
 
   ```sh
   cd tex && make
   ```

4. Create the release package

5. Upload the release package and user manual files

6. Update the website

7. Build and upload the JFlex Maven Plugin to SourceForge -
   see the following for more information:
   - [Maven site deploy][maven-site-deploy]
   - [Sourceforge shell service][sf-shell]

   1. Create a shell on SourceForge
     ```sh   
     ssh -t decamps,jflex@shell.sf.net create
     ```

   2. Build, check, then upload the JFlex Maven Plugin site to SourceForge 
     ```sh   
     cd jflex-maven-plugin && mvn site
     ```
     - The site is built under `jflex-maven-plugin/target/site/` - visit the
       index.html page there with a browser to sanity check that links work,
       etc.
       
     - Then upload the built site to SourceForge:
       ```sh
       mvn -Prelease site:deploy
       ```

## Post-release step

Run the post-release.pl script, which does the following:
   - switches back to the master branch
   - Changes the JFlex version in all POMs to the supplied
     snapshot version (X.Y.Z-SNAPSHOT)
   - Changes the bootstrap JFlex version in the de.jflex:jflex
     POM to the latest release version.
   - Commits the changes
   ```sh
   ./post-release.pl
   git push
   ```

[sonatype]: http://oss.sonatype.org/
[maven-site-deploy]: http://maven.apache.org/plugins/maven-site-plugin/examples/site-deploy-to-sourceforge.net.html
[sf-ssh]: https://sourceforge.net/p/forge/documentation/SSH%20Keys/
[sf-shell]: https://sourceforge.net/p/forge/documentation/Shell%20Service/
[sonatype-repo-usage]: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
