/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.BitSet;
import java.util.function.IntPredicate;
import org.junit.jupiter.api.Test;

/**
 * Test for issue <a href="https://github.com/jflex-de/jflex/issues/216">#216 Macros in character
 * classes are not supported</a>.
 *
 * <p>The first three rules are the test of the issue. {@code {X}}, {@code [{NCD}]} and {@code [^]}
 * each match one character, and together they match every character.
 *
 * <p>The other ten rules match a digit and one character after it. The digit corresponds to the
 * rule and action, and the rule has a character class built from macros for the second character.
 * Because these rules match two characters, they do not change what the first three rules do with a
 * single character, and each class can be tested with every character.
 *
 * <p>The tests compute the expected classes from the macro definitions with {@link BitSet}, and
 * compare them with the scanner for all code points.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_macros/cclmacros.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/ccl_macros/cclmacros.sysout",
    quiet = true,
    minimizedDfaStatesCount = 24,
    minimizedDfa = {
      "states 24, inputs 20, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2 -> 3; 3 -> 4; 4 -> 5; 5 -> 6; 6 -> 7; 7 -> 8; 8 -> 9; 9 -> 10;"
          + " 10 -> 11; 11 -> 12; 12 -> 1; 13 -> 12; 14 -> 13; 15-19 -> 1",
      "1 [a0]:",
      "2 [a0]: 15-16 -> 14",
      "3 [a0]: 16 -> 15",
      "4 [a0]: 15-17 -> 16",
      "5 [a0]: 15 -> 17; 17 -> 17",
      "6 [a0]: 0-14 -> 18; 17-19 -> 18",
      "7 [a0]: 16 -> 19",
      "8 [a0]: 18 -> 20",
      "9 [a0]: 1-10 -> 21; 19 -> 21",
      "10 [a0]: 11-14 -> 22",
      "11 [a0]: 15-16 -> 23",
      "12 [a1]:",
      "13 [a2]:",
      "14 [a3]:",
      "15 [a4]:",
      "16 [a5]:",
      "17 [a6]:",
      "18 [a7]:",
      "19 [a8]:",
      "20 [a9]:",
      "21 [a10]:",
      "22 [a11]:",
      "23 [a12]:",
      "a0: /* NCD */ return 11;",
      "a1: /* X */ return 10;",
      "a2: /* default */ return 12;",
      "a3: return 0;",
      "a4: return 1;",
      "a5: return 2;",
      "a6: return 3;",
      "a7: return 4;",
      "a8: return 5;",
      "a9: return 6;",
      "a10: return 7;",
      "a11: return 8;",
      "a12: return 9;"
    },
    charClassesCount = 20,
    charClasses = {
      "{ [0-'/'][':'-'@']['['-'`']['j']['l']['n'-191][215][223-1631][1642-1775][1786-1114111] }",
      "{ ['0'] }",
      "{ ['1'] }",
      "{ ['2'] }",
      "{ ['3'] }",
      "{ ['4'] }",
      "{ ['5'] }",
      "{ ['6'] }",
      "{ ['7'] }",
      "{ ['8'] }",
      "{ ['9'] }",
      "{ ['A'] }",
      "{ ['B']['E'-'Z'][192-214][216-222] }",
      "{ ['C'] }",
      "{ ['D'] }",
      "{ ['a'-'c'] }",
      "{ ['d'-'f'] }",
      "{ ['g'-'i'] }",
      "{ ['k']['m'] }",
      "{ [1632-1641][1776-1785] }"
    })
public class CclmacrosTest implements JFlexTest {

  /** {@code {X}} */
  private static final int X_RULE = 10;

  /** {@code [{NCD}]} */
  private static final int NCD_RULE = 11;

  /** {@code [^]} */
  private static final int DEFAULT_RULE = 12;

  // --- the macros of the spec, as sets of code points

  /** {@code A = [A]} */
  private static final BitSet A = of('A');

  /** {@code NCD = [^CD]} */
  private static final BitSet NCD = not(of('C', 'D'));

  /** {@code C = [BC--{NCD}]} */
  private static final BitSet C = minus(of('B', 'C'), NCD);

  /** {@code X = [{A}{C}]} */
  private static final BitSet X = or(A, C);

  /** {@code M = [a-f]} */
  private static final BitSet M = range('a', 'f');

  /** {@code N = [d-i]} */
  private static final BitSet N = range('d', 'i');

  /** {@code NN = [^{N}]} */
  private static final BitSet NN = not(N);

