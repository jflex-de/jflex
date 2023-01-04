// test: UnicodeCodePointEscapes
/*
 * Copyright (C) 2019-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_codepoint_escapes;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.testing.unicodedata.UnicodeDataScanners.Dataset;
import de.jflex.ucd.NamedCodepointRange;
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
            NamedCodepointRange.create("inverse matched", 0x0000, 0x0000),
            NamedCodepointRange.create("matched", 0x0001, 0x0003),
            NamedCodepointRange.create("inverse matched", 0x0004, 0x000F),
            NamedCodepointRange.create("matched", 0x0010, 0x0011),
            NamedCodepointRange.create("inverse matched", 0x0012, 0x0CFE),
            NamedCodepointRange.create("matched", 0x0CFF, 0x0D00),
            NamedCodepointRange.create("inverse matched", 0x0D01, 0x0FFE),
            NamedCodepointRange.create("matched", 0x0FFF, 0x1001),
            NamedCodepointRange.create("inverse matched", 0x1002, 0xD7FF),
            NamedCodepointRange.create("inverse matched", 0xE000, 0xFFFE),
            NamedCodepointRange.create("matched", 0xFFFF, 0x10001),
            NamedCodepointRange.create("inverse matched", 0x10002, 0x10FFFE),
            NamedCodepointRange.create("matched", 0x10FFFF, 0x10FFFF));
  }
}
