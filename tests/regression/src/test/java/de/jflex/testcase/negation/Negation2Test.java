/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests the negation operator {@code !} with character classes and macros, and the upto operator
 * {@code ~} against expressions that should be equivalent. Spec rules have distinct characters, so
 * that they do not depend on each other.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/negation/negation2.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/negation/negation2.sysout",
    quiet = true,
    minimizedDfaStatesCount = 27,
    // one class for each of the 16 characters in the spec, and one for all others
    charClassesCount = 17,
    distinctCharClasses = {"abcklm()<>[]/*+-x"},
    sameCharClass = {"x\n\u0000d\uFFFF😀"})
public class Negation2Test implements JFlexTest {

  /** <code>!(!{X}|!{Y})</code> with {@code X = a [ab]*} and {@code Y = [ab]* b} */
  private static final int INTERSECTION = 1;

  /** {@code "(" ![c] ")"} */
  private static final int NOT_C = 2;

  /** {@code "<" [^c] ">"} */
  private static final int NO_C = 3;

  /** {@code "/*" ~"*\/"} */
  private static final int UPTO = 4;

  /** {@code "/+" ([^+] | \++ [^+/])* \++ "/"} */
  private static final int UPTO_BY_HAND = 5;

  /** {@code "/-" !([^]* "-/" [^]*) "-/"} */
  private static final int UPTO_BY_NEGATION = 6;

  /** <code>"[" !{K} "]"</code> with {@code K = "kl" | m} */
  private static final int NOT_MACRO = 7;

  /** {@code [^]} */
  private static final int OTHER = 0;

  private Negation2 scanner;

  private void scan(String input) {
    scanner = new Negation2(new StringReader(input));
  }

  private void assertToken(int rule, String text, int line, int column) throws IOException {
    String at = "at line " + line + ", column " + column;
    assertEquals(rule, scanner.yylex(), "rule " + at);
    assertEquals(text, scanner.yytext(), "text " + at);
    assertEquals(text.length(), scanner.yylength(), "length " + at);
    assertEquals(line, scanner.line(), "line " + at);
    assertEquals(column, scanner.column(), "column " + at);
  }

