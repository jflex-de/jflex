/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.bol;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests BOL and EOL operators. {@link Bol2Test} has the tests for a spec without a character class
 * for line terminators.
 */
public class BolTest {
  BolScanner scanner;

  @BeforeEach
  public void testMustInitializeScanner() {
    scanner = null;
  }

  @AfterEach
  public void end() throws Exception {
    if (scanner != null) {
      assertEquals(State.END_OF_FILE, scanner.yylex());
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

  private static BolScanner createScanner(final String content) {
    return new BolScanner(new StringReader(content));
  }
}
