/*
 * Copyright (C) 2009-2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode.unicode_9_0;

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
public class UnicodeCompatibilityPropertiesTest_9_0 {

  private static final String TEST_DIR =
      getPathForClass(UnicodeCompatibilityPropertiesTest_9_0.class);

  /** Test the character class syntax of the Unicode 9.0 'alnum' compatibility property. */
  @Test
  public void alnum() throws Exception {
    checkCompatibility(
        "alnum",
        UnicodeCompatibilityProperties_alnum_9_0.class,
        UnicodeCompatibilityProperties_alnum_9_0::new,
        UnicodeCompatibilityProperties_alnum_9_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 9.0 'blank' compatibility property. */
  @Test
  public void blank() throws Exception {
    checkCompatibility(
        "blank",
        UnicodeCompatibilityProperties_blank_9_0.class,
        UnicodeCompatibilityProperties_blank_9_0::new,
        UnicodeCompatibilityProperties_blank_9_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 9.0 'graph' compatibility property. */
  @Test
  public void graph() throws Exception {
    checkCompatibility(
        "graph",
        UnicodeCompatibilityProperties_graph_9_0.class,
        UnicodeCompatibilityProperties_graph_9_0::new,
        UnicodeCompatibilityProperties_graph_9_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 9.0 'print' compatibility property. */
  @Test
  public void print() throws Exception {
    checkCompatibility(
        "print",
        UnicodeCompatibilityProperties_print_9_0.class,
        UnicodeCompatibilityProperties_print_9_0::new,
        UnicodeCompatibilityProperties_print_9_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 9.0 'xdigit' compatibility property. */
  @Test
  public void xdigit() throws Exception {
    checkCompatibility(
        "xdigit",
        UnicodeCompatibilityProperties_xdigit_9_0.class,
        UnicodeCompatibilityProperties_xdigit_9_0::new,
        UnicodeCompatibilityProperties_xdigit_9_0.YYEOF);
  }

  private static <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>>
      void checkCompatibility(
          String propName, Class<T> scannerClass, Function<Reader, T> constructorRef, int eof)
          throws IOException {
    Path expectedFile =
        Paths.get("javatests")
            .resolve(TEST_DIR)
            .resolve("UnicodeCompatibilityProperties_" + propName + "_9_0.output");
    TestingUnicodeProperties.checkProperty(
        constructorRef, eof, expectedFile, UnicodeDataScanners.Dataset.ALL);
  }
}
