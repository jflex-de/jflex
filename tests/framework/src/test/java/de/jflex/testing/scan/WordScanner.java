/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.scan;

import java.io.IOException;
import java.io.Reader;

/**
 * A hand-written scanner with the methods of a generated scanner, for the tests of {@link
 * ScannerTest}. A token is a run of ASCII letters ({@link #WORD}) or any other single char ({@link
 * #OTHER}). Has its own position accessors, and no {@link TestScanner} interface.
 */
class WordScanner {
  static final int YYEOF = -1;
  static final int OTHER = 0;
  static final int WORD = 1;

  private final Reader in;
  private int next;
  private boolean started;
  private final StringBuilder text = new StringBuilder();
  private boolean atEOF;

  private int line;
  private int column;
  private long offset;
  // the position after the current match
  private int nextLine;
  private int nextColumn;
  private long nextOffset;

  WordScanner(Reader in) {
    this.in = in;
  }

  public int yylex() throws IOException {
    if (!started) {
      next = in.read();
      started = true;
    }
    line = nextLine;
    column = nextColumn;
    offset = nextOffset;
    text.setLength(0);
    if (next < 0) {
      atEOF = true;
      return YYEOF;
    }
    if (isLetter(next)) {
      while (isLetter(next)) {
        consume();
      }
      return WORD;
    }
    consume();
    return OTHER;
  }

  private static boolean isLetter(int c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
  }

  private void consume() throws IOException {
    text.append((char) next);
    nextOffset++;
    if (next == '\n') {
      nextLine++;
      nextColumn = 0;
    } else {
      nextColumn++;
    }
    next = in.read();
  }

  public String yytext() {
    return text.toString();
  }

  public int yylength() {
    return text.length();
  }

  public boolean yyatEOF() {
    return atEOF;
  }

  public int line() {
    return line;
  }

  public int column() {
    return column;
  }

  public long offset() {
    return offset;
  }

  /** As a spec with {@code %implements}, but without counters. */
  static class WithInterface extends WordScanner implements TestScanner {
    WithInterface(Reader in) {
      super(in);
    }
  }

  /** As a spec with {@code %implements}, all counters and {@code %counter_methods}. */
  static class WithCounters extends WithInterface {
    WithCounters(Reader in) {
      super(in);
    }

    @Override
    public int yyline() {
      return line();
    }

    @Override
    public int yycolumn() {
      return column();
    }

    @Override
    public long yychar() {
      return offset();
    }
  }

  /** A scanner with a wrong {@code yylength()}. */
  static class WrongLength extends WithCounters {
    WrongLength(Reader in) {
      super(in);
    }

    @Override
    public int yylength() {
      return super.yylength() + 1;
    }
  }
}
