/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jlex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test {@code %ignorecase} with JLex semantics ({@code --jlex}).
 *
 * <p>Character classes are caseless, in addition to strings and chars.
 *
 * <p>The first tests are those of {@code CaselessJflexTest} for the same rules without {@code
 * --jlex}. The results are different for {@link #hello_mixedCase}, {@link #hello_within} and {@link
 * #hello_withinMixedCase}. The rule {@code "hello"} cannot match and should produce a warning,
 * because {@code [a-z]+} matches upper case letters.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/caseless_jlex/caseless.flex",
    // never-match warning for "hello"
    sysout = "src/test/java/de/jflex/testcase/caseless_jlex/caseless.sysout",
    quiet = true,
    jlexCompat = true,
    minimizedDfaStatesCount = 10,
    minimizedDfa = {
      "states 10, inputs 13, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 3; 3 -> 4; 4 -> 5; 5-9 -> 6; 10 -> 7; 11 -> 8",
      "1 [a0]:",
      "2 [a1]:",
      "3 [a1]: 1 -> 2",
      "4 [a0]: 0-12 -> 9",
      "5 [a2]: 4-9 -> 6",
      "6 [a3]: 4-9 -> 6",
      "7 [a4]: 10 -> 7",
      "8 [a5]: 11 -> 8",
      "9 [a6]:",
      "a0: return State.OTHER;",
      "a1: return State.NEW_LINE;",
      "a2: return State.A;",
      "a3: return State.WORD;",
      "a4: return State.LATIN1;",
      "a5: return State.CYRILLIC;",
      "a6: return State.NOT_DELTA;"
    },
    charClassesCount = 13,
    charClasses = {
      "{ [0-9][11-12][14-'\"']['$'-'@']['['-'`']['{'-191][215][223][255-303][306-382][384-1044]"
          + "[1048-1076][1080-8489][8492-55295][57344-1114111] }",
      "{ [10] }",
      "{ [13] }",
      "{ ['#'] }",
      "{ ['A']['a'] }",
      "{ ['B'-'D']['F'-'G']['I'-'K']['M'-'N']['P'-'Z']['b'-'d']['f'-'g']['i'-'k']['m'-'n']['p'-'z']"
          + "[304-305][383][8490] }",
      "{ ['E']['e'] }",
      "{ ['H']['h'] }",
      "{ ['L']['l'] }",
      "{ ['O']['o'] }",
      "{ [192-214][216-222][224-254][8491] }",
      "{ [1045-1047][1077-1079] }",
      "{ [55296-57343] }"
    })
public class CaselessJlexTest implements JFlexTest {

  private Caseless scanner;

  private void scan(String input) {
    scanner = new Caseless(new StringReader(input));
  }

