/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.ucd.UcdReleases;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * The cases that run the hand-written implementations of the UAX #14 and UAX #29 segmentation rules
 * on the test files of the UCD.
 *
 * <p>The specs and their {@code .test} files are in {@code src/test/uax} of the unicode test
 * module, one per Unicode version. The scanner input is the UCD's {@code *BreakTest.txt} and the
 * golden is the test file's own column of expected breaks, so both come from the UCD.
 *
 * <p>The test files of older Unicode versions have no code points outside the BMP. For them, a
 * second input replaces BMP code points of the tests by supplementary code points of the same break
 * property, which leaves the expected breaks unchanged: the rules only look at the property. The
 * generator checks the property values in the UCD of that version.
 */
final class BreakTestCases {

  /** The cases of one segmentation algorithm. */
  private static final class Family {
    final String caseDir;
    final String namePrefix;
    final String testFile;
    final String propertyFile;

    /** Pairs of a BMP and a supplementary code point that have the same break property. */
    final int[][] substitutions;

    Family(
        String caseDir,
        String namePrefix,
        String testFile,
        String propertyFile,
        int[]... substitutions) {
      this.caseDir = caseDir;
      this.namePrefix = namePrefix;
      this.testFile = testFile;
      this.propertyFile = propertyFile;
      this.substitutions = substitutions;
    }
  }

  // The supplementary code points: U+10000 LINEAR B SYLLABLE B008 A (a letter), U+10400 and
  // U+10428 DESERET CAPITAL and SMALL LETTER LONG I, U+1D7CE MATHEMATICAL BOLD DIGIT ZERO,
  // U+1D167 MUSICAL SYMBOL COMBINING TREMOLO-1 (a non-spacing mark).
  private static final Family[] FAMILIES = {
    new Family(
        "unicode-line-break",
        "UnicodeLineBreakAlgorithm",
        "auxiliary/LineBreakTest.txt",
        "LineBreak.txt",
        new int[] {0x0023, 0x10000},
        new int[] {0x0031, 0x1D7CE},
        new int[] {0x0308, 0x1D167}),
    new Family(
        "unicode-grapheme-break",
        "UnicodeGraphemeBreakRules",
        "auxiliary/GraphemeBreakTest.txt",
        "auxiliary/GraphemeBreakProperty.txt",
        new int[] {0x0308, 0x1D167}),
    new Family(
        "unicode-word-break",
        "UnicodeWordBreakRules",
        "auxiliary/WordBreakTest.txt",
        "auxiliary/WordBreakProperty.txt",
        new int[] {0x0061, 0x10000},
        new int[] {0x0031, 0x1D7CE},
        new int[] {0x0308, 0x1D167}),
    new Family(
        "unicode-sentence-break",
        "UnicodeSentenceBreakRules",
        "auxiliary/SentenceBreakTest.txt",
        "auxiliary/SentenceBreakProperty.txt",
        new int[] {0x0041, 0x10400},
        new int[] {0x0061, 0x10428},
        new int[] {0x0308, 0x1D167}),
  };

  private static final Pattern CODE_POINT = Pattern.compile("[0-9A-Fa-f]{4,6}");

  /**
   * Generates the cases of a version that have a spec in {@code uaxDir}.
   *
   * @return the number of cases
   */
  static int generate(Version version, Path ucdDir, Path uaxDir, Path casesDir) throws IOException {
    int count = 0;
    for (Family family : FAMILIES) {
      String name = family.namePrefix + "_" + GeneratedCase.underscoreVersion(version);
      Path spec = uaxDir.resolve(family.caseDir).resolve(name + ".flex");
      if (!Files.isRegularFile(spec)) {
        continue;
      }
      Path testFile = require(ucdDir, version, family.testFile);
      Path dir = casesDir.resolve(family.caseDir);
      Files.createDirectories(dir);
      Files.copy(spec, dir.resolve(name + ".flex"), StandardCopyOption.REPLACE_EXISTING);
      Files.copy(
          uaxDir.resolve(family.caseDir).resolve(name + ".test"),
          dir.resolve(name + ".test"),
          StandardCopyOption.REPLACE_EXISTING);
      GeneratedCase.write(dir.resolve(name + "-flex.output"), "");
      List<String> input = new ArrayList<>();
      List<String> expected = new ArrayList<>();
      boolean hasSupplementary = false;
      for (String line : UcdText.lines(testFile.toFile())) {
        // JFlex scanners read UTF-16; a lone surrogate cannot reach them.
        if (hasSurrogate(line)) {
          continue;
        }
        input.add(line);
        if (!line.startsWith("#")) {
          String breaks = UcdText.stripComment(line);
          if (!breaks.isEmpty()) {
            expected.add(breaks);
            hasSupplementary |= hasSupplementary(breaks);
          }
        }
      }
      GeneratedCase.write(dir.resolve(name + "-0.input"), join(input));
      GeneratedCase.write(dir.resolve(name + "-0.output"), join(expected));
      if (!hasSupplementary) {
        List<String> substituted =
            substitute(expected, family, require(ucdDir, version, family.propertyFile).toFile());
        GeneratedCase.write(
            dir.resolve(name + "-1.input"), supplementaryHeader(family) + join(substituted));
        GeneratedCase.write(dir.resolve(name + "-1.output"), join(substituted));
      }
      count++;
    }
    return count;
  }