  private void assertEof() throws IOException {
    assertEquals(Negation2.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  private void assertTokens(int rule, String... texts) throws IOException {
    scan(String.join("", texts));
    int column = 0;
    for (String text : texts) {
      assertToken(rule, text, 0, column);
      column += text.length();
    }
    assertEof();
  }

  private void assertOnlyFallback(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1), 0, i);
    }
    assertEof();
  }

  // --- intersection !(!{X}|!{Y})

  @Test
  public void inBothMacros() throws Exception {
    assertTokens(INTERSECTION, "ab");
    assertTokens(INTERSECTION, "aab");
    assertTokens(INTERSECTION, "abab");
    assertTokens(INTERSECTION, "abbb");
  }

  /** Starts with {@code a}, but does not end in {@code b}. */
  @Test
  public void inFirstMacroOnly() throws Exception {
    assertOnlyFallback("a");
    assertOnlyFallback("aaa");
  }

  /** Ends in {@code b}, but does not start with {@code a}. */
  @Test
  public void inSecondMacroOnly() throws Exception {
    assertOnlyFallback("b");
    assertOnlyFallback("bbb");
  }

  @Test
  public void inNoMacro() throws Exception {
    assertOnlyFallback("ba");
    assertOnlyFallback("bbaa");
  }

  /** The match is the longest prefix that is in both macros. */
  @Test
  public void longestTextInBothMacros() throws Exception {
    scan("abbaa");
    assertToken(INTERSECTION, "abb", 0, 0);
    assertToken(OTHER, "a", 0, 3);
    assertToken(OTHER, "a", 0, 4);
    assertEof();
    scan("bbaba");
    assertToken(OTHER, "b", 0, 0);
    assertToken(OTHER, "b", 0, 1);
    assertToken(INTERSECTION, "ab", 0, 2);
    assertToken(OTHER, "a", 0, 4);
    assertEof();
  }

  // --- ![c] against [^c]

  /** {@code ![c]} is every text but {@code c}. */
  @Test
  public void negatedClass() throws Exception {
    assertOnlyFallback("(c)");
    assertTokens(NOT_C, "()");
    assertTokens(NOT_C, "(cc)");
    assertTokens(NOT_C, "(d)");
    assertTokens(NOT_C, "(dd)");
    assertTokens(NOT_C, "(cd)");
    assertTokens(NOT_C, "(\n)");
    // the negation is "c)", the rule has no other end than the last ")"
    assertTokens(NOT_C, "(c))");
  }

  /** {@code [^c]} is one character that is not {@code c}. */
  @Test
  public void classOfOtherCharacters() throws Exception {
    assertOnlyFallback("<c>");
    assertOnlyFallback("<>");
    assertOnlyFallback("<cc>");
    assertOnlyFallback("<dd>");
    assertOnlyFallback("<cd>");
    scan("<d>>");
    assertToken(NO_C, "<d>", 0, 0);
    assertToken(OTHER, ">", 0, 3);
    assertEof();
    assertTokens(NO_C, "<d>", "<)>", "<<>", "<>>");
    scan("<\n>");
    assertToken(NO_C, "<\n>", 0, 0);
    assertEof();
  }

  // --- ! of a macro that is an alternation

  /** <code>!{K}</code> is every text but {@code kl} and {@code m}. */
  @Test
  public void negatedMacro() throws Exception {
    assertOnlyFallback("[kl]");
    assertOnlyFallback("[m]");
    assertTokens(NOT_MACRO, "[]");
    assertTokens(NOT_MACRO, "[k]");
    assertTokens(NOT_MACRO, "[l]");
    assertTokens(NOT_MACRO, "[lk]");
    assertTokens(NOT_MACRO, "[klm]");
    assertTokens(NOT_MACRO, "[mkl]");
    assertTokens(NOT_MACRO, "[mm]");
    assertTokens(NOT_MACRO, "[klkl]");
    assertTokens(NOT_MACRO, "[x]");
  }

  // --- ~"*/"

  @Test
  public void upto() throws Exception {
    assertTokens(UPTO, "/**/");
    assertTokens(UPTO, "/***/");
    assertTokens(UPTO, "/*x*/");
    assertTokens(UPTO, "/*/*/");
    assertTokens(UPTO, "/* x\n * / */");
  }

  /** Up to the first end, and not further. */
  @Test
  public void uptoFirstEnd() throws Exception {
    scan("/*x*/x*/");
    assertToken(UPTO, "/*x*/", 0, 0);
    assertToken(OTHER, "x", 0, 5);
    assertToken(OTHER, "*", 0, 6);
    assertToken(OTHER, "/", 0, 7);
    assertEof();
    assertTokens(UPTO, "/**/", "/**/");
  }

  @Test
  public void uptoWithoutEnd() throws Exception {
    assertOnlyFallback("/*");
    assertOnlyFallback("/*/");
    assertOnlyFallback("/*x*");
    assertOnlyFallback("/*x* /");
  }

  /** The two other rules for the same comment (but different char than star). */
  @Test
  public void uptoByHandAndByNegation() throws Exception {
    assertTokens(UPTO_BY_HAND, "/++/", "/+++/", "/+x+/", "/+/+/");
    assertTokens(UPTO_BY_NEGATION, "/--/", "/---/", "/-x-/", "/-/-/");
    assertOnlyFallback("/+/");
    assertOnlyFallback("/-/");
  }

  /**
   * Scans the input and returns one string per token, in the form {@code rule:length}. The rule
   * number {@code commentRule} is written as {@code comment} instead, so that the results for the
   * three comment rules can be compared directly.
   */
  private List<String> tokens(String input, int commentRule) throws IOException {
    scan(input);
    List<String> result = new ArrayList<>();
    for (int rule = scanner.yylex(); rule != Negation2.YYEOF; rule = scanner.yylex()) {
      result.add((rule == commentRule ? "comment" : rule) + ":" + scanner.yylength());
    }
    return result;
  }

  /**
   * The three comment rules match the same texts, up to the * character. Test that for every text
   * of up to 7 characters out of {@code / * x} and line feed after the start of a comment, the
   * three rules produce the same tokens.
   */
  @Test
  public void uptoIsEquivalentToTheOtherRules() throws Exception {
    char[] alphabet = {'/', '*', 'x', '\n'};
    int maxLength = 7;
    int inputs = 0;
    int comments = 0;
    for (int length = 0; length <= maxLength; length++) {
      int count = 1 << (2 * length);
      for (int n = 0; n < count; n++) {
        StringBuilder text = new StringBuilder("/*");
        for (int i = 0; i < length; i++) {
          text.append(alphabet[(n >> (2 * i)) & 3]);
        }
        String input = text.toString();
        List<String> expected = tokens(input, UPTO);
        assertEquals(
            expected, tokens(input.replace('*', '+'), UPTO_BY_HAND), "by hand, for " + input);
        assertEquals(
            expected,
            tokens(input.replace('*', '-'), UPTO_BY_NEGATION),
            "by negation, for " + input);
        inputs++;
        if (expected.get(0).startsWith("comment")) {
          comments++;
        }
      }
    }
    // 4^0 + ... + 4^7 inputs
    assertEquals(21845, inputs);
    assertTrue(comments > 0 && comments < inputs, "inputs with and without a comment");
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }
}
