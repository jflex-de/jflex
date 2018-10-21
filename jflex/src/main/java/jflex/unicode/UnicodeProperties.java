/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Unicode Properties                                                *
 * Copyright (c) 2008-2103  Steve Rowe <sarowe@gmail.com>                  *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.unicode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jflex.core.IntCharSet;
import jflex.chars.Interval;
import jflex.unicode.data.Unicode_1_1;
import jflex.unicode.data.Unicode_2_0;
import jflex.unicode.data.Unicode_2_1;
import jflex.unicode.data.Unicode_3_0;
import jflex.unicode.data.Unicode_3_1;
import jflex.unicode.data.Unicode_3_2;
import jflex.unicode.data.Unicode_4_0;
import jflex.unicode.data.Unicode_4_1;
import jflex.unicode.data.Unicode_5_0;
import jflex.unicode.data.Unicode_5_1;
import jflex.unicode.data.Unicode_5_2;
import jflex.unicode.data.Unicode_6_0;
import jflex.unicode.data.Unicode_6_1;
import jflex.unicode.data.Unicode_6_2;
import jflex.unicode.data.Unicode_6_3;
import jflex.unicode.data.Unicode_7_0;
import jflex.unicode.data.Unicode_8_0;
import jflex.unicode.data.Unicode_9_0;

/**
 * This class was automatically generated by jflex-unicode-maven-plugin based on data files
 * downloaded from unicode.org.
 *
 * @author JFlex contributors.
 */
public class UnicodeProperties {

  /** Constant {@code UNICODE_VERSIONS="1.1, 1.1.5, 2, 2.0, 2.0.14, 2.1, 2.1.9,"{trunked}} */
  public static final String UNICODE_VERSIONS =
      "1.1, 1.1.5, 2, 2.0, 2.0.14, 2.1, 2.1.9, 3, 3.0, 3.0.1, 3.1, 3.1.0, 3.2, 3.2.0, 4, 4.0, 4.0.1, 4.1, 4.1.0, 5, 5.0, 5.0.0, 5.1, 5.1.0, 5.2, 5.2.0, 6, 6.0, 6.0.0, 6.1, 6.1.0, 6.2, 6.2.0, 6.3, 6.3.0, 7, 7.0, 7.0.0, 8, 8.0, 8.0.0, 9, 9.0, 9.0.0";

  private static final String DEFAULT_UNICODE_VERSION = "9.0";
  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

  private int maximumCodePoint;
  private Map<String, IntCharSet> propertyValueIntervals = new HashMap<>();
  private String caselessMatchPartitions;
  private int caselessMatchPartitionSize;
  private IntCharSet caselessMatches[];

  /**
   * Unpacks the Unicode data corresponding to the default Unicode version: "{@value
   * #DEFAULT_UNICODE_VERSION}".
   *
   * @throws jflex.unicode.UnicodeProperties.UnsupportedUnicodeVersionException if the default
   *     version is not supported.
   */
  public UnicodeProperties() throws UnsupportedUnicodeVersionException {
    init(DEFAULT_UNICODE_VERSION);
  }

  /**
   * Unpacks the Unicode data corresponding to the given version.
   *
   * @param version The Unicode version for which to unpack data
   * @throws jflex.unicode.UnicodeProperties.UnsupportedUnicodeVersionException if the given version
   *     is not supported.
   */
  public UnicodeProperties(String version) throws UnsupportedUnicodeVersionException {
    init(version);
  }

  /**
   * Returns the maximum code point for the selected Unicode version.
   *
   * @return the maximum code point for the selected Unicode version.
   */
  public int getMaximumCodePoint() {
    return maximumCodePoint;
  }

  /**
   * Returns the character interval set associated with the given property value for the selected
   * Unicode version.
   *
   * @param propertyValue The Unicode property or property value (or alias for one of these) for
   *     which to return the corresponding character intervals.
   * @return The character interval set corresponding to the given property value, if a match
   *     exists, and null otherwise.
   */
  public IntCharSet getIntCharSet(String propertyValue) {
    return propertyValueIntervals.get(normalize(propertyValue));
  }

