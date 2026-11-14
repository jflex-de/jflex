/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testcase.spoon_feed_reader.SpoonFeedMaxCharsReaderWrapper;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test that {@code zzFin} is properly updated when reallocating {@code zzBuffer}. Bugs <a
 * href="http://sourceforge.net/p/jflex/bugs/118">#118</a> and <a
 * href="http://sourceforge.net/p/jflex/bugs/110">#110</a>.
 *
 * <p>The spec has a buffer of 10 characters ({@code %buffer 10}) and the general lookahead rule
 * {@code "a"+/"b"+}. The scanner has to refill and to grow the buffer inside the base and inside
 * the lookahead, and the text of the match must never contain a {@code b}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/genlook2/genlook2.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/genlook2/genlook2.sysout",
    quiet = true,
    minimizedDfaStatesCount = 9,
    minimizedDfa = {
      "states 9, inputs 3, lexical states 1",
      "entry: 0 0 1 2",
      "0: 0 -> 3; 1 -> 4; 2 -> 3",
      "1: 1 -> 5",
      "2: 2 -> 6",
      "3 [a0]:",
      "4 [a0]: 1 -> 7; 2 -> 8",
      "5 [a1]: 1 -> 5",
      "6 [a2]: 2 -> 6",
      "7: 1 -> 7; 2 -> 8",
      "8 [a3]: 2 -> 8",
      "a0: /* no lookahead */ return 0;",
      "a1 FORWARD_ACTION:",
      "a2 BACKWARD_ACTION:",
      "a3 GENERAL_LOOK entry 2: /* match should never contain a 'b', which is lookahead only */"
          + " return 1;"
    },
    charClassesCount = 3,
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class Genlook2Test implements JFlexTest {

  /** {@code "a"+/"b"+} */
  private static final int LOOK = 1;

  /** {@code [^]} */
  private static final int OTHER = 0;

  /** Largest length of a run of {@code a} or {@code b}, 2.5 times the size of the buffer. */
  private static final int MAX_RUN = 25;

  /** Lengths of runs below, at and above the size of the buffer. */
  private static final int[] RUNS = {1, 2, 9, 10, 11};

  private Genlook2 scanner;

  /** What is scanned, for the failure message. */
  private String scanned;

  /**
   * Starts a scanner on the input.
   *
   * @param spoonFeed if true, the scanner gets one character per call of {@code read}, so that it
   *     has to refill its buffer for every character. Otherwise it gets as much as fits into the
   *     buffer.
   */
  private void scan(String input, boolean spoonFeed) {
    Reader reader = new StringReader(input);
    if (spoonFeed) {
      reader = new SpoonFeedMaxCharsReaderWrapper(1, reader);
    }
    scanner = new Genlook2(reader);
    scanned = (spoonFeed ? " in spoon-fed " : " in ") + input;
  }

  private void assertToken(int rule, String text, int offset) throws IOException {
    String at = "at offset " + offset + scanned;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(offset, scanner.offset(), "offset " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Genlook2.YYEOF, scanner.yylex(), "EOF" + scanned);
    assertTrue(scanner.yyatEOF());
  }

  private static String repeat(char c, int count) {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < count; i++) {
      s.append(c);
    }
    return s.toString();
  }

  /**
   * Constructs an input from run lengths, scans it, and checks every token up to the end of input.
   *
   * <p>The input is {@code prefix} times {@code x}, then alternating runs of {@code a} and {@code
   * b}. For example, {@code assertRuns(false, 2, 3, 2, 1)} scans {@code xxaaabba}.
   *
   * <p>The expected tokens are:
   *
   * <ul>
   *   <li>each {@code x} of the prefix: one {@link #OTHER} token;
   *   <li>each run of {@code a} followed by a run of {@code b}: one {@link #LOOK} token with the
   *       whole run as the match;
   *   <li>each {@code b}: one {@link #OTHER} token;
   *   <li>each {@code a} of a last run of {@code a} at the end of input: one {@link #OTHER} token,
   *       because there is no {@code b} for the lookahead.
   * </ul>
   *
   * @param spoonFeed whether the scanner gets only one character per read, see {@link #scan}
   * @param prefix the number of {@code x} before the first run
   * @param runs the lengths of the runs. The first is a run of {@code a}, the second a run of
   *     {@code b}, and so on in turn.
   */
  private void assertRuns(boolean spoonFeed, int prefix, int... runs) throws IOException {
    StringBuilder input = new StringBuilder(repeat('x', prefix));
    for (int i = 0; i < runs.length; i++) {
      input.append(repeat(i % 2 == 0 ? 'a' : 'b', runs[i]));
    }
    scan(input.toString(), spoonFeed);
    int offset = 0;
    for (int i = 0; i < prefix; i++) {
      assertToken(OTHER, "x", offset++);
    }
    for (int i = 0; i < runs.length; i++) {
      boolean isA = i % 2 == 0;
      if (isA && i + 1 < runs.length) {
        assertToken(LOOK, repeat('a', runs[i]), offset);
        offset += runs[i];
      } else {
        for (int j = 0; j < runs[i]; j++) {
          assertToken(OTHER, isA ? "a" : "b", offset++);
        }
      }
    }
    assertEof();
  }

  /** The input of the former golden file test: the first lookahead begins at the buffer's end. */
  @Test
  public void lookaheadAtEndOfBuffer() throws Exception {
    scan("aaaaaaaaabbaabbbbbbb", false);
    assertToken(LOOK, "aaaaaaaaa", 0);
    assertToken(OTHER, "b", 9);
    assertToken(OTHER, "b", 10);
    assertToken(LOOK, "aa", 11);
    assertToken(OTHER, "b", 13);
    assertToken(OTHER, "b", 14);
    assertToken(OTHER, "b", 15);
    assertToken(OTHER, "b", 16);
    assertToken(OTHER, "b", 17);
    assertToken(OTHER, "b", 18);
    assertToken(OTHER, "b", 19);
    assertEof();
  }

  /** The same through {@link #assertRuns}, for checking the assertRuns method. */
  @Test
  public void lookaheadAtEndOfBufferAsRuns() throws Exception {
    assertRuns(false, 0, 9, 2, 2, 7);
    assertRuns(true, 0, 9, 2, 2, 7);
  }

  /** Test for when base, lookahead or both are longer than the buffer. */
  @Test
  public void baseAndLookaheadOfAllLengths() throws Exception {
    for (int a = 1; a <= MAX_RUN; a++) {
      for (int b = 1; b <= MAX_RUN; b++) {
        assertRuns(false, 0, a, b);
      }
    }
  }

  /** As before, with a refill of the buffer before every character. */
  @Test
  public void baseAndLookaheadOfAllLengthsSpoonFed() throws Exception {
    for (int a = 1; a <= MAX_RUN; a++) {
      for (int b = 1; b <= MAX_RUN; b++) {
        assertRuns(true, 0, a, b);
      }
    }
  }

  /** Without a {@code b} before the end of input there is no match of the lookahead rule. */
  @Test
  public void noLookaheadAtEof() throws Exception {
    for (int a = 1; a <= MAX_RUN; a++) {
      assertRuns(false, 0, a);
      assertRuns(true, 0, a);
    }
  }

  /** The last run of {@code a} has no lookahead, the ones before have. */
  @Test
  public void noLookaheadForLastRun() throws Exception {
    for (int a : RUNS) {
      for (int b : RUNS) {
        for (int a2 = 1; a2 <= MAX_RUN; a2++) {
          assertRuns(false, 0, a, b, a2);
          assertRuns(true, 0, a, b, a2);
        }
      }
    }
  }

  /**
   * Two matches of the lookahead rule after a prefix of 0 to 10 other characters, so that the
   * matches begin and end at every position relative to the buffer.
   */
  @Test
  public void shiftedInBuffer() throws Exception {
    for (int prefix = 0; prefix <= 10; prefix++) {
      for (int a : RUNS) {
        for (int b : RUNS) {
          for (int a2 : RUNS) {
            for (int b2 : RUNS) {
              assertRuns(false, prefix, a, b, a2, b2);
              assertRuns(true, prefix, a, b, a2, b2);
            }
          }
        }
      }
    }
  }

  /** A character that is neither {@code a} nor {@code b} ends base and lookahead. */
  @Test
  public void otherCharacters() throws Exception {
    scan("axbaabxab\nb", false);
    assertToken(OTHER, "a", 0);
    assertToken(OTHER, "x", 1);
    assertToken(OTHER, "b", 2);
    assertToken(LOOK, "aa", 3);
    assertToken(OTHER, "b", 5);
    assertToken(OTHER, "x", 6);
    assertToken(LOOK, "a", 7);
    assertToken(OTHER, "b", 8);
    assertToken(OTHER, "\n", 9);
    assertToken(OTHER, "b", 10);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("", false);
    assertEof();
  }
}
