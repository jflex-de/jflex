/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Unicode Maven 2 plugin                                            *
 * Copyright (c) 2008 Steve Rowe <steve_rowe@users.sf.net>                 *
 *                                                                         *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.SortedMap;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;


/**
 * Generates source code for JFlex Unicode character property handling.
 * 
 * @goal generate-unicode-properties
 * @phase generate-sources
 *
 */
public class JFlexUnicodeMojo extends AbstractMojo {

  /** Top-level directory URL from which to download Unicode data */
  private static final String UNICODE_DOT_ORG_URL
    = "http://www.unicode.org/Public/";

  /**
   * Pattern for Unicode version links in the page at
   * {@value #UNICODE_DOT_ORG_URL}
   */
  private static final Pattern UNICODE_VERSION_LINK_PATTERN = Pattern.compile
    ("<a href=\"((\\d+(?:\\.\\d+){1,2})(?i:-(Update(\\d*)))?/)\">");

  /** Pattern for non-beta UnicodeData(-X.X.X).txt files. */
  private static final Pattern NON_BETA_UNICODE_DATA_LINK_PATTERN
    = Pattern.compile("<a href=\"(UnicodeData(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta PropertyAliases(-X.X.X).txt files. */
  private static final Pattern NON_BETA_PROPERTY_ALIASES_LINK_PATTERN
    = Pattern.compile("<a href=\"(PropertyAliases(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta PropertyValueAliases(-X.X.X).txt files. */
  private static final Pattern NON_BETA_PROPERTY_VALUE_ALIASES_LINK_PATTERN
    = Pattern.compile("<a href=\"(PropertyValueAliases(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta DerivedCoreProperties(-X.X.X).txt files. */
  private static final Pattern NON_BETA_DERIVED_CORE_PROPERTIES_LINK_PATTERN
    = Pattern.compile("<a href=\"(DerivedCoreProperties(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta Scripts(-X.X.X).txt files. */
  private static final Pattern NON_BETA_SCRIPTS_LINK_PATTERN
    = Pattern.compile("<a href=\"(Scripts(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta Blocks(-X.X.X).txt files. */
  private static final Pattern NON_BETA_BLOCKS_LINK_PATTERN
    = Pattern.compile("<a href=\"(Blocks(?:|-\\d+(?:\\.\\d+){2}).txt)\">");

  /** Pattern for non-beta PropList(-X.X.X).txt files. */
  private static final Pattern NON_BETA_PROP_LIST_LINK_PATTERN
    = Pattern.compile("<a href=\"(PropList(?:|-\\d+(?:\\.\\d+){2}).txt)\">");


  /** Buffer size to use when reading web page content */
  private static final int BUF_SIZE = 4096;

  /** The date, for use in comments on the output class. */
  private static final String date
    = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

  /**
   * Name of the directory into which JFlex should generate the parser.
   *
   * @parameter expression="${basedir}/src/main/java/jflex"
   */
  private File outputDirectory = null;

  /**
   * If true, UnicodeProperties.java will be generated whether or not it
   * already exists; if false, it will be generated only if it does not already
   * exist.
   *
   * @parameter expression="${force}" default-value="false"
   */
  private boolean force = false;

  /**
   * Maps validated major.minor unicode versions to information about the
   * version.
   */
  private SortedMap<String,UnicodeVersion> unicodeVersions
    = new TreeMap<String,UnicodeVersion>();

  /** The name of the output file (without .java) and the contained class. */
  private static final String OUTPUT_CLASS_NAME = "UnicodeProperties";

  /**
   * <ol>
   *   <li>Collects and validates Unicode versions to support from
   *       <a href="http://www.unicode.org/Public/">Unicode.org</a>;
   *       finds the most recent updates for non-beta versions, so that
   *       JFlex scanner specs can specify major or major.minor Unicode
   *       versions as an optional parameter to the %unicode option.</li>
   *   <li>For each version:
   *     <ol type="a">
   *       <li>Downloads the following Unicode data files:
   *         <ul>
   *           <li>UnicodeData(-X.X.X).txt</li>
   *           <li>PropertyAliases(-X.X.X).txt</li>
   *           <li>PropertyValueAliases(-X.X.X).txt</li>
   *           <li>DerivedCoreProperties(-X.X.X).txt</li>
   *           <li>Scripts(-X.X.X).txt</li>
   *           <li>Blocks(-X.X.X).txt</li>
   *           <li>PropList(-X.X.X).txt</li>
   *         </ul>
   *       <li>Parses the data files, extracting ranges of code points
   *           and property values associated with them (see
   *           <a href="http://www.unicode.org/reports/tr23/">The Unicode
   *           Character Property Model</a>).</li>
   *     </ol>
   *   </li>
   *   <li>Generates Java source for class UnicodeProperties:
   *     <ol type="a">
   *       <li>Has a constructor taking in a string representing
   *           the Unicode version given as parameter to the %unicode
   *           JFlex specification option.</li>
   *       <li>Has a default constructor that defaults the version
   *           of Unicode to that supported by the JRE, for JFlex
   *           specifications that have parameter-less %unicode
   *           options.</li>
   *       <li>Has per-Unicode-version compressed tables for each
   *           property value, which will be unpacked by the
   *           constructor for the selected Unicode version.</li>
   *       <li>Binds the Unicode-version-specific unpacked tables
   *           to code point ranges for named property values.</li>
   *       <li>Binds property value aliases, e.g. \p{Letter} for \p{L} (which
   *           is [\p{Lu}\p{Ll}\p{Lt}\p{Lm}\p{Lo}] ) - see
   *           <a href="http://www.unicode.org/Public/UNIDATA/UCD.html#General_Category_Values">
   *           Unicode General Category Property Values</a>.</li>
   *       <li>Has Unicode-version-specific method maximumCodePoint():int.</li>
   *     </ol>
   *   </li>
   * </ol>
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    if ( ! getOutputFile().exists() || force) {
      try {
        getLog().info
          ("Downloading Unicode data from " + UNICODE_DOT_ORG_URL + "\n");
        collectUnicodeVersions();
        emitUnicodeProperties();
      } catch (IOException e) {
        throw new MojoExecutionException("Exception", e);
      }
    } else {
      getLog().info("Skipping generation of " + getOutputFile()
                    + " because it already exists.");
      getLog().info("To override, in jflex-unicode-maven-plugin's POM config,"
                    + " set force=\"true\"");
    }
  }

  /**
   * Searches unicode.org for available Unicode versions, and collects all
   * supported property data for each version.
   *
   * @throws IOException If there is a problem fetching or parsing the data
   */
  private void collectUnicodeVersions() throws IOException {
    // Maps available versions to maps from update numbers to relative URLs.
    // A version with no update is given update number "-1" for the purposes
    // of comparison.
    SortedMap<String,SortedMap<Integer,String>> allUnicodeVersions
      = new TreeMap<String,SortedMap<Integer,String>>();

    URL unicodeURL = new URL(UNICODE_DOT_ORG_URL);
    Matcher matcher
      = UNICODE_VERSION_LINK_PATTERN.matcher(getPageContent(unicodeURL));
    while (matcher.find()) {
      String relativeURL = matcher.group(1);
      String baseVersion = matcher.group(2);
      String update = matcher.group(3);
      int updateNumber = -1;
      if (null != update) {
        updateNumber = 0;
        if (null != matcher.group(4) && matcher.group(4).length() > 0) {
          updateNumber = Integer.parseInt(matcher.group(4));
        }
      }
      SortedMap<Integer,String> updates = allUnicodeVersions.get(baseVersion);
      if (null == updates) {
        updates = new TreeMap<Integer,String>(Collections.reverseOrder());
        allUnicodeVersions.put(baseVersion, updates);
      }
      updates.put(updateNumber, relativeURL);
    }
    for (String version : allUnicodeVersions.keySet()) {
      populateUnicodeVersion(version, allUnicodeVersions.get(version));
    }
  }

  /**
   * Given a Unicode version identifier and a corresponding set of relative
   * URLs, one for each available update, populates properties for this Unicode
   * version.
   *
   * @param version The Unicode version, either in form "X.X.X" or "X.X"
   * @param relativeURLs A sorted map from update number to relative URL
   * @throws IOException If there is a problem fetching or parsing the data
   */
  private void populateUnicodeVersion(String version,
                                      SortedMap<Integer,String> relativeURLs)
    throws IOException {

    URL unicodeDataURL = null;
    URL propertyAliasesURL = null;
    URL propertyValueAliasesURL = null;
    URL derivedCorePropertiesURL = null;
    URL blocksURL = null;
    URL scriptsURL = null;
    URL propListURL = null;

    // The relative URLs are sorted in reverse order of update number; as a
    // result, the most recent update is first, the next most recent is next,
    // etc.  The first relative URL with a non-beta UnicodeData-X.X.X.txt
    // will be used.
    for (String relativeURL : relativeURLs.values()) {
      URL baseURL = new URL(UNICODE_DOT_ORG_URL + relativeURL);
      String versionedDirectoryListing = getPageContent(baseURL);

      // As of version 4.1.0, UnicodeData.txt lives in the ucd/ subdir.
      if (-1 != versionedDirectoryListing.indexOf("<a href=\"ucd/\">")) {
        baseURL = new URL(baseURL, "ucd/");
        relativeURL = "ucd/" + relativeURL;
        versionedDirectoryListing = getPageContent(baseURL);
      }

      // Archaic link: <a href="UnicodeData-1.1.5.txt">
      // Modern link: <a href="UnicodeData.txt">
      // Beta link: <a href="UnicodeData-5.1.0d12.txt">
      if (null == unicodeDataURL) {
        Matcher matcher = NON_BETA_UNICODE_DATA_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          unicodeDataURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == propertyAliasesURL) {
        Matcher matcher = NON_BETA_PROPERTY_ALIASES_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          propertyAliasesURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == propertyValueAliasesURL) {
        Matcher matcher = NON_BETA_PROPERTY_VALUE_ALIASES_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          propertyValueAliasesURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == derivedCorePropertiesURL) {
        Matcher matcher = NON_BETA_DERIVED_CORE_PROPERTIES_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          derivedCorePropertiesURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == scriptsURL) {
        Matcher matcher = NON_BETA_SCRIPTS_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          scriptsURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == blocksURL) {
        Matcher matcher = NON_BETA_BLOCKS_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          blocksURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
      if (null == propListURL) {
        Matcher matcher = NON_BETA_PROP_LIST_LINK_PATTERN.matcher
          (versionedDirectoryListing);
        if (matcher.find()) {
          String filename = matcher.group(1);
          propListURL = new URL(baseURL, filename);
          getLog().info("\t\t" + relativeURL + filename);
        }
      }
    }
    if (null != unicodeDataURL) { // Non-beta version found
      UnicodeVersion unicodeVersion = new UnicodeVersion
        (version, unicodeDataURL, propertyAliasesURL, propertyValueAliasesURL,
         derivedCorePropertiesURL, scriptsURL, blocksURL, propListURL);
      unicodeVersions.put(unicodeVersion.majorMinorVersion, unicodeVersion);
      getLog().info("Completed processing Unicode "
                    + unicodeVersion.majorMinorVersion + "\n");
    }
  }

  /**
   * Fetches the contents of the page at the given URL.
   *
   * @param url The location of the page to fetch.
   * @return The contents of the fetched page
   * @throws IOException If there is an error fetching the given page.
   */
  private String getPageContent(URL url) throws IOException {
    InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
    StringBuilder builder = new StringBuilder();
    char[] buf = new char[BUF_SIZE];
    int charsRead;
    while ((charsRead = reader.read(buf)) > 0) {
      builder.append(buf, 0, charsRead);
    }
    return builder.toString();
  }

  /**
   * Writes out UnicodeProperties.java based on data fetched from unicode.org.
   *
   * @throws IOException If there is an error writing the output file.
   */
  private void emitUnicodeProperties() throws IOException {
    StringBuilder builder = new StringBuilder();
    emitHeader(builder);
    for (UnicodeVersion unicodeVersion : unicodeVersions.values()) {
      unicodeVersion.emitMaximumCodePoint(builder);
      unicodeVersion.emitPropertyValuesArray(builder);
      unicodeVersion.emitIntervalsArray(builder);
      unicodeVersion.emitPropertyValueAliasesArray(builder);
      unicodeVersion.emitCaselessMatchPartitions(builder);
    }
    emitFooter(builder);
    writeOutputFile(builder);
  }

  /**
   * Populates the given StringBuilder with the contents of
   * UnicodeProperties.java up to and including the class declaration line.
   *
   * @param builder Where to emit the header for UnicodeProperties.java.
   */
  private void emitHeader(StringBuilder builder) {
    builder
      .append(
        "/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n")
      .append(
        " * JFlex Unicode Properties                                                *\n")
      .append(
        " * Copyright (c) 2008 Steve Rowe <steve_rowe@users.sf.net>                 *\n")
      .append(
        " *                                                                         *\n")
      .append(
        " *                                                                         *\n")
      .append(
        " * This program is free software; you can redistribute it and/or modify    *\n")
      .append(
        " * it under the terms of the GNU General Public License. See the file      *\n")
      .append(
        " * COPYRIGHT for more information.                                         *\n")
      .append(
        " *                                                                         *\n")
      .append(
        " * This program is distributed in the hope that it will be useful,         *\n")
      .append(
        " * but WITHOUT ANY WARRANTY; without even the implied warranty of          *\n")
      .append(
        " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *\n")
      .append(
        " * GNU General Public License for more details.                            *\n")
      .append(
        " *                                                                         *\n")
      .append(
        " * You should have received a copy of the GNU General Public License along *\n")
      .append(
        " * with this program; if not, write to the Free Software Foundation, Inc., *\n")
      .append(
        " * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *\n")
      .append(
        " *                                                                         *\n")
      .append(
        " * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */\n\n")
      .append("package jflex;\n\n")
      .append("import java.util.ArrayList;\n")
      .append("import java.util.HashMap;\n")
      .append("import java.util.List;\n")
      .append("import java.util.Map;\n")
      .append("import java.util.regex.Matcher;\n")
      .append("import java.util.regex.Pattern;\n")
      .append("\n/**\n")
      .append(" * This class was automatically generated by")
      .append(" jflex-unicode-maven-plugin based\n")
      .append(" * on data files downloaded from unicode.org on ")
      .append(date).append(".\n")
      .append(" */\n")
      .append("class ").append(OUTPUT_CLASS_NAME).append(" {\n\n")
      .append("  private static final Pattern WORD_SEP_PATTERN")
      .append(" = Pattern.compile(\"[-_\\\\s]\");\n\n")
     ///////////////////////////////////////////////////////////////////////////
     // From <http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4313773>:
     //
     //    JDK 1.0.x -> Unicode 1.1.5
     //    JDK 1.1 to JDK 1.1.6 -> Unicode 2.0
     //    JDK 1.1.7 and up, Java 2 SE 1.2.x and Java 2 SE 1.3.x -> Unicode 2.1
     //
     // From <http://java.sun.com/j2se/1.4.2/docs/api/java/lang/Character.html>:
     //
     //    Java 2 SE 1.4.2 -> Unicode 3.0
     //
     // (Inspection of downloaded docs for 1.4.0 and 1.4.1, both end-of-life'd,
     // from java.sun.com confirms that they, too, were based on Unicode 3.0.)
     //
     // From <http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Character.html>:
     //
     //    Java 2 SE 5.0 -> Unicode 4.0
     //
     // From <http://java.sun.com/javase/6/docs/api/java/lang/Character.html>:
     //
     //    Java SE 6 -> Unicode 4.0
     //
     // NOTE: The output of this Maven plugin is intended for incorporation in
     //       JFlex verion 1.5, which requires Java 2 SE 5.0+.
     ///////////////////////////////////////////////////////////////////////////
      .append("  // Both Java5 and Java6 JREs are based on Unicode 4.0\n")
      .append("  private static final String DEFAULT_UNICODE_VERSION = \"4.0\";\n\n");
  }

  /**
   * Populates the given StringBuilder with the non-version-specific methods
   * in UnicodeProperties.java, as well as the closing brace for the class.
   *
   * @param builder Where to emit the footer for UnicodeProperties.java.
   */
  private void emitFooter(StringBuilder builder) {
    builder
      .append("\n\n")
      .append("  private int maximumCodePoint;\n")
      .append("  private Map<String,List<Interval>> propertyValueIntervals\n")
      .append("    = new HashMap<String,List<Interval>>();\n")
      .append("  private String caselessMatchPartitions;\n")
      .append("  private int caselessMatchPartitionSize;\n")
      .append("  private IntCharSet caselessMatches[];\n\n")
      .append("  public ").append(OUTPUT_CLASS_NAME)
      .append("() throws UnsupportedUnicodeVersionException {\n")
      .append("    init(DEFAULT_UNICODE_VERSION);\n")
      .append("  }\n\n")
      .append("  public ").append(OUTPUT_CLASS_NAME)
      .append("(String version) throws UnsupportedUnicodeVersionException {\n")
      .append("    init(version);\n")
      .append("  }\n\n")
      .append("  public int getMaximumCodePoint() {\n")
      .append("    return maximumCodePoint;\n")
      .append("  }\n\n")
      .append("  public List<Interval> getIntervals")
      .append("(String propertyValue) {\n")
      .append("    return propertyValueIntervals.get(normalize(propertyValue));\n")
      .append("  }\n\n")
      .append("  private void init(String version) ")
      .append("throws UnsupportedUnicodeVersionException {\n");

    boolean isFirst = true;
    for (String majorMinorVersion : unicodeVersions.keySet()) {
      if (isFirst) {
        builder.append("    if (");
        isFirst = false;
      } else {
        builder.append("    } else if (");
      }
      if (majorMinorVersion.indexOf(".0") == majorMinorVersion.length() - 2) {
        String majorVersion
          = majorMinorVersion.substring(0, majorMinorVersion.indexOf("."));
        builder.append("version.equals(\"").append(majorVersion)
          .append("\") || ");
      }
      UnicodeVersion unicodeVersion = unicodeVersions.get(majorMinorVersion);
      String versionSuffix = unicodeVersion.getVersionSuffix();
      builder.append("version.equals(\"").append(majorMinorVersion)
        .append("\") || version.equals(\"")
        .append(unicodeVersion.majorMinorUpdateVersion).append("\")) {\n")
        .append("      bind(propertyValues").append(versionSuffix)
        .append(", intervals").append(versionSuffix)
        .append(", propertyValueAliases").append(versionSuffix)
        .append(",\n         maximumCodePoint").append(versionSuffix)
        .append(", caselessMatchPartitions").append(versionSuffix)
        .append(", caselessMatchPartitionSize").append(versionSuffix)
        .append(");\n");
    }
    builder.append("    } else {\n")
      .append("      throw new UnsupportedUnicodeVersionException(version);\n")
      .append("    }\n")
      .append("  }\n\n")
      .append("  private void bind(String[] propertyValues, String[] intervals,\n")
      .append("                    String[] propertyValueAliases, int maximumCodePoint,")
      .append("                    String caselessMatchPartitions, int caselessMatchPartitionSize) {\n")
      // IntCharSet caselessMatches[] is lazily initialized - don't unpack here
      .append("    this.caselessMatchPartitions = caselessMatchPartitions;\n")
      .append("    this.caselessMatchPartitionSize = caselessMatchPartitionSize;\n")
      .append("    this.maximumCodePoint = maximumCodePoint;\n")
      .append("    for (int n = 0 ; n < propertyValues.length ; ++n) {\n")
      .append("      String propertyValue = propertyValues[n];\n")
      .append("      String propertyIntervals = intervals[n];\n")
      .append("      int numCodePoints\n")
      .append("        = propertyIntervals.codePointCount(0, propertyIntervals.length());\n")
      .append("      List<Interval> intervalsList ")
      .append("= new ArrayList<Interval>(numCodePoints / 2);\n")
      .append("      for (int index = 0 ; index < propertyIntervals.length() ; ) {\n")
      .append("        int start = propertyIntervals.codePointAt(index);\n")
      .append("        index += (start <= 0xFFFF ? 1 : 2);\n")
      .append("        int end = propertyIntervals.codePointAt(index);\n")
      .append("        index += (end <= 0xFFFF ? 1 : 2);\n")
      // TODO: When JFlex supports code points greater than U+FFFF, remove this condition:
      .append("        if (start <= 0xFFFF && end <= 0xFFFF) {\n")
      .append("          intervalsList.add(new Interval((char)start, (char)end));\n")
      .append("        }\n")
      .append("      }\n")
      .append("      propertyValueIntervals.put(propertyValue, intervalsList);\n")
      .append("      if (2 == propertyValue.length()) {\n")
      .append("        List<Interval> singleLetterPropValueList\n")
      .append("          = propertyValueIntervals.get(propertyValue.substring(0, 1));\n")
      .append("        if (null == singleLetterPropValueList) {\n")
      .append("          singleLetterPropValueList = new ArrayList<Interval>();\n")
      .append("        }\n")
      .append("        singleLetterPropValueList.addAll(intervalsList);\n")
      .append("      }\n")
      .append("    }\n")
      .append("    for (int n = 0 ; n < propertyValueAliases.length ; n += 2) {\n")
      .append("      String alias = propertyValueAliases[n];\n")
      .append("      String propertyValue = propertyValueAliases[n + 1];\n")
      .append("      List<Interval> targetIntervals = propertyValueIntervals.get(propertyValue);\n")
      .append("      if (null != targetIntervals) {\n")
      .append("        propertyValueIntervals.put(alias, targetIntervals);\n")
      .append("      }\n")
      .append("    }\n")
      .append("    bindInvariantIntervals();\n")
      .append("  }\n\n");
    builder.append("  private void bindInvariantIntervals() {\n")
      .append("    List<Interval> asciiIntervals = new ArrayList<Interval>();\n")
      .append("    asciiIntervals.add(new Interval('\\000', '\\u00FF'));\n")
      .append("    propertyValueIntervals.put(normalize(\"ASCII\"), asciiIntervals);\n")
      .append("    List<Interval> anyIntervals = new ArrayList<Interval>();\n")
      //TODO: Change 'Any' interval to cover through maximumCodePoint when JFlex moves beyond the BMP
      .append("    anyIntervals.add(new Interval('\\000', '\\uFFFF'));\n")
      .append("    propertyValueIntervals.put(normalize(\"Any\"), anyIntervals);\n")
      .append("  }\n\n");
    builder.append("  /**\n")
      .append("   * Returns a set of character intervals representing all characters\n")
      .append("   * that are case-insensitively equivalent to the given character,\n")
      .append("   * including the given character itself.\n")
      .append("   *\n")
      .append("   * @param c The character for which to return case-insensitive\n")
      .append("   *  equivalents.\n")
      .append("   * @return All case-insensitively equivalent characters, or null\n")
      .append("   *  if the given character is case-insensitively equivalent to itself.\n")
      .append("   */\n")
      .append("  public IntCharSet getCaselessMatches(char c) {\n")
      .append("    if (null == caselessMatches)\n")
      .append("      initCaselessMatches();\n")
      .append("    return caselessMatches[c];\n")
      .append("  }\n\n");
    builder.append("  private void initCaselessMatches() {\n")
      .append("    caselessMatches = new IntCharSet[maximumCodePoint + 1];\n")
      .append("    int[] members = new int[caselessMatchPartitionSize];\n")
      .append("    for (int index = 0 ; index < caselessMatchPartitions.length() ; ) {\n")
      .append("      IntCharSet partition = new IntCharSet();\n")
      .append("      for (int n = 0 ; n < caselessMatchPartitionSize ; ++n) {\n")
      .append("        int c = caselessMatchPartitions.codePointAt(index);\n")
      .append("        index += (c <= 0xFFFF ? 1 : 2);\n")
      .append("        members[n] = c;\n")
      //TODO: Change the character type from char to int
      //TODO: Remove BMP boundary condition
      .append("        if (c > 0 && c <= 0xFFFF)\n")
      .append("          partition.add((char)c);\n")
      .append("      }\n")
      .append("      if (partition.containsElements()) {\n")
      .append("        for (int n = 0 ; n < caselessMatchPartitionSize ; ++n) {\n")
      .append("          if (members[n] > 0)\n")
      .append("            caselessMatches[members[n]] = partition;\n")
      .append("        }\n")
      .append("      }\n")
      .append("    }\n")
      .append("  }\n\n");
    builder.append("  String normalize(String identifier) {\n")
      .append("    if (null == identifier)\n")
      .append("      return identifier;\n")
      .append("    String normalized\n")
      .append("      = WORD_SEP_PATTERN.matcher(identifier.toLowerCase()).replaceAll(\"\");\n")
      .append("    return normalized.replace(':', '=');\n")
      .append("  }\n\n");
    builder.append("  class UnsupportedUnicodeVersionException ")
      .append("extends Exception {\n")
      .append("    public UnsupportedUnicodeVersionException(String version) {\n")
      .append("      super(\"Unsupported Unicode version '\" + version\n")
      .append("            + \"'.\\nSupported versions: ");
    isFirst = true;
    for (String majorMinorVersion : unicodeVersions.keySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        builder.append(", ");
      }
      if (majorMinorVersion.indexOf(".0") == majorMinorVersion.length() - 2) {
        String majorVersion
          = majorMinorVersion.substring(0, majorMinorVersion.indexOf("."));
        builder.append(majorVersion).append(", ");
      }
      builder.append(majorMinorVersion).append(", ")
        .append(unicodeVersions.get(majorMinorVersion).majorMinorUpdateVersion);
    }
    builder.append("\");\n")
      .append("    }\n")
      .append("  }\n")
      .append("}\n");
  }

  /**
   * Writes the contents of the given StringBuilder out to
   * UnicodeProperties.java.
   *
   * @param builder What to write out
   * @throws IOException If there is an error writing out UnicodeProperties.java.
   */
  private void writeOutputFile(StringBuilder builder) throws IOException {
    PrintWriter writer = new PrintWriter(getOutputFile(), "UTF-8");
    writer.write(builder.toString());
    writer.flush();
    writer.close();
  }

  /**
   * Constructs and returns the name of the output file, based on the name
   * of the output class {@value #OUTPUT_CLASS_NAME}.
   *
   * @return The name of the output file.
   */
  private File getOutputFile() {
    return new File(outputDirectory, OUTPUT_CLASS_NAME + ".java");
  }
}
