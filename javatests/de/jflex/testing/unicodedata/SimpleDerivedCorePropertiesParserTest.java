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

/** Test {@link SimpleDerivedCorePropertiesParser}. */
public class SimpleDerivedCorePropertiesParserTest {

  @Test
  public void parseProperties_range() throws Exception {
    assertThat(parse("25AE..25B6    ; Math # So   [9] BLACK VERTICAL RECTANGLE..BLACK"))
        .containsExactly(NamedCodepointRange.create("Math", 0x25AE, 0x25B6));
  }

  @Test
  public void parseProperties_rangeLong() throws Exception {
    assertThat(parse("10FFFE..10FFFF; Default_Ignorable_Code_Point # Cn   [2] <noncharacter-10FFF"))
        .containsExactly(
            NamedCodepointRange.create("Default_Ignorable_Code_Point", 0x10FFFE, 0x10FFFF));
  }

  @Test
  public void parseProperties_singlePoint() throws Exception {
    assertThat(parse("25E2          ; Math # So       BLACK LOWER RIGHT TRIANGLE"))
        .containsExactly(NamedCodepointRange.create("Math", 0x25E2, 0x25E2));
  }

  private static ImmutableList<NamedCodepointRange<String>> parse(String line) throws IOException {
    return SimpleDerivedCorePropertiesParser.parseProperties(CharSource.wrap(line).openStream());
  }
}
