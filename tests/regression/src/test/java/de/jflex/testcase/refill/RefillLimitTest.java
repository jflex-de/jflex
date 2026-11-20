/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

import static de.jflex.testcase.refill.RefillTest.HIGH;
import static de.jflex.testcase.refill.RefillTest.PAIR;
import static de.jflex.testcase.refill.RefillTest.chunked;
import static de.jflex.testcase.refill.RefillTest.describe;
import static de.jflex.testcase.refill.RefillTest.expectedTokens;
import static de.jflex.testcase.refill.RefillTest.forAllInputs;
import static de.jflex.testcase.refill.RefillTest.tokens;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.EOFException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for refilling the scanner buffer with {@code %token_size_limit}, where the buffer cannot
 * grow. See {@link RefillTest}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/refill/refill-limit.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    // the same rules as refill.flex
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
public class RefillLimitTest implements JFlexTest {

  /** The {@code %token_size_limit} of {@code refill-limit.flex}. */
  private static final int LIMIT = 4;

  private static List<String> limitTokens(String input) throws IOException {
    return tokens(new RefillLimit(new StringReader(input))::yylex);
  }

  /**
   * With {@code %token_size_limit}, a match is guaranteed if it is at least two {@code char}s
   * smaller than the limit.
   */
  @Test
  public void limitWithSurrogatePairAfterMatch() throws Exception {
    assertEquals(Arrays.asList("bb", PAIR, "a"), limitTokens("bb" + PAIR + "a"));
    assertEquals(Arrays.asList("a", "bb", PAIR, "a"), limitTokens("abb" + PAIR + "a"));
    assertEquals(Arrays.asList(PAIR, PAIR, HIGH, PAIR), limitTokens(PAIR + PAIR + HIGH + PAIR));
  }

  /**
   * The match {@code bbb} and the character after it take 5 {@code char}s, which is more than the
   * limit. The scanner must not drop the high surrogate to make room for the low surrogate.
   */
  @Test
  public void limitReachedBySurrogatePairAfterMatch() throws Exception {
    assertLimitReached("bbb" + PAIR + "a");
    assertLimitReached("bbb" + HIGH + HIGH);
    assertEquals(Arrays.asList("bbb", "a", PAIR), limitTokens("bbba" + PAIR));
  }

  private static void assertLimitReached(String input) {
    RefillLimit scanner = new RefillLimit(new StringReader(input));
    EOFException e = assertThrows(EOFException.class, scanner::yylex, describe(input));
    assertEquals("Scan buffer limit reached [" + LIMIT + "]", e.getMessage());
  }

  @Test
  public void allShortInputsWithLimit() throws Exception {
    forAllInputs(
        (input, chunkSize, description) -> {
          List<String> expected = expectedTokens(input);
          List<String> actual = new ArrayList<>();
          RefillLimit scanner = new RefillLimit(chunked(input, chunkSize));
          try {
            for (String token = scanner.yylex(); token != null; token = scanner.yylex()) {
              actual.add(token);
            }
          } catch (EOFException e) {
            // the limit was reached in the match after the ones in actual
            assertTrue(actual.size() < expected.size(), description);
            assertEquals(expected.subList(0, actual.size()), actual, description);
            String match = expected.get(actual.size());
            assertTrue(
                match.length() > LIMIT - 2, "limit reached for " + match + ", " + description);
            return;
          }
          assertEquals(expected, actual, description);
        });
  }
}
