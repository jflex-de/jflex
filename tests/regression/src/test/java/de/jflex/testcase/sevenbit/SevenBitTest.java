/*
 * Copyright (C) 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.sevenbit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** Test generation of a 7-bit scanner. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/sevenbit/sevenbit.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]: 1 -> 2",
      "a0: return 1;",
      "a1: return 0;"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-8][11-12][14-31]['!'-127] }", "{ [9-10][13][' '] }"})
public class SevenBitTest implements JFlexTest {

  /** <code>{WhiteSpace}+</code> */
  private static final int WHITE_SPACE = 0;

  /** {@code [^]} */
  private static final int OTHER = 1;

  private SevenBit scanner;

  private void scan(String input) {
    scanner = new SevenBit(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(-1, scanner.yylex());
  }

  @Test
  public void run() throws java.io.IOException {
    scan("abc\n\n");
    assertToken(OTHER, "a");
    assertToken(OTHER, "b");
    assertToken(OTHER, "c");
    assertToken(WHITE_SPACE, "\n\n");
    assertEof();
  }

  @Test
  public void everySevenBitChar() throws Exception {
    for (char c = 0; c < 128; c++) {
      String input = String.valueOf(c);
      scan(input);
      assertToken(" \t\r\n".indexOf(c) >= 0 ? WHITE_SPACE : OTHER, input);
      assertEof();
    }
  }

  /** What the manual says for an input character with a code greater than 127. */
  @Test
  public void charOutsideOfCharacterSet() throws Exception {
    scan("a\u0080");
    assertToken(OTHER, "a");
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> scanner.yylex());
  }
}
