/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.emitter.unicode_version;

import static de.jflex.ucd_generator.util.JavaStrings.escapedUTF16Char;
import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_generator.emitter.common.UcdEmitter;
import de.jflex.ucd_generator.ucd.CodepointRangeSet;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_generator.util.JavaStrings;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.velocity.Velocity;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import org.apache.velocity.runtime.parser.ParseException;

/** Emitter for a {@code Unicode_x_y.java}. */
public class UnicodeVersionEmitter extends UcdEmitter {

  private static final String TEMPLATE_NAME = "Unicode_x_y.java";
  private static final String UNICODE_VERSION_TEMPLATE =
      JavaPackageUtils.getPathForClass(UnicodeVersionEmitter.class) + "/" + TEMPLATE_NAME + ".vm";
  private static final String CP_ZERO = escapedUTF16Char(0);

  private final UcdVersion ucdVersion;
  private final UnicodeData unicodeData;

  public UnicodeVersionEmitter(String packageName, UcdVersion ucdVersion, UnicodeData unicodeData) {
    super(packageName);
    this.ucdVersion = ucdVersion;
    this.unicodeData = unicodeData;
  }

  public void emitUnicodeVersion(OutputStream output) throws IOException, ParseException {
    UnicodeVersionVars unicodeVersionVars = createUnicodeVersionVars();
    Velocity.render(
        readResource(UNICODE_VERSION_TEMPLATE), TEMPLATE_NAME, unicodeVersionVars, output);
  }

  private UnicodeVersionVars createUnicodeVersionVars() {
    UnicodeVersionVars unicodeVersionVars = new UnicodeVersionVars();
    unicodeVersionVars.templateName = TEMPLATE_NAME;
    unicodeVersionVars.packageName = getTargetPackage();
    unicodeVersionVars.className = ucdVersion.version().unicodeClassName();
    unicodeVersionVars.maxCodePoint = unicodeData.maximumCodePoint();
    ImmutableSortedMap<String, CodepointRangeSet> intervals = unicodeData.intervals();
    unicodeVersionVars.propertyValues =
        ImmutableList.copyOf(intervals.keySet()).stream()
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
    EntryTransformer<String, CodepointRangeSet, String> function =
        new EntryTransformer<String, CodepointRangeSet, String>() {

          @Override
          public String transformEntry(String propertyValue, CodepointRangeSet rangeSet) {
            String codeComment =
                String.format(
                    "// Unicode %s property value: {%s}\n",
                    ucdVersion.version().toMajorMinorString(), propertyValue);
            String value =
                rangeSet.ranges().stream()
                    .map(this::intervalToCodesource)
                    .collect(joining("\n        + "));
            return codeComment + "    " + value;
          }

          private String intervalToCodesource(CodepointRange interval) {
            return "\""
                + escapedUTF16Char(interval.start())
                + escapedUTF16Char(interval.end())
                + "\"";
          }
        };

    return Maps.transformEntries(intervals, function).values();
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