  /**
   * Returns the set of all properties, property values, and their aliases supported by the
   * specified Unicode version.
   *
   * @return The set of all properties supported by the specified Unicode version
   */
  public Set<String> getPropertyValues() {
    return propertyValueIntervals.keySet();
  }

  /**
   * Returns a set of character intervals representing all characters that are case-insensitively
   * equivalent to the given character, including the given character itself.
   *
   * <p>The first call to this method lazily initializes the backing data.
   *
   * @param c The character for which to return case-insensitive equivalents.
   * @return All case-insensitively equivalent characters, or null if the given character is
   *     case-insensitively equivalent only to itself.
   */
  public IntCharSet getCaselessMatches(int c) {
    if (null == caselessMatches) initCaselessMatches();
    return caselessMatches[c];
  }

  /**
   * Unpacks the caseless match data. Called from {@link #getCaselessMatches(int)} to lazily
   * initialize.
   */
  private void initCaselessMatches() {
    caselessMatches = new IntCharSet[maximumCodePoint + 1];
    int[] members = new int[caselessMatchPartitionSize];
    for (int index = 0; index < caselessMatchPartitions.length(); ) {
      IntCharSet partition = new IntCharSet();
      for (int n = 0; n < caselessMatchPartitionSize; ++n) {
        int c = caselessMatchPartitions.codePointAt(index);
        index += Character.charCount(c);
        members[n] = c;
        if (c > 0) partition.add(c); // ignore trailing zero padding
      }
      if (partition.containsElements()) {
        for (int n = 0; n < caselessMatchPartitionSize; ++n) {
          if (members[n] > 0) caselessMatches[members[n]] = partition;
        }
      }
    }
  }

