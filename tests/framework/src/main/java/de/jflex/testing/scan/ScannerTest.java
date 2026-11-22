/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.scan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base class for tests that check the tokens of a generated scanner.
 *
 * <pre>
 * public class LookTest extends ScannerTest&lt;Look, Integer&gt; implements JFlexTest {
 *   public LookTest() {
 *     super(Look::new, Look::yylex, Look.YYEOF);
 *   }
 *
 *   &#64;Test
 *   public void shortBase() throws Exception {
 *     scan("abcde\n");
 *     assertToken(AB, "ab", 0, 0);
 *     assertEach(OTHER, "cde\n", 0, 2);
 *     assertEof();
 *   }
 * }
 * </pre>
 *
 * <p>The assertions access the scanner through {@link #yytext()}, {@link #yylength()}, {@link
 * #yyatEOF()}, {@link #yyline()}, {@link #yycolumn()} and {@link #yychar()}. These call the methods
 * of {@link TestScanner}, which the spec {@code %implements}. A test for a spec without {@code
 * %implements} can override the methods that its assertions use, for instance
 *
 * <pre>
 *   &#64;Override
 *   protected int yyline() {
 *     return scanner.line();
 *   }
 * </pre>
 *
 * @param <S> the scanner class
 * @param <T> the type of the tokens that the scanning method returns. For a scanner that returns
 *     {@code int}, this is {@code Integer}.
 */
public abstract class ScannerTest<S, T> {

  /** The number of chars of the input that a failure message shows. */
  private static final int MESSAGE_INPUT_LENGTH = 80;

  private final Function<Reader, S> newScanner;
  private final Lexer<S, T> yylex;
  private final T eof;

  /**
   * The scanner of the last {@link #scan(String)}, for calls that are specific to the scanner
   * class, such as {@code yybegin} or {@code yystate}.
   */
  protected S scanner;

  /** The input of the last {@link #scan(String)}. */
  private String input;

  /** The number of matches that the assertions have asked for since the last scan. */
  private int matches;

  /**
   * @param newScanner makes a scanner for a reader, usually the constructor {@code MyScanner::new}
   * @param yylex the scanning method, usually {@code MyScanner::yylex}
   * @param eof what the scanning method returns at the end of input, for instance {@code
   *     MyScanner.YYEOF} or {@code null}
   */
  protected ScannerTest(Function<Reader, S> newScanner, Lexer<S, T> yylex, T eof) {
    this.newScanner = newScanner;
    this.yylex = yylex;
    this.eof = eof;
  }

  /** Makes a new {@link #scanner} that reads the input. */
  protected final void scan(String input) {
    this.input = input;
    this.matches = 0;
    this.scanner = newScanner.apply(new StringReader(input));
  }

  /** Checks that the next match has the provided token and text. */
  protected final void assertToken(T token, String text) throws IOException {
    assertNextMatch(token, text, "");
  }

  /** Checks that the next match has the provided token, text and position. */
  protected final void assertToken(T token, String text, int line, int column) throws IOException {
    Supplier<String> at = assertNextMatch(token, text, " at line " + line + ", column " + column);
    assertEquals(line, yyline(), () -> "line of " + at.get());
    assertEquals(column, yycolumn(), () -> "column of " + at.get());
  }

  /**
   * Checks that the next match has the provided token, text and position.
   *
   * @param offset the number of chars from the start of the input, as counted by {@code %char}
   */
  protected final void assertToken(T token, String text, int line, int column, long offset)
      throws IOException {
    Supplier<String> at =
        assertNextMatch(
            token, text, " at line " + line + ", column " + column + ", offset " + offset);
    assertEquals(line, yyline(), () -> "line of " + at.get());
    assertEquals(column, yycolumn(), () -> "column of " + at.get());
    assertEquals(offset, yychar(), () -> "offset of " + at.get());
  }

  /** Checks that the next matches have the provided token and one char of the text each. */
  protected final void assertEach(T token, String text) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(token, text.substring(i, i + 1));
    }
  }

  /**
   * Checks that the next matches have the provided token and one char of the text each. The first
   * match must be at {@code line} and {@code column}, each of the others one column after the one
   * before and same line.
   */
  protected final void assertEach(T token, String text, int line, int column) throws IOException {
    for (int i = 0; i < text.length(); i++) {
      assertToken(token, text.substring(i, i + 1), line, column + i);
    }
  }

  /** Checks that there is no further match and that the scanner is at the end of its input. */
  protected final void assertEof() throws IOException {
    T actual = next();
    Supplier<String> at = () -> "match " + matches + " of " + quotedInput() + " is not the end";
    assertEquals(eof, actual, at);
    assertTrue(
        yyatEOF(), () -> "yyatEOF() after " + (matches - 1) + " matches of " + quotedInput());
  }

  /**
   * Whether {@code assertToken} also checks {@link #yylength()} against the length of the expected
   * text. A test class can override this to return {@code false}.
   */
  protected boolean checkLength() {
    return true;
  }

  /** The text of the current match. */
  protected String yytext() {
    return testScanner("yytext()").yytext();
  }

  /** The length of the current match. */
  protected int yylength() {
    return testScanner("yylength()").yylength();
  }

  /** Whether the scanner has reached the end of its input. */
  protected boolean yyatEOF() {
    return testScanner("yyatEOF()").yyatEOF();
  }

  /** The line of the current match, counted from 0. */
  protected int yyline() {
    return testScanner("yyline()").yyline();
  }

  /** The column of the current match, counted from 0. */
  protected int yycolumn() {
    return testScanner("yycolumn()").yycolumn();
  }

  /** The offset of the current match from the start of the input, counted from 0. */
  protected long yychar() {
    return testScanner("yychar()").yychar();
  }

  /**
   * Scans the next match and checks its token, text and length.
   *
   * @param position the expected position for the failure messages, may be empty
   * @return the description of the match for further failure messages
   */
  private Supplier<String> assertNextMatch(T token, String text, String position)
      throws IOException {
    T actual = next();
    int number = matches;
    Supplier<String> at = () -> "match " + number + position + " of " + quotedInput();
    assertEquals(token, actual, () -> "token of " + at.get());
    assertEquals(text, yytext(), () -> "text of " + at.get());
    if (checkLength()) {
      assertEquals(text.length(), yylength(), () -> "length of " + at.get());
    }
    return at;
  }

  private T next() throws IOException {
    requireScanner();
    matches++;
    return yylex.next(scanner);
  }

  private void requireScanner() {
    if (scanner == null) {
      throw new IllegalStateException("There is no scanner. Call scan(input) first.");
    }
  }

  private TestScanner testScanner(String method) {
    requireScanner();
    if (!(scanner instanceof TestScanner)) {
      throw new IllegalStateException(
          scanner.getClass().getName()
              + " is not a "
              + TestScanner.class.getName()
              + ". Add %implements to the spec, or override "
              + method
              + " in "
              + getClass().getName()
              + ".");
    }
    return (TestScanner) scanner;
  }

  private String quotedInput() {
    return input == null ? "unknown input" : Chars.quoted(input, MESSAGE_INPUT_LENGTH);
  }
}
