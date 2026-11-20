/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eofclose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.jupiter.api.Test;

/**
 * Test for {@code %eofclose false}. The spec has {@code %eofclose} first, so that the scanner
 * closes its reader if JFlex ignores {@code %eofclose false}. Not closing is also the default.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/eofclose/eofnoclose.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]: 0 -> 1",
      "2 [a1]:",
      "a0: /* ignore */",
      "a1: /* that too */"
    },
    charClassesCount = 3,
    charClasses = {
      "{ [0-9][14-132][134-8231][8234-55295][57344-1114111] }",
      "{ [10] }",
      "{ [11-13][133][8232-8233][55296-57343] }"
    })
public class EofNoCloseTest implements JFlexTest {

  @Test
  public void doesNotCloseReaderAtEof() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader(EofCloseTest.CONTENT);
    EofNoClose scanner = new EofNoClose(reader);
    assertEquals(EofNoClose.YYEOF, scanner.yylex());
    assertFalse(reader.isClosed(), "The scanner should not close the reader at EOF");
    assertEquals(-1, reader.read(), "The reader is still usable, and has no more content");
  }

  @Test
  public void doesNotCloseReaderOfEmptyInput() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader("");
    assertEquals(EofNoClose.YYEOF, new EofNoClose(reader).yylex());
    assertFalse(reader.isClosed());
  }
}