  /**
   * Based on the given version, selects and binds the corresponding Unicode data to facilitate
   * mappings from property values to character intervals.
   *
   * @param version The Unicode version for which to bind data
   * @throws UnsupportedUnicodeVersionException if the given version is not supported.
   */
  private void init(String version) throws UnsupportedUnicodeVersionException {

    if (Objects.equals(version, "1.1") || Objects.equals(version, "1.1.5")) {
      bind(
          Unicode_1_1.propertyValues,
          Unicode_1_1.intervals,
          Unicode_1_1.propertyValueAliases,
          Unicode_1_1.maximumCodePoint,
          Unicode_1_1.caselessMatchPartitions,
          Unicode_1_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "2")
        || Objects.equals(version, "2.0")
        || Objects.equals(version, "2.0.14")) {
      bind(
          Unicode_2_0.propertyValues,
          Unicode_2_0.intervals,
          Unicode_2_0.propertyValueAliases,
          Unicode_2_0.maximumCodePoint,
          Unicode_2_0.caselessMatchPartitions,
          Unicode_2_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "2.1") || Objects.equals(version, "2.1.9")) {
      bind(
          Unicode_2_1.propertyValues,
          Unicode_2_1.intervals,
          Unicode_2_1.propertyValueAliases,
          Unicode_2_1.maximumCodePoint,
          Unicode_2_1.caselessMatchPartitions,
          Unicode_2_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "3")
        || Objects.equals(version, "3.0")
        || Objects.equals(version, "3.0.1")) {
      bind(
          Unicode_3_0.propertyValues,
          Unicode_3_0.intervals,
          Unicode_3_0.propertyValueAliases,
          Unicode_3_0.maximumCodePoint,
          Unicode_3_0.caselessMatchPartitions,
          Unicode_3_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "3.1") || Objects.equals(version, "3.1.0")) {
      bind(
          Unicode_3_1.propertyValues,
          Unicode_3_1.intervals,
          Unicode_3_1.propertyValueAliases,
          Unicode_3_1.maximumCodePoint,
          Unicode_3_1.caselessMatchPartitions,
          Unicode_3_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "3.2") || Objects.equals(version, "3.2.0")) {
      bind(
          Unicode_3_2.propertyValues,
          Unicode_3_2.intervals,
          Unicode_3_2.propertyValueAliases,
          Unicode_3_2.maximumCodePoint,
          Unicode_3_2.caselessMatchPartitions,
          Unicode_3_2.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "4")
        || Objects.equals(version, "4.0")
        || Objects.equals(version, "4.0.1")) {
      bind(
          Unicode_4_0.propertyValues,
          Unicode_4_0.intervals,
          Unicode_4_0.propertyValueAliases,
          Unicode_4_0.maximumCodePoint,
          Unicode_4_0.caselessMatchPartitions,
          Unicode_4_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "4.1") || Objects.equals(version, "4.1.0")) {
      bind(
          Unicode_4_1.propertyValues,
          Unicode_4_1.intervals,
          Unicode_4_1.propertyValueAliases,
          Unicode_4_1.maximumCodePoint,
          Unicode_4_1.caselessMatchPartitions,
          Unicode_4_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "5")
        || Objects.equals(version, "5.0")
        || Objects.equals(version, "5.0.0")) {
      bind(
          Unicode_5_0.propertyValues,
          Unicode_5_0.intervals,
          Unicode_5_0.propertyValueAliases,
          Unicode_5_0.maximumCodePoint,
          Unicode_5_0.caselessMatchPartitions,
          Unicode_5_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "5.1") || Objects.equals(version, "5.1.0")) {
      bind(
          Unicode_5_1.propertyValues,
          Unicode_5_1.intervals,
          Unicode_5_1.propertyValueAliases,
          Unicode_5_1.maximumCodePoint,
          Unicode_5_1.caselessMatchPartitions,
          Unicode_5_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "5.2") || Objects.equals(version, "5.2.0")) {
      bind(
          Unicode_5_2.propertyValues,
          Unicode_5_2.intervals,
          Unicode_5_2.propertyValueAliases,
          Unicode_5_2.maximumCodePoint,
          Unicode_5_2.caselessMatchPartitions,
          Unicode_5_2.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "6")
        || Objects.equals(version, "6.0")
        || Objects.equals(version, "6.0.0")) {
      bind(
          Unicode_6_0.propertyValues,
          Unicode_6_0.intervals,
          Unicode_6_0.propertyValueAliases,
          Unicode_6_0.maximumCodePoint,
          Unicode_6_0.caselessMatchPartitions,
          Unicode_6_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "6.1") || Objects.equals(version, "6.1.0")) {
      bind(
          Unicode_6_1.propertyValues,
          Unicode_6_1.intervals,
          Unicode_6_1.propertyValueAliases,
          Unicode_6_1.maximumCodePoint,
          Unicode_6_1.caselessMatchPartitions,
          Unicode_6_1.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "6.2") || Objects.equals(version, "6.2.0")) {
      bind(
          Unicode_6_2.propertyValues,
          Unicode_6_2.intervals,
          Unicode_6_2.propertyValueAliases,
          Unicode_6_2.maximumCodePoint,
          Unicode_6_2.caselessMatchPartitions,
          Unicode_6_2.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "6.3") || Objects.equals(version, "6.3.0")) {
      bind(
          Unicode_6_3.propertyValues,
          Unicode_6_3.intervals,
          Unicode_6_3.propertyValueAliases,
          Unicode_6_3.maximumCodePoint,
          Unicode_6_3.caselessMatchPartitions,
          Unicode_6_3.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "7")
        || Objects.equals(version, "7.0")
        || Objects.equals(version, "7.0.0")) {
      bind(
          Unicode_7_0.propertyValues,
          Unicode_7_0.intervals,
          Unicode_7_0.propertyValueAliases,
          Unicode_7_0.maximumCodePoint,
          Unicode_7_0.caselessMatchPartitions,
          Unicode_7_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "8")
        || Objects.equals(version, "8.0")
        || Objects.equals(version, "8.0.0")) {
      bind(
          Unicode_8_0.propertyValues,
          Unicode_8_0.intervals,
          Unicode_8_0.propertyValueAliases,
          Unicode_8_0.maximumCodePoint,
          Unicode_8_0.caselessMatchPartitions,
          Unicode_8_0.caselessMatchPartitionSize);
    } else if (Objects.equals(version, "9")
        || Objects.equals(version, "9.0")
        || Objects.equals(version, "9.0.0")) {
      bind(
          Unicode_9_0.propertyValues,
          Unicode_9_0.intervals,
          Unicode_9_0.propertyValueAliases,
          Unicode_9_0.maximumCodePoint,
          Unicode_9_0.caselessMatchPartitions,
          Unicode_9_0.caselessMatchPartitionSize);
    } else {
      throw new UnsupportedUnicodeVersionException();
    }
  }

