/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include_in_rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/116">#116 (enable %include in rules
 * section)</a>: the spec includes {@code extra-jflex-rules.inc.jflex} between its rules {@code
 * "abc"} and {@code "def"}.
 *
 * <p>The included rule {@code "de"{LETTER}} uses a macro of the including spec and is before {@code
 * "def"}, which therefore can never match. JFlex has to report this for line 23 of the including
 * spec, i.e. it has to count lines there correctly after the include.
 *
 * <p>The order between an included rule and an <em>earlier</em> rule of the including spec is in
 * {@link IncludeInRulesOrderTest}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/include_in_rules/IncludeInRules.flex",
    // never-match warning for "def"
    sysout = "src/test/java/de/jflex/testcase/include_in_rules/IncludeInRules.sysout",
    minimizedDfaStatesCount = 11,
    minimizedDfa = {
      "states 11, inputs 11, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2-3 -> 1; 4 -> 3; 5-7 -> 1; 8 -> 4; 9-10 -> 1",
      "1 [a0]:",
      "2 [a0]: 2 -> 5",
      "3 [a0]: 5 -> 6",
      "4 [a0]: 9 -> 7",
      "5: 3 -> 8",
      "6: 1-10 -> 9",
      "7: 10 -> 10",
      "8 [a1]:",
      "9 [a2]:",
      "10 [a3]:",
      "a0: return 0;",
      "a1: return 1;",
      "a2: return 3;",
      "a3: return 2;"
    },
    charClassesCount = 11,
    charClasses = {
      "{ [0-'`']['{'-1114111] }",
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['d'] }",
      "{ ['e'] }",
      "{ ['f'] }",
      "{ ['g'-'w'] }",
      "{ ['x'] }",
      "{ ['y'] }",
      "{ ['z'] }"
    })
public class IncludeInRulesTest implements JFlexTest {

  /** {@code "abc"}, before the include */
  private static final int ABC = 1;

  /** {@code "xyz"}, included */
  private static final int XYZ = 2;

  /** {@code "de"{LETTER}}, included, with the macro from the including spec */
  private static final int DE_LETTER = 3;

  /** {@code [^]}, after the include */
  private static final int OTHER = 0;

  private IncludeInRulesScanner scanner;

  private void scan(String input) {
    scanner = new IncludeInRulesScanner(new StringReader(input));
  }

  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
    assertEquals(text.length(), scanner.yylength());
  }

  private void assertEof() throws IOException {
    assertEquals(IncludeInRulesScanner.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** Checks that the scanner matches every character of the input with the fallback rule. */
  private void assertNoMatch(String input) throws IOException {
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertToken(OTHER, input.substring(i, i + 1));
    }
    assertEof();
  }

  /** Rules before the include, in the included file and after the include are all there. */
  @Test
  public void rulesBeforeInAndAfterInclude() throws Exception {
    scan("xyz def abc");
    assertToken(XYZ, "xyz");
    assertToken(OTHER, " ");
    assertToken(DE_LETTER, "def");
    assertToken(OTHER, " ");
    assertToken(ABC, "abc");
    assertEof();
  }

  @Test
  public void includedRuleBeforeLaterRule() throws Exception {
    scan("def");
    assertToken(DE_LETTER, "def");
    assertEof();
  }

  /** {@code LETTER = [a-z]} is defined in the including spec and used in the included rule. */
  @Test
  public void macroInIncludedRule() throws Exception {
    scan("deadezdeg");
    assertToken(DE_LETTER, "dea");
    assertToken(DE_LETTER, "dez");
    assertToken(DE_LETTER, "deg");
    assertEof();
  }

  @Test
  public void notALetter() throws Exception {
    assertNoMatch("deF");
    assertNoMatch("de1");
    assertNoMatch("de{");
    assertNoMatch("de`");
  }

  @Test
  public void prefixesOnly() throws Exception {
    assertNoMatch("ab");
    assertNoMatch("xy");
    assertNoMatch("de");
    assertNoMatch("abxyde");
  }

  @Test
  public void adjacentMatches() throws Exception {
    scan("abcxyzdexabcd");
    assertToken(ABC, "abc");
    assertToken(XYZ, "xyz");
    assertToken(DE_LETTER, "dex");
    assertToken(ABC, "abc");
    assertToken(OTHER, "d");
    assertEof();
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertEof();
  }

  /** The {@code %debug} trace. */
  @Test
  public void debugTrace() throws Exception {
    ByteArrayOutputStream trace = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(trace, true, "UTF-8"))) {
      scan("xyz def abc");
      while (scanner.yylex() != IncludeInRulesScanner.YYEOF) {}
    }
    String expected =
        String.join(
            System.lineSeparator(),
            "match: --xyz--",
            "action [6] { return 2; }",
            "match: -- --",
            "action [25] { return 0; }",
            "match: --def--",
            "action [7] { return 3; }",
            "match: -- --",
            "action [25] { return 0; }",
            "match: --abc--",
            "action [21] { return 1; }",
            "match: <<EOF>>",
            "action [27] { return -1; }",
            "");
    assertEquals(expected, trace.toString("UTF-8"));
  }
}
