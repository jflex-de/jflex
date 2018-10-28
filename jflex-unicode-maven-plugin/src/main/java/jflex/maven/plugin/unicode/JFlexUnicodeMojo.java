/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.6 Unicode Maven 3 plugin                                        *
 * Copyright (c) 2008 Steve Rowe <steve_rowe@users.sf.net>                 *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.maven.plugin.unicode;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Generates source code for JFlex Unicode character property handling. */
@Mojo(name = "generate-unicode-properties", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class JFlexUnicodeMojo extends AbstractMojo {

  /** Top-level directory URL from which to download Unicode data */
  private static final String UNICODE_DOT_ORG_URL = "http://www.unicode.org/Public/";

  /** Pattern for Unicode version links in the page at {@value #UNICODE_DOT_ORG_URL} */
  private static final Pattern UNICODE_VERSION_LINK_PATTERN =
      Pattern.compile(
          "<a\\s+href\\s*=\\s*\"((\\d+(?:\\.\\d+){1,2})(?i:-(Update(\\d*)))?/)\"\\s*>",
          Pattern.CASE_INSENSITIVE);

  /** Buffer size to use when reading web page content */
  private static final int BUF_SIZE = 4096;

  /** Name of the directory into which the code will be generated. */
  @Parameter(defaultValue = "${basedir}/src/main/java/jflex/unicode")
  private File outputDirectory = null;

  /** Maps validated major.minor unicode versions to information about the version. */
  private SortedMap<String, UnicodeVersion> unicodeVersions = new TreeMap<>();

  /** The name of the skeleton file for the output class. */
  private static final String SKELETON_FILENAME = Emitter.OUTPUT_CLASS_NAME + ".java.skeleton";

  /** Pattern for links that lead to sub-directories on Unicode.org directory listing web pages. */
  private static final Pattern DIRECTORY_LINK_PATTERN =
      Pattern.compile("<a\\s+href\\s*=\\s*\"([^/\"]+/)\"\\s*>", Pattern.CASE_INSENSITIVE);

  /**
   *
   *
   * <ol>
   *   <li>Collects and validates Unicode versions to support from <a
   *       href="http://www.unicode.org/Public/">Unicode.org</a>; finds the most recent updates for
   *       non-beta versions, so that JFlex scanner specs can specify major or major.minor Unicode
   *       versions as an optional parameter to the %unicode option.
   *   <li>For each version:
   *       <ol type="a">
   *         <li>Downloads the following Unicode data files:
   *             <ul>
   *               <li>UnicodeData(-X.X.X).txt
   *               <li>PropertyAliases(-X.X.X).txt
   *               <li>PropertyValueAliases(-X.X.X).txt
   *               <li>DerivedCoreProperties(-X.X.X).txt
   *               <li>Scripts(-X.X.X).txt
   *               <li>Blocks(-X.X.X).txt
   *               <li>PropList(-X.X.X).txt
   *               <li>LineBreak(-X.X.X).txt
   *               <li>GraphemeBreakProperty.txt
   *               <li>SentenceBreakProperty.txt
   *               <li>WordBreakProperty.txt
   *               <li>DerivedAge.txt (the latest version's only)
   *             </ul>
   *         <li>Parses the data files, extracting ranges of code points and property values
   *             associated with them (see <a href="http://www.unicode.org/reports/tr23/">The
   *             Unicode Character Property Model</a>).
   *       </ol>
   *   <li>Generates Java source for class UnicodeProperties:
   *       <ol type="a">
   *         <li>Has a constructor taking in a string representing the Unicode version given as
   *             parameter to the %unicode JFlex specification option.
   *         <li>Has a default constructor that defaults the version of Unicode to that supported by
   *             the JRE, for JFlex specifications that have parameter-less %unicode options.
   *         <li>Has per-Unicode-version compressed tables for each property value, which will be
   *             unpacked by the constructor for the selected Unicode version.
   *         <li>Binds the Unicode-version-specific unpacked tables to code point ranges for named
   *             property values.
   *         <li>Binds property value aliases, e.g. \p{Letter} for \p{L} (which is
   *             [\p{Lu}\p{Ll}\p{Lt}\p{Lm}\p{Lo}] ) - see <a
   *             href="http://www.unicode.org/Public/UNIDATA/UCD.html#General_Category_Values">
   *             Unicode General Category Property Values</a>.
   *         <li>Has Unicode-version-specific method maximumCodePoint():int.
   *       </ol>
   * </ol>
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      getLog().info("Downloading Unicode data from " + UNICODE_DOT_ORG_URL + "\n");
      collectUnicodeVersions();
      Emitter emitter = new Emitter(outputDirectory);
      emitter.emitUnicodeProperties(SKELETON_FILENAME);
      emitter.emitVersionedUnicodeData();
    } catch (Exception e) {
      throw new MojoExecutionException("Exception", e);
    }
  }

  /**
   * Searches unicode.org for available Unicode versions, and collects all supported property data
   * for each version.
   *
   * @throws IOException If there is a problem fetching or parsing the data
   */
  private void collectUnicodeVersions() throws IOException {
    // Maps available versions to maps from update numbers to relative URLs.
    // A version with no update is given update number "-1" for the purposes
    // of comparison.
    SortedMap<String, SortedMap<Integer, String>> allUnicodeVersions = new TreeMap<>();

    URL unicodeURL = new URL(UNICODE_DOT_ORG_URL);
    Matcher matcher = UNICODE_VERSION_LINK_PATTERN.matcher(getPageContent(unicodeURL));
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
      SortedMap<Integer, String> updates = allUnicodeVersions.get(baseVersion);
      if (null == updates) {
        updates = new TreeMap<>(Collections.reverseOrder());
        allUnicodeVersions.put(baseVersion, updates);
      }
      updates.put(updateNumber, relativeURL);
    }
    for (Entry<String, SortedMap<Integer, String>> entry : allUnicodeVersions.entrySet()) {
      populateUnicodeVersion(entry.getKey(), entry.getValue());
    }
  }

  /**
   * Given a Unicode version identifier and a corresponding set of relative URLs, one for each
   * available update, populates properties for this Unicode version.
   *
   * @param version The Unicode version, either in form "X.X.X" or "X.X"
   * @param relativeURLs A sorted map from update number to relative URL
   * @throws IOException If there is a problem fetching or parsing the data
   */
  private void populateUnicodeVersion(String version, SortedMap<Integer, String> relativeURLs)
      throws IOException {

    EnumMap<DataFileType, URL> dataFiles = new EnumMap<>(DataFileType.class);

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
        versionedDirectoryListing = getPageContent(baseURL);
      }

      for (DataFileType fileType : DataFileType.values()) {
        if (null == dataFiles.get(fileType)) {
          URL url = fileType.getURL(version, baseURL, versionedDirectoryListing);
          if (null != url) {
            dataFiles.put(fileType, url);
          }
        }
      }

      // Visit nested directories, e.g. <a href="auxiliary/">
      Matcher matcher = DIRECTORY_LINK_PATTERN.matcher(versionedDirectoryListing);
      while (matcher.find()) {
        URL nestedBaseURL = new URL(baseURL, matcher.group(1));
        String nestedVersionedDirectoryListing = getPageContent(nestedBaseURL);
        for (DataFileType fileType : DataFileType.values()) {
          if (null == dataFiles.get(fileType)) {
            URL url = fileType.getURL(version, nestedBaseURL, nestedVersionedDirectoryListing);
            if (null != url) {
              dataFiles.put(fileType, url);
            }
          }
        }
      }
    }
    if (null != dataFiles.get(DataFileType.UNICODE_DATA)) {
      // If UnicodeData(-X.X.X).txt was found, then this version of Unicode
      // is not a beta version, so we can proceed to fetch, parse, and emit.
      UnicodeVersion unicodeVersion = new UnicodeVersion(version, dataFiles);
      fetchAndParseDataFiles(unicodeVersion);
      unicodeVersion.addCompatibilityProperties();
      unicodeVersions.put(unicodeVersion.majorMinorVersion, unicodeVersion);
      getLog()
          .info(
              "Completed downloading and parsing Unicode "
                  + unicodeVersion.majorMinorVersion
                  + " data.\n");
    }
  }
  /**
   * Fetches and parses the data files defined for this Unicode version.
   *
   * @throws IOException If there is a problem fetching or parsing any of this version's data files.
   */
  void fetchAndParseDataFiles(UnicodeVersion unicodeVersion) throws IOException {
    // Use the enum ordering to process in the correct order
    for (EnumMap.Entry<DataFileType, URL> entry : unicodeVersion.dataFiles.entrySet()) {
      DataFileType fileType = entry.getKey();
      URL url = entry.getValue();
      getLog().info("\t\tFetching/parsing: " + url.getPath());
      fileType.scan(url, unicodeVersion);
      getLog().info("\t\tCompleted: " + url.getPath());
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
    try (InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8")) {
      StringBuilder builder = new StringBuilder();
      char[] buf = new char[BUF_SIZE];
      int charsRead;
      while ((charsRead = reader.read(buf)) > 0) {
        builder.append(buf, 0, charsRead);
      }
      return builder.toString();
    }
  }
}
