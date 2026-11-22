/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.scan;

import static de.jflex.testing.scan.WordScanner.OTHER;
import static de.jflex.testing.scan.WordScanner.WORD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

/** Test for {@link ScannerTest}, {@link TestScanner} and {@link Chars}. */
public class ScannerTestTest {

  /**
   * Extends {@link ScannerTest} in the same way as test class, for a scanner that implements {@link
   * TestScanner}.
   */
  private static class Checks<S extends WordScanner> extends ScannerTest<S, Integer> {
    Checks(Function<Reader, S> newScanner) {
      super(newScanner, WordScanner::yylex, WordScanner.YYEOF);
    }
  }

  /**
   * The same for a scanner that does not implement {@link TestScanner}. The class overrides the
   * accessors of {@link ScannerTest} and calls the scanner's own methods.
   */
  private static class OverridingChecks extends Checks<WordScanner> {
    OverridingChecks() {
      super(WordScanner::new);
    }

    @Override
    protected String yytext() {
      return scanner.yytext();
    }

    @Override
    protected int yylength() {
      return scanner.yylength();
    }

    @Override
    protected boolean yyatEOF() {
      return scanner.yyatEOF();
    }

    @Override
    protected int yyline() {
      return scanner.line();
    }

    @Override
    protected int yycolumn() {
      return scanner.column();
    }

    @Override
    protected long yychar() {
      return scanner.offset();
    }
  }

  private static Checks<WordScanner.WithCounters> withCounters() {
    return new Checks<>(WordScanner.WithCounters::new);
  }

  private static String failure(Executable assertion) {
    return assertThrows(AssertionFailedError.class, assertion).getMessage();
  }

  private static void assertStartsWith(String prefix, String message) {
    assertTrue(message.startsWith(prefix), () -> "<" + message + "> starts with <" + prefix + ">");
  }

