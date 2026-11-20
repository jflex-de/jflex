/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_bug;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Test against <a href="https://github.com/jflex-de/jflex/issues/81">#81 {@code char[] ZZ_CMAP} is
 * incorrect</a>
 *
 * <p>Generated CclBug.java does not compile, because of missing ",".
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_bug/ccl-bug.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 14,
    charClassesCount = 39,
    sameCharClass = {"\t\n", "09", "()*+-/", "BEGJZbegjz", "#\r\u00FF"},
    distinctCharClasses = {"acdfhimnortACDFHIMNORT", "b0_$#", "<=>!", "'\\ \t", "{}.("})
public class CclBugTest implements JFlexTest {

  /** Runs the scanner and returns what it prints */
  private static String unmatched(String input) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      CclBug scanner = new CclBug(new StringReader(input));
      assertEquals(CclBug.YYEOF, scanner.yylex());
    }
    return out.toString("UTF-8");
  }

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    new CclBug(new StringReader(""));
  }

  /** Input that the rules match completely. */
  @Test
  public void matchesExpression() throws Exception {
    assertEquals(
        "",
        unmatched(
            "x1 == 'it\\'s' and not $y_2 <= 2.5 OR {a MATCH b}\n\t(c+d)*e/f-10 != g in h >= i"));
  }

  /** The characters of the first character class are in no rule. */
  @Test
  public void doesNotMatchOtherCharacters() throws Exception {
    assertEquals("#&\u00FF", unmatched("a # b & c \u00FF"));
  }
}
