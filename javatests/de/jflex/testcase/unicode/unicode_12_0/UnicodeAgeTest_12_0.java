/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_12_0;

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

/** Test for Age property in {@link jflex.core.unicode.data.Unicode_12_0}. */
@Generated("de.jflex.migration.unicodedatatest.UnicodeAgeGenerator")
// Generated from java/de/jflex/migration/unicodedatatest/UnicodeAgeTest_x_y.java.vm
public class UnicodeAgeTest_12_0 {

  private static final String TEST_DIR = getPathForClass(UnicodeAgeTest_12_0.class);

  @Test
  public void age() throws Exception {
    UnicodeProperties properties = new UnicodeProperties("12.0");
    assertThat(properties.getPropertyValues()).contains("age=1.1");
    assertThat(properties.getPropertyValues()).contains("age=2.0");
    assertThat(properties.getPropertyValues()).contains("age=2.1");
    assertThat(properties.getPropertyValues()).contains("age=3.0");
    assertThat(properties.getPropertyValues()).contains("age=3.1");
    assertThat(properties.getPropertyValues()).contains("age=3.2");
    assertThat(properties.getPropertyValues()).contains("age=4.0");
    assertThat(properties.getPropertyValues()).contains("age=4.1");
    assertThat(properties.getPropertyValues()).contains("age=5.0");
    assertThat(properties.getPropertyValues()).contains("age=5.1");
    assertThat(properties.getPropertyValues()).contains("age=5.2");
    assertThat(properties.getPropertyValues()).contains("age=6.0");
    assertThat(properties.getPropertyValues()).contains("age=6.1");
    assertThat(properties.getPropertyValues()).contains("age=6.2");
    assertThat(properties.getPropertyValues()).contains("age=6.3");
    assertThat(properties.getPropertyValues()).contains("age=7.0");
    assertThat(properties.getPropertyValues()).contains("age=8.0");
    assertThat(properties.getPropertyValues()).contains("age=9.0");
    assertThat(properties.getPropertyValues()).contains("age=10.0");
    assertThat(properties.getPropertyValues()).contains("age=11.0");
    assertThat(properties.getPropertyValues()).contains("age=12.0");
  }

  /** Tests character class syntax of the Unicode 12.0 Age=1.1 property. */
  @Test
  public void ageIntervals_1_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_1_1::new),
        UnicodeAge_12_0_age_1_1.YYEOF,
        "UnicodeAge_12_0_age_1_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=2.0 property. */
  @Test
  public void ageIntervals_2_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_2_0::new),
        UnicodeAge_12_0_age_2_0.YYEOF,
        "UnicodeAge_12_0_age_2_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=2.1 property. */
  @Test
  public void ageIntervals_2_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_2_1::new),
        UnicodeAge_12_0_age_2_1.YYEOF,
        "UnicodeAge_12_0_age_2_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=3.0 property. */
  @Test
  public void ageIntervals_3_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_3_0::new),
        UnicodeAge_12_0_age_3_0.YYEOF,
        "UnicodeAge_12_0_age_3_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=3.1 property. */
  @Test
  public void ageIntervals_3_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_3_1::new),
        UnicodeAge_12_0_age_3_1.YYEOF,
        "UnicodeAge_12_0_age_3_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=3.2 property. */
  @Test
  public void ageIntervals_3_2() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_3_2::new),
        UnicodeAge_12_0_age_3_2.YYEOF,
        "UnicodeAge_12_0_age_3_2.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=4.0 property. */
  @Test
  public void ageIntervals_4_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_4_0::new),
        UnicodeAge_12_0_age_4_0.YYEOF,
        "UnicodeAge_12_0_age_4_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=4.1 property. */
  @Test
  public void ageIntervals_4_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_4_1::new),
        UnicodeAge_12_0_age_4_1.YYEOF,
        "UnicodeAge_12_0_age_4_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=5.0 property. */
  @Test
  public void ageIntervals_5_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_5_0::new),
        UnicodeAge_12_0_age_5_0.YYEOF,
        "UnicodeAge_12_0_age_5_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=5.1 property. */
  @Test
  public void ageIntervals_5_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_5_1::new),
        UnicodeAge_12_0_age_5_1.YYEOF,
        "UnicodeAge_12_0_age_5_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=5.2 property. */
  @Test
  public void ageIntervals_5_2() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_5_2::new),
        UnicodeAge_12_0_age_5_2.YYEOF,
        "UnicodeAge_12_0_age_5_2.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=6.0 property. */
  @Test
  public void ageIntervals_6_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_6_0::new),
        UnicodeAge_12_0_age_6_0.YYEOF,
        "UnicodeAge_12_0_age_6_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=6.1 property. */
  @Test
  public void ageIntervals_6_1() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_6_1::new),
        UnicodeAge_12_0_age_6_1.YYEOF,
        "UnicodeAge_12_0_age_6_1.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=6.2 property. */
  @Test
  public void ageIntervals_6_2() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_6_2::new),
        UnicodeAge_12_0_age_6_2.YYEOF,
        "UnicodeAge_12_0_age_6_2.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=6.3 property. */
  @Test
  public void ageIntervals_6_3() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_6_3::new),
        UnicodeAge_12_0_age_6_3.YYEOF,
        "UnicodeAge_12_0_age_6_3.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=7.0 property. */
  @Test
  public void ageIntervals_7_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_7_0::new),
        UnicodeAge_12_0_age_7_0.YYEOF,
        "UnicodeAge_12_0_age_7_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=8.0 property. */
  @Test
  public void ageIntervals_8_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_8_0::new),
        UnicodeAge_12_0_age_8_0.YYEOF,
        "UnicodeAge_12_0_age_8_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=9.0 property. */
  @Test
  public void ageIntervals_9_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_9_0::new),
        UnicodeAge_12_0_age_9_0.YYEOF,
        "UnicodeAge_12_0_age_9_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=10.0 property. */
  @Test
  public void ageIntervals_10_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_10_0::new),
        UnicodeAge_12_0_age_10_0.YYEOF,
        "UnicodeAge_12_0_age_10_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=11.0 property. */
  @Test
  public void ageIntervals_11_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_11_0::new),
        UnicodeAge_12_0_age_11_0.YYEOF,
        "UnicodeAge_12_0_age_11_0.output");
  }
  /** Tests character class syntax of the Unicode 12.0 Age=12.0 property. */
  @Test
  public void ageIntervals_12_0() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_12_0::new),
        UnicodeAge_12_0_age_12_0.YYEOF,
        "UnicodeAge_12_0_age_12_0.output");
  }

  /**
   * Tests subtracting Age Unicode property values in character sets for Unicode 12.0, e.g. {@code
   * [\p{Age:2.0}--\p{Age:1.1}]}.
   */
  @Test
  public void ageIntervals_subtraction() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_subtraction::new),
        UnicodeAge_12_0_age_subtraction.YYEOF,
        "UnicodeAge_12_0_age_subtraction.output");
  }

  /** Tests character class syntax of the Unicode 12.0 Age=Unassigned property. */
  @Test
  public void ageIntervals_unassigned() throws Exception {
    assertAgeInterval(
        ScannerFactory.of(UnicodeAge_12_0_age_unassigned::new),
        UnicodeAge_12_0_age_unassigned.YYEOF,
        "UnicodeAge_12_0_age_unassigned.output");
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
