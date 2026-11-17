/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Test {@code %ignorecase} with JFlex semantics.
 *
 * <p>Only strings and chars are caseless.
 */
public class CaselessJflexTest {

  private CaselessScanner scanner;

  @AfterEach
  public void endOfFile() throws Exception {
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  @Test
  public void a() throws Exception {
    scanner = createScanner("a");
    assertEquals(State.A, scanner.yylex());
  }

  @Test
  public void a_upper() throws Exception {
    scanner = createScanner("A");
    assertEquals(State.A, scanner.yylex(), "'A' matches 'a' in caseless mode");
  }

  @Test
  public void hello() throws Exception {
    // [a-z]+ has higher priority than "hello" in the rules
    scanner = createScanner("hello");
    assertEquals(State.WORD, scanner.yylex());
  }

  @Test
  public void hello_mixedCase() throws Exception {
    // [a-z]+ has higher priority than the "hello" rule, but doesn't match "HelLo"
    scanner = createScanner("HelLo");
    assertEquals(State.HELLO, scanner.yylex(), "'HelLo' matches 'hello' in caseless mode");
  }

  @Test
  public void hello_within() throws Exception {
    scanner = createScanner("blubHELLObla");
    assertEquals(State.WORD, scanner.yylex());
    assertEquals(State.HELLO, scanner.yylex());
    assertEquals(State.WORD, scanner.yylex());
  }

  @Test
  public void hello_withinMixedCase() throws Exception {
    scanner = createScanner("sdfahelLobla");
    assertEquals(State.WORD, scanner.yylex()); // sdfa
    assertEquals(
        State.OTHER,
        scanner.yylex(),
        "'helLo' is mixed case and doesn't match 'hello' in JFlex mode"); // helLo
    assertEquals(State.WORD, scanner.yylex()); // bla
  }

  @Test
  public void hello_newline() throws Exception {
    scanner = createScanner("HELLO\n;");
    assertEquals(State.HELLO, scanner.yylex());
    assertEquals(State.NEW_LINE, scanner.yylex());
    assertEquals(State.OTHER, scanner.yylex());
  }

  @Test
  public void other() throws Exception {
    scanner = createScanner("asd123hello;qwe");
    assertEquals(State.WORD, scanner.yylex(), "'asd' is word");
    assertEquals(State.OTHER, scanner.yylex(), "'1' is other"); // 1
    assertEquals(State.OTHER, scanner.yylex(), "'2' is other"); // 2
    assertEquals(State.OTHER, scanner.yylex(), "'3' is other"); // 3
    assertEquals(
        State.WORD,
        scanner.yylex(),
        "'hello' is concatenated and doesn't create a HELLO qqtoken in that case");
    assertEquals(State.OTHER, scanner.yylex(), "';' is other");
    assertEquals(State.WORD, scanner.yylex()); // qwe
  }

  private static CaselessScanner createScanner(String input) throws IOException {
    return new CaselessScanner(new StringReader(input));
  }
}
