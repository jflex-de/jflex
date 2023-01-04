/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testdigit;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import org.junit.Test;

/** Test for {@link UnicodeDigitFlexGenerator}. */
public class UnicodeDigitFlexGeneratorTest {
  @Test
  public void digit_D() {
    UnicodeVersion unicodeVersion = UnicodeVersion.create("2.1");
    UnicodeDigitFlexGenerator generator = new UnicodeDigitFlexGenerator(unicodeVersion, "\\D");
    UnicodeDigitFlexTemplateVars vars = generator.createTemplateVars();
    assertThat(vars.className).isEqualTo("UnicodeDigit_upperD_2_1");
    assertThat(generator.getOuputFileName(vars)).isEqualTo("UnicodeDigit_upperD_2_1.flex");
  }
}