  /** {@code DEEP1 = [j-l]} */
  private static final BitSet DEEP1 = range('j', 'l');

  /** {@code DEEP2 = [{DEEP1}--k]} */
  private static final BitSet DEEP2 = minus(DEEP1, of('k'));

  /** {@code DEEP3 = [^{DEEP2}]} */
  private static final BitSet DEEP3 = not(DEEP2);

  /**
   * {@code DIGIT = [:digit:]}. JFlex and the JDK can have different Unicode versions, but not for
   * the range that the rule uses.
   */
  private static final BitSet DIGIT =
      where(c -> Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER);

  /** {@code UPPER = \p{Lu}}, see {@link #DIGIT} */
  private static final BitSet UPPER =
      where(c -> Character.getType(c) == Character.UPPERCASE_LETTER);

  /** The classes of the rules {@code "0"} to {@code "9"}, for the character after the digit. */
  private static final BitSet[] SELECTED = {
    // "0" [{M}]
    M,
    // "1" [{M}&&{N}]
    and(M, N),
    // "2" [{M}||{N}]
    or(M, N),
    // "3" [{M}~~{N}]
    xor(M, N),
    // "4" [^{M}]
    not(M),
    // "5" [{M}--{NN}]
    minus(M, NN),
    // "6" [{DEEP3}&&[j-m]]
    and(DEEP3, range('j', 'm')),
    // "7" [{DIGIT}&&[U+0000-U+06FF]]
    and(DIGIT, range(0, 0x06FF)),
    // "8" [{UPPER}&&[U+0000-U+00FF]]
    and(UPPER, range(0, 0x00FF)),
    // "9" {M}
    M
  };

  private static BitSet of(int... codePoints) {
    BitSet result = new BitSet();
    for (int c : codePoints) {
      result.set(c);
    }
    return result;
  }

  private static BitSet range(int from, int to) {
    BitSet result = new BitSet();
    result.set(from, to + 1);
    return result;
  }

  private static BitSet where(IntPredicate property) {
    BitSet result = new BitSet();
    for (int c = 0; c <= Character.MAX_CODE_POINT; c++) {
      result.set(c, property.test(c));
    }
    return result;
  }

  /** {@code [^a]} */
  private static BitSet not(BitSet a) {
    BitSet result = (BitSet) a.clone();
    result.flip(0, Character.MAX_CODE_POINT + 1);
    return result;
  }

  /** {@code [a||b]} */
  private static BitSet or(BitSet a, BitSet b) {
    BitSet result = (BitSet) a.clone();
    result.or(b);
    return result;
  }

  /** {@code [a&&b]} */
  private static BitSet and(BitSet a, BitSet b) {
    BitSet result = (BitSet) a.clone();
    result.and(b);
    return result;
  }

  /** {@code [a--b]} */
  private static BitSet minus(BitSet a, BitSet b) {
    BitSet result = (BitSet) a.clone();
    result.andNot(b);
    return result;
  }

  /** {@code [a~~b]} */
  private static BitSet xor(BitSet a, BitSet b) {
    BitSet result = (BitSet) a.clone();
    result.xor(b);
    return result;
  }

  private static int singleRule(int c) {
    if (X.get(c)) {
      return X_RULE;
    }
    return NCD.get(c) ? NCD_RULE : DEFAULT_RULE;
  }

  /** The tests over all code points leave out the high surrogates. */
  private static boolean isHighSurrogate(int c) {
    return Character.MIN_HIGH_SURROGATE <= c && c <= Character.MAX_HIGH_SURROGATE;
  }

  private static String hex(int c) {
    return String.format("U+%04X", c);
  }

  private Cclmacros scanner;

  private void scan(String input) {
    scanner = new Cclmacros(new StringReader(input));
  }

