# How to release JFlex

This document describes how the release manager of the JFlex team can release
JFlex and the JFlex Maven Plugin.

JFlex and the JFlex Maven Plugin will be deployed to the Sonatype OSS Maven
repository at [oss.sonatype.org][sonatype].
The Maven Central repository is synchronized with this repository.
For more information, see [Sonatype respository usage guide][sonatype-repo-usage].

## Prepare to release (only once)

1. First, create a JIRA account on <http://issues.sonatype.org>, then make sure that your
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
         <servers>
           <server>
             <id>ossrh</id>
             <username> ... </username>
             <password> ... </password>
           </server>
         </servers>
       </settings>
    ```

3. Make sure you have installed Perl and the `XML::LibXSLT` module (required
   by the `prepare-release.pl` and `post-release.pl` scripts).

4. Get the source:

   ```sh
   git clone git@github.com:jflex-de/jflex.git
   ```

5. Make sure all changes are committed

   ```sh
   cd jflex
   git status
   ```

## Perform the release

### Run the `prepare-release.pl` script

```sh
./scripts/prepare-release.pl
```

The script does the following:

- Creates a release branch **branch-x.y.z**
- Changes the version in all POMs by removing all -SNAPSHOT suffixes
- Changes the versions in java comments and @version tags
- Commits the changes

If something goes wrong with one of the steps performed by
the script, it will halt.  You can return to the state before
it ran by checking out the master branch and deleting the
release branch:

```sh
git checkout master
git reset --hard
git branch -D branch-x.y.z
```

Then:

  1. Create a pull request to merge **branch-x.y.z** into **master**.
     This is important so that **aggregated-java-sources** is built by
     Travis with the final release version.
  2. Check that all tests come back green.
  3. **Hold off merge into master** until deployment has succeeded.

### Build all artifacts

```bash
./mvnw install
```

### Create the release package

Run the packaging script:

```sh
./scripts/mk-release.sh
```

This generates the documentation and builds the .tar.gz and .zip file.

Go into `releases/jflex-$version` and see if things look as expected.

### Stage the release to the Sonatype OSS Maven repository

```sh
./scripts/mvn-deploy.sh
```

### Publish the staged release on Sonatype

After staging the release, you have to perform several manual steps
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
   6. Click the "Refresh" button again.
   7. Click the "Release" button, to the right of the "Refresh" button -
     this is the final step to release the artifacts.  Maven Central
     will then sync within less than one day.

### Commit the release branch

If you are happy with the changes, you can merge the release branch
**branch-x.y.z** from the pull request created before, and then tag and push:

```sh
git tag -s vX.Y.Z
git push origin vX.Y.Z
```

### Create github release

- Create github release on <https://github.com/jflex-de/jflex/releases>
  with the release tag used above.
- Add the release package files (at least `.tar.gz` and signature files,
  because they will be referenced from the jflex website) and manual.

### Update the website

In repository `jflex-web`, in `pages/`

   1. Update index.md with news and current release
   2. Sync changelog.md with the jflex repo
   3. Copy over generated `manual.html` and `manual.pdf` from jflex repo
   4. Update version numbers in download.md
   5. Commit and push to master
   6. Copy over the release package files into `release/`
   7. Run `make deploy`

### Update jflex-maven-plugin website

Generate jflex-maven-plugin site in repo `jflex` in dir `jflex-maven-plugin` with

```sh
mvn site
```

Copy contents of `target/site` to directory `jflex-maven-plugin` on branch `gh-pages` in repo `jflex-wev`. Push to github to deploy.

### Tag the _aggregate-java-sources_ branch

Travis will update the **aggregated-java-sources** branch from master.

Once this is done,

```sh
git checkout aggregated-java-sources
# Verify that the last commit was to update for the release version
git log
# Tag
git tag sources-vX.Y.Z
git push origin sources-vX.Y.Z
```

### Post-release

```sh
./scripts/post-release.pl --snapshot x.y.z--SNAPSHOT
# Review
git diff HEAD^1
git push
```

The`post-release.pl` script does the following:

- Creates a new branch **new_snapshot** for the future release
- Bump the JFlex version in all POMs to the supplied snapshot version
- Changes the bootstrap JFlex version in the de.jflex:jflex POM to the latest release version.
- Commits the changes

Finally:

- create a pull request with these changes
- merge into master

[sonatype]: http://oss.sonatype.org/
[maven-site-deploy]: http://maven.apache.org/plugins/maven-site-plugin/examples/site-deploy-to-sourceforge.net.html
[sf-ssh]: https://sourceforge.net/p/forge/documentation/SSH%20Keys/
[sf-shell]: https://sourceforge.net/p/forge/documentation/Shell%20Service/
[sonatype-repo-usage]: https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
