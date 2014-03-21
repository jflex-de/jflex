#! /usr/bin/perl -w
    eval 'exec /usr/bin/perl -S $0 ${1+"$@"}'
        if 0; #$running_under_some_shell

#
# post-release.pl
#
# Performs the following:
#
#   - svn switch's your working copy back to trunk
#   - Changes the JFlex version in all POMs to the supplied
#     snapshot version (X.Y.Z-SNAPSHOT)
#   - Switches all <scm> URLs from /tags/release_X_Y_Z to /trunk
#   - Changes the bootstrap JFlex version in the de.jflex:jflex
#     POM to the latest release version.
#   - Commits the changed POMs
#
# For more information, see HOWTO_release.txt.
#

use strict;
use warnings;
use File::Find ();
use XML::LibXML;
use XML::LibXSLT;
use Getopt::Long;

my $snapshot;
my $usage = "Usage: $0 --snapshot <new-snapshot-version>\n e.g.: $0 --snapshot 1.6.0-SNAPSHOT\n";
GetOptions("snapshot=s" => \$snapshot) or die($usage);
die $usage unless (defined($snapshot) && $snapshot =~ /-SNAPSHOT$/);

my $sheet =<<'__STYLESHEET__';
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:pom="http://maven.apache.org/POM/4.0.0"
                xmlns="http://maven.apache.org/POM/4.0.0"
                exclude-result-prefixes="pom">
  <xsl:param name="snapshot"/>
  <xsl:param name="latest-release"/>

  <xsl:variable name="tag-suffix" select="'/jflex/code/tags/'"/>
  <xsl:variable name="trunk-suffix" select="'/jflex/code/trunk'"/>
  <xsl:variable name="scm-connection" 
                select="concat(substring-before(/pom:project/pom:scm/pom:connection,$tag-suffix),$trunk-suffix)"/>
  <xsl:variable name="scm-developerConnection" 
                select="concat(substring-before(/pom:project/pom:scm/pom:developerConnection,$tag-suffix),$trunk-suffix)"/>

  <xsl:variable name="url-tag-suffix" select="'/jflex/code/HEAD/tree/tags/'"/>
  <xsl:variable name="url-trunk-suffix" select="'/jflex/code/HEAD/tree/trunk'"/>
  <xsl:variable name="scm-url" 
                select="concat(substring-before(/pom:project/pom:scm/pom:url,$url-tag-suffix),$url-trunk-suffix)"/>

  <!-- Convert SCM /tags/release_X_Y_Z URLs -> /trunk --> 
  <xsl:template match="/pom:project/pom:scm/pom:connection">
    <connection><xsl:value-of select="$scm-connection"/></connection>
  </xsl:template>
  <xsl:template match="/pom:project/pom:scm/pom:developerConnection">
    <developerConnection><xsl:value-of select="$scm-developerConnection"/></developerConnection>
  </xsl:template>
  <xsl:template match="/pom:project/pom:scm/pom:url">
    <url><xsl:value-of select="$scm-url"/></url>
  </xsl:template>

  <!-- Replace all JFlex versions with the new JFlex snapshot version, --> 
  <!-- except for the bootstrap version in the de.jflex:jflex POM.     -->
  <xsl:template 
      match=" /pom:project[pom:groupId='de.jflex' or (not(pom:groupId) and pom:parent/pom:groupId='de.jflex')]/pom:version
             |/pom:project/pom:parent[pom:groupId='de.jflex' and pom:artifactId='jflex-parent']/pom:version
             |/pom:project/pom:dependencies/pom:dependency[pom:groupId='de.jflex' and pom:artifactId='jflex']/pom:version
             |/pom:project/pom:build/pom:plugins/pom:plugin
              [   (pom:groupId='de.jflex' and pom:artifactId='jflex-maven-plugin')
              and not(/pom:project/pom:parent/pom:groupId='de.jflex' and /pom:project/pom:artifactId='jflex')]/pom:version">
    <version><xsl:value-of select="$snapshot"/></version>
  </xsl:template>

  <!-- Replace the bootstrap version with the latest release version -->
  <!-- in the de.jflex:jflex POM.                                    -->
  <!-- TODO: remove maven-jflex-plugin check after releasing JFlex 1.5.0 --> 
  <xsl:template 
      match="/pom:project/pom:build/pom:plugins/pom:plugin
             [   /pom:project/pom:parent/pom:groupId='de.jflex' 
             and /pom:project/pom:artifactId='jflex'
             and (  pom:artifactId='jflex-maven-plugin' 
                 or pom:artifactId='maven-jflex-plugin')]
             /pom:version">
    <version><xsl:value-of select="$latest-release"/></version>
  </xsl:template>
  <!-- bootstrap maven-jflex-plugin -> jflex-maven-plugin -->
  <!-- TODO: remove this after releasing JFlex 1.5.0      -->
  <xsl:template 
      match="/pom:project/pom:build/pom:plugins/pom:plugin
             [   /pom:project/pom:parent/pom:groupId='de.jflex' 
             and /pom:project/pom:artifactId='jflex'
             and pom:artifactId='maven-jflex-plugin']
             /pom:artifactId">
    <artifactId><xsl:text>jflex-maven-plugin</xsl:text></artifactId>
  </xsl:template>

  <xsl:template match="@*|*|processing-instruction()|comment()">
    <xsl:copy>
      <xsl:apply-templates select="@*|*|text()|processing-instruction()|comment()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
