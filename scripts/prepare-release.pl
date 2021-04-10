#! /usr/bin/perl -w
    eval 'exec /usr/bin/perl -S $0 ${1+"$@"}'
        if 0; #$running_under_some_shell
#
# prepare-release.pl
#
# Performs the following:
#
#   - Creates a git release branch
#   - Changes the version in all POMs by removing all -SNAPSHOT suffixes
#   - Changes version in java comments and version tags
#   - Commits the changes to the release branch
#
# For more information, see HOWTO_release.txt
#

use strict;
use warnings;
use File::Find ();
use XML::LibXML;
use XML::LibXSLT;

my $sheet =<<'__STYLESHEET__';
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:pom="http://maven.apache.org/POM/4.0.0"
                xmlns="http://maven.apache.org/POM/4.0.0"
                exclude-result-prefixes="pom">
  <xsl:param name="release"/>

  <!-- Replace all JFlex versions with the new JFlex release version, -->
  <!-- except for the bootstrap version in the de.jflex:jflex POM.    -->
  <xsl:template
      match=" /pom:project[(pom:groupId='de.jflex' or (not(pom:groupId) and pom:parent/pom:groupId='de.jflex'))
                           and not (pom:artifactId='cup-maven-plugin')]/pom:version
             |/pom:project/pom:parent[pom:groupId='de.jflex' and pom:artifactId='jflex-parent']/pom:version
             |/pom:project/pom:build/pom:plugins/pom:plugin
              [   (pom:groupId='de.jflex' and pom:artifactId='jflex-maven-plugin')
              and not(/pom:project/pom:parent/pom:groupId='de.jflex' and /pom:project/pom:artifactId='jflex')
            ">
    <version><xsl:value-of select="$release"/></version>
  </xsl:template>

  <xsl:template match="@*|*|processing-instruction()|comment()">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()|processing-instruction()|comment()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
__STYLESHEET__

my $snapshot = get_snapshot_version();
(my $release = $snapshot) =~ s/-SNAPSHOT//;
my $branch = "branch-$release";

select STDOUT;
$| = 1; # Turn on auto-flush

print "Clean checkout?  ";
my $stat_results=`git status -s`;
if ($stat_results) {
  print "NO!\n\n$stat_results\nAborting.\n";
  exit 1;
}
print "Yes.\n\n";

print "Creating release branch..\n";
system ("git checkout -b $branch");
if ($?) {
  print "FAILED.\n";
  exit 1;
}
print "OK.\n\n";

print "Switching JFlex version -> $release\n";
print " updating version in pom.xml files\n";
File::Find::find({wanted => \&wanted, follow => 1, follow_skip => 2}, '.');
print "\ndone.\n\n";

print " updating version in Build.java";
system ('perl -pi -e "s/version = \"(.*)-SNAPSHOT/version = \"\\1/" jflex/src/main/java/jflex/base/Build.java ');
print "\ndone.\n\n";

print " updating version in jflex/bin/jflex*";
system ('perl -pi -e "s/-SNAPSHOT//" jflex/bin/jflex');
system ('perl -pi -e "s/-SNAPSHOT//" jflex/bin/jflex.bat');
print "\ndone.\n\n";

print " updating version in jflex/examples/common/include.xml";
system ('perl -pi -e "s/-SNAPSHOT//" jflex/examples/common/include.xml');
print "\ndone.\n\n";

print " updating version in docs/xmanual.tex";
system ('perl -pi -e "s/-SNAPSHOT//" docs/xmanual.tex');
print "\ndone.\n\n";

print " updating version in docs/docs.bzl";
system ('perl -pi -e "s/-SNAPSHOT//" docs/docs.bzl');
print "\ndone.\n\n";

print " updating version in docs/Makefile";
system ('perl -pi -e "s/-SNAPSHOT//" docs/Makefile');
print "\ndone.\n\n";

print " updating version in jflex/README.md";
system ('perl -pi -e "s/-SNAPSHOT//" jflex/README.md');
print "\ndone.\n\n";

print " updating version in scripts/mk-release.sh";
system ('perl -pi -e "s/-SNAPSHOT//" scripts/mk-release.sh');
print "\ndone.\n\n";

print " updating version in comments and version tags in jflex/**.java";
system ('find jflex -name "*.java" | xargs perl -pi -e "s/-SNAPSHOT(.*)\\*/         \\1*/"');
system ('find jflex -name "LexScan.flex" | xargs perl -pi -e "s/-SNAPSHOT(.*)\\*/         \\1*/"');
system ('find jflex -name "LexParse.cup" | xargs perl -pi -e "s/-SNAPSHOT(.*)\\*/         \\1*/"');
system ('find jflex -name "*.java" | xargs perl -pi -e "s/@version (.*)-SNAPSHOT/@version \\1/"');
print "\ndone.\n\n";

print " updating version in jflex-maven-plugin/src/site/apt/usage.apt";
system (qq!perl -pi -e "s~<version>.*?</version>~<version>$release</version>~" jflex-maven-plugin/src/site/apt/usage.apt!);
print "\ndone.\n\n";

print "Committing version update ...\n";
my $ret_val = system
   (qq!git commit -a -m "bump version: JFlex $release-SNAPSHOT -> $release"!);
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with git's return value
}
print "\ndone.\n\n";

print "Now on branch $branch. 'git push' to publish.\n\n";

exit;

sub get_snapshot_version {
  # Get the current project version, assumed to be "something-SNAPSHOT",
  # from the parent POM
  my $parent_pom = XML::LibXML->load_xml(location => 'pom.xml');
  my $xpath_context = XML::LibXML::XPathContext->new($parent_pom);
  $xpath_context->registerNs('pom', 'http://maven.apache.org/POM/4.0.0');
  return $xpath_context->findvalue('/pom:project/pom:version');
}

sub wanted {
  transform($File::Find::fullname) if (/^pom\.xml\z/);
}

sub transform {
  my $pom = shift;
  my $xslt = XML::LibXSLT->new();
  my $style_doc = XML::LibXML->load_xml('string' => $sheet);
  my $stylesheet = $xslt->parse_stylesheet($style_doc);
  my $results = $stylesheet->transform_file
      ($pom, release => "'$release'");
  $stylesheet->output_file($results, $pom); # replace existing file
}