  /**
   * Unpacks data for the selected Unicode version, populating {@link #propertyValueIntervals}.
   *
   * @param propertyValues The list of property values, in same order as the packed data
   *     corresponding to them, in the given intervals, for the selected Unicode version.
   * @param intervals The packed character intervals corresponding to and in the same order as the
   *     given propertyValues, for the selected Unicode version.
   * @param propertyValueAliases Key/value pairs mapping property value aliases to property values,
   *     for the selected Unicode version.
   * @param maximumCodePoint The maximum code point for the selected Unicode version.
   * @param caselessMatchPartitions The packed caseless match partition data for the selected
   *     Unicode version
   * @param caselessMatchPartitionSize The partition data record length (the maximum number of
   *     elements in a caseless match partition) for the selected Unicode version.
   */
  private void bind(
      String[] propertyValues,
      String[] intervals,
      String[] propertyValueAliases,
      int maximumCodePoint,
      String caselessMatchPartitions,
      int caselessMatchPartitionSize) {
    // IntCharSet caselessMatches[] is lazily initialized - don't unpack here
    this.caselessMatchPartitions = caselessMatchPartitions;
    this.caselessMatchPartitionSize = caselessMatchPartitionSize;
    this.maximumCodePoint = maximumCodePoint;
    for (int n = 0; n < propertyValues.length; ++n) {
      String propertyValue = propertyValues[n];
      String propertyIntervals = intervals[n];
      IntCharSet set = new IntCharSet();
      for (int index = 0; index < propertyIntervals.length(); ) {
        int start = propertyIntervals.codePointAt(index);
        index += Character.charCount(start);
        int end = propertyIntervals.codePointAt(index);
        index += Character.charCount(end);
        set.add(new Interval(start, end));
      }
      propertyValueIntervals.put(propertyValue, set);
      if (2 == propertyValue.length()) {
        String singleLetter = propertyValue.substring(0, 1);
        IntCharSet singleLetterPropValueSet = propertyValueIntervals.get(singleLetter);
        if (null == singleLetterPropValueSet) {
          singleLetterPropValueSet = new IntCharSet();
          propertyValueIntervals.put(singleLetter, singleLetterPropValueSet);
        }
        singleLetterPropValueSet.add(set);
      }
    }
    for (int n = 0; n < propertyValueAliases.length; n += 2) {
      String alias = propertyValueAliases[n];
      String propertyValue = propertyValueAliases[n + 1];
      IntCharSet targetSet = propertyValueIntervals.get(propertyValue);
      if (null != targetSet) {
        propertyValueIntervals.put(alias, targetSet);
      }
    }
    bindInvariantIntervals();
  }

  /** Adds intervals for \p{ASCII} and \p{Any} to {@link #propertyValueIntervals}. */
  private void bindInvariantIntervals() {
    IntCharSet asciiSet = new IntCharSet(new Interval(0, 0x7F));
    propertyValueIntervals.put(normalize("ASCII"), asciiSet);

    IntCharSet anySet = new IntCharSet(new Interval(0, maximumCodePoint));
    propertyValueIntervals.put(normalize("Any"), anySet);
  }

  /**
   * Normalizes the given identifier, by: downcasing; removing whitespace, underscores, hyphens, and
   * parentheses; and substituting '=' for every ':'.
   *
   * @param identifier The identifier to normalize
   * @return The normalized identifier
   */
  private String normalize(String identifier) {
    if (null == identifier) return identifier;
    Matcher matcher = WORD_SEP_PATTERN.matcher(identifier.toLowerCase(Locale.ENGLISH));
    return matcher.replaceAll("").replace(':', '=');
  }

  public static class UnsupportedUnicodeVersionException extends Exception {
    private static final long serialVersionUID = -1718158223161422981L;

    public UnsupportedUnicodeVersionException() {
      super("Supported versions: " + UNICODE_VERSIONS);
    }
  }
}