  /** Checks the next token: the matched rule + text and position (from 0). */
  private void assertToken(State rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(State.END_OF_FILE, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that the input is one token of the rule. */
  private void assertSingleToken(State rule, String input) throws IOException {
    scan(input);
    assertToken(rule, input, 0, 0);
    assertEof();
  }

  // --- the tests of CaselessJflexTest

  @Test
  public void a() throws Exception {
    scan("a");
    assertToken(State.A, "a", 0, 0);
    assertEof();
  }

  /** 'A' matches 'a' in caseless mode. */
  @Test
  public void a_upper() throws Exception {
    scan("A");
    assertToken(State.A, "A", 0, 0);
    assertEof();
  }

  @Test
  public void hello() throws Exception {
    // [a-z]+ has higher priority than "hello" in the rules
    scan("hello");
    assertToken(State.WORD, "hello", 0, 0);
    assertEof();
  }

  /** With JFlex semantics this would be HELLO, because {@code [a-z]+} does not match 'HelLo'. */
  @Test
  public void hello_mixedCase() throws Exception {
    // [a-z]+ has higher priority than the "hello" rule, and matches "HelLo" in JLex mode
    scan("HelLo");
    assertToken(State.WORD, "HelLo", 0, 0);
    assertEof();
  }

  /** With JFlex semantics these would be three tokens. */
  @Test
  public void hello_within() throws Exception {
    scan("blubHELLObla");
    assertToken(State.WORD, "blubHELLObla", 0, 0);
    assertEof();
  }

  /** With JFlex semantics these would be three tokens (WORD, OTHER, WORD). */
  @Test
  public void hello_withinMixedCase() throws Exception {
    scan("sdfahelLobla");
    assertToken(State.WORD, "sdfahelLobla", 0, 0);
    assertEof();
  }

  /** With JFlex semantics the first token would be HELLO. */
  @Test
  public void hello_newline() throws Exception {
    scan("HELLO\n;");
    assertToken(State.WORD, "HELLO", 0, 0);
    assertToken(State.NEW_LINE, "\n", 0, 5);
    assertToken(State.OTHER, ";", 1, 0);
    assertEof();
  }

  @Test
  public void other() throws Exception {
    scan("asd123hello;qwe");
    assertToken(State.WORD, "asd", 0, 0);
    assertToken(State.OTHER, "1", 0, 3);
    assertToken(State.OTHER, "2", 0, 4);
    assertToken(State.OTHER, "3", 0, 5);
    assertToken(State.WORD, "hello", 0, 6);
    assertToken(State.OTHER, ";", 0, 11);
    assertToken(State.WORD, "qwe", 0, 12);
    assertEof();
  }

  /** The rule {@code a} is before {@code [a-z]+}, but the longer match wins. */
  @Test
  public void a_longer() throws Exception {
    scan("a\nA\naA\nAb");
    assertToken(State.A, "a", 0, 0);
    assertToken(State.NEW_LINE, "\n", 0, 1);
    assertToken(State.A, "A", 1, 0);
    assertToken(State.NEW_LINE, "\n", 1, 1);
    assertToken(State.WORD, "aA", 2, 0);
    assertToken(State.NEW_LINE, "\n", 2, 2);
    assertToken(State.WORD, "Ab", 3, 0);
    assertEof();
  }

  // --- caseless character classes

  /** The range {@code [a-z]} has every letter in both cases. */
  @Test
  public void range_everyLetter() throws Exception {
    assertSingleToken(State.WORD, "bcdefghijklmnopqrstuvwxyz");
    assertSingleToken(State.WORD, "BCDEFGHIJKLMNOPQRSTUVWXYZ");
    assertSingleToken(State.WORD, "zZyYxXwWvVuUtTsSrRqQpPoOnNmMlLkKjJiIhHgGfFeEdDcCbBaA");
  }

  /**
   * U+212A KELVIN SIGN (K) is an upper case letter with the lower case k. It is in the caseless
   * {@code [a-z]}.
   */
  @Test
  public void range_kelvinSign() throws Exception {
    assertSingleToken(State.WORD, "K");
    assertSingleToken(State.WORD, "kKK");
  }

  /** U+017F LATIN SMALL LETTER LONG S (ſ) has the upper case S. */
  @Test
  public void range_longS() throws Exception {
    assertSingleToken(State.WORD, "ſ");
    assertSingleToken(State.WORD, "sſS");
  }

  /**
   * U+0130 LATIN CAPITAL LETTER I WITH DOT ABOVE (İ) has the lower case i, and U+0131 LATIN SMALL
   * LETTER DOTLESS I (ı) has the upper case I in the Unicode character database.
   */
  @Test
  public void range_dottedAndDotlessI() throws Exception {
    assertSingleToken(State.WORD, "İ");
    assertSingleToken(State.WORD, "ı");
    assertSingleToken(State.WORD, "iIİı");
  }

  /**
   * A range outside of ASCII: {@code [U+00E0-U+00FE]+}, small a with grave (à) to small thorn (þ).
   */
  @Test
  public void latin1Range() throws Exception {
    assertSingleToken(State.LATIN1, "àéþ");
    assertSingleToken(State.LATIN1, "ÀÉ");
    assertSingleToken(State.LATIN1, "àÀéÉþÞ");
  }

  /** The division sign (÷) is in the range. */
  @Test
  public void latin1Range_withoutCase() throws Exception {
    scan("é÷É×é");
    assertToken(State.LATIN1, "é÷É", 0, 0);
    assertToken(State.OTHER, "×", 0, 3);
    assertToken(State.LATIN1, "é", 0, 4);
    assertEof();
  }

  /** ß and y with diaeresis are next to the range, in either case. */
  @Test
  public void latin1Range_outside() throws Exception {
    // U+00DF sharp s (ß), U+1E9E capital sharp s (ẞ), U+00FF and U+0178 y with diaeresis (ÿ, Ÿ)
    scan("ßẞÿŸ");
    assertToken(State.OTHER, "ß", 0, 0);
    assertToken(State.OTHER, "ẞ", 0, 1);
    assertToken(State.OTHER, "ÿ", 0, 2);
    assertToken(State.OTHER, "Ÿ", 0, 3);
    assertEof();
  }

  /** U+212B ANGSTROM SIGN (Å) has the lower case a with ring above (å), which is in the range. */
  @Test
  public void latin1Range_angstromSign() throws Exception {
    assertSingleToken(State.LATIN1, "Å");
    assertSingleToken(State.LATIN1, "åÅÅ");
  }

  /** A class that comes from a macro is caseless as well ({@code CYRILLIC}) */
  @Test
  public void classFromMacro() throws Exception {
    assertSingleToken(State.CYRILLIC, "ежз");
    assertSingleToken(State.CYRILLIC, "ЕЖЗ");
    assertSingleToken(State.CYRILLIC, "еЕжЖзЗ");
  }

  @Test
  public void classFromMacro_outside() throws Exception {
    // U+0434 (д) and U+0438 (и) are next to the range,
    // U+0414 (Д) and U+0418 (И) are the upper case
    scan("дДеиИ");
    assertToken(State.OTHER, "д", 0, 0);
    assertToken(State.OTHER, "Д", 0, 1);
    assertToken(State.CYRILLIC, "е", 0, 2);
    assertToken(State.OTHER, "и", 0, 3);
    assertToken(State.OTHER, "И", 0, 4);
    assertEof();
  }

  /** {@code "#" [^U+03B4]} matches characters that have nothing to do with delta. */
  @Test
  public void negatedClass() throws Exception {
    scan("#x#X#γ##\n#\n#");
    assertToken(State.NOT_DELTA, "#x", 0, 0);
    assertToken(State.NOT_DELTA, "#X", 0, 2);
    assertToken(State.NOT_DELTA, "#γ", 0, 4);
    assertToken(State.NOT_DELTA, "##", 0, 6);
    assertToken(State.NEW_LINE, "\n", 0, 8);
    // the negated class contains \n
    assertToken(State.NOT_DELTA, "#\n", 1, 0);
    // "#" alone
    assertToken(State.OTHER, "#", 2, 0);
    assertEof();
  }

  /**
   * With caseless classes, {@code [^U+03B4]} is the negation of small and capital delta (δ and Δ),
   * and matches neither of them.
   */
  @Disabled(
      "JFlex bug: with --jlex and %ignorecase a negated class matches the characters that it"
          + " excludes, if they have another case. The class is negated first, and the case"
          + " variants of the result are added after that.")
  @Test
  public void negatedClass_excludesBothCases() throws Exception {
    scan("#δ#Δ");
    assertToken(State.OTHER, "#", 0, 0);
    assertToken(State.OTHER, "δ", 0, 1);
    assertToken(State.OTHER, "#", 0, 2);
    assertToken(State.OTHER, "Δ", 0, 3);
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
