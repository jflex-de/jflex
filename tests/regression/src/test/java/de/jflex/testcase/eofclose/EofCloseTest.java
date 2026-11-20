/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eofclose;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Test for the {@code %eofclose} directive: the scanner closes its reader at the end of the input.
 * {@link EofNoCloseTest} is the test for the opposite ({@code %eofclose false}).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/eofclose/eofclose.flex",
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
public class EofCloseTest implements JFlexTest {

  static final String CONTENT = "bla\nblub\n\n";

  @Test
  public void closesReaderAtEof() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader(CONTENT);
    EofClose scanner = new EofClose(reader);
    assertEquals(EofClose.YYEOF, scanner.yylex());
    assertTrue(reader.isClosed(), "The scanner should close the reader at EOF");
    assertThrows(IOException.class, reader::read, "Reading from a closed reader");
  }

  @Test
  public void doesNotCloseReaderBeforeEof() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader(CONTENT);
    new EofClose(reader);
    assertFalse(reader.isClosed());
  }

  @Test
  public void scanningAgainAfterEof() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader(CONTENT);
    EofClose scanner = new EofClose(reader);
    assertEquals(EofClose.YYEOF, scanner.yylex());
    assertEquals(EofClose.YYEOF, scanner.yylex());
    assertTrue(reader.isClosed());
  }

  @Test
  public void closesReaderOfEmptyInput() throws Exception {
    CloseTrackingReader reader = new CloseTrackingReader("");
    assertEquals(EofClose.YYEOF, new EofClose(reader).yylex());
    assertTrue(reader.isClosed());
  }
}
