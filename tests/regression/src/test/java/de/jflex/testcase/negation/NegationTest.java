/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Test for bug <a href="https://github.com/jflex-de/jflex/issues/567">#567 (r|!r does not always
 * match everything)</a>.
 *
 * <p>The first rule is {@code r | !r} for {@code r = [^]* [^a]}, the texts that do not end in
 * {@code a}. It must match every text, including "". The other rule {@code aba} can never match.
 * Must produce two warnings: empty text match warning for the first, never match warning for the
 * second.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/negation/negation.flex",
    // the warnings for the empty match and the rule that is never matched
    sysout = "src/test/java/de/jflex/testcase/negation/negation.sysout",
    quiet = true,
    minimizedDfaStatesCount = 1,
    minimizedDfa = {
      "states 1, inputs 3, lexical states 1",
      "entry: 0 0",
      "0 [a0]: 0-2 -> 0",
      "a0: /* all */ return 1;"
    },
    charClassesCount = 3,
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class NegationTest implements JFlexTest {

  /** {@code ([^]* [^a]) | !([^]* [^a])} */
  private static final int ALL = 1;

  private Negation scanner;

  private void scan(String input) {
    scanner = new Negation(new StringReader(input));
  }

  /** Checks that the input is one match of the first rule, followed by the end of input. */
  private void assertOneMatch(String input) throws IOException {
    scan(input);
    assertEquals(ALL, scanner.yylex(), "rule");
    assertEquals(input, scanner.yytext(), "text");
    assertEquals(input.length(), scanner.yylength(), "length");
    assertEquals(0, scanner.line(), "line");
    assertEquals(0, scanner.column(), "column");
    assertEquals(Negation.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** The first input of the former golden file test: ends in {@code \n}, so {@code r} matches. */
  @Test
  public void notEndingInA() throws Exception {
    assertOneMatch("aba\n");
    assertOneMatch("ab");
    assertOneMatch("b");
  }

  /** The second input of the former golden file test, {@code !r} matches. */
  @Test
  public void endingInA() throws Exception {
    assertOneMatch("baba");
    assertOneMatch("a");
    assertOneMatch("aa");
  }

  /** The text of the second rule is matched by the first rule. */
  @Test
  public void textOfSecondRule() throws Exception {
    assertOneMatch("aba");
  }

  @Test
  public void severalLines() throws Exception {
    assertOneMatch("a\nb\r\na\n\na");
    assertOneMatch("a\nb\r\na\n\n");
  }

  /** Characters outside the BMP are in the class of {@code [^a]}. */
  @Test
  public void supplementaryCharacters() throws Exception {
    assertOneMatch("a😀");
    assertOneMatch("😀a");
  }

  /**
   * The first rule matches the empty text, but the empty input is the end of input: there is no
   * empty match before it, and no loop.
   */
  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEquals(Negation.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
    assertEquals(Negation.YYEOF, scanner.yylex());
  }

  /** An input that is longer than the buffer of the scanner is still one match. */
  @Test
  public void longInput() throws Exception {
    char[] input = new char[20_000];
    Arrays.fill(input, 'a');
    assertOneMatch(new String(input));
    input[input.length - 1] = 'b';
    assertOneMatch(new String(input));
    for (int i = 0; i < input.length; i++) {
      input[i] = "ab\n".charAt(i % 3);
    }
    assertOneMatch(new String(input));
  }
}
