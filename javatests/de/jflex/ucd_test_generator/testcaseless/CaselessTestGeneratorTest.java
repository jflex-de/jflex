/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testcaseless;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.ucd.UcdFiles;
import de.jflex.ucd_test_generator.testcaseless.CaselessTestGenerator.Equivalences;
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
        CaselessTestGenerator.parseUnicodeData(
            UcdFiles.zippedFile("ucd_7_0", "UnicodeData.txt").toPath());
    assertThat(equivalences.get(0x0041)).containsExactly(0x0041, 0x0061);
    assertThat(equivalences.get(0xAC00)).containsExactly(0xAC00);
  }
}
