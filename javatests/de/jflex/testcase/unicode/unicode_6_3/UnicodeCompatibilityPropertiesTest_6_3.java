/*
 * Copyright (C) 2009-2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
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

// Generate from UnicodeCompatibilityPropertiesTest.java.vm
/**
 * Test for compatibility property, derived from UnicodeData(-X.X.X).txt, PropList(-X|-X.X.X).txt
 * and/or DerivedCoreProperties(-X.X.X).txt.
 */
@Generated(
    "de.jflex.migration.unicodedatatest.testcompat.UnicodeCompatibilityPropertiesTestGenerator")
public class UnicodeCompatibilityPropertiesTest_6_3 {

  private static final String TEST_DIR =
      getPathForClass(UnicodeCompatibilityPropertiesTest_6_3.class);

  /** Test the character class syntax of the Unicode 6.3 'alnum' compatibility property. */
  @Test
  public void alnum() throws Exception {
    checkCompatibility(
        "alnum",
        UnicodeCompatibilityProperties_alnum_6_3.class,
        UnicodeCompatibilityProperties_alnum_6_3::new,
        UnicodeCompatibilityProperties_alnum_6_3.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.3 'blank' compatibility property. */
  @Test
  public void blank() throws Exception {
    checkCompatibility(
        "blank",
        UnicodeCompatibilityProperties_blank_6_3.class,
        UnicodeCompatibilityProperties_blank_6_3::new,
        UnicodeCompatibilityProperties_blank_6_3.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.3 'graph' compatibility property. */
  @Test
  public void graph() throws Exception {
    checkCompatibility(
        "graph",
        UnicodeCompatibilityProperties_graph_6_3.class,
        UnicodeCompatibilityProperties_graph_6_3::new,
        UnicodeCompatibilityProperties_graph_6_3.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.3 'print' compatibility property. */
  @Test
  public void print() throws Exception {
    checkCompatibility(
        "print",
        UnicodeCompatibilityProperties_print_6_3.class,
        UnicodeCompatibilityProperties_print_6_3::new,
        UnicodeCompatibilityProperties_print_6_3.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.3 'xdigit' compatibility property. */
  @Test
  public void xdigit() throws Exception {
    checkCompatibility(
        "xdigit",
        UnicodeCompatibilityProperties_xdigit_6_3.class,
        UnicodeCompatibilityProperties_xdigit_6_3::new,
        UnicodeCompatibilityProperties_xdigit_6_3.YYEOF);
  }

  private static <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>>
      void checkCompatibility(
          String propName, Class<T> scannerClass, Function<Reader, T> constructorRef, int eof)
          throws IOException {
    Path expectedFile =
        Paths.get("javatests")
            .resolve(TEST_DIR)
            .resolve("UnicodeCompatibilityProperties_" + propName + "_6_3.output");
    TestingUnicodeProperties.checkProperty(
        constructorRef, eof, expectedFile, UnicodeDataScanners.Dataset.ALL);
  }
}