  private static Path require(Path ucdDir, Version version, String file) {
    Path path = UcdReleases.findFile(ucdDir, version, file);
    if (path == null) {
      throw new IllegalStateException("No " + file + " for Unicode " + version);
    }
    return path;
  }

  /**
   * The tests that change when the BMP code points of the family's substitutions are replaced by
   * their supplementary code points.
   *
   * @throws IllegalStateException if a pair does not have the same property in this version
   */
  private static List<String> substitute(List<String> tests, Family family, File propertyFile)
      throws IOException {
    List<Range> property = new ArrayList<>();
    for (String line : UcdText.lines(propertyFile)) {
      Range range = UcdText.parseRange(UcdText.stripComment(line));
      if (range != null) {
        property.add(range);
      }
    }
    for (int[] pair : family.substitutions) {
      String bmp = valueOf(property, pair[0]);
      String supplementary = valueOf(property, pair[1]);
      if (bmp == null || !bmp.equals(supplementary)) {
        throw new IllegalStateException(
            String.format(
                Locale.ROOT,
                "%s: U+%04X is %s but U+%04X is %s",
                propertyFile,
                pair[0],
                bmp,
                pair[1],
                supplementary));
      }
    }
    List<String> substituted = new ArrayList<>();
    for (String test : tests) {
      List<String> tokens = new ArrayList<>(Arrays.asList(test.split("\\s+")));
      boolean changed = false;
      for (int i = 0; i < tokens.size(); i++) {
        String token = tokens.get(i);
        if (!CODE_POINT.matcher(token).matches()) {
          continue;
        }
        int codePoint = UcdText.hex(token);
        for (int[] pair : family.substitutions) {
          if (codePoint == pair[0]) {
            tokens.set(i, String.format(Locale.ROOT, "%04X", pair[1]));
            changed = true;
          }
        }
      }
      if (changed) {
        substituted.add(String.join(" ", tokens));
      }
    }
    return substituted;
  }

  private static String supplementaryHeader(Family family) {
    StringBuilder header =
        new StringBuilder("# The tests of ")
            .append(family.testFile)
            .append(" without supplementary code points, with\n");
    for (int[] pair : family.substitutions) {
      header.append(
          String.format(
              Locale.ROOT,
              "# U+%04X replaced by U+%04X, which has the same property in %s\n",
              pair[0],
              pair[1],
              family.propertyFile));
    }
    return header.append('\n').toString();
  }

  /** The value of the property at the code point, or null if the file does not list it. */
  private static String valueOf(List<Range> property, int codePoint) {
    for (Range range : property) {
      if (range.start <= codePoint && codePoint <= range.end) {
        return range.value;
      }
    }
    return null;
  }

  /** Whether the test on the line, before its comment, has a surrogate code point. */
  private static boolean hasSurrogate(String line) {
    for (String token : UcdText.stripComment(line).split("\\s+")) {
      if (CODE_POINT.matcher(token).matches()) {
        int codePoint = UcdText.hex(token);
        if (codePoint >= 0xD800 && codePoint <= 0xDFFF) {
          return true;
        }
      }
    }
    return false;
  }

  /** Whether the test has a code point outside the BMP. */
  private static boolean hasSupplementary(String test) {
    for (String token : test.split("\\s+")) {
      if (CODE_POINT.matcher(token).matches() && UcdText.hex(token) > 0xFFFF) {
        return true;
      }
    }
    return false;
  }

  private static String join(List<String> lines) {
    StringBuilder text = new StringBuilder();
    for (String line : lines) {
      text.append(line).append('\n');
    }
    return text.toString();
  }

  private BreakTestCases() {}
}
