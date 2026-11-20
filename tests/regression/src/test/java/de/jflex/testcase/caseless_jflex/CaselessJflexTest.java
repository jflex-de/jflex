/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test {@code %ignorecase} with JFlex semantics.
 *
 * <p>Only strings and chars are caseless.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/caseless_jflex/caseless.flex",
    // no warnings: "hello" can match, other than with --jlex
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 16,
    charClassesCount = 15,
    charClasses = {
      "{ [0-9][14-'@']['B'-'D']['F'-'G']['I'-'K']['M'-'N']['P'-'`']['{'-132][134-8231]"
          + "[8234-55295][57344-1114111] }",
      "{ [10] }",
      "{ [11-12][133][8232-8233][55296-57343] }",
      "{ [13] }",
      "{ ['A'] }",
      "{ ['E'] }",
      "{ ['H'] }",
      "{ ['L'] }",
      "{ ['O'] }",
      "{ ['a'] }",
      "{ ['b'-'d']['f'-'g']['i'-'k']['m'-'n']['p'-'z'] }",
      "{ ['e'] }",
      "{ ['h'] }",
      "{ ['l'] }",
      "{ ['o'] }"
    })
public class CaselessJflexTest implements JFlexTest {

  private CaselessScanner scanner;

  private void scan(String input) {
    scanner = new CaselessScanner(new StringReader(input));
  }

  /** Checks the next token: the matched rule and text. */
  private void assertToken(State rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(State.END_OF_FILE, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void a() throws Exception {
    scan("a");
    assertToken(State.A, "a");
    assertEof();
  }

  /** 'A' matches 'a' in caseless mode. */
  @Test
  public void a_upper() throws Exception {
    scan("A");
    assertToken(State.A, "A");
    assertEof();
  }

  @Test
  public void hello() throws Exception {
    // [a-z]+ has higher priority than "hello" in the rules
    scan("hello");
    assertToken(State.WORD, "hello");
    assertEof();
  }

  /** 'HelLo' matches 'hello' in caseless mode. */
  @Test
  public void hello_mixedCase() throws Exception {
    // [a-z]+ has higher priority than the "hello" rule, but doesn't match "HelLo"
    scan("HelLo");
    assertToken(State.HELLO, "HelLo");
    assertEof();
  }

  @Test
  public void hello_within() throws Exception {
    scan("blubHELLObla");
    assertToken(State.WORD, "blub");
    assertToken(State.HELLO, "HELLO");
    assertToken(State.WORD, "bla");
    assertEof();
  }

  /** 'helLo' is mixed case and is no match of 'hello' here, because [a-z]+ matches 'hel'. */
  @Test
  public void hello_withinMixedCase() throws Exception {
    scan("sdfahelLobla");
    assertToken(State.WORD, "sdfahel");
    assertToken(State.OTHER, "L");
    assertToken(State.WORD, "obla");
    assertEof();
  }

  @Test
  public void hello_newline() throws Exception {
    scan("HELLO\n;");
    assertToken(State.HELLO, "HELLO");
    assertToken(State.NEW_LINE, "\n");
    assertToken(State.OTHER, ";");
    assertEof();
  }

  @Test
  public void other() throws Exception {
    scan("asd123hello;qwe");
    assertToken(State.WORD, "asd");
    assertToken(State.OTHER, "1");
    assertToken(State.OTHER, "2");
    assertToken(State.OTHER, "3");
    // 'hello' is lower case, so it is a word and not a HELLO token
    assertToken(State.WORD, "hello");
    assertToken(State.OTHER, ";");
    assertToken(State.WORD, "qwe");
    assertEof();
  }

  /** Character classes stay case-sensitive: {@code [a-z]+} does not match upper case. */
  @Test
  public void upperCaseIsNoWord() throws Exception {
    scan("Ab");
    assertToken(State.A, "A");
    assertToken(State.WORD, "b");
    assertEof();

    scan("BZ");
    assertToken(State.OTHER, "B");
    assertToken(State.OTHER, "Z");
    assertEof();
  }

  /** The longer match wins: 'h' is a word, but 'hELLO' matches the caseless string. */
  @Test
  public void hello_lowerCaseStart() throws Exception {
    scan("hELLO");
    assertToken(State.HELLO, "hELLO");
    assertEof();

    scan("hELL");
    assertToken(State.WORD, "h");
    assertToken(State.OTHER, "E");
    assertToken(State.OTHER, "L");
    assertToken(State.OTHER, "L");
    assertEof();
  }
}
