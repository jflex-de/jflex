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
