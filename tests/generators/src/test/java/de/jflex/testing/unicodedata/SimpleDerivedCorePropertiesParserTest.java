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

/** Test {@link SimpleDerivedCorePropertiesParser}. */
public class SimpleDerivedCorePropertiesParserTest {

  @Test
  public void parseProperties_range() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("Math", 0x25AE, 0x25B6)),
        parse("25AE..25B6    ; Math # So   [9] BLACK VERTICAL RECTANGLE..BLACK"));
  }

  @Test
  public void parseProperties_rangeLong() throws Exception {
    assertEquals(
        Arrays.asList(
            NamedCodepointRange.create("Default_Ignorable_Code_Point", 0x10FFFE, 0x10FFFF)),
        parse("10FFFE..10FFFF; Default_Ignorable_Code_Point # Cn   [2] <noncharacter-10FFF"));
  }

  @Test
  public void parseProperties_singlePoint() throws Exception {
    assertEquals(
        Arrays.asList(NamedCodepointRange.create("Math", 0x25E2, 0x25E2)),
        parse("25E2          ; Math # So       BLACK LOWER RIGHT TRIANGLE"));
  }

  private static List<NamedCodepointRange<String>> parse(String line) throws IOException {
    return SimpleDerivedCorePropertiesParser.parseProperties(new StringReader(line));
  }
}
