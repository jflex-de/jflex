/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdReleases;
import de.jflex.ucd.UcdVersion;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Generates the Unicode test cases of the testsuite from the Unicode Character Database, for every
 * version in {@link UcdReleases}.
 *
 * <p>The cases check the character classes of JFlex against the UCD, so this generator must not use
 * JFlex or the code that builds its tables.
 *
 * <p>Arguments: the directory with the UCD downloads of {@code tests/unicode/pom.xml}; the unicode
 * test module, for the shared resources and the UAX specs; and the output directory, which gets the
 * same layout: {@code src/test/cases} and {@code src/test/resources}.
 */
public final class UnicodeCasesGenerator {
  private final Path ucdDir;
  private final Path suiteDir;
  private final Path casesDir;

  public static void main(String[] args) throws IOException {
    if (args.length != 3) {
      System.err.println("Usage: UnicodeCasesGenerator <ucd-dir> <test-module-dir> <out-dir>");
      System.exit(1);
    }
    Path outDir = Paths.get(args[2]);
    int count =
        new UnicodeCasesGenerator(Paths.get(args[0]), Paths.get(args[1]), outDir).generate();
    System.out.println("Generated " + count + " Unicode test cases in " + outDir);
  }

  private UnicodeCasesGenerator(Path ucdDir, Path suiteDir, Path outDir) {
    this.ucdDir = ucdDir;
    this.suiteDir = suiteDir;
    this.casesDir = outDir.resolve("src").resolve("test").resolve("cases");
  }

  /** Generates the cases of all versions and copies the resources they use. */
  private int generate() throws IOException {
    Path resources = suiteDir.resolve("src").resolve("test").resolve("resources");
    Path resourcesOut = casesDir.resolveSibling("resources");
    Files.createDirectories(resourcesOut);
    try (Stream<Path> files = Files.list(resources)) {
      for (Path file : (Iterable<Path>) files::iterator) {
        if (Files.isRegularFile(file)) {
          Files.copy(
              file, resourcesOut.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
      }
    }
    int count = 0;
    for (Version version : UcdReleases.versions()) {
      count += generate(version);
    }
    return count;
  }

  private int generate(Version version) throws IOException {
    UcdVersion ucd = UcdVersion.findUcdFiles(version, UcdReleases.ucdFiles(ucdDir, version));
    File unicodeData = require(ucd, UcdFileType.UnicodeData, "1.1");
    File propList = require(ucd, UcdFileType.PropList, "2.0");
    File derivedCoreProperties = require(ucd, UcdFileType.DerivedCoreProperties, "3.1");
    File lineBreak = require(ucd, UcdFileType.LineBreak, "3.0");
    File scripts = require(ucd, UcdFileType.Scripts, "3.1");
    File scriptExtensions = require(ucd, UcdFileType.ScriptExtensions, "6.0");
    File propertyValueAliases = require(ucd, UcdFileType.PropertyValueAliases, "3.2");
    File graphemeBreak = require(ucd, UcdFileType.GraphemeBreakProperty, "4.1");
    File wordBreak = require(ucd, UcdFileType.WordBreakProperty, "4.1");
    File sentenceBreak = require(ucd, UcdFileType.SentenceBreakProperty, "4.1");

    List<GeneratedCase> cases = new ArrayList<>();
    cases.addAll(GeneralCategoryCases.generate(version, unicodeData, propertyValueAliases));
    cases.add(ClassSyntaxCases.letter(version, unicodeData));
    cases.add(ClassSyntaxCases.lowercase(version, unicodeData, propList, derivedCoreProperties));
    cases.add(ClassSyntaxCases.uppercase(version, unicodeData, propList, derivedCoreProperties));
    cases.addAll(ClassSyntaxCases.whiteSpace(version, unicodeData, propList));
    cases.addAll(ClassSyntaxCases.word(version, unicodeData, propList, derivedCoreProperties));
    cases.addAll(BinaryPropertyCases.misc(version, unicodeData));
    if (propList != null) {
      cases.addAll(BinaryPropertyCases.propList(version, propList));
    }
    if (derivedCoreProperties != null) {
      cases.addAll(BinaryPropertyCases.derivedCoreProperties(version, derivedCoreProperties));
    }
    if (lineBreak != null) {
      cases.add(EnumeratedPropertyCases.lineBreak(version, lineBreak));
    }
    if (scripts != null) {
      cases.addAll(ScriptsCases.generate(version, scripts, scriptExtensions, propertyValueAliases));
    }
    if (graphemeBreak != null) {
      cases.add(
          EnumeratedPropertyCases.breakProperty(
              version, graphemeBreak, "unicode-grapheme-break", "GraphemeClusterBreak"));
      cases.add(
          EnumeratedPropertyCases.breakProperty(
              version, wordBreak, "unicode-word-break", "WordBreak"));
      cases.add(
          EnumeratedPropertyCases.breakProperty(
              version, sentenceBreak, "unicode-sentence-break", "SentenceBreak"));
    }
    for (GeneratedCase c : cases) {
      c.write(casesDir);
    }
    Path uaxDir = suiteDir.resolve("src").resolve("test").resolve("uax");
    return cases.size() + BreakTestCases.generate(version, ucdDir, uaxDir, casesDir);
  }

  /**
   * The file of {@code type} in the version, or null if the version is older than {@code since}.
   *
   * @throws IllegalStateException if the version should have the file but has not
   */
  private static File require(UcdVersion ucd, UcdFileType type, String since) {
    File file = ucd.getFile(type);
    if (file == null
        && Version.MAJOR_MINOR_COMPARATOR.compare(ucd.version(), new Version(since)) >= 0) {
      throw new IllegalStateException("No " + type + " file for Unicode " + ucd.version());
    }
    return file;
  }
}
