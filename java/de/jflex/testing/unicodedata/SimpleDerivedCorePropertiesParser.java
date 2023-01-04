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
 * Parser of unicode {@code DerivedCoreProperties.txt}.
 *
 * <pre>{@code
 * # For documentation, see DerivedProperties.html
 * FFE2          ; Math # Sm       FULLWIDTH NOT SIGN
 * FFE9..FFEC    ; Math # Sm   [4] HALFWIDTH LEFTWARDS ARROW..HALFWIDTH DOWNWARDS ARROW
 * }</pre>
 */
public class SimpleDerivedCorePropertiesParser extends AbstractSimpleParser {

  private static final Pattern PATTERN =
      Pattern.compile("^([0-9A-F]{4,6})(\\.\\.[0-9A-F]{4,6})?\\s*;\\s([^#]*).*$");

  public SimpleDerivedCorePropertiesParser(Reader reader, PatternHandler handler) {
    super(PATTERN, reader, handler);
  }

  /** Parses the unicode {@code Blocks.txt} and returns the defined blocks. */
  public static ImmutableList<NamedCodepointRange<String>> parseProperties(Path blocksTxt)
      throws IOException {
    return parseProperties(Files.newBufferedReader(blocksTxt, StandardCharsets.UTF_8));
  }

  static ImmutableList<NamedCodepointRange<String>> parseProperties(Reader reader)
      throws IOException {
    ImmutableList.Builder<NamedCodepointRange<String>> list = ImmutableList.builder();
    SimpleDerivedCorePropertiesParser parser =
        new SimpleDerivedCorePropertiesParser(
            reader, regexpGroups -> list.add(createBlock(regexpGroups)));
    parser.parse();
    return list.build();
  }

  private static NamedCodepointRange<String> createBlock(List<String> regexpGroups) {
    if (regexpGroups.size() == 3) {
      return NamedCodepointRange.create(
          regexpGroups.get(2).trim(), createRange(regexpGroups.get(0), regexpGroups.get(1)));
    } else {
      return NamedCodepointRange.create(
          regexpGroups.get(1).trim(), createRange(regexpGroups.get(0)));
    }
  }

  private static CodepointRange createRange(String start, String end) {
    return CodepointRange.create(
        Integer.parseInt(start, 16), Integer.parseInt(end.substring("..".length()), 16));
  }

  private static CodepointRange createRange(String point) {
    return CodepointRange.createPoint(Integer.parseInt(point, 16));
  }
}
