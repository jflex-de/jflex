/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testblock;

import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.UnicodePropertyFlexGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/** Generates the flex of the scanners for a all blocks of a given Unicode version. */
class UnicodeBlockFlexGenerator {

  public static UnicodePropertyFlexGenerator<String> create(
      UnicodeVersion version, List<NamedCodepointRange<String>> blocks) {
    String className = "UnicodeBlocks_" + version.underscoreVersion();
    SortedSet<String> blockNames = new TreeSet<>();
    blockNames.add("No Block");
    for (NamedCodepointRange<String> block : blocks) {
      blockNames.add(block.name());
    }
    TreeMap<String, String> properties = new TreeMap<>();
    for (String b : blockNames) {
      properties.put("Block:" + b, "\"" + b + "\"");
    }
    return new UnicodePropertyFlexGenerator<>(version, className, properties, String.class);
  }

  private UnicodeBlockFlexGenerator() {}
}
