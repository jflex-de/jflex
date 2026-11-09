/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testage;

import de.jflex.testing.unicodedata.SimpleDerivedCorePropertiesParser;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;

/**
 * The expected output of the age scanners of a Unicode version, computed from {@code
 * DerivedAge.txt}.
 *
 * <p>The scanners read all code points of a {@link Dataset} and record a label for each code point
 * that a rule matches. The expected output lists the maximal runs of consecutive code points with
 * the same label, in code point order. Runs do not extend over the surrogates, which the datasets
 * leave out.
 */
final class UnicodeAgeGoldens {

  private static final int NO_AGE = -1;

  /** The ages up to the Unicode version, oldest first. */
  private final List<Version> ages;

  /** The index in {@link #ages} of the age of each code point, or {@link #NO_AGE}. */
  private final int[] ageIndex;

  private UnicodeAgeGoldens(List<Version> ages, int[] ageIndex) {
    this.ages = ages;
    this.ageIndex = ageIndex;
  }

  /**
   * Reads the ages of a Unicode version.
   *
   * @param version the Unicode version
   * @param ages the ages up to {@code version}, oldest first
   * @param dataset the code points the scanners read
   * @param derivedAgeTxt the {@code DerivedAge.txt} of {@code version}, or of a later version
   */
  static UnicodeAgeGoldens create(
      Version version, List<Version> ages, Dataset dataset, Path derivedAgeTxt) throws IOException {
    int[] ageIndex = new int[dataset.lastCodePoint() + 1];
    Arrays.fill(ageIndex, NO_AGE);
    for (NamedCodepointRange<String> range :
        SimpleDerivedCorePropertiesParser.parseProperties(derivedAgeTxt)) {
      Version age = new Version(range.name());
      if (Version.MAJOR_MINOR_COMPARATOR.compare(age, version) > 0) {
        // A later DerivedAge.txt also has the code points of the later versions.
        continue;
      }
      int index = ages.indexOf(age);
      if (index == NO_AGE) {
        throw new IllegalArgumentException(
            "Age " + age + " in " + derivedAgeTxt + " is not one of " + ages);
      }
      int end = Math.min(range.end(), dataset.lastCodePoint());
      for (int codePoint = range.start(); codePoint <= end; codePoint++) {
        ageIndex[codePoint] = index;
      }
    }
    return new UnicodeAgeGoldens(ages, ageIndex);
  }

  /** The expected output of the scanner for {@code \p{Age:age}}, i.e. age or older. */
  List<NamedCodepointRange<String>> age(Version age) {
    int maxIndex = ages.indexOf(age);
    String label = "Age:" + age;
    return blocks(cp -> ageIndex[cp] != NO_AGE && ageIndex[cp] <= maxIndex ? label : null);
  }

  /** The expected output of the scanner for {@code \p{Age:Unassigned}}. */
  List<NamedCodepointRange<String>> unassigned() {
    return blocks(cp -> ageIndex[cp] == NO_AGE ? "Age:Unassigned" : null);
  }

  /**
   * The expected output of the scanner with one rule {@code [\p{Age:newer}--\p{Age:older}]} per
   * pair of consecutive ages, see {@link UnicodeAgeSubtractionFlexGenerator}.
   */
  List<NamedCodepointRange<String>> subtraction() {
    String[] labels = new String[ages.size()];
    for (int i = 1; i < ages.size(); i++) {
      labels[i] = String.format("[\\p{Age:%s}--\\p{Age:%s}]", ages.get(i), ages.get(i - 1));
    }
    // The oldest age and the unassigned code points match no rule.
    return blocks(cp -> ageIndex[cp] > 0 ? labels[ageIndex[cp]] : null);
  }

  /** Returns the maximal runs of code points with the same label; null means no label. */
  private List<NamedCodepointRange<String>> blocks(IntFunction<String> label) {
    List<NamedCodepointRange<String>> blocks = new ArrayList<>();
    String current = null;
    int start = 0;
    for (int codePoint = 0; codePoint <= ageIndex.length; codePoint++) {
      String next =
          codePoint == ageIndex.length || isSurrogate(codePoint) ? null : label.apply(codePoint);
      if (!Objects.equals(next, current)) {
        if (current != null) {
          blocks.add(NamedCodepointRange.create(current, start, codePoint - 1));
        }
        current = next;
        start = codePoint;
      }
    }
    return blocks;
  }

  private static boolean isSurrogate(int codePoint) {
    return codePoint >= 0xD800 && codePoint <= 0xDFFF;
  }
}
