/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for refilling the scanner buffer ({@code zzRefill()}) when the end of the buffer meets a
 * surrogate character or a {@code \r}.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/1090">#1090</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/refill/refill.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 4, lexical states 1",
      "entry: 0 0",
      "0: 0-2 -> 1; 3 -> 2",
      "1 [a0]:",
      "2 [a0]: 1 -> 1; 3 -> 2",
      "a0: return yytext();"
    },
    charClassesCount = 4,
    charClasses = {"{ [0-12][14-'`']['c'-1114111] }", "{ [13] }", "{ ['a'] }", "{ ['b'] }"})
public class RefillTest implements JFlexTest {

  /** A high surrogate. Unpaired if not followed by {@link #LOW}. */
  static final String HIGH = "\uD83D";

  /** A low surrogate. Unpaired if not preceded by {@link #HIGH}. */
  private static final String LOW = "\uDE00";

  /** U+1F600, a character that takes two Java {@code char}s. */
  static final String PAIR = HIGH + LOW;

  /** The {@code yylex()} method of a scanner in this test. */
  @FunctionalInterface
  interface Lexer {
    String yylex() throws IOException;
  }

  /** Scans to the end of the input and returns the text of all tokens. */
  static List<String> tokens(Lexer lexer) throws IOException {
    List<String> tokens = new ArrayList<>();
    for (String token = lexer.yylex(); token != null; token = lexer.yylex()) {
      tokens.add(token);
    }
    return tokens;
  }

  private static List<String> tokens(String input) throws IOException {
    return tokens(new Refill(new StringReader(input))::yylex);
  }

  /**
   * Engineer a refill buffer that ends with a high surrogate (which is not a complete character)
   * and check that the scanner handles that situation correctly. After the match {@code a}, the
   * match {@code bbb} starts at buffer position 1 and ends at the end of the buffer. This causes a
   * refill, which moves it to the start of the buffer, which leaves room for one more {@code char},
   * which is the high surrogate.
   */
  @Test
  public void surrogatePairAfterMovedMatch() throws Exception {
    assertEquals(Arrays.asList("a", "bbb", PAIR, "a"), tokens("abbb" + PAIR + "a"));
  }

  /**
   * Like {@link #surrogatePairAfterMovedMatch()}, but with a high surrogate that remains unpaired.
   */
  @Test
  public void highSurrogatesAfterMovedMatch() throws Exception {
    assertEquals(Arrays.asList("a", "bbb", HIGH, PAIR), tokens("abbb" + HIGH + PAIR));
    assertEquals(Arrays.asList("a", "bbb", HIGH, HIGH), tokens("abbb" + HIGH + HIGH));
    assertEquals(Arrays.asList("a", "bbb", HIGH), tokens("abbb" + HIGH));
  }

  /** The buffer is full, and the last {@code char} is the high surrogate. */
  @Test
  public void surrogatePairAcrossEndOfBuffer() throws Exception {
    assertEquals(Arrays.asList("bbb", PAIR, "a"), tokens("bbb" + PAIR + "a"));
    assertEquals(Arrays.asList("a", "bb", PAIR, "a"), tokens("abb" + PAIR + "a"));
  }

  /** The input ends in a high surrogate, and is shorter than the buffer. */
  @Test
  public void unpairedHighSurrogateAtEndOfInput() throws Exception {
    assertEquals(Arrays.asList(HIGH), tokens(HIGH));
    assertEquals(Arrays.asList("a", "b", HIGH), tokens("ab" + HIGH));
    assertEquals(Arrays.asList(PAIR, HIGH), tokens(PAIR + HIGH));
  }

  /** The input ends in a high surrogate and is the last {@code char} of a full buffer. */
  @Test
  public void unpairedHighSurrogateAtEndOfInputAndBuffer() throws Exception {
    assertEquals(Arrays.asList("bbb", HIGH), tokens("bbb" + HIGH));
    assertEquals(Arrays.asList("a", "bb", HIGH), tokens("abb" + HIGH));
  }

