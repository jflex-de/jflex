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
package de.jflex.testcase.unicode.unicode_3_1;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.util.javac.JavaPackageUtils.getPathForClass;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Generated;
import jflex.core.unicode.UnicodeProperties;
import org.junit.Test;

/** Test for Age property in {@link jflex.core.unicode.data.Unicode_3_1}. */
@Generated("de.jflex.migration.unicodedatatest.UnicodeAgeGenerator")
// Generated from java/de/jflex/migration/unicodedatatest/UnicodeAgeTest_x_y.java.vm
public class UnicodeAgeTest_3_1 {

  private static final String TEST_DIR = getPathForClass(UnicodeAgeTest_3_1.class);

  @Test
  public void age() throws Exception {
    UnicodeProperties properties = new UnicodeProperties("3.1");
    assertThat(properties.getPropertyValues()).contains("age=1.1");
    assertThat(properties.getPropertyValues()).contains("age=2.0");
    assertThat(properties.getPropertyValues()).contains("age=2.1");
    assertThat(properties.getPropertyValues()).contains("age=3.0");
    assertThat(properties.getPropertyValues()).contains("age=3.1");
  }

  /** Tests character class syntax of the Unicode 3.1 Age=1.1 property. */
  @Test
  public void ageIntervals_1_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_1_1::new),
        UnicodeAge_3_1_age_1_1.YYEOF,
        "UnicodeAge_3_1_age_1_1.output");
  }
  /** Tests character class syntax of the Unicode 3.1 Age=2.0 property. */
  @Test
  public void ageIntervals_2_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_2_0::new),
        UnicodeAge_3_1_age_2_0.YYEOF,
        "UnicodeAge_3_1_age_2_0.output");
  }
  /** Tests character class syntax of the Unicode 3.1 Age=2.1 property. */
  @Test
  public void ageIntervals_2_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_2_1::new),
        UnicodeAge_3_1_age_2_1.YYEOF,
        "UnicodeAge_3_1_age_2_1.output");
  }
  /** Tests character class syntax of the Unicode 3.1 Age=3.0 property. */
  @Test
  public void ageIntervals_3_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_3_0::new),
        UnicodeAge_3_1_age_3_0.YYEOF,
        "UnicodeAge_3_1_age_3_0.output");
  }
  /** Tests character class syntax of the Unicode 3.1 Age=3.1 property. */
  @Test
  public void ageIntervals_3_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_3_1::new),
        UnicodeAge_3_1_age_3_1.YYEOF,
        "UnicodeAge_3_1_age_3_1.output");
  }

  /**
   * Tests subtracting Age Unicode property values in character sets for Unicode 3.1, e.g. {@code
   * [\p{Age:2.0}--\p{Age:1.1}]}.
   */
  @Test
  public void ageIntervals_subtraction() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_subtraction::new),
        UnicodeAge_3_1_age_subtraction.YYEOF,
        "UnicodeAge_3_1_age_subtraction.output");
  }

  /** Tests character class syntax of the Unicode 3.1 Age=Unassigned property. */
  @Test
  public void ageIntervals_unassigned() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_3_1_age_unassigned::new),
        UnicodeAge_3_1_age_unassigned.YYEOF,
        "UnicodeAge_3_1_age_unassigned.output");
  }

  private static void assertAgeInterval(
      ScannerFactory<? extends AbstractEnumeratedPropertyDefinedScanner> scannerFactory,
      int eof,
      String expectedFileName)
      throws IOException {
    Path expectedFile = Paths.get("javatests").resolve(TEST_DIR).resolve(expectedFileName);
    UnicodeDataScanners.assertAgeInterval(
        scannerFactory, eof, UnicodeDataScanners.Dataset.ALL, expectedFile);
  }
}
