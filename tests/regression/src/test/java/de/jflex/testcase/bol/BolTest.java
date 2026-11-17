/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.bol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests BOL and EOL operators. */
public class BolTest {
  // standard BOL and EOL tests
  BolScanner scanner;
  // test BOL in absence of char class for NL characters
  Bol2Scanner scanner2;

  @BeforeEach
  public void testMustInitializeScanner() {
    scanner = null;
    scanner2 = null;
  }

  @AfterEach
  public void end() throws Exception {
    if (scanner != null) {
      assertEquals(State.END_OF_FILE, scanner.yylex());
    }
    if (scanner2 != null) {
      assertEquals(State.END_OF_FILE, scanner2.yylex());
    }
  }

  @Test
  public void initial() throws Exception {
    scanner = createScanner("");
    assertEquals(State.INITIAL, scanner.state);
  }

  @Test
  public void bolAndEol() throws Exception {
    scanner = createScanner("hello\n");
    assertEquals(State.HELLO_AT_BOL_AND_EOL, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void bolAndEol2() throws Exception {
    scanner = createScanner("hello\r\n");
    assertEquals(State.HELLO_AT_BOL_AND_EOL, scanner.yylex());
    assertEquals(State.OTHER, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void bolAndEol3() throws Exception {
    scanner = createScanner("\nhello\r\n");
    assertEquals(State.LINE_FEED, scanner.yylex());
    assertEquals(State.HELLO_AT_BOL_AND_EOL, scanner.yylex());
    assertEquals(State.OTHER, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void bolAndEof() throws Exception {
    // EOF does not count as EOL. Should it?
    scanner = createScanner("hello");
    assertEquals(State.HELLO_AT_BOL, scanner.yylex());
  }

  @Test
  public void eol() throws Exception {
    scanner = createScanner("  hello\n");
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.HELLO_AT_EOL, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void hello() throws Exception {
    scanner = createScanner("  hello  \n");
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.HELLO_SIMPLY, scanner.yylex());
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void bol() throws Exception {
    scanner = createScanner("hello \n");
    assertEquals(State.HELLO_AT_BOL, scanner.yylex());
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void space() throws Exception {
    scanner = createScanner(" \n");
    assertEquals(State.SPACE, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void other() throws Exception {
    scanner = createScanner("abc\n");
    assertEquals(State.OTHER, scanner.yylex());
    assertEquals(State.OTHER, scanner.yylex());
    assertEquals(State.OTHER, scanner.yylex());
    assertEquals(State.LINE_FEED, scanner.yylex());
  }

  @Test
  public void bolNoNLCCL() throws Exception {
    scanner2 = createScanner2("hello");
    assertEquals(State.HELLO_AT_BOL, scanner2.yylex());
  }

  @Test
  public void bolNoNLCCL2() throws Exception {
    scanner2 = createScanner2("hello\n");
    assertEquals(State.HELLO_AT_BOL, scanner2.yylex());
    assertEquals(State.OTHER, scanner2.yylex());
  }

  @Test
  public void noBolNoNLCCL() throws Exception {
    scanner2 = createScanner2("\n  hello  ");
    assertEquals(State.OTHER, scanner2.yylex());
    assertEquals(State.OTHER, scanner2.yylex());
    assertEquals(State.OTHER, scanner2.yylex());
    assertEquals(State.HELLO_SIMPLY, scanner2.yylex());
    assertEquals(State.OTHER, scanner2.yylex());
    assertEquals(State.OTHER, scanner2.yylex());
  }

  private static BolScanner createScanner(final String content) throws IOException {
    return new BolScanner(new StringReader(content));
  }

  private static Bol2Scanner createScanner2(final String content) throws IOException {
    return new Bol2Scanner(new StringReader(content));
  }
}