  /**
   * The match {@code bbb\r} fills the buffer. To count lines, the scanner has to refill the buffer
   * to see if a {@code \n} follows the {@code \r}.
   */
  @Test
  public void lineCountAfterCarriageReturnAtEndOfBuffer() throws Exception {
    Refill scanner = new Refill(new StringReader("bbb\r\nbbb\rb"));
    assertEquals("bbb\r", scanner.yylex());
    assertEquals(0, scanner.yyline());
    assertEquals("\n", scanner.yylex());
    assertEquals(0, scanner.yyline(), "\\r\\n is one line end");
    assertEquals("bbb\r", scanner.yylex());
    assertEquals(1, scanner.yyline());
    assertEquals("b", scanner.yylex());
    assertEquals(2, scanner.yyline());
    assertNull(scanner.yylex());
  }

  // Exhaustive tests: all short inputs over an alphabet that covers the characters that are special
  // to zzRefill, with readers that return the input in pieces of different sizes.

  private static final char[] ALPHABET = {
    'a', 'b', '\r', '\n', HIGH.charAt(0), LOW.charAt(0),
  };

  /** The exhaustive tests scan all inputs up to this length. */
  private static final int MAX_LENGTH = 6;

  /** Maximal number of {@code char}s that the readers in the exhaustive tests return per read. */
  private static final int[] CHUNK_SIZES = {1, 3, Integer.MAX_VALUE};

  /** A reader for {@code input} that returns at most {@code chunkSize} chars per read. */
  static Reader chunked(String input, int chunkSize) {
    return new StringReader(input) {
      @Override
      public int read(char[] buffer, int offset, int length) throws IOException {
        return super.read(buffer, offset, Math.min(length, chunkSize));
      }
    };
  }

  /** The matches of {@code refill.flex} and {@code refill-limit.flex} on {@code input}. */
  static List<String> expectedTokens(String input) {
    List<String> tokens = new ArrayList<>();
    int start = 0;
    while (start < input.length()) {
      int end = start;
      while (end < input.length() && input.charAt(end) == 'b') {
        end++;
      }
      if (end < input.length() && input.charAt(end) == '\r') {
        end++;
      } else if (end == start) {
        end += Character.charCount(input.codePointAt(start));
      }
      tokens.add(input.substring(start, end));
      start = end;
    }
    return tokens;
  }

  /**
   * The value of {@code yyline} for a match that begins at {@code start}: the number of line ends
   * before {@code start}, where a {@code \r\n} is complete after the {@code \n}.
   */
  private static int expectedLine(String input, int start) {
    int line = 0;
    for (int i = 0; i < start; i++) {
      char c = input.charAt(i);
      boolean beforeNewline = i + 1 < input.length() && input.charAt(i + 1) == '\n';
      if (c == '\n' || (c == '\r' && !beforeNewline)) {
        line++;
      }
    }
    return line;
  }

  static String describe(String input) {
    StringBuilder result = new StringBuilder("input \"");
    for (char c : input.toCharArray()) {
      if (c == 'a' || c == 'b') {
        result.append(c);
      } else {
        result.append(String.format("\\u%04X", (int) c));
      }
    }
    return result.append('"').toString();
  }

  /** Calls {@code check} with all inputs over {@link #ALPHABET} up to {@link #MAX_LENGTH}. */
  static void forAllInputs(InputCheck check) throws IOException {
    forAllInputs(new StringBuilder(), check);
  }

  private static void forAllInputs(StringBuilder prefix, InputCheck check) throws IOException {
    String input = prefix.toString();
    for (int chunkSize : CHUNK_SIZES) {
      check.check(input, chunkSize, describe(input) + ", reads of " + chunkSize);
    }
    if (prefix.length() < MAX_LENGTH) {
      for (char c : ALPHABET) {
        prefix.append(c);
        forAllInputs(prefix, check);
        prefix.setLength(prefix.length() - 1);
      }
    }
  }

  @FunctionalInterface
  interface InputCheck {
    void check(String input, int chunkSize, String description) throws IOException;
  }

  @Test
  public void allShortInputs() throws Exception {
    forAllInputs(
        (input, chunkSize, description) -> {
          Refill scanner = new Refill(chunked(input, chunkSize));
          int start = 0;
          for (String expected : expectedTokens(input)) {
            assertEquals(expected, scanner.yylex(), description);
            assertEquals(expectedLine(input, start), scanner.yyline(), "line, " + description);
            start += expected.length();
          }
          assertNull(scanner.yylex(), description);
        });
  }
}
