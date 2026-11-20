/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_caseless;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for issue <a href"https://github.com/jflex-de/jflex/issues/974">#974 Unexpected exception
 * encountered in JFlex</a>.
 *
 * <p>Tests that the generated scanner can handle case-insensitive character classes with characters
 * outside the current input char set.
 *
 * <p>The spec has {@code %full} and {@code %caseless}, and the rule {@code sb}. The case variants
 * of {@code s} are {@code S} and U+017F (Latin small letter long s), which is not in the 8 bit
 * character set. The class of {@code s} has to be {@code S} and {@code s} only.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_caseless/ccl_caseless.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0-1 -> 1; 2 -> 2",
      "1 [a0]:",
      "2 [a0]: 1 -> 3",
      "3 [a1]:",
      "a0: /* default */",
      "a1: return 1;"
    },
    charClassesCount = 3,
    charClasses = {
      "{ [0-'A']['C'-'R']['T'-'a']['c'-'r']['t'-255] }",
      "{ ['B']['b'] }",
      "{ ['S']['s'] }"
    })
public class Ccl_caselessGoldenTest extends AbstractGoldenTest implements JFlexTest {

  /** Creates a scanner conforming to the {@code ccl_caseless.flex} specification. */
  private final ScannerFactory<Ccl_caseless> scannerFactory = ScannerFactory.of(Ccl_caseless::new);

  private final File testRuntimeDir = new File("src/test/java/de/jflex/testcase/ccl_caseless");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "ccl_caseless-0.input"),
            new File(testRuntimeDir, "ccl_caseless-0.output"));
    compareSystemOutWith(golden);

    Ccl_caseless scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  /** All four ways to write {@code sb}. */
  @Test
  public void caseVariants() throws Exception {
    Ccl_caseless scanner = new Ccl_caseless(new StringReader("sb.sB.Sb.SB"));
    for (String text : new String[] {"sb", "sB", "Sb", "SB"}) {
      assertEquals(1, scanner.yylex());
      assertEquals(text, scanner.yytext());
    }
    assertEquals(Ccl_caseless.YYEOF, scanner.yylex());
  }

  /** U+017F is a case variant of {@code s}, but not a character of this scanner. */
  @Test
  public void longS() throws Exception {
    Ccl_caseless scanner = new Ccl_caseless(new StringReader("\u017Fb"));
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> scanner.yylex());
  }
}
