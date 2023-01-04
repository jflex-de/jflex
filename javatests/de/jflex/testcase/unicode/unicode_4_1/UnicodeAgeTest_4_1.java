/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_4_1;

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

/** Test for Age property in {@link jflex.core.unicode.data.Unicode_4_1}. */
@Generated("de.jflex.migration.unicodedatatest.UnicodeAgeGenerator")
// Generated from java/de/jflex/migration/unicodedatatest/UnicodeAgeTest_x_y.java.vm
public class UnicodeAgeTest_4_1 {

  private static final String TEST_DIR = getPathForClass(UnicodeAgeTest_4_1.class);

  @Test
  public void age() throws Exception {
    UnicodeProperties properties = new UnicodeProperties("4.1");
    assertThat(properties.getPropertyValues()).contains("age=1.1");
    assertThat(properties.getPropertyValues()).contains("age=2.0");
    assertThat(properties.getPropertyValues()).contains("age=2.1");
    assertThat(properties.getPropertyValues()).contains("age=3.0");
    assertThat(properties.getPropertyValues()).contains("age=3.1");
    assertThat(properties.getPropertyValues()).contains("age=3.2");
    assertThat(properties.getPropertyValues()).contains("age=4.0");
    assertThat(properties.getPropertyValues()).contains("age=4.1");
  }

  /** Tests character class syntax of the Unicode 4.1 Age=1.1 property. */
  @Test
  public void ageIntervals_1_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_1_1::new),
        UnicodeAge_4_1_age_1_1.YYEOF,
        "UnicodeAge_4_1_age_1_1.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=2.0 property. */
  @Test
  public void ageIntervals_2_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_2_0::new),
        UnicodeAge_4_1_age_2_0.YYEOF,
        "UnicodeAge_4_1_age_2_0.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=2.1 property. */
  @Test
  public void ageIntervals_2_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_2_1::new),
        UnicodeAge_4_1_age_2_1.YYEOF,
        "UnicodeAge_4_1_age_2_1.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=3.0 property. */
  @Test
  public void ageIntervals_3_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_3_0::new),
        UnicodeAge_4_1_age_3_0.YYEOF,
        "UnicodeAge_4_1_age_3_0.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=3.1 property. */
  @Test
  public void ageIntervals_3_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_3_1::new),
        UnicodeAge_4_1_age_3_1.YYEOF,
        "UnicodeAge_4_1_age_3_1.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=3.2 property. */
  @Test
  public void ageIntervals_3_2() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_3_2::new),
        UnicodeAge_4_1_age_3_2.YYEOF,
        "UnicodeAge_4_1_age_3_2.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=4.0 property. */
  @Test
  public void ageIntervals_4_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_4_0::new),
        UnicodeAge_4_1_age_4_0.YYEOF,
        "UnicodeAge_4_1_age_4_0.output");
  }
  /** Tests character class syntax of the Unicode 4.1 Age=4.1 property. */
  @Test
  public void ageIntervals_4_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_4_1::new),
        UnicodeAge_4_1_age_4_1.YYEOF,
        "UnicodeAge_4_1_age_4_1.output");
  }

  /**
   * Tests subtracting Age Unicode property values in character sets for Unicode 4.1, e.g. {@code
   * [\p{Age:2.0}--\p{Age:1.1}]}.
   */
  @Test
  public void ageIntervals_subtraction() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_subtraction::new),
        UnicodeAge_4_1_age_subtraction.YYEOF,
        "UnicodeAge_4_1_age_subtraction.output");
  }

  /** Tests character class syntax of the Unicode 4.1 Age=Unassigned property. */
  @Test
  public void ageIntervals_unassigned() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_4_1_age_unassigned::new),
        UnicodeAge_4_1_age_unassigned.YYEOF,
        "UnicodeAge_4_1_age_unassigned.output");
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
