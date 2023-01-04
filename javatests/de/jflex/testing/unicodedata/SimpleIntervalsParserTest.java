/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.unicodedata;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import de.jflex.ucd.NamedCodepointRange;
import java.io.IOException;
import org.junit.Test;

/** Test {@link SimpleIntervalsParser}. */
public class SimpleIntervalsParserTest {

  @Test
  public void parseUnicodeBlocks_newFormat() throws Exception {
    assertThat(parseBlock("0000..007F; Basic Latin"))
        .containsExactly(NamedCodepointRange.create("Basic Latin", 0x0000, 0x007F));
  }

  @Test
  public void parseUnicodeBlocks_newFormatWithComments() throws Exception {
    assertThat(parseBlock("1F910..1F93E  ; Grapheme_Base # So  [47] ZIPPER-MOUTH FACE..HANDBALL"))
        .containsExactly(NamedCodepointRange.create("Grapheme_Base", 0x1F910, 0x1F93E));
  }

  @Test
  public void parseUnicodeBlocks_legacyFormat() throws Exception {
    assertThat(parseBlock("0000; 007F; Basic Latin"))
        .containsExactly(NamedCodepointRange.create("Basic Latin", 0x0000, 0x007F));
  }

  @Test
  public void parseUnicodeBlocks_legacyFormatWithComments() throws Exception {
    assertThat(parseBlock("1D7CE..1D7FF  ; XID_Continue # Nd  [50] MATHEMATICAL BOLD"))
        .containsExactly(NamedCodepointRange.create("XID_Continue", 0x1D7CE, 0x1D7FF));
  }

  private static ImmutableList<NamedCodepointRange<String>> parseBlock(String line)
      throws IOException {
    return SimpleIntervalsParser.parseUnicodeBlocks(CharSource.wrap(line).openStream());
  }
}
