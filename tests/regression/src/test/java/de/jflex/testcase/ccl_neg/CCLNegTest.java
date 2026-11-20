/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_neg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests a negated character class with overlapping content: {@code [^\n\s]}, where {@code \n} is
 * part of {@code \s}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_neg/neg_overlap.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 5, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 3; 4 -> 1",
      "1 [a0]: 0 -> 1; 4 -> 1",
      "2 [a1]:",
      "3 [a2]: 2 -> 3",
      "a0: return 1;",
      "a1: return 0;",
      "a2: return 2;"
    },
    charClassesCount = 5,
    sameCharClass = {"a_0\u00E4\uFFFF", " \t\u00A0\u3000", "\r\u000B\u000C\u0085\u2028\u2029"},
    distinctCharClasses = {"a \n\r\uD800"})
public class CCLNegTest implements JFlexTest {

  /** {@code [^\n\s]+} */
  private static final int NOT_SPACE = 1;

  /** {@code [\n]+} */
  private static final int NL = 2;

  /** The dot. */
  private static final int DEFAULT = 0;

  private CCLNeg scanner;

  private void scan(String input) {
    scanner = new CCLNeg(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(-1, scanner.yylex());
  }

  @Test
  public void test() throws Exception {
    scan("not_space then\n space");
    assertToken(NOT_SPACE, "not_space");
    assertToken(DEFAULT, " ");
    assertToken(NOT_SPACE, "then");
    assertToken(NL, "\n");
    assertToken(DEFAULT, " ");
    assertToken(NOT_SPACE, "space");
    assertEof();
  }

  /** The negated class has neither {@code \n} nor the rest of {@code \s}. */
  @Test
  public void whiteSpaceEndsMatch() throws Exception {
    scan("a\tb c\n\n\nd");
    assertToken(NOT_SPACE, "a");
    assertToken(DEFAULT, "\t");
    assertToken(NOT_SPACE, "b");
    assertToken(DEFAULT, " ");
    assertToken(NOT_SPACE, "c");
    assertToken(NL, "\n\n\n");
    assertToken(NOT_SPACE, "d");
    assertEof();
  }

  /** The negated class has everything else, also supplementary characters and surrogates. */
  @Test
  public void notSpace() throws Exception {
    String input = "\u0000.-\u00E4\u20AC" + new String(Character.toChars(0x1F600)) + "\uD800x";
    scan(input);
    assertToken(NOT_SPACE, input);
    assertEof();
  }
}
