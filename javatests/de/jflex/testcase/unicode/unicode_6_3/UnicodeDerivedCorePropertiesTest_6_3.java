/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode.unicode_6_3;

import static de.jflex.util.javac.JavaPackageUtils.getPathForClass;

import de.jflex.testing.unicodedata.AbstractEnumeratedPropertyDefinedScanner;
import de.jflex.testing.unicodedata.TestingUnicodeProperties;
import de.jflex.testing.unicodedata.UnicodeDataScanners;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import javax.annotation.Generated;
import org.junit.Test;

// generated from UnicodeDerivedCorePropertiesTestGenerator.java.vm

/**
 * Test the derived core properties.
 *
 * @since Unicode 3.1
 */
@Generated(
    "de.jflex.migration.unicodedatatest.testderivedcoreprop.UnicodeDerivedCorePropertiesTestGenerator")
public class UnicodeDerivedCorePropertiesTest_6_3 {

  private static final String TEST_DIR =
      getPathForClass(UnicodeDerivedCorePropertiesTest_6_3.class);

  /** Test the character class syntax of the Unicode 6.3. */
  @Test
  public void test_Alphabetic() throws Exception {
    checkDerivedCoreProperty(
        "Alphabetic",
        UnicodeDerivedCoreProperties_Alphabetic_6_3.class,
        UnicodeDerivedCoreProperties_Alphabetic_6_3::new,
        UnicodeDerivedCoreProperties_Alphabetic_6_3.YYEOF);
  }

  private <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>>
      void checkDerivedCoreProperty(
          String propertyName, Class<T> scannerClass, Function<Reader, T> constructorRef, int eof)
          throws IOException {
    Path expectedFile =
        Paths.get("javatests")
            .resolve(TEST_DIR)
            .resolve("UnicodeDerivedCoreProperties_" + propertyName + "_6_3.output");
    TestingUnicodeProperties.checkProperty(
        constructorRef, eof, expectedFile, UnicodeDataScanners.Dataset.ALL);
  }
}
