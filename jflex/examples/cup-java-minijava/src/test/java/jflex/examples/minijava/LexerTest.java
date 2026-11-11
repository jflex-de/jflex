/*
 * Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: GPL-2.0-only
 */

package jflex.examples.minijava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the generated {@link Lexer}.
 *
 * <p>The lexer is probably already correct thanks to the regression tests. This test class is
 * mostly here to show how the lexer behaves.
 */
public class LexerTest {

  private Lexer lexer;

  @AfterEach
  public void resetLexer() {
    lexer = null;
  }

  @Test
  public void scan_tokenIdentifier() throws Exception {
    scan("helloWorld");
    assertEquals(sym.IDENTIFIER, nextToken());
  }

  @Test
  public void scan_assignment() throws Exception {
    scan("boolean debug = 2 == 1 + 1");
    assertEquals(sym.BOOLEAN, nextToken());
    assertEquals(sym.IDENTIFIER, nextToken());
    assertEquals(sym.EQ, nextToken());
    assertEquals(sym.INTEGER_LITERAL, nextToken());
    assertEquals(sym.EQEQ, nextToken());
    assertEquals(sym.INTEGER_LITERAL, nextToken());
    assertEquals(sym.PLUS, nextToken());
    assertEquals(sym.INTEGER_LITERAL, nextToken());
    assertEquals(sym.EOF, nextToken());
  }

  @SuppressWarnings("TryFailThrowable")
  @Test
  public void scan_illegalChar() throws Exception {
    scan("boolean debug;");
    assertEquals(sym.BOOLEAN, nextToken());
    assertEquals(sym.IDENTIFIER, nextToken());
    try {
      nextToken();
      fail("Character `;` is not declared in the minijava.flex");
    } catch (UnknownCharacterException expected) {
    }
  }

  private void scan(String input) {
    Reader in = new StringReader(input);
    lexer = new Lexer(in);
  }

  private int nextToken() throws IOException, UnknownCharacterException {
    return lexer.next_token().sym;
  }
}
