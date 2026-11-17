/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.ucd.NamedCodepointRange;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Test {@link SimpleIntervalsParser}. */
public class SimpleIntervalsParserTest {

  @Test
  public void parseUnicodeBlocks_newFormat() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("Basic Latin", 0x0000, 0x007F)),
        parseBlock("0000..007F; Basic Latin"));
  }

  @Test
  public void parseUnicodeBlocks_newFormatWithComments() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("Grapheme_Base", 0x1F910, 0x1F93E)),
        parseBlock("1F910..1F93E  ; Grapheme_Base # So  [47] ZIPPER-MOUTH FACE..HANDBALL"));
  }

  @Test
  public void parseUnicodeBlocks_legacyFormat() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("Basic Latin", 0x0000, 0x007F)),
        parseBlock("0000; 007F; Basic Latin"));
  }

  @Test
  public void parseUnicodeBlocks_legacyFormatWithComments() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("XID_Continue", 0x1D7CE, 0x1D7FF)),
        parseBlock("1D7CE..1D7FF  ; XID_Continue # Nd  [50] MATHEMATICAL BOLD"));
  }

  private static List<NamedCodepointRange<String>> parseBlock(String line) throws IOException {
    return SimpleIntervalsParser.parseUnicodeBlocks(new StringReader(line));
  }
}
