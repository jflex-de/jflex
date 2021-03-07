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
