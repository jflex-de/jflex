/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eof;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Test for the {@code %eof} and {@code %eofthrow} directives together with an {@code <<EOF>>} rule,
 * see <a href="https://github.com/jflex-de/jflex/issues/743">#743</a>.
 *
 * <p>The {@code %eof} code of the spec contains a {@code throw} of a checked exception. Without the
 * {@code %eofthrow} code, the scanner would not compile.
 *
 * <p>The {@code %eof} code and the {@code <<EOF>>} action both add an entry to the list {@code
 * events} of the scanner when they run.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/eof/eof.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/eof/eof.sysout",
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]: 0 -> 1",
      "2 [a1]:",
      "a0: return \"line\";",
      "a1: return \"newline\";"
    },
    charClassesCount = 3,
    charClasses = {
      "{ [0-9][14-132][134-8231][8234-55295][57344-1114111] }",
      "{ [10] }",
      "{ [11-13][133][8232-8233][55296-57343] }"
    })
public class EofTest implements JFlexTest {

  /** {@code .+} */
  private static final String LINE = "line";

  /** {@code \n} */
  private static final String NEWLINE = "newline";

  /** {@code <<EOF>>} */
  private static final String EOF_RULE = "";

  /** Entry of the {@code %eof} code in the events of the scanner. */
  private static final String EOF_CODE = "EOF";

  /** Entry of the {@code <<EOF>>} action in the events of the scanner. */
  private static final String EOF_ACTION = "<<EOF>>";

  private Eof scanner;

  private void scan(String input) {
    scanner = new Eof(new StringReader(input));
  }

  private void assertToken(String rule, String text) throws IOException {
    assertEquals(rule, scanner.yylex(), "rule for " + text);
    assertEquals(text, scanner.yytext(), "text");
    assertEquals(text.length(), scanner.yylength(), "length of " + text);
    assertFalse(scanner.yyatEOF(), "at EOF after " + text);
    assertEquals(Collections.emptyList(), scanner.events, "events after " + text);
  }

  /** Checks that the next call of the scanner runs the {@code <<EOF>>} action. */
  private void assertEof(String... events) throws IOException {
    assertEquals(EOF_RULE, scanner.yylex());
    assertEquals(Arrays.asList(events), scanner.events);
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void eofCodeBeforeEofRule() throws Exception {
    scan("bla\nblub\n");
    assertFalse(scanner.yyatEOF());
    assertToken(LINE, "bla");
    assertToken(NEWLINE, "\n");
    assertToken(LINE, "blub");
    assertToken(NEWLINE, "\n");
    assertEof(EOF_CODE, EOF_ACTION);
  }

  /**
   * When the scanner is called again at the end of input, the {@code <<EOF>>} action runs each
   * time, but the {@code %eof} code runs "exactly once", as specified in the manual.
   */
  @Test
  public void eofCodeOnce() throws Exception {
    scan("bla\n");
    assertToken(LINE, "bla");
    assertToken(NEWLINE, "\n");
    assertEof(EOF_CODE, EOF_ACTION);
    assertEof(EOF_CODE, EOF_ACTION, EOF_ACTION);
    assertEof(EOF_CODE, EOF_ACTION, EOF_ACTION, EOF_ACTION);
  }

  /**
   * A match that ends at the end of input is not the end of file. EOF code runs only in the next
   * call.
   */
  @Test
  public void matchUpToEof() throws Exception {
    scan("bla");
    assertToken(LINE, "bla");
    assertEof(EOF_CODE, EOF_ACTION);
  }

  @Test
  public void emptyInput() throws Exception {
    scan("");
    assertFalse(scanner.yyatEOF());
    assertEquals(Collections.emptyList(), scanner.events);
    assertEof(EOF_CODE, EOF_ACTION);
    assertEof(EOF_CODE, EOF_ACTION, EOF_ACTION);
  }

  /** The text of the end of file match is empty. */
  @Test
  public void eofText() throws Exception {
    scan("bla");
    assertToken(LINE, "bla");
    assertEof(EOF_CODE, EOF_ACTION);
    assertEquals("", scanner.yytext());
    assertEquals(0, scanner.yylength());
  }

  /**
   * After {@code yyreset} the scanner is not at the end of input, and the {@code %eof} code runs
   * again, once, at the end of the new input.
   */
  @Test
  public void eofCodeAgainAfterReset() throws Exception {
    scan("bla");
    assertEquals(LINE, scanner.yylex());
    assertEof(EOF_CODE, EOF_ACTION);

    scanner.yyreset(new StringReader("blub\n"));
    assertFalse(scanner.yyatEOF());
    assertEquals(Arrays.asList(EOF_CODE, EOF_ACTION), scanner.events);

    assertEquals(LINE, scanner.yylex());
    assertEquals("blub", scanner.yytext());
    assertEquals(NEWLINE, scanner.yylex());
    assertEquals("\n", scanner.yytext());
    assertFalse(scanner.yyatEOF());
    assertEquals(Arrays.asList(EOF_CODE, EOF_ACTION), scanner.events);

    assertEof(EOF_CODE, EOF_ACTION, EOF_CODE, EOF_ACTION);
    assertEof(EOF_CODE, EOF_ACTION, EOF_CODE, EOF_ACTION, EOF_ACTION);
  }

  /**
   * {@code yyreset} before the end of input. Test that the {@code %eof} code has not run and runs
   * once.
   */
  @Test
  public void resetBeforeEof() throws Exception {
    scan("bla\nblub");
    assertToken(LINE, "bla");
    scanner.yyreset(new StringReader(""));
    assertFalse(scanner.yyatEOF());
    assertEof(EOF_CODE, EOF_ACTION);
    assertEof(EOF_CODE, EOF_ACTION, EOF_ACTION);
  }
}
