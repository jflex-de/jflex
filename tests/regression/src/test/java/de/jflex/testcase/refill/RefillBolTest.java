/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

import static de.jflex.testcase.refill.RefillTest.tokens;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for refilling the scanner buffer when the scanner has to decide if it is at the beginning
 * of a line ({@code ^}) after a {@code \r} at the end of the buffer. See {@link RefillTest}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/refill/refill-bol.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 6,
    minimizedDfa = {
      "states 6, inputs 4, lexical states 1",
      "entry: 0 1",
      "0: 0-1 -> 2; 2 -> 3; 3 -> 2",
      "1: 0-1 -> 2; 2 -> 3; 3 -> 4",
      "2 [a0]:",
      "3 [a0]: 1 -> 2; 2 -> 5",
      "4 [a1]:",
      "5: 1 -> 2; 2 -> 5",
      "a0: return yytext();",
      "a1: return \"^x\";"
    },
    charClassesCount = 4,
    charClasses = {
      "{ [0-12][14-'a']['c'-'w']['y'-1114111] }",
      "{ [13] }",
      "{ ['b'] }",
      "{ ['x'] }"
    })
public class RefillBolTest implements JFlexTest {

  private static List<String> bolTokens(String input) throws IOException {
    return tokens(new RefillBol(new StringReader(input))::yylex);
  }

  /**
   * The match {@code bbb\r} fills the buffer. For {@code ^}, the scanner has to refill the buffer
   * to see if a {@code \n} follows the {@code \r}.
   */
  @Test
  public void beginningOfLineAfterCarriageReturnAtEndOfBuffer() throws Exception {
    assertEquals(Arrays.asList("bbb\r", "^x", "x"), bolTokens("bbb\rxx"));
    assertEquals(Arrays.asList("bbb\r", "\n", "^x", "x"), bolTokens("bbb\r\nxx"));
    assertEquals(Arrays.asList("bbb\r"), bolTokens("bbb\r"));
  }
}
