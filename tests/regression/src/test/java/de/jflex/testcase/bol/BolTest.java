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
 * Tests the beginning of line operator {@code ^} and the end of line operator {@code $}, and the
 * line, column and character counts next to them. {@link Bol2Test} has the tests for a spec without
 * a character class for line terminators.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/bol/bol.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 19,
    charClassesCount = 9,
    charClasses = {
      "{ [0-9][14-31]['!'-'d']['f'-'g']['i'-'k']['m'-'n']['p'-132][134-8231][8234-1114111] }",
      "{ [10] }",
      "{ [11-12][133][8232-8233] }",
      "{ [13] }",
      "{ [' '] }",
      "{ ['e'] }",
      "{ ['h'] }",
      "{ ['l'] }",
      "{ ['o'] }"
    },
    sameCharClass = {"\u000B\u000C\u0085\u2028\u2029", "x\t\uD800"})
public class BolTest implements JFlexTest {

  /** {@code ^"hello"$} */
  private static final State AT_BOL_AND_EOL = State.HELLO_AT_BOL_AND_EOL;

  /** {@code ^"hello"} */
  private static final State AT_BOL = State.HELLO_AT_BOL;

  /** {@code "hello"$} */
  private static final State AT_EOL = State.HELLO_AT_EOL;

  /** {@code "hello"} */
  private static final State SIMPLY = State.HELLO_SIMPLY;

  /** {@code \n} */
  private static final State LINE_FEED = State.LINE_FEED;

  private static final State SPACE = State.SPACE;

  /** {@code [^]}, which is also the rule for the line terminators other than {@code \n}. */
  private static final State OTHER = State.OTHER;

  /** Other line terminators, apart from {@code \n} and {@code \r\n}. */
  private static final String[] OTHER_LINE_TERMINATORS = {
    "\r", "\u000B", "\u000C", "\u0085", "\u2028", "\u2029"
  };

  private BolScanner scanner;

  private void scan(String input) {
    scanner = new BolScanner(new StringReader(input));
  }

