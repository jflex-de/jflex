/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.yylexthrow_comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Tests that a spec with comments after its options compiles, in particular a comment on the line
 * of a {@code %yylexthrow}.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/790">#790</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/yylexthrow_comment/yylexthrow.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]: 1 -> 2",
      "a0: return 1;",
      "a1: return 0;"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-8][11-12][14-31]['!'-1114111] }", "{ [9-10][13][' '] }"})
public class YylexthrowTest implements JFlexTest {

  /** <code>{WhiteSpace}+</code> */
  private static final int WHITE_SPACE = 0;

  /** {@code [^]} */
  private static final int OTHER = 1;

  /** Both {@code %yylexthrow} exceptions are declared, and nothing of the comments. */
  @Test
  public void yylexDeclaresExceptions() throws Exception {
    assertEquals(
        Arrays.asList(IOException.class, RuntimeException.class, SecurityException.class),
        Arrays.asList(Yylexthrow.class.getMethod("yylex").getExceptionTypes()));
  }

  @Test
  public void scans() throws Exception {
    Yylexthrow scanner = new Yylexthrow(new StringReader("/ \t\r\n*"));
    assertEquals(OTHER, scanner.yylex());
    assertEquals("/", scanner.yytext());
    assertEquals(WHITE_SPACE, scanner.yylex());
    assertEquals(" \t\r\n", scanner.yytext());
    assertEquals(OTHER, scanner.yylex());
    assertEquals("*", scanner.yytext());
    assertEquals(-1, scanner.yylex());
  }
}
