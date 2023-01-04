/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testcaseless;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator.Equivalences;
import java.nio.file.Paths;
import org.junit.Test;

public class CaselessTestGeneratorTest {

  /**
   * Test that caseless equivalences have been parsed.
   *
   * <pre>
   * {@code AC00;<Hangul Syllable,First>;Lo;0;L;;;;;N;;;;;}.
   * </pre>
   */
  @Test
  public void parseUnicodeData() throws Exception {
    Equivalences<Integer> equivalences =
        CaselessTestGenerator.parseUnicodeData(Paths.get("external/ucd_7_0/UnicodeData.txt"));
    assertThat(equivalences.get(0x0041)).containsExactly(0x0041, 0x0061);
    assertThat(equivalences.get(0xAC00)).containsExactly(0xAC00);
  }
}
