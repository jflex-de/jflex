/*
 * Copyright (C) 2014-2021 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2021 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
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
package de.jflex.testcase.unicode.unicode_2_0;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.testing.unicodedata.Ages.assertAgeInterval;

import de.jflex.util.scanner.ScannerFactory;
import jflex.core.unicode.UnicodeProperties;
import org.junit.Before;
import org.junit.Test;

public class UnicodeAgeTest_2_0 {

  UnicodeProperties properties;

  @Before
  public void init() throws Exception {
    properties = new UnicodeProperties("2.0");
  }

  @Test
  public void age() {
    assertThat(properties.getPropertyValues()).contains("age=1.1");
    assertThat(properties.getPropertyValues()).contains("age=2.0");
  }

  /** Tests character class syntax of the Unicode 2.0 Age=1.1 property. */
  @Test
  public void ageIntervals_1_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_2_0_age_1_1::new),
        UnicodeAge_2_0_age_1_1.YYEOF,
        "UnicodeAge_2_0_age_1_1.output");
  }

  /** Tests character class syntax of the Unicode 2.0 Age=2.0 property. */
  @Test
  public void ageIntervals_2_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_2_0_age_2_0::new),
        UnicodeAge_2_0_age_2_0.YYEOF,
        "UnicodeAge_2_0_age_2_0.output");
  }

  /**
   * Tests subtracting Age Unicode property values in character sets for Unicode 2.0, e.g. {@code
   * [\p{Age:2.0}--\p{Age:1.1}]}.
   */
  @Test
  public void ageIntervals_substraction() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_2_0_age_subtraction::new),
        UnicodeAge_2_0_age_2_0.YYEOF,
        "UnicodeAge_2_0_age_subtraction.output");
  }

  /** Tests character class syntax of the Unicode 2.0 Age=Unassigned property. */
  @Test
  public void ageIntervals_unassigned() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_2_0_age_unassigned::new),
        UnicodeAge_2_0_age_unassigned.YYEOF,
        "UnicodeAge_2_0_age_unassigned.output");
  }
}