__STYLESHEET__

my $latest_release = get_latest_release_version();

select STDOUT;
$| = 1; # Turn on auto-flush

print "Clean checkout?  ";
my $stat_results=`svn stat`;
if ($stat_results) {
  print "NO!\n\n$stat_results\nAborting.\n";
  exit 1;
}
print "Yes.\n\n";

my $repo_prefix = "https://";
if ( $ENV{'SF_USER'} ) {
  $repo_prefix = "svn+ssh://$ENV{'SF_USER'}\@";
}

my $trunk_url = "${repo_prefix}svn.code.sf.net/p/jflex/code/trunk";
print "svn switch'ing to ${trunk_url} ...\n";
my $ret_val = system(qq!svn switch "$trunk_url"!);
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with svn's return value
}
print "\ndone.\n\n";

print "Switching JFlex version -> $snapshot\n";
print " and SCM URLs from /tags/... -> /trunk in all POMs\n";
print " and boostrap JFlex version -> $latest_release in the de.jflex:jflex POM ...\n";
File::Find::find({wanted => \&wanted, follow => 1}, '.');

print "Updating version in Main.java\n";
system (qq!perl -pi -e "s/\Q$latest_release\E/$snapshot/" jflex/src/main/java/jflex/Main.java !);

print "Updating version in the testsuite's Exec.java\n";
system (qq!perl -pi -e "s/\Q$latest_release\E/$snapshot/"!
       . q! testsuite/jflex-testsuite-maven-plugin/src/main/java/jflextest/Exec.java !);


print " updating version in bin/jflex*";
system (qq!perl -pi -e "s/\Q$latest_release\E/$snapshot/" jflex/bin/jflex !);
system (qq!perl -pi -e "s/\Q$latest_release\E/$snapshot/" jflex/bin/jflex.bat !);
print "\ndone.\n\n";

#  <property name="bootstrap.version" value="1.5.0" />
#  <property name="version" value="1.5.1-SNAPSHOT" />
print "Updating version -> $snapshot and",
      " bootstrap JFlex version -> $latest_release\n",
      " in jflex/build.xml\n";
system(qq!perl -pi -e "s/(property\\s+name\\s*=\\s*[\\"']version[\\"']\\s+value\\s*=\\s*[\\"'])[^\\"]+/\\\${1}$snapshot/;!
      .qq!  s/(property\\s+name\\s*=\\s*[\\"']bootstrap\\.version[\\"']\\s+value\\s*=\\s*[\\"'])[^\\"']+/\\\${1}$latest_release/;"!
      . q! jflex/build.xml !);

print "\ndone.\n\n";

print "Committing the changed files ...\n";
$ret_val = system
   (qq!svn ci -m "JFlex <version>s -> $snapshot; SCM URLs -> /trunk; and bootstrap JFlex version -> $latest_release"!);
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with svn's return value
}
print "\ndone.\n\n";

exit;

sub get_latest_release_version {
  # Get the latest release version from the parent POM
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
      ($pom, 'snapshot' => "'$snapshot'", 
             'latest-release' => "'$latest_release'");
  $stylesheet->output_file($results, $pom); # replace existing file
}
