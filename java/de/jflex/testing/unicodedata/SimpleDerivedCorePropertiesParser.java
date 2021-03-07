/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
