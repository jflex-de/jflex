/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testsuite.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Reads the text formats of the Unicode Character Database. */
final class UcdText {
  private static final String HEX = "[0-9A-Fa-f]{4,6}";

  /** {@code 0958..0961 ; ALetter}, also without the spaces, the value up to the next field. */
  private static final Pattern RANGE_LINE =
      Pattern.compile("^(" + HEX + ")\\.\\.(" + HEX + ")\\s*;\\s*([^;#]*)");

  /** {@code 0950 ; ALetter}, also without the spaces. */
  private static final Pattern SINGLE_LINE = Pattern.compile("^(" + HEX + ")\\s*;\\s*([^;#]*)");

  /** The header of a property in the PropList format before Unicode 3.1. */
  private static final Pattern PROPERTY_DUMP =
      Pattern.compile("Property\\s+dump\\s+for:\\s+0x[0-9A-Fa-f]+\\s+\\((.+)\\)");

  private static final Pattern DUMP_RANGE = Pattern.compile("^(" + HEX + ")\\.\\.(" + HEX + ")");
  private static final Pattern DUMP_SINGLE = Pattern.compile("^(" + HEX + ")(?:[^0-9A-Fa-f]|$)");

  private static final Pattern FIRST_IN_RANGE = Pattern.compile("<[^,]+, First>");
  private static final Pattern LAST_IN_RANGE = Pattern.compile("<[^,]+, Last>");

  /**
   * The lines of a UCD file, without line terminators. The files are ASCII; the bytes of any other
   * character are kept as they are.
   */
  static List<String> lines(File file) throws IOException {
    List<String> lines = new ArrayList<>();
    try (BufferedReader reader =
        Files.newBufferedReader(file.toPath(), StandardCharsets.ISO_8859_1)) {
      String line;
      while ((line = reader.readLine()) != null) {
        lines.add(line);
      }
    }
    return lines;
  }

  /** The line without its comment and surrounding whitespace; empty for comment and blank lines. */
  static String stripComment(String line) {
    int hash = line.indexOf('#');
    return (hash < 0 ? line : line.substring(0, hash)).trim();
  }

  static int hex(String codePoint) {
    return Integer.parseInt(codePoint, 16);
  }

  /**
   * Parses a {@code XXXX ; value} or {@code XXXX..YYYY ; value} line, the value being the field
   * after the first semicolon.
   *
   * @param line a line without comment
   * @return the range, or null if the line is not of this form
   */
  static Range parseRange(String line) {
    Matcher m = RANGE_LINE.matcher(line);
    if (m.find()) {
      return new Range(hex(m.group(1)), hex(m.group(2)), m.group(3).trim());
    }
    m = SINGLE_LINE.matcher(line);
    if (m.find()) {
      int codePoint = hex(m.group(1));
      return new Range(codePoint, codePoint, m.group(2).trim());
    }
    return null;
  }

  /** A line of {@code UnicodeData.txt}. */
  static final class UnicodeDataLine {
    final int codePoint;
    final String name;
    final String generalCategory;

    private UnicodeDataLine(int codePoint, String name, String generalCategory) {
      this.codePoint = codePoint;
      this.name = name;
      this.generalCategory = generalCategory;
    }

    /** Whether this is the first code point of a range, e.g. {@code <Hangul Syllable, First>}. */
    boolean isFirst() {
      return FIRST_IN_RANGE.matcher(name).matches();
    }

    /** Whether this is the last code point of a range, e.g. {@code <Hangul Syllable, Last>}. */
    boolean isLast() {
      return LAST_IN_RANGE.matcher(name).matches();
    }

    /**
     * Whether this is the single entry for the unified ideographs in {@code UnicodeData-1.1.5.txt},
     * which does not list the end of the range.
     */
    boolean isCjkRepresentative() {
      return codePoint == 0x4E00 && name.equals("<CJK IDEOGRAPH REPRESENTATIVE>");
    }
  }

  /**
   * Parses a line of {@code UnicodeData.txt}.
   *
   * @return the line, or null for comment and blank lines
   */
  static UnicodeDataLine parseUnicodeData(String line) {
    String trimmed = line.trim();
    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
      return null;
    }
    String[] fields = trimmed.split(";", -1);
    if (fields.length < 3) {
      return null;
    }
    return new UnicodeDataLine(hex(fields[0]), fields[1], fields[2]);
  }

  /**
   * The ranges of each property in {@code PropList.txt} or {@code DerivedCoreProperties.txt} from
   * Unicode 3.1 on, in file order and as in the file: surrogates included.
   */
  static Map<String, List<Range>> binaryProperties(File file) throws IOException {
    Map<String, List<Range>> properties = new LinkedHashMap<>();
    for (String line : lines(file)) {
      Range range = parseRange(stripComment(line));
      if (range != null) {
        add(properties, range.value, range);
      }
    }
    return properties;
  }

  /**
   * The ranges of each property in the {@code PropList} files before Unicode 3.1, which are
   * "Property dump" sections, in file order and as in the file: surrogates included.
   */
  static Map<String, List<Range>> propertyDump(File file) throws IOException {
    Map<String, List<Range>> properties = new LinkedHashMap<>();
    String property = null;
    for (String line : lines(file)) {
      Matcher m = PROPERTY_DUMP.matcher(line);
      if (m.find()) {
        property = m.group(1);
        continue;
      }
      m = DUMP_RANGE.matcher(line);
      if (m.find()) {
        add(properties, property, new Range(hex(m.group(1)), hex(m.group(2))));
        continue;
      }
      m = DUMP_SINGLE.matcher(line);
      if (m.find()) {
        int codePoint = hex(m.group(1));
        add(properties, property, new Range(codePoint, codePoint));
      }
    }
    return properties;
  }

  private static void add(Map<String, List<Range>> properties, String property, Range range) {
    if (property == null) {
      throw new IllegalStateException("Range before the first property: " + range);
    }
    List<Range> ranges = properties.get(property);
    if (ranges == null) {
      ranges = new ArrayList<>();
      properties.put(property, ranges);
    }
    ranges.add(range);
  }

  private UcdText() {}
}
