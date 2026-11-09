/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Cases for enumerated properties whose values are listed per range in a UCD file: {@code
 * LineBreak.txt}, {@code Scripts.txt} and the {@code auxiliary/*BreakProperty.txt} files.
 *
 * <p>The scanner has one rule per property value and prints the value of every code point; code
 * points that the file does not list have the default value of the property.
 */
final class EnumeratedPropertyCases {

  /** The ranges listed in a file, and the set of values they use. */
  static final class Property {
    final List<Range> ranges = new ArrayList<>();
    final SortedSet<String> values = new TreeSet<>();

    void add(Range range) {
      if (range.start > Ranges.MAX_CODE_POINT) {
        return;
      }
      ranges.add(
          range.end > Ranges.MAX_CODE_POINT
              ? new Range(range.start, Ranges.MAX_CODE_POINT, range.value)
              : range);
      values.add(range.value);
    }
  }

  /** Reads a file of {@code XXXX ; value} and {@code XXXX..YYYY ; value} lines. */
  static Property parse(File file) throws IOException {
    Property property = new Property();
    for (String line : UcdText.lines(file)) {
      Range range = UcdText.parseRange(UcdText.stripComment(line));
      if (range != null) {
        property.add(range);
      }
    }
    return property;
  }

  /**
   * Reads {@code LineBreak.txt}, which before Unicode 3.2 had the format of {@code
   * UnicodeData.txt}: a {@code First} and a {@code Last} line for a range of ideographs.
   */
  static Property parseLineBreak(File file) throws IOException {
    Property property = new Property();
    List<String> lines = UcdText.lines(file);
    for (int i = 0; i < lines.size(); i++) {
      String[] fields = fields(lines.get(i));
      if (fields == null) {
        continue;
      }
      if (fields.length >= 3 && fields[2].matches("<[^,]+, First>")) {
        property.values.add(fields[1]);
        if (++i < lines.size()) {
          String[] last = fields(lines.get(i));
          if (last != null && last.length >= 3 && last[2].matches("<[^,]+, Last>")) {
            property.add(new Range(UcdText.hex(fields[0]), UcdText.hex(last[0]), fields[1]));
          }
        }
      } else {
        Range range = UcdText.parseRange(fields[0] + ";" + fields[1]);
        if (range != null) {
          property.add(range);
        }
      }
    }
    return property;
  }

  /** The trimmed fields of a data line, or null for comment and blank lines. */
  private static String[] fields(String line) {
    String data = UcdText.stripComment(line);
    if (data.isEmpty()) {
      return null;
    }
    String[] fields = data.split(";", -1);
    if (fields.length < 2) {
      return null;
    }
    for (int i = 0; i < fields.length; i++) {
      fields[i] = fields[i].trim();
    }
    return fields;
  }

  /** The case of the {@code LineBreak} property. */
  static GeneratedCase lineBreak(Version version, File lineBreak) throws IOException {
    return create(
        "unicode-line-break",
        "UnicodeLineBreak",
        version,
        "LineBreak",
        "LineBreak",
        parseLineBreak(lineBreak),
        "XX",
        Collections.singleton("SG"));
  }

  /**
   * The case of a property from {@code auxiliary}, whose files list the single code points and
   * ranges.
   *
   * @param caseDir e.g. {@code unicode-word-break}
   * @param propertyName as in {@code \p{WordBreak:ALetter}}
   */
  static GeneratedCase breakProperty(
      Version version, File file, String caseDir, String propertyName) throws IOException {
    return create(
        caseDir,
        "Unicode" + propertyName,
        version,
        propertyName,
        propertyName,
        parse(file),
        "Other",
        Collections.<String>emptySet());
  }

  /**
   * A case whose scanner has one rule per value.
   *
   * @param namePrefix the name of the case before the version
   * @param propertyName the name of the property in the description
   * @param rulePrefix the property name in the rules and the output, e.g. {@code LineBreak} in
   *     {@code \p{LineBreak:AL}}, or null for the values alone, as in {@code \p{Latin}}
   * @param defaultValue the value of the code points that the file does not list
   * @param skipValues values that get no rule and no output: the surrogates
   */
  static GeneratedCase create(
      String caseDir,
      String namePrefix,
      Version version,
      String propertyName,
      String rulePrefix,
      Property property,
      String defaultValue,
      Set<String> skipValues) {
    SortedSet<String> values = new TreeSet<>(property.values);
    values.add(defaultValue);
    StringBuilder rules = new StringBuilder("<<EOF>> { printOutput(); return 1; }\n");
    for (String value : values) {
      if (!skipValues.contains(value)) {
        rules.append(rule(label(rulePrefix, value)));
      }
    }
    List<Range> output = new ArrayList<>();
    for (Range range : Ranges.complete(property.ranges, defaultValue, Ranges.MAX_CODE_POINT)) {
      if (!skipValues.contains(range.value)) {
        output.add(range.withValue(label(rulePrefix, range.value)));
      }
    }
    return new GeneratedCase(
        caseDir,
        namePrefix + "_" + GeneratedCase.underscoreVersion(version),
        version,
        "Tests character class syntax of the Unicode "
            + GeneratedCase.dotVersion(version)
            + " "
            + propertyName
            + " property.",
        GeneratedCase.Include.ENUMERATED,
        rules.toString(),
        output);
  }

  static String label(String rulePrefix, String value) {
    return rulePrefix == null ? value : rulePrefix + ":" + value;
  }

  /** The rule that records {@code label} for the code points of {@code \p{label}}. */
  static String rule(String label) {
    return "\\p{" + label + "} { setCurCharPropertyValue(\"" + label + "\"); }\n";
  }

  private EnumeratedPropertyCases() {}
}
