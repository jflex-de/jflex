/*
 * Copyright (C) 2009-2014 Steve Rowe <sarowe@gmail.com>
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
package de.jflex.testcase.unicode.unicode_6_0;

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
public class UnicodeCompatibilityPropertiesTest_6_0 {

  private static final String TEST_DIR =
      getPathForClass(UnicodeCompatibilityPropertiesTest_6_0.class);

  /** Test the character class syntax of the Unicode 6.0 'alnum' compatibility property. */
  @Test
  public void alnum() throws Exception {
    checkCompatibility(
        "alnum",
        UnicodeCompatibilityProperties_alnum_6_0.class,
        UnicodeCompatibilityProperties_alnum_6_0::new,
        UnicodeCompatibilityProperties_alnum_6_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.0 'blank' compatibility property. */
  @Test
  public void blank() throws Exception {
    checkCompatibility(
        "blank",
        UnicodeCompatibilityProperties_blank_6_0.class,
        UnicodeCompatibilityProperties_blank_6_0::new,
        UnicodeCompatibilityProperties_blank_6_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.0 'graph' compatibility property. */
  @Test
  public void graph() throws Exception {
    checkCompatibility(
        "graph",
        UnicodeCompatibilityProperties_graph_6_0.class,
        UnicodeCompatibilityProperties_graph_6_0::new,
        UnicodeCompatibilityProperties_graph_6_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.0 'print' compatibility property. */
  @Test
  public void print() throws Exception {
    checkCompatibility(
        "print",
        UnicodeCompatibilityProperties_print_6_0.class,
        UnicodeCompatibilityProperties_print_6_0::new,
        UnicodeCompatibilityProperties_print_6_0.YYEOF);
  }

  /** Test the character class syntax of the Unicode 6.0 'xdigit' compatibility property. */
  @Test
  public void xdigit() throws Exception {
    checkCompatibility(
        "xdigit",
        UnicodeCompatibilityProperties_xdigit_6_0.class,
        UnicodeCompatibilityProperties_xdigit_6_0::new,
        UnicodeCompatibilityProperties_xdigit_6_0.YYEOF);
  }

  private static <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>>
      void checkCompatibility(
          String propName, Class<T> scannerClass, Function<Reader, T> constructorRef, int eof)
          throws IOException {
    Path expectedFile =
        Paths.get("javatests")
            .resolve(TEST_DIR)
            .resolve("UnicodeCompatibilityProperties_" + propName + "_6_0.output");
    TestingUnicodeProperties.checkProperty(
        constructorRef, eof, expectedFile, UnicodeDataScanners.Dataset.ALL);
  }
}
