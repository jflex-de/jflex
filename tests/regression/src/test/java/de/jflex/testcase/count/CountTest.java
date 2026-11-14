/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.count;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test for line, column and character counting ({@code %line}, {@code %column}, {@code %char}).
 *
 * <p>All three counters count from 0. {@code yycolumn} and {@code yychar} count Java {@code char}s,
 * the same as {@code yylength()}. A character outside the BMP counts as two.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/count/count.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/count/count.sysout",
    quiet = true,
    minimizedDfaStatesCount = 4,
    minimizedDfa = {
      "states 4, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a0]: 1 -> 3",
      "3 [a0]: 1 -> 1",
      "a0: yypushback(pushback); pushback = 0; return 1;"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-'`']['{'-1114111] }", "{ ['a'-'z'] }"})
public class CountTest implements JFlexTest {

  /** The initial size of the scanner buffer ({@code ZZ_BUFFERSIZE}). */
  private static final int BUFFER_SIZE = 16384;

  private Count scanner;

  private void scan(String input) {
    scanner = new Count(new StringReader(input));
  }

  private void assertToken(String text, int line, int column, long offset) throws IOException {
    String at = "at line " + line + ", column " + column + ", char " + offset;
    assertTokenWithoutColumn(text, line, offset, at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  /**
   * Checks that the next token is the {@code \n} of a line break {@code \r\n}. It is in the line
   * that it ends. Its column is not checked here because it is wrong, see {@link
   * #columnOfLfAfterCr()}.
   */
  private void assertLfAfterCr(int line, long offset) throws IOException {
    assertTokenWithoutColumn("\n", line, offset, "at line " + line + ", char " + offset);
  }

  private void assertTokenWithoutColumn(String text, int line, long offset, String at)
      throws IOException {
    assertEquals(1, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(offset, scanner.offset(), "char " + at);
  }

  /** Checks that the input ends here. The counters are those of the position after the input. */
  private void assertEof(int line, int column, long offset) throws IOException {
    String at = "at line " + line + ", column " + column + ", char " + offset;
    assertEquals(Count.YYEOF, scanner.yylex(), "end of input " + at);
    assertTrue(scanner.yyatEOF());
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
    assertEquals(offset, scanner.offset(), "char " + at);
  }

  private static String repeat(String s, int times) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < times; i++) {
      result.append(s);
    }
    return result.toString();
  }

  @Test
  public void threeLines() throws Exception {
    scan("bla blub\nblubber\nbla\n");
    assertToken("bla", 0, 0, 0);
    assertToken(" ", 0, 3, 3);
    assertToken("blu", 0, 4, 4);
    assertToken("b", 0, 7, 7);
    assertToken("\n", 0, 8, 8);
    assertToken("blu", 1, 0, 9);
    assertToken("bbe", 1, 3, 12);
    assertToken("r", 1, 6, 15);
    assertToken("\n", 1, 7, 16);
    assertToken("bla", 2, 0, 17);
    assertToken("\n", 2, 3, 20);
    assertEof(3, 0, 21);
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof(0, 0, 0);
  }

  /** {@code [a-z]{2,3}} on one to seven letters: as many matches of three letters as possible. */
  @Test
  public void repetition() throws Exception {
    scan("a");
    assertToken("a", 0, 0, 0);
    assertEof(0, 1, 1);

    scan("ab");
    assertToken("ab", 0, 0, 0);
    assertEof(0, 2, 2);

    scan("abc");
    assertToken("abc", 0, 0, 0);
    assertEof(0, 3, 3);

    scan("abcd");
    assertToken("abc", 0, 0, 0);
    assertToken("d", 0, 3, 3);
    assertEof(0, 4, 4);

    scan("abcde");
    assertToken("abc", 0, 0, 0);
    assertToken("de", 0, 3, 3);
    assertEof(0, 5, 5);

    scan("abcdef");
    assertToken("abc", 0, 0, 0);
    assertToken("def", 0, 3, 3);
    assertEof(0, 6, 6);

    scan("abcdefg");
    assertToken("abc", 0, 0, 0);
    assertToken("def", 0, 3, 3);
    assertToken("g", 0, 6, 6);
    assertEof(0, 7, 7);
  }

  /** Only {@code [a-z]} repeats. */
  @Test
  public void noRepetitionOfOtherCharacters() throws Exception {
    scan("aBC12yz");
    assertToken("a", 0, 0, 0);
    assertToken("B", 0, 1, 1);
    assertToken("C", 0, 2, 2);
    assertToken("1", 0, 3, 3);
    assertToken("2", 0, 4, 4);
    assertToken("yz", 0, 5, 5);
    assertEof(0, 7, 7);
  }

  @Test
  public void cr() throws Exception {
    scan("ab\rcd\r");
    assertToken("ab", 0, 0, 0);
    assertToken("\r", 0, 2, 2);
    assertToken("cd", 1, 0, 3);
    assertToken("\r", 1, 2, 5);
    assertEof(2, 0, 6);
  }

  /** {@code \r\n} is one line break, and both characters are in the line that they end. */
  @Test
  public void crLf() throws Exception {
    scan("ab\r\ncd\r\n");
    assertToken("ab", 0, 0, 0);
    assertToken("\r", 0, 2, 2);
    assertLfAfterCr(0, 3);
    assertToken("cd", 1, 0, 4);
    assertToken("\r", 1, 2, 6);
    assertLfAfterCr(1, 7);
    assertEof(2, 0, 8);
  }

  /**
   * The {@code \n} of {@code \r\n} is one column after the {@code \r}, when it is a match of its
   * own.
   */
  @Disabled(
      "TODO: JFlex bug. When a match ends with \\r"
          + " and the next match starts with \\n"
          + ", yycolumn of the second match is 0 in the line of the \\r"
          + ".")
  @Test
  public void columnOfLfAfterCr() throws Exception {
    scan("ab\r\ncd\r\n");
    assertToken("ab", 0, 0, 0);
    assertToken("\r", 0, 2, 2);
    assertToken("\n", 0, 3, 3);
    assertToken("cd", 1, 0, 4);
    assertToken("\r", 1, 2, 6);
    assertToken("\n", 1, 3, 7);
    assertEof(2, 0, 8);
  }

  /** {@code \n\r} is two line breaks. */
  @Test
  public void lfCr() throws Exception {
    scan("ab\n\rcd");
    assertToken("ab", 0, 0, 0);
    assertToken("\n", 0, 2, 2);
    assertToken("\r", 1, 0, 3);
    assertToken("cd", 2, 0, 4);
    assertEof(2, 2, 6);
  }

  /** Only the last {@code \r} belongs to the {@code \n}. */
  @Test
  public void crCrLf() throws Exception {
    scan("\r\r\n\n");
    assertToken("\r", 0, 0, 0);
    assertToken("\r", 1, 0, 1);
    assertLfAfterCr(1, 2);
    assertToken("\n", 2, 0, 3);
    assertEof(3, 0, 4);
  }

  /** The other line terminators of Unicode */
  @Test
  public void unicodeLineTerminators() throws Exception {
    for (String terminator : new String[] {"\u000B", "\u000C", "\u0085", "\u2028", "\u2029"}) {
      scan("ab" + terminator + "cd" + terminator + terminator + "\n");
      assertToken("ab", 0, 0, 0);
      assertToken(terminator, 0, 2, 2);
      assertToken("cd", 1, 0, 3);
      assertToken(terminator, 1, 2, 5);
      assertToken(terminator, 2, 0, 6);
      // not one line break together with the terminator before it
      assertToken("\n", 3, 0, 7);
      assertEof(4, 0, 8);
    }
  }

  /** A tab is one column. Other control and space characters are no line breaks. */
  @Test
  public void tabIsOneColumn() throws Exception {
    scan("\ta\t\tbc\u001C\u00A0\u2027\u202Ad");
    assertToken("\t", 0, 0, 0);
    assertToken("a", 0, 1, 1);
    assertToken("\t", 0, 2, 2);
    assertToken("\t", 0, 3, 3);
    assertToken("bc", 0, 4, 4);
    assertToken("\u001C", 0, 6, 6);
    assertToken("\u00A0", 0, 7, 7);
    assertToken("\u2027", 0, 8, 8);
    assertToken("\u202A", 0, 9, 9);
    assertToken("d", 0, 10, 10);
    assertEof(0, 11, 11);
  }

  /**
   * A character outside the BMP is one match of length 2, and counts as two for {@code yycolumn}
   * and {@code yychar}.
   */
  @Test
  public void supplementaryCharacter() throws Exception {
    // U+1F600 and U+10000
    scan("a\uD83D\uDE00b\n\uD800\uDC00\uD83D\uDE00c");
    assertToken("a", 0, 0, 0);
    assertToken("\uD83D\uDE00", 0, 1, 1);
    assertToken("b", 0, 3, 3);
    assertToken("\n", 0, 4, 4);
    assertToken("\uD800\uDC00", 1, 0, 5);
    assertToken("\uD83D\uDE00", 1, 2, 7);
    assertToken("c", 1, 4, 9);
    assertEof(1, 5, 10);
  }

  /** Counters when the scanner reads the input in more than one part. */
  @Test
  public void bufferRefill() throws Exception {
    int words = (BUFFER_SIZE - 4) / 3;
    for (int padding = 0; padding <= 6; padding++) {
      String firstLine = repeat("\t", padding) + repeat("abc", words);
      scan(firstLine + "\r\nxyz\nq");
      int column = 0;
      for (; column < padding; column++) {
        assertToken("\t", 0, column, column);
      }
      for (; column < firstLine.length(); column += 3) {
        assertToken("abc", 0, column, column);
      }
      assertEquals(BUFFER_SIZE - 4 + padding, column);
      assertToken("\r", 0, column, column);
      assertLfAfterCr(0, column + 1);
      assertToken("xyz", 1, 0, column + 2);
      assertToken("\n", 1, 3, column + 5);
      assertToken("q", 2, 0, column + 6);
      assertEof(2, 1, column + 7);
    }
  }

  /** The same with {@code \n} as the line break, and with lines that are shorter than a buffer. */
  @Test
  public void bufferRefillManyLines() throws Exception {
    // 5 characters per line, so that the line break is at every position relative to a match of
    // three letters, and the end of the buffer at every position in a line over the refills
    int lines = 3 * BUFFER_SIZE / 5 + 7;
    scan(repeat("abcd\n", lines));
    for (int line = 0; line < lines; line++) {
      assertToken("abc", line, 0, 5L * line);
      assertToken("d", line, 3, 5L * line + 3);
      assertToken("\n", line, 4, 5L * line + 4);
    }
    assertEof(lines, 0, 5L * lines);
  }

  /** Characters that are pushed back are counted once, with the match that they end up in. */
  @Test
  public void pushback() throws Exception {
    scan("abcde\nf");
    scanner.pushback = 2;
    assertToken("a", 0, 0, 0);
    assertToken("bcd", 0, 1, 1);
    scanner.pushback = 1;
    assertToken("", 0, 4, 4);
    assertToken("e", 0, 4, 4);
    assertToken("\n", 0, 5, 5);
    assertToken("f", 1, 0, 6);
    assertEof(1, 1, 7);
  }

  /** A line break that is pushed back is counted once. */
  @Test
  public void pushbackOfLineBreak() throws Exception {
    scan("a\nb\rc");
    assertToken("a", 0, 0, 0);
    scanner.pushback = 1;
    assertToken("", 0, 1, 1);
    assertToken("\n", 0, 1, 1);
    assertToken("b", 1, 0, 2);
    scanner.pushback = 1;
    assertToken("", 1, 1, 3);
    assertToken("\r", 1, 1, 3);
    assertToken("c", 2, 0, 4);
    assertEof(2, 1, 5);
  }

  /** {@code yyreset} sets the three counters back to 0. */
  @Test
  public void reset() throws Exception {
    scan("ab\ncd\nef");
    assertToken("ab", 0, 0, 0);
    assertToken("\n", 0, 2, 2);
    assertToken("cd", 1, 0, 3);

    // before the end of the first input
    scanner.yyreset(new StringReader("x\nyz"));
    assertToken("x", 0, 0, 0);
    assertToken("\n", 0, 1, 1);
    assertToken("yz", 1, 0, 2);
    assertEof(1, 2, 4);

    // at the end of the second input
    scanner.yyreset(new StringReader("\nq"));
    assertToken("\n", 0, 0, 0);
    assertToken("q", 1, 0, 1);
    assertEof(1, 1, 2);
  }
}
