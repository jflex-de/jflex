/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include_in_rules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test for the order of rules across a {@code %include} in the rules section. The rules of the
 * included file are at the position of the {@code %include}. If more than one rule matches the
 * longest text, the first rule in that order should take precedence.
 *
 * <p>The spec includes {@code order-rules.inc.jflex} between its rules {@code "abc"} and {@code
 * "def"}. The included rule {@code [ad][be]{LETTER}} overlaps both. The included rule {@code "abc"}
 * can never match, and neither can {@code "def"}. Check for warning and correct line number
 * reporting.
 */
@Disabled("TODO: JFlex bug. With %include, rule priority is the line number in the included file")
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/include_in_rules/IncludeInRulesOrder.flex",
    // never-match warnings for the included "abc" and for "def"
    sysout = "src/test/java/de/jflex/testcase/include_in_rules/IncludeInRulesOrder.sysout",
    quiet = true,
    minimizedDfaStatesCount = 8,
    charClassesCount = 8,
    charClasses = {
      "{ [0-'`']['{'-1114111] }",
      "{ ['a'] }",
      "{ ['b'] }",
      "{ ['c'] }",
      "{ ['d'] }",
      "{ ['e'] }",
      "{ ['f'] }",
      "{ ['g'-'z'] }"
    })
public class IncludeInRulesOrderTest implements JFlexTest {

  /** {@code "abc"}, before the include */
  private static final int ABC = 1;

  /** {@code [ad][be]{LETTER}}, included */
  private static final int OVERLAP = 3;

  /** {@code [^]}, after the include */
  private static final int OTHER = 0;

  private IncludeInRulesOrderScanner scanner;

  private void scan(String input) {
    scanner = new IncludeInRulesOrderScanner(new StringReader(input));
  }

  /** Checks the next token: the matched rule and text. */
  private void assertToken(int rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext());
    assertEquals(text.length(), scanner.yylength());
  }

  private void assertEof() throws IOException {
    assertEquals(IncludeInRulesOrderScanner.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  /** The rule before the include takes precedence over the included rules. */
  @Test
  public void ruleBeforeIncludeWins() throws Exception {
    scan("abc");
    assertToken(ABC, "abc");
    assertEof();
  }

  /** The included rule takes precedence over the rule after the include. */
  @Test
  public void includedRuleWinsOverLaterRule() throws Exception {
    scan("def");
    assertToken(OVERLAP, "def");
    assertEof();
  }

  @Test
  public void onlyIncludedRuleMatches() throws Exception {
    scan("abdaecdbzdea");
    assertToken(OVERLAP, "abd");
    assertToken(OVERLAP, "aec");
    assertToken(OVERLAP, "dbz");
    assertToken(OVERLAP, "dea");
    assertEof();
  }

  @Test
  public void fallback() throws Exception {
    scan("acb");
    assertToken(OTHER, "a");
    assertToken(OTHER, "c");
    assertToken(OTHER, "b");
    assertEof();
  }
}
