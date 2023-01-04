/*
 * Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: GPL-2.0-only
 */

package jflex.examples.minijava;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.After;
import org.junit.Test;

/**
 * Test for the generated {@link Lexer}.
 *
 * <p>The lexer is probably already correct thanks to the regression tests. This test class is
 * mostly here to show how the lexer behaves.
 */
public class LexerTest {

  private Lexer lexer;

  @After
  public void resetLexer() {
    lexer = null;
  }

  @Test
  public void scan_tokenIdentifier() throws Exception {
    scan("helloWorld");
    assertThat(nextToken()).isEqualTo(sym.IDENTIFIER);
  }

  @Test
  public void scan_assignment() throws Exception {
    scan("boolean debug = 2 == 1 + 1");
    assertThat(nextToken()).isEqualTo(sym.BOOLEAN);
    assertThat(nextToken()).isEqualTo(sym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(sym.EQ);
    assertThat(nextToken()).isEqualTo(sym.INTEGER_LITERAL);
    assertThat(nextToken()).isEqualTo(sym.EQEQ);
    assertThat(nextToken()).isEqualTo(sym.INTEGER_LITERAL);
    assertThat(nextToken()).isEqualTo(sym.PLUS);
    assertThat(nextToken()).isEqualTo(sym.INTEGER_LITERAL);
    assertThat(nextToken()).isEqualTo(sym.EOF);
  }

  @SuppressWarnings("TryFailThrowable")
  @Test
  public void scan_illegalChar() throws Exception {
    scan("boolean debug;");
    assertThat(nextToken()).isEqualTo(sym.BOOLEAN);
    assertThat(nextToken()).isEqualTo(sym.IDENTIFIER);
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