  private void assertToken(State rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertToken(State rule, String text, int line, int column, int offset)
      throws IOException {
    assertToken(rule, text);
    assertEquals(line, scanner.line(), "line of " + rule);
    assertEquals(column, scanner.column(), "column of " + rule);
    assertEquals(offset, scanner.offset(), "offset of " + rule);
  }

  private void assertEof() throws IOException {
    assertEquals(State.END_OF_FILE, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void initial() throws Exception {
    scan("");
    assertEquals(State.INITIAL, scanner.state);
    assertEof();
  }

  @Test
  public void bolAndEol() throws Exception {
    scan("hello\n");
    assertToken(AT_BOL_AND_EOL, "hello");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  /** The match of {@code $} does not include the line terminator, here {@code \r\n}. */
  @Test
  public void bolAndEol2() throws Exception {
    scan("hello\r\n");
    assertToken(AT_BOL_AND_EOL, "hello");
    assertToken(OTHER, "\r");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void bolAndEol3() throws Exception {
    scan("\nhello\r\n");
    assertToken(LINE_FEED, "\n");
    assertToken(AT_BOL_AND_EOL, "hello");
    assertToken(OTHER, "\r");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void bolAndEof() throws Exception {
    // EOF does not count as EOL. Should it?
    scan("hello");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void eol() throws Exception {
    scan("  hello\n");
    assertToken(SPACE, " ");
    assertToken(SPACE, " ");
    assertToken(AT_EOL, "hello");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  /** As {@link #bolAndEof()}: the end of the input is no end of line. */
  @Test
  public void eolAndEof() throws Exception {
    scan(" hello");
    assertToken(SPACE, " ");
    assertToken(SIMPLY, "hello");
    assertEof();
  }

  @Test
  public void hello() throws Exception {
    scan("  hello  \n");
    assertToken(SPACE, " ");
    assertToken(SPACE, " ");
    assertToken(SIMPLY, "hello");
    assertToken(SPACE, " ");
    assertToken(SPACE, " ");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void bol() throws Exception {
    scan("hello \n");
    assertToken(AT_BOL, "hello");
    assertToken(SPACE, " ");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void space() throws Exception {
    scan(" \n");
    assertToken(SPACE, " ");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void other() throws Exception {
    scan("abc\n");
    assertToken(OTHER, "a");
    assertToken(OTHER, "b");
    assertToken(OTHER, "c");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  /** A line that is not the first of the input. */
  @Test
  public void bolInLaterLine() throws Exception {
    scan("x\nhello y");
    assertToken(OTHER, "x");
    assertToken(LINE_FEED, "\n");
    assertToken(AT_BOL, "hello");
    assertToken(SPACE, " ");
    assertToken(OTHER, "y");
    assertEof();
  }

  @Test
  public void eolInLaterLine() throws Exception {
    scan("x\n hello\n");
    assertToken(OTHER, "x");
    assertToken(LINE_FEED, "\n");
    assertToken(SPACE, " ");
    assertToken(AT_EOL, "hello");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void bolAndEolInEveryLine() throws Exception {
    scan("hello\nhello\nhello\n");
    for (int line = 0; line < 3; line++) {
      assertToken(AT_BOL_AND_EOL, "hello");
      assertToken(LINE_FEED, "\n");
    }
    assertEof();
  }

  @Test
  public void twoMatchesInOneLine() throws Exception {
    scan("hellohello\n");
    assertToken(AT_BOL, "hello");
    assertToken(AT_EOL, "hello");
    assertToken(LINE_FEED, "\n");
    assertEof();
    scan("hellohellohello\n");
    assertToken(AT_BOL, "hello");
    assertToken(SIMPLY, "hello");
    assertToken(AT_EOL, "hello");
    assertToken(LINE_FEED, "\n");
    assertEof();
  }

  @Test
  public void prefixOfHello() throws Exception {
    scan("hell\nhello");
    assertToken(OTHER, "h");
    assertToken(OTHER, "e");
    assertToken(OTHER, "l");
    assertToken(OTHER, "l");
    assertToken(LINE_FEED, "\n");
    assertToken(AT_BOL, "hello");
    assertEof();
  }

  @Test
  public void otherLineTerminators() throws Exception {
    for (String terminator : OTHER_LINE_TERMINATORS) {
      scan("hello" + terminator + "hello" + terminator + " hello" + terminator + "hello ");
      assertToken(AT_BOL_AND_EOL, "hello");
      assertToken(OTHER, terminator);
      assertToken(AT_BOL_AND_EOL, "hello");
      assertToken(OTHER, terminator);
      assertToken(SPACE, " ");
      assertToken(AT_EOL, "hello");
      assertToken(OTHER, terminator);
      assertToken(AT_BOL, "hello");
      assertToken(SPACE, " ");
      assertEof();
    }
  }

  @Test
  public void carriageReturnAndLineFeed() throws Exception {
    scan("hello\r\n\nhello\n\rhello");
    assertToken(AT_BOL_AND_EOL, "hello", 0, 0, 0);
    assertToken(OTHER, "\r", 0, 5, 5);
    assertToken(LINE_FEED, "\n", 0, 6, 6);
    assertToken(LINE_FEED, "\n", 1, 0, 7);
    assertToken(AT_BOL_AND_EOL, "hello", 2, 0, 8);
    assertToken(LINE_FEED, "\n", 2, 5, 13);
    assertToken(OTHER, "\r", 3, 0, 14);
    assertToken(AT_BOL, "hello", 4, 0, 15);
    assertEof();
  }

  @Test
  public void positions() throws Exception {
    scan("hello\n hello \r\nhello");
    assertToken(AT_BOL_AND_EOL, "hello", 0, 0, 0);
    assertToken(LINE_FEED, "\n", 0, 5, 5);
    assertToken(SPACE, " ", 1, 0, 6);
    assertToken(SIMPLY, "hello", 1, 1, 7);
    assertToken(SPACE, " ", 1, 6, 12);
    assertToken(OTHER, "\r", 1, 7, 13);
    assertToken(LINE_FEED, "\n", 1, 8, 14);
    assertToken(AT_BOL, "hello", 2, 0, 15);
    assertEof();
    assertEquals(2, scanner.line());
    assertEquals(5, scanner.column());
    assertEquals(20, scanner.offset());
  }

  @Test
  public void positionsWithOtherLineTerminators() throws Exception {
    for (String terminator : OTHER_LINE_TERMINATORS) {
      scan("hello" + terminator + "hello" + terminator);
      assertToken(AT_BOL_AND_EOL, "hello", 0, 0, 0);
      assertToken(OTHER, terminator, 0, 5, 5);
      assertToken(AT_BOL_AND_EOL, "hello", 1, 0, 6);
      assertToken(OTHER, terminator, 1, 5, 11);
      assertEof();
      assertEquals(2, scanner.line());
      assertEquals(0, scanner.column());
      assertEquals(12, scanner.offset());
    }
  }
}
