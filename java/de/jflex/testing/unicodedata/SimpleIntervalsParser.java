/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.NamedCodepointRange;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser of unicode {@code Blocks.txt} file, also used in golden files.
 *
 * <p>Recent Unicode
 *
 * <pre>{@code
 * # Property:	Block
 * #
 * # @missing: 0000..10FFFF; No_Block
 *
 * 0000..007F; Basic Latin
 * 0080..00FF; Latin-1 Supplement
 * }</pre>
 *
 * <p>Archaic unicode
 *
 * <pre>{@code
 * # Start Code; End Code; Block Name
 * 0000; 007F; Basic Latin
 * 0080; 00FF; Latin-1 Supplement
 * }</pre>
 */
public class SimpleIntervalsParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("^([0-9A-F]{4,6})(?:\\.\\.|;\\s*)([0-9A-F]{4,6})(?:\\s*; )?([^#]*).*$");

  public SimpleIntervalsParser(Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }

  /** Parses the unicode {@code Blocks.txt} and returns the defined blocks. */
  public static ImmutableList<NamedCodepointRange<String>> parseUnicodeBlocks(Path blocksTxt)
      throws IOException {
    return parseUnicodeBlocks(Files.newBufferedReader(blocksTxt, StandardCharsets.UTF_8));
  }

  static ImmutableList<NamedCodepointRange<String>> parseUnicodeBlocks(Reader reader)
      throws IOException {
    ImmutableList.Builder<NamedCodepointRange<String>> list = ImmutableList.builder();
    SimpleIntervalsParser parser =
        new SimpleIntervalsParser(reader, regexpGroups -> list.add(createBlock(regexpGroups)));
    parser.parse();
    return list.build();
  }

  private static NamedCodepointRange<String> createBlock(List<String> regexpGroups) {
    return NamedCodepointRange.create(regexpGroups.get(2).trim(), createRange(regexpGroups));
  }

  public static ImmutableList<CodepointRange> parseRanges(Path expectedFile) throws IOException {
    ImmutableList.Builder<CodepointRange> expectedBlocks = ImmutableList.builder();
    SimpleIntervalsParser parser =
        new SimpleIntervalsParser(
            Files.newBufferedReader(expectedFile),
            regexpGroups -> expectedBlocks.add(createRange(regexpGroups)));
    parser.parse();
    return expectedBlocks.build();
  }

  private static CodepointRange createRange(List<String> regexpGroups) {
    return CodepointRange.create(
        Integer.parseInt(regexpGroups.get(0), 16), Integer.parseInt(regexpGroups.get(1), 16));
  }
}
