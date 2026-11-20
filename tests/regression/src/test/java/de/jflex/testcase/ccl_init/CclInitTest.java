/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_init;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/986">char class directives delete
 * existing classes #986</a>.
 *
 * <p>The macro {@code x = ab} is defined before {@code %unicode}. The classes of {@code a} and
 * {@code b} must still be there after the directive.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_init/ccl.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 1",
      "1 [a0]:",
      "2 [a0]: 2 -> 3",
      "3 [a1]:",
      "a0: return 1;",
      "a1: return 0;"
    },
    charClassesCount = 3,
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class CclInitTest implements JFlexTest {

  /** {@code {x}}, which is {@code ab} */
  private static final int X = 0;

  /** {@code [^]} */
  private static final int OTHER = 1;

  private Ccl scanner;

  private void scan(String input) {
    scanner = new Ccl(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(Ccl.YYEOF, scanner.yylex());
  }

  @Test
  public void test_init() throws Exception {
    scan("cab");
    assertToken(OTHER, "c");
    assertToken(X, "ab");
    assertEof();
  }

  /** {@code a} and {@code b} alone are not the macro. */
  @Test
  public void partsOfMacro() throws Exception {
    scan("aabba");
    assertToken(OTHER, "a");
    assertToken(X, "ab");
    assertToken(OTHER, "b");
    assertToken(OTHER, "a");
    assertEof();
  }

  /** The character set is the one of {@code %unicode}. */
  @Test
  public void unicodeInput() throws Exception {
    String supplementary = new String(Character.toChars(0x1F600));
    scan("\u00E4\u20AC" + supplementary + "ab");
    assertToken(OTHER, "\u00E4");
    assertToken(OTHER, "\u20AC");
    assertToken(OTHER, supplementary);
    assertToken(X, "ab");
    assertEof();
  }
}