  @Test
  public void pass() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("ab cd\n.e");
    t.assertToken(WORD, "ab");
    t.assertToken(OTHER, " ", 0, 2);
    t.assertToken(WORD, "cd", 0, 3, 3);
    t.assertToken(OTHER, "\n", 0, 5, 5);
    t.assertToken(OTHER, ".", 1, 0, 6);
    t.assertToken(WORD, "e", 1, 1);
    t.assertEof();
  }

  @Test
  public void pass_each() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("ab+-\n*/");
    t.assertToken(WORD, "ab");
    t.assertEach(OTHER, "+-\n", 0, 2);
    t.assertEach(OTHER, "*/");
    t.assertEof();

    t.scan("");
    t.assertEach(OTHER, "");
    t.assertEach(OTHER, "", 0, 0);
    t.assertEof();
  }

  /** The scanner field contains the scanner class, and a new scan starts with a new scanner. */
  @Test
  public void scannerField() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("a");
    WordScanner.WithCounters first = t.scanner;
    t.assertToken(WORD, "a");
    assertFalse(t.scanner.yyatEOF());
    t.assertEof();
    assertTrue(t.scanner.yyatEOF());

    t.scan("b");
    assertTrue(first != t.scanner);
    t.assertToken(WORD, "b", 0, 0, 0);
    t.assertEof();
  }

  @Test
  public void fail_token() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("ab cd");
    t.assertToken(WORD, "ab");
    assertEquals(
        "token of match 2 of \"ab cd\" ==> expected: <1> but was: <0>",
        failure(() -> t.assertToken(WORD, " ")));
  }

  @Test
  public void fail_text() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("ab\n\"é");
    assertEquals(
        "text of match 1 at line 0, column 0 of \"ab\\u000A\\u0022\\u00E9\""
            + " ==> expected: <a> but was: <ab>",
        failure(() -> t.assertToken(WORD, "a", 0, 0)));
  }

  @Test
  public void fail_position() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("a\nb");
    t.assertToken(WORD, "a");
    t.assertToken(OTHER, "\n");
    assertEquals(
        "line of match 3 at line 0, column 0 of \"a\\u000Ab\" ==> expected: <0> but was: <1>",
        failure(() -> t.assertToken(WORD, "b", 0, 0)));

    t.scan("a b");
    t.assertToken(WORD, "a");
    assertStartsWith(
        "column of match 2 at line 0, column 2, offset 1 of \"a b\" ==> ",
        failure(() -> t.assertToken(OTHER, " ", 0, 2, 1)));

    t.scan("a b");
    t.assertToken(WORD, "a");
    assertStartsWith(
        "offset of match 2 at line 0, column 1, offset 7 of \"a b\" ==> ",
        failure(() -> t.assertToken(OTHER, " ", 0, 1, 7)));
  }

  @Test
  public void fail_each() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("+-a");
    assertStartsWith("token of match 3 of \"+-a\" ==> ", failure(() -> t.assertEach(OTHER, "+-a")));

    t.scan("+\n-");
    assertStartsWith(
        "line of match 3 at line 0, column 2 of ",
        failure(() -> t.assertEach(OTHER, "+\n-", 0, 0)));
  }

  @Test
  public void fail_eof() throws Exception {
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan("a b");
    t.assertToken(WORD, "a");
    assertEquals(
        "match 2 of \"a b\" is not the end ==> expected: <-1> but was: <0>", failure(t::assertEof));

    // a token is expected at the end
    t.scan("a");
    t.assertToken(WORD, "a");
    assertStartsWith("token of match 2 of \"a\" ==> ", failure(() -> t.assertToken(WORD, "a")));
  }

  @Test
  public void fail_length() throws Exception {
    Checks<WordScanner.WrongLength> t = new Checks<>(WordScanner.WrongLength::new);
    t.scan("ab");
    assertEquals(
        "length of match 1 of \"ab\" ==> expected: <2> but was: <3>",
        failure(() -> t.assertToken(WORD, "ab")));
  }

  @Test
  public void checkLength_off() throws Exception {
    Checks<WordScanner.WrongLength> t =
        new Checks<WordScanner.WrongLength>(WordScanner.WrongLength::new) {
          @Override
          protected boolean checkLength() {
            return false;
          }
        };
    t.scan("ab c");
    t.assertToken(WORD, "ab");
    t.assertToken(OTHER, " ", 0, 2, 2);
    t.assertToken(WORD, "c");
    t.assertEof();
  }

  /** The message cuts off a long input. */
  @Test
  public void fail_longInput() throws Exception {
    StringBuilder input = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      input.append("a ");
    }
    Checks<WordScanner.WithCounters> t = withCounters();
    t.scan(input.toString());
    String message = failure(() -> t.assertToken(OTHER, "a"));
    assertStartsWith("token of match 1 of \"a a a ", message);
    assertTrue(message.contains(" a a \"... (200 chars) ==> "), message);
  }

  @Test
  public void assertionBeforeScan() {
    Checks<WordScanner.WithCounters> t = withCounters();
    IllegalStateException e =
        assertThrows(IllegalStateException.class, () -> t.assertToken(WORD, "a"));
    assertEquals("There is no scanner. Call scan(input) first.", e.getMessage());
    assertThrows(IllegalStateException.class, t::assertEof);
    assertThrows(IllegalStateException.class, t::yytext);
  }

  /** A scanner without the counter methods: the assertions without position work. */
  @Test
  public void counterNotGenerated() throws Exception {
    Checks<WordScanner.WithInterface> t = new Checks<>(WordScanner.WithInterface::new);
    t.scan("a b");
    t.assertToken(WORD, "a");
    UnsupportedOperationException e =
        assertThrows(UnsupportedOperationException.class, () -> t.assertToken(OTHER, " ", 0, 1));
    assertEquals(
        WordScanner.WithInterface.class.getName()
            + " has no yyline(). The spec needs %line and %counter_methods.",
        e.getMessage());
    assertTrue(
        assertThrows(UnsupportedOperationException.class, t::yycolumn)
            .getMessage()
            .contains("has no yycolumn(). The spec needs %column and"));
    assertTrue(
        assertThrows(UnsupportedOperationException.class, t::yychar)
            .getMessage()
            .contains("has no yychar(). The spec needs %char and"));
    t.assertToken(WORD, "b");
    t.assertEof();
  }

  @Test
  public void scannerWithoutInterface() throws Exception {
    Checks<WordScanner> t = new Checks<>(WordScanner::new);
    t.scan("a");
    IllegalStateException e =
        assertThrows(IllegalStateException.class, () -> t.assertToken(WORD, "a"));
    assertEquals(
        WordScanner.class.getName()
            + " is not a de.jflex.testing.scan.TestScanner."
            + " Add %implements to the spec, or override yytext() in "
            + t.getClass().getName()
            + ".",
        e.getMessage());
  }

  @Test
  public void scannerWithoutInterface_overrides() throws Exception {
    OverridingChecks t = new OverridingChecks();
    t.scan("ab\n+");
    t.assertToken(WORD, "ab", 0, 0, 0);
    t.assertEach(OTHER, "\n");
    t.assertToken(OTHER, "+", 1, 0, 3);
    t.assertEof();

    t.scan("a");
    assertStartsWith(
        "column of match 1 at line 0, column 1 of \"a\" ==> ",
        failure(() -> t.assertToken(WORD, "a", 0, 1)));
  }

  @Test
  public void hex() {
    assertEquals("U+0041", Chars.hex('A'));
    assertEquals("U+000A", Chars.hex('\n'));
    assertEquals("U+1F600", Chars.hex(0x1F600));
    assertEquals("U+10FFFF", Chars.hex(0x10FFFF));

    assertEquals("", Chars.hex(""));
    assertEquals("\\u0041\\u000A", Chars.hex("A\n"));
    assertEquals("\\uD83D\\uDE00", Chars.hex("😀"));
  }

  @Test
  public void quoted() {
    assertEquals("\"\"", Chars.quoted("", 3));
    assertEquals("\"a ~\"", Chars.quoted("a ~", 3));
    assertEquals("\"\\u0009\\u007F\\u005C\\u0022\"", Chars.quoted("\t\u007f\\\"", 80));
    assertEquals("\"abc\"... (4 chars)", Chars.quoted("abcd", 3));
  }
}
