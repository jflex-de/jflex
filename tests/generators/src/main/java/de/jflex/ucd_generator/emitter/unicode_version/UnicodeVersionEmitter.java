/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.emitter.unicode_version;

import static de.jflex.ucd_generator.util.JavaStrings.escapedUTF16Char;
import static java.util.stream.Collectors.joining;

import de.jflex.template.Template;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.emitter.common.UcdEmitter;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.util.JavaStrings;
import de.jflex.util.javac.JavaPackageUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

/** Emitter for a {@code Unicode_x_y.java}. */
public class UnicodeVersionEmitter extends UcdEmitter {

  private static final String TEMPLATE_NAME = "Unicode_x_y.java";
  private static final String UNICODE_VERSION_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodeVersionEmitter.class)
          + "/"
          + TEMPLATE_NAME
          + ".template";
  private static final String CP_ZERO = escapedUTF16Char(0);

  private final UcdVersion ucdVersion;
  private final UnicodeData unicodeData;

  public UnicodeVersionEmitter(String packageName, UcdVersion ucdVersion, UnicodeData unicodeData) {
    super(packageName);
    this.ucdVersion = ucdVersion;
    this.unicodeData = unicodeData;
  }

  public void emitUnicodeVersion(OutputStream output) throws IOException {
    UnicodeVersionVars unicodeVersionVars = createUnicodeVersionVars();
    Template.render(readResource(UNICODE_VERSION_TEMPLATE), unicodeVersionVars, output);
  }

  private UnicodeVersionVars createUnicodeVersionVars() {
    UnicodeVersionVars unicodeVersionVars = new UnicodeVersionVars();
    unicodeVersionVars.templateName = TEMPLATE_NAME;
    unicodeVersionVars.packageName = getTargetPackage();
    unicodeVersionVars.className = ucdVersion.version().unicodeClassName();
    unicodeVersionVars.maxCodePointHex = String.format("%x", unicodeData.maximumCodePoint());
    SortedMap<String, CodepointRangeSet> intervals = unicodeData.intervals();
    unicodeVersionVars.propertyValues =
        intervals.keySet().stream()
            .map(v -> String.format("\"%s\"", v))
            .collect(joining(",\n    "));
    unicodeVersionVars.intervals = String.join(",\n    ", intervalsToCodesource(intervals));
    unicodeVersionVars.propertyValueAliases =
        unicodeData.usedPropertyValueAliases().stream()
            .map(e -> String.format("\"%s\", \"%s\"", e.getKey(), e.getValue()))
            .collect(joining(",\n    "));
    unicodeVersionVars.maxCaselessMatchPartitionSize = unicodeData.maxCaselessMatchPartitionSize();
    unicodeVersionVars.caselessMatchPartitions =
        unicodeData.uniqueCaselessMatchPartitions().stream()
            .map(
                partition ->
                    partitionToCodesource(
                        partition, unicodeVersionVars.maxCaselessMatchPartitionSize))
            .collect(joining("\"\n          + \""));
    return unicodeVersionVars;
  }

  private Collection<String> intervalsToCodesource(Map<String, CodepointRangeSet> intervals) {
    List<String> result = new ArrayList<>();
    for (Map.Entry<String, CodepointRangeSet> entry : intervals.entrySet()) {
      String codeComment =
          String.format(
              "// Unicode %s property value: {%s}\n",
              ucdVersion.version().toMajorMinorString(), entry.getKey());
      String value =
          entry.getValue().ranges().stream()
              .map(UnicodeVersionEmitter::intervalToCodesource)
              .collect(joining("\n        + "));
      result.add(codeComment + "    " + value);
    }
    return result;
  }

  private static String intervalToCodesource(CodepointRange interval) {
    return "\"" + escapedUTF16Char(interval.start()) + escapedUTF16Char(interval.end()) + "\"";
  }

  private static String partitionToCodesource(
      SortedSet<Integer> partition, int caselessMatchPartitionSize) {
    StringBuilder sb = new StringBuilder();
    Iterator<String> escapedChars =
        partition.stream().map(JavaStrings::escapedUTF16Char).iterator();
    while (escapedChars.hasNext()) {
      sb.append(escapedChars.next());
    }
    // Add \u0000 placeholders to fill out the fixed record size
    for (int i = 0; i < caselessMatchPartitionSize - partition.size(); ++i) {
      sb.append(CP_ZERO);
    }
    return sb.toString();
  }
}
