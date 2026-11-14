/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.cupsym;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import org.junit.jupiter.api.Test;

/**
 * Test for bug <a href="https://github.com/jflex-de/jflex/issues/58">#58 %cupsym doesn't affect all
 * code</a>.
 *
 * <p>When {@code %cupdebug} was used, the code generated for {@code getTokenName} used {@code sym}
 * as the name of the CUP symbol class even if {@code %cupsym} was specified.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/cupsym/cupsym.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/cupsym/cupsym.sysout",
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]: 1 -> 2",
      "a0: /* do nothing */",
      "a1: return new java_cup.runtime.Symbol(sym2.WORD, yytext());"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-'`']['{'-1114111] }", "{ ['a'-'z'] }"})
public class CupsymTest implements JFlexTest {

  private Cupsym scanner;

  private void scan(String input) {
    scanner = new Cupsym(new StringReader(input));
  }

  /** Checks that the next token is a word with the given text. */
  private void assertWord(String text, Symbol token) {
    assertEquals(sym2.WORD, token.sym);
    assertEquals(text, token.value);
    assertEquals(text, scanner.yytext());
  }

  /** Checks that the scanner is at the end of input and stays there. */
  private void assertEof() throws Exception {
    for (int i = 0; i < 2; i++) {
      Symbol token = scanner.next_token();
      assertEquals(sym2.EOF, token.sym);
      assertNull(token.value);
      assertTrue(scanner.yyatEOF());
    }
  }

  /** The symbol at the end of input is {@code sym2.EOF}. */
  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }

  @Test
  public void eofAfterIgnoredInput() throws Exception {
    scan("SOME FILE\n1, 2, 3");
    assertEof();
  }

  @Test
  public void words() throws Exception {
    scan("some file\ncontent, ignoredAnyway");
    assertWord("some", scanner.next_token());
    assertWord("file", scanner.next_token());
    assertWord("content", scanner.next_token());
    assertWord("ignored", scanner.next_token());
    assertWord("nyway", scanner.next_token());
    assertEof();
  }

  /** {@code %cup} makes the class a scanner for a CUP parser. */
  @Test
  public void isCupScanner() throws Exception {
    java_cup.runtime.Scanner cupScanner = new Cupsym(new StringReader("a"));
    assertEquals(sym2.WORD, cupScanner.next_token().sym);
    assertEquals(sym2.EOF, cupScanner.next_token().sym);
  }

  /**
   * {@code %cupdebug}: the scanning method {@code debug_next_token} returns what {@code next_token}
   * does and prints the text and the name of the token, which is the name of the constant in {@link
   * sym2} (#58).
   */
  @Test
  public void debugNextToken() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      scan("some file\n");
      assertWord("some", scanner.debug_next_token());
      assertWord("file", scanner.debug_next_token());
      assertEquals(sym2.EOF, scanner.debug_next_token().sym);
    }
    String expected =
        String.join(System.lineSeparator(), " --some--WORD--", " --file--WORD--", " ----EOF--", "");
    assertEquals(expected, out.toString("UTF-8"));
  }

  @Test
  public void debugNextTokenAtEof() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      scan("SOME FILE\nCONTENT IGNORED ANYWAY\n");
      assertEquals(sym2.EOF, scanner.debug_next_token().sym);
    }
    assertEquals(" ----EOF--" + System.lineSeparator(), out.toString("UTF-8"));
  }
}