  /** Checks the next token: the matched rule + text and position (from 0). */
  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Cclmacros.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /**
   * Checks the rule for the digit {@code rule} with every code point after the digit: the rule
   * matches both iff the code point is in {@code SELECTED[rule]}. Otherwise the digit and the code
   * point are matched one by one, by the first three rules.
   */
  private void assertSelected(int rule) throws IOException {
    BitSet selected = SELECTED[rule];
    scan("");
    for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
      final int c = cp;
      if (isHighSurrogate(c)) {
        continue;
      }
      int charCount = Character.charCount(c);
      scanner.yyreset(new StringReader(rule + new String(Character.toChars(c))));
      if (selected.get(c)) {
        assertEquals(rule, scanner.yylex(), () -> "rule for " + rule + " + " + hex(c));
        assertEquals(1 + charCount, scanner.yylength(), () -> "length for " + hex(c));
      } else {
        assertEquals(NCD_RULE, scanner.yylex(), () -> "rule for " + rule + " before " + hex(c));
        assertEquals(1, scanner.yylength(), () -> "length for " + rule + " before " + hex(c));
        assertEquals(singleRule(c), scanner.yylex(), () -> "rule for " + hex(c));
        assertEquals(charCount, scanner.yylength(), () -> "length for " + hex(c));
      }
      assertEquals(Cclmacros.YYEOF, scanner.yylex(), () -> "end of input after " + hex(c));
    }
  }

  // --- the first three rules (#216)

  /** The values that the spec comments give for the macros. */
  @Test
  public void macroDefinitions() {
    assertEquals(of('C'), C, "C = [BC--{NCD}]");
    assertEquals(of('A', 'C'), X, "X = [{A}{C}]");
    assertEquals(range('d', 'f'), SELECTED[1], "[{M}&&{N}]");
    assertEquals(SELECTED[1], SELECTED[5], "[{M}--{NN}]");
    assertEquals(of('k', 'm'), SELECTED[6], "[{DEEP3}&&[j-m]]");
  }

  /** The input of the golden file that this test replaces. */
  @Test
  public void macrosInClasses() throws Exception {
    scan("AXBCDX");
    assertToken(X_RULE, "A", 0, 0);
    assertToken(NCD_RULE, "X", 0, 1);
    assertToken(NCD_RULE, "B", 0, 2);
    assertToken(X_RULE, "C", 0, 3);
    assertToken(DEFAULT_RULE, "D", 0, 4);
    assertToken(NCD_RULE, "X", 0, 5);
    assertEof();
  }

  /** X is A and C, the default rule is left with D, and NCD is every other code point. */
  @Test
  public void singleCodePoints() throws Exception {
    scan("");
    for (int cp = 0; cp <= Character.MAX_CODE_POINT; cp++) {
      final int c = cp;
      if (isHighSurrogate(c)) {
        continue;
      }
      int expected = c == 'A' || c == 'C' ? X_RULE : c == 'D' ? DEFAULT_RULE : NCD_RULE;
      assertEquals(expected, singleRule(c), () -> "expected rule for " + hex(c));
      scanner.yyreset(new StringReader(new String(Character.toChars(c))));
      assertEquals(expected, scanner.yylex(), () -> "rule for " + hex(c));
      assertEquals(Character.charCount(c), scanner.yylength(), () -> "length for " + hex(c));
      assertEquals(Cclmacros.YYEOF, scanner.yylex(), () -> "end of input after " + hex(c));
    }
  }

  /** A negated class has the surrogates. An unpaired one is a character like any other. */
  @Test
  public void unpairedSurrogates() throws Exception {
    scan("\uD800A\uDC00\uDBFFa");
    assertToken(NCD_RULE, "\uD800", 0, 0);
    assertToken(X_RULE, "A", 0, 1);
    assertToken(NCD_RULE, "\uDC00", 0, 2);
    assertToken(NCD_RULE, "\uDBFF", 0, 3);
    assertToken(NCD_RULE, "a", 0, 4);
    assertEof();
  }

  /** The digits are in NCD when the character after them is not in the class of their rule. */
  @Test
  public void digitAlone() throws Exception {
    scan("1A12d0");
    assertToken(NCD_RULE, "1", 0, 0);
    assertToken(X_RULE, "A", 0, 1);
    assertToken(NCD_RULE, "1", 0, 2);
    // not "d" of rule 1: the rule of the digit before it wins
    assertToken(2, "2d", 0, 3);
    assertToken(NCD_RULE, "0", 0, 5);
    assertEof();
  }

  // --- the digit rules

  /** {@code "0" [{M}]}: the macro is used before another rule splits its class. */
  @Test
  public void macroBeforeItsClassIsSplit() throws Exception {
    scan("0a0f0g");
    assertToken(0, "0a", 0, 0);
    assertToken(0, "0f", 0, 2);
    assertToken(NCD_RULE, "0", 0, 4);
    assertToken(NCD_RULE, "g", 0, 5);
    assertEof();
    assertSelected(0);
  }

  /** {@code "1" [{M}&&{N}]} */
  @Test
  public void intersection() throws Exception {
    scan("1d1c");
    assertToken(1, "1d", 0, 0);
    assertToken(NCD_RULE, "1", 0, 2);
    assertToken(NCD_RULE, "c", 0, 3);
    assertEof();
    assertSelected(1);
  }

  /** {@code "2" [{M}||{N}]} */
  @Test
  public void union() throws Exception {
    scan("2a2i2j");
    assertToken(2, "2a", 0, 0);
    assertToken(2, "2i", 0, 2);
    assertToken(NCD_RULE, "2", 0, 4);
    assertToken(NCD_RULE, "j", 0, 5);
    assertEof();
    assertSelected(2);
  }

  /** {@code "3" [{M}~~{N}]} */
  @Test
  public void symmetricDifference() throws Exception {
    scan("3c3g3d");
    assertToken(3, "3c", 0, 0);
    assertToken(3, "3g", 0, 2);
    assertToken(NCD_RULE, "3", 0, 4);
    assertToken(NCD_RULE, "d", 0, 5);
    assertEof();
    assertSelected(3);
  }

  /** {@code "4" [^{M}]} */
  @Test
  public void complement() throws Exception {
    scan("4g4D444a");
    assertToken(4, "4g", 0, 0);
    assertToken(4, "4D", 0, 2);
    assertToken(4, "44", 0, 4);
    assertToken(NCD_RULE, "4", 0, 6);
    assertToken(NCD_RULE, "a", 0, 7);
    assertEof();
    assertSelected(4);
  }

  /** {@code "5" [{M}--{NN}]}: the second operand is the negated class of a macro. */
  @Test
  public void differenceWithNegatedMacro() throws Exception {
    scan("5f5g");
    assertToken(5, "5f", 0, 0);
    assertToken(NCD_RULE, "5", 0, 2);
    assertToken(NCD_RULE, "g", 0, 3);
    assertEof();
    assertSelected(5);
  }

  /** {@code "6" [{DEEP3}&&[j-m]]}: DEEP3 uses DEEP2, which uses DEEP1. */
  @Test
  public void macroInMacroInMacro() throws Exception {
    scan("6k6m6j");
    assertToken(6, "6k", 0, 0);
    assertToken(6, "6m", 0, 2);
    assertToken(NCD_RULE, "6", 0, 4);
    assertToken(NCD_RULE, "j", 0, 5);
    assertEof();
    assertSelected(6);
  }

  /** {@code "7" [{DIGIT}&&[U+0000-U+06FF]]} with {@code DIGIT = [:digit:]} */
  @Test
  public void predefinedClassInMacro() throws Exception {
    // U+0669 ARABIC-INDIC DIGIT NINE; U+07C0 NKO DIGIT ZERO is outside of the range
    scan("707\u06697\u07C0");
    assertToken(7, "70", 0, 0);
    assertToken(7, "7\u0669", 0, 2);
    assertToken(NCD_RULE, "7", 0, 4);
    assertToken(NCD_RULE, "\u07C0", 0, 5);
    assertEof();
    assertSelected(7);
  }

  /** {@code "8" [{UPPER}&&[U+0000-U+00FF]]} with {@code UPPER = \p{Lu}} */
  @Test
  public void unicodePropertyInMacro() throws Exception {
    // U+00C9 E WITH ACUTE; U+00D7 MULTIPLICATION SIGN; U+0100 A WITH MACRON is outside
    scan("8D8\u00C98\u00D78\u0100");
    assertToken(8, "8D", 0, 0);
    assertToken(8, "8\u00C9", 0, 2);
    assertToken(NCD_RULE, "8", 0, 4);
    assertToken(NCD_RULE, "\u00D7", 0, 5);
    assertToken(NCD_RULE, "8", 0, 6);
    assertToken(NCD_RULE, "\u0100", 0, 7);
    assertEof();
    assertSelected(8);
  }

  /**
   * {@code "9" {M}}: the macro that the rules above use inside of classes is the regular expression
   * here, and the rules above have split its class into {@code [a-c]} and {@code [d-f]}.
   */
  @Test
  public void macroAsRegExpAfterItsClassIsSplit() throws Exception {
    scan("9a9f9g");
    assertToken(9, "9a", 0, 0);
    assertToken(9, "9f", 0, 2);
    assertToken(NCD_RULE, "9", 0, 4);
    assertToken(NCD_RULE, "g", 0, 5);
    assertEof();
    assertSelected(9);
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
