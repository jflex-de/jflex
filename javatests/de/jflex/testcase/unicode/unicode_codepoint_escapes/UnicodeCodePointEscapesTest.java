// test: UnicodeCodePointEscapes
/*
 * Copyright (C) 2019-2021 Google, LLC.
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
package de.jflex.testcase.unicode.unicode_codepoint_escapes;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.unicodedata.BlockSpec;
import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.util.javac.JavaPackageUtils;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/** Tests <code>\\u{X+}, \\u{X+( X+)*}</code> hexadecimal codepoint escape syntax. */
public class UnicodeCodePointEscapesTest {

  private static final Path CLASS_PATH =
      Paths.get("javatests")
          .resolve(JavaPackageUtils.getPathForClass(UnicodeCodePointEscapesTest.class));

  /** Creates a scanner conforming to the {@code UnicodeCodePointEscapes.flex} specification. */
  private final ScannerFactory<UnicodeCodePointEscapes> scannerFactory =
      ScannerFactory.of(UnicodeCodePointEscapes::new);

  /**
   * Tests that the scanner detects all codepoints.
   *
   * <pre>
   * 0000..0000; inverse matched
   * 0001..0003; matched
   * 0004..000F; inverse matched
   * 0010..0011; matched
   * 0012..0CFE; inverse matched
   * 0CFF..0D00; matched
   * 0D01..0FFE; inverse matched
   * 0FFF..1001; matched
   * 1002..D7FF; inverse matched
   * E000..FFFE; inverse matched
   * FFFF..10001; matched
   * 10002..10FFFE; inverse matched
   * 10FFFF..10FFFF; matched
   * </pre>
   */
  @Test
  public void test() throws Exception {
    UnicodeCodePointEscapes scanner =
        scannerFactory.createScannerForFile(
            new File("testsuite/testcases/src/test/resources/" + Dataset.ALL.dataFile()));
    scanner.yylex();
    assertThat(scanner.blocks())
        .containsExactly(
            BlockSpec.create("inverse matched", 0x0000, 0x0000),
            BlockSpec.create("matched", 0x0001, 0x0003),
            BlockSpec.create("inverse matched", 0x0004, 0x000F),
            BlockSpec.create("matched", 0x0010, 0x0011),
            BlockSpec.create("inverse matched", 0x0012, 0x0CFE),
            BlockSpec.create("matched", 0x0CFF, 0x0D00),
            BlockSpec.create("inverse matched", 0x0D01, 0x0FFE),
            BlockSpec.create("matched", 0x0FFF, 0x1001),
            BlockSpec.create("inverse matched", 0x1002, 0xD7FF),
            BlockSpec.create("inverse matched", 0xE000, 0xFFFE),
            BlockSpec.create("matched", 0xFFFF, 0x10001),
            BlockSpec.create("inverse matched", 0x10002, 0x10FFFE),
            BlockSpec.create("matched", 0x10FFFF, 0x10FFFF));
  }
}
