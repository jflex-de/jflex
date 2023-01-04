/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testblock;

import static com.google.common.collect.ImmutableSortedMap.toImmutableSortedMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import de.jflex.migration.unicodedatatest.base.UnicodePropertyFlexGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.ucd.NamedCodepointRange;
import java.util.Comparator;

/** Generates the flex of the scanners for a all blocks of a given Unicode version. */
class UnicodeBlockFlexGenerator {

  public static UnicodePropertyFlexGenerator<String> create(
      UnicodeVersion version, ImmutableList<NamedCodepointRange<String>> blocks) {
    String className = "UnicodeBlocks_" + version.underscoreVersion();
    ImmutableSortedSet<String> blockNames =
        ImmutableSortedSet.<String>naturalOrder()
            .add("No Block")
            .addAll(blocks.stream().map(NamedCodepointRange::name).iterator())
            .build();
    return new UnicodePropertyFlexGenerator<>(
        version,
        className,
        blockNames.stream()
            .collect(
                toImmutableSortedMap(
                    Comparator.naturalOrder(), b -> "Block:" + b, b -> "\"" + b + "\"")),
        String.class);
  }

  private UnicodeBlockFlexGenerator() {}
}
