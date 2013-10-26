#! /usr/bin/perl -w
    eval 'exec /usr/bin/perl -S $0 ${1+"$@"}'
        if 0; #$running_under_some_shell

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
  <xsl:param name="tag"/>

  <xsl:variable name="trunk-suffix" select="'/jflex/code/trunk'"/>
  <xsl:variable name="tag-suffix" select="concat('/jflex/code/tags/', $tag)"/>
  <xsl:variable name="scm-connection" 
                select="concat(substring-before(/pom:project/pom:scm/pom:connection,$trunk-suffix),$tag-suffix)"/>
  <xsl:variable name="scm-developerConnection" 
                select="concat(substring-before(/pom:project/pom:scm/pom:developerConnection,$trunk-suffix),$tag-suffix)"/>

  <xsl:variable name="url-trunk-suffix" select="'/jflex/code/HEAD/tree/trunk'"/>
  <xsl:variable name="url-tag-suffix" select="concat('/jflex/code/HEAD/tree/tags/', $tag)"/>
  <xsl:variable name="scm-url" 
                select="concat(substring-before(/pom:project/pom:scm/pom:url,$url-trunk-suffix),$url-tag-suffix)"/>

  <!-- Convert SCM /trunk URLs -> /tags/release_X_Y_Z --> 
  <xsl:template match="/pom:project/pom:scm/pom:connection">
    <connection><xsl:value-of select="$scm-connection"/></connection>
  </xsl:template>
  <xsl:template match="/pom:project/pom:scm/pom:developerConnection">
    <developerConnection><xsl:value-of select="$scm-developerConnection"/></developerConnection>
  </xsl:template>
  <xsl:template match="/pom:project/pom:scm/pom:url">
    <url><xsl:value-of select="$scm-url"/></url>
  </xsl:template>

  <!-- Replace all JFlex versions with the new JFlex release version, --> 
  <!-- except for the bootstrap version in the de.jflex:jflex POM.    -->
  <xsl:template 
      match=" /pom:project[pom:groupId='de.jflex' or (not(pom:groupId) and pom:parent/pom:groupId='de.jflex')]/pom:version
             |/pom:project/pom:parent[pom:groupId='de.jflex' and pom:artifactId='jflex-parent']/pom:version
             |/pom:project/pom:dependencies/pom:dependency[pom:groupId='de.jflex' and pom:artifactId='jflex']/pom:version
             |/pom:project/pom:build/pom:plugins/pom:plugin
              [   (pom:groupId='de.jflex' and pom:artifactId='jflex-maven-plugin')
              and not(/pom:project/pom:parent/pom:groupId='de.jflex' and /pom:project/pom:artifactId='jflex')]/pom:version">
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
(my $tag = "release_$release") =~ s/\./_/g;

select STDOUT;
$| = 1; # Turn on auto-flush

print "Clean checkout?  ";
my $stat_results=`svn stat`;
if ($stat_results) {
  print "NO!\n\n$stat_results\nAborting.\n";
  exit 1;
}

print "Switching JFlex version -> $release\n";
print " and SCM URLs from /trunk -> /tags/$tag in all POMs ... ";
File::Find::find({wanted => \&wanted, follow => 1}, '.');
print "done.\n";

print "Committing the changed POMs ... ";
my $ret_val = system
   (qq/svn ci -m "JFlex <version>s -> $release and SCM URLs -> /tags/$tag"/);
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with svn's return value
}
print "done.\n";

$tag_url = "https://svn.code.sf.net/p/jflex/code/tags/$tag";
print "Tagging the release as $tag_url ... ";
$ret_val = system(qq/svn copy . "$tag_url"/); 
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with svn's return value
}
print "done.\n";

print "svn switch'ing to ${tag_url} ... "
$ret_val = system(qq/svn switch "${tag_url}"/);
if ($ret_val) {
  print STDERR "ERROR - Aborting.\n";
  exit $ret_val >> 8; # Exit with svn's return value
}
print "done.\n";

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
      ($pom, release => "'$release'", tag => "'$tag'");
  $stylesheet->output_file($results, $pom); # replace existing file
}
