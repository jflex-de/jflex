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

// generated from UnicodeDerivedCorePropertiesTestGenerator.java.vm

/**
 * Test the derived core properties.
 *
 * @since Unicode 3.1
 */
@Generated(
    "de.jflex.migration.unicodedatatest.testderivedcoreprop.UnicodeDerivedCorePropertiesTestGenerator")
public class UnicodeDerivedCorePropertiesTest_9_0 {

  private static final String TEST_DIR =
      getPathForClass(UnicodeDerivedCorePropertiesTest_9_0.class);

  /** Test the character class syntax of the Unicode 9.0. */
  @Test
  public void test_Alphabetic() throws Exception {
    checkDerivedCoreProperty(
        "Alphabetic",
        UnicodeDerivedCoreProperties_Alphabetic_9_0.class,
        UnicodeDerivedCoreProperties_Alphabetic_9_0::new,
        UnicodeDerivedCoreProperties_Alphabetic_9_0.YYEOF);
  }

  private <T extends AbstractEnumeratedPropertyDefinedScanner<Boolean>>
      void checkDerivedCoreProperty(
          String propertyName, Class<T> scannerClass, Function<Reader, T> constructorRef, int eof)
          throws IOException {
    Path expectedFile =
        Paths.get("javatests")
            .resolve(TEST_DIR)
            .resolve("UnicodeDerivedCoreProperties_" + propertyName + "_9_0.output");
    TestingUnicodeProperties.checkProperty(
        constructorRef, eof, expectedFile, UnicodeDataScanners.Dataset.ALL);
  }
}
