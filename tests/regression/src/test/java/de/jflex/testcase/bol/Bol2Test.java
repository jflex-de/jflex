/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.bol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests the beginning of line operator {@code ^} in a spec that has no character class for line
 * terminators. The scanner still has to see where a line begins.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/bol/bol2.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 13,
    minimizedDfa = {
      "states 13, inputs 5, lexical states 1",
      "entry: 0 1",
      "0: 0-1 -> 2; 2 -> 3; 3-4 -> 2",
      "1: 0-1 -> 2; 2 -> 4; 3-4 -> 2",
      "2 [a0]:",
      "3 [a0]: 1 -> 5",
      "4 [a0]: 1 -> 6",
      "5: 3 -> 7",
      "6: 3 -> 8",
      "7: 3 -> 9",
      "8: 3 -> 10",
      "9: 4 -> 11",
      "10: 4 -> 12",
      "11 [a1]:",
      "12 [a2]:",
      "a0: return State.OTHER;",
      "a1: return State.HELLO_SIMPLY;",
      "a2: return State.HELLO_AT_BOL;"
    },
    charClassesCount = 5,
    // class 0 is everything but the letters of "hello", line terminators included
    charClasses = {
      "{ [0-'d']['f'-'g']['i'-'k']['m'-'n']['p'-1114111] }",
      "{ ['e'] }",
      "{ ['h'] }",
      "{ ['l'] }",
      "{ ['o'] }"
    },
    sameCharClass = {"x \n\r\u000B\u000C\u0085\u2028\u2029"})
public class Bol2Test implements JFlexTest {

  /** {@code ^"hello"} */
  private static final State AT_BOL = State.HELLO_AT_BOL;

  /** {@code "hello"} */
  private static final State NOT_AT_BOL = State.HELLO_SIMPLY;

  /** {@code [^]} */
  private static final State OTHER = State.OTHER;

  /** What JFlex takes as the end of a line, apart from {@code \r\n}. */
  private static final String[] LINE_TERMINATORS = {
    "\n", "\r", "\u000B", "\u000C", "\u0085", "\u2028", "\u2029"
  };

  private Bol2Scanner scanner;

  private void scan(String input) {
    scanner = new Bol2Scanner(new StringReader(input));
  }

  private void assertToken(State rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  /** Asserts that the next tokens are one {@code [^]} match for each char of the text. */
  private void assertOther(String text) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(OTHER, text.substring(i, i + 1));
    }
  }

  private void assertEof() throws IOException {
    assertEquals(State.END_OF_FILE, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void startOfInput() throws Exception {
    scan("hello");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void afterLineTerminator() throws Exception {
    for (String terminator : LINE_TERMINATORS) {
      scan("x" + terminator + "hello");
      assertOther("x" + terminator);
      assertToken(AT_BOL, "hello");
      assertEof();
    }
  }

  @Test
  public void afterLineTerminatorAtStartOfInput() throws Exception {
    for (String terminator : LINE_TERMINATORS) {
      scan(terminator + "hello");
      assertOther(terminator);
      assertToken(AT_BOL, "hello");
      assertEof();
    }
  }

  /** {@code \r\n} is one line terminator. */
  @Test
  public void afterCrLf() throws Exception {
    scan("x\r\nhello");
    assertOther("x\r\n");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void everyLineOfSeveral() throws Exception {
    scan("hello\nhello\r\nhello\rhello\n\nhello");
    assertToken(AT_BOL, "hello");
    assertOther("\n");
    assertToken(AT_BOL, "hello");
    assertOther("\r\n");
    assertToken(AT_BOL, "hello");
    assertOther("\r");
    assertToken(AT_BOL, "hello");
    assertOther("\n\n");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void notAtStartOfLine() throws Exception {
    scan("  hello  ");
    assertOther("  ");
    assertToken(NOT_AT_BOL, "hello");
    assertOther("  ");
    assertEof();
    scan("x\n hello");
    assertOther("x\n ");
    assertToken(NOT_AT_BOL, "hello");
    assertEof();
  }

  /** Only the first match of a line is at its beginning. */
  @Test
  public void secondMatchOfLine() throws Exception {
    scan("hellohello\nhellohello");
    assertToken(AT_BOL, "hello");
    assertToken(NOT_AT_BOL, "hello");
    assertOther("\n");
    assertToken(AT_BOL, "hello");
    assertToken(NOT_AT_BOL, "hello");
    assertEof();
  }

  /** A line terminator after the match does not make it one at the beginning of a line. */
  @Test
  public void beforeLineTerminator() throws Exception {
    for (String terminator : LINE_TERMINATORS) {
      scan(" hello" + terminator);
      assertOther(" ");
      assertToken(NOT_AT_BOL, "hello");
      assertOther(terminator);
      assertEof();
    }
  }

  /** A prefix of {@code hello} at the beginning of a line is matched char by char. */
  @Test
  public void prefixAtStartOfLine() throws Exception {
    scan("x\nhell\nhello");
    assertOther("x\nhell\n");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  /** Several lines, some of them with indent or trailing space. */
  @Test
  public void linesWithAndWithoutIndent() throws Exception {
    scan("hello\n  hello  \n  hello\nhello \n \nsdfa\nhello");
    assertToken(AT_BOL, "hello");
    assertOther("\n  ");
    assertToken(NOT_AT_BOL, "hello");
    assertOther("  \n  ");
    assertToken(NOT_AT_BOL, "hello");
    assertOther("\n");
    assertToken(AT_BOL, "hello");
    assertOther(" \n \nsdfa\n");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
