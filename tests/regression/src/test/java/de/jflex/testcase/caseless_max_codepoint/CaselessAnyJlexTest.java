/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_max_codepoint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test {@code %ignorecase} with {@code [^]} in JLex mode ({@code --jlex}), for a Unicode version
 * with the maximum code point U+FFFF.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/caseless_max_codepoint/caseless-any-jlex.flex",
    noOutput = true,
    quiet = true,
    jlexCompat = true,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]:",
      "a0: return 0;",
      "a1: return 1;"
    },
    charClasses = {"{ [0-'J']['L'-'j']['l'-65535] }", "{ ['K']['k'] }"})
public class CaselessAnyJlexTest implements JFlexTest {

  /** {@code k}, in either case */
  private static final int K = 1;

  /** {@code [^]} */
  private static final int ANY = 0;

  private CaselessAnyJlex scanner;

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  @Test
  public void bothCases() throws Exception {
    scanner = new CaselessAnyJlex(new StringReader("kKxX"));
    assertToken(K, "k");
    assertToken(K, "K");
    assertToken(ANY, "x");
    assertToken(ANY, "X");
    assertEquals(CaselessAnyJlex.YYEOF, scanner.yylex());
  }

  /** The Kelvin sign U+212A is no case variant of {@code k} for this scanner. */
  @Test
  public void kelvinSign() throws Exception {
    scanner = new CaselessAnyJlex(new StringReader("\u212A"));
    assertToken(ANY, "\u212A");
  }

  /** {@code [^]} matches up to the maximum code point of the Unicode version. */
  @Test
  public void everyOtherBmpChar() throws Exception {
    for (int c = 0; c <= 0xFFFF; c++) {
      if (c == 'k' || c == 'K') {
        continue;
      }
      String input = String.valueOf((char) c);
      scanner = new CaselessAnyJlex(new StringReader(input));
      assertToken(ANY, input);
      assertEquals(CaselessAnyJlex.YYEOF, scanner.yylex());
    }
  }

  /**
   * A supplementary character is outside of the character set of Unicode 2.0, so the scanner has no
   * character class for it.
   */
  @Test
  public void charOutsideOfCharacterSet() throws Exception {
    scanner = new CaselessAnyJlex(new StringReader("k\uD83D\uDE00"));
    assertToken(K, "k");
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> scanner.yylex());
  }

  /** An unpaired surrogate is in the character set. */
  @Test
  public void unpairedSurrogates() throws Exception {
    scanner = new CaselessAnyJlex(new StringReader("\uDE00\uD83Dk\uD83D"));
    assertToken(ANY, "\uDE00");
    assertToken(ANY, "\uD83D");
    assertToken(K, "k");
    assertToken(ANY, "\uD83D");
    assertEquals(CaselessAnyJlex.YYEOF, scanner.yylex());
  }
}
