/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_16;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests {@code %16bit} with the rule {@code [^]}.
 *
 * <p>Check for previous bug where the scanner on input with an {@link
 * ArrayIndexOutOfBoundsException}, because of an overflow in {@code ZZ_PACKED_CMAP} (using {@code
 * \u10000}).
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_16/ccl.flex",
    // no warnings
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 1, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1",
      "1 [a0]:",
      "a0: return 1;"
    },
    charClassesCount = 1,
    charClasses = {"{ [0-1114111] }"})
public class Ccl16Test implements JFlexTest {

  private CclScanner scanner;

  private void scan(String input) {
    scanner = new CclScanner(new StringReader(input));
  }

  /** Asserts that the next token is a match of {@code [^]} with the text. */
  private void assertAny(String text) throws IOException {
    assertEquals(1, scanner.yylex(), "rule for " + hex(text));
    assertEquals(text, scanner.yytext());
  }

  private void assertEof() throws IOException {
    assertEquals(CclScanner.YYEOF, scanner.yylex());
    assertTrue(scanner.yyatEOF());
  }

  private static String hex(String text) {
    StringBuilder result = new StringBuilder();
    for (char c : text.toCharArray()) {
      result.append(String.format("\\u%04X", (int) c));
    }
    return result.toString();
  }

  /** The input of the test when the bug was found. */
  @Test
  public void ok() throws Exception {
    String input = "some content\n" + "ignored anyway\n";
    scan(input);
    for (int i = 0; i < input.length(); i++) {
      assertAny(input.substring(i, i + 1));
    }
    assertEof();
  }

  /** Every {@code char} that is not a surrogate */
  @Test
  public void everyBmpChar() throws Exception {
    StringBuilder input = new StringBuilder();
    for (char c = 0; c < Character.MIN_SURROGATE; c++) {
      input.append(c);
    }
    for (int c = Character.MAX_SURROGATE + 1; c <= Character.MAX_VALUE; c++) {
      input.append((char) c);
    }
    scan(input.toString());
    for (int i = 0; i < input.length(); i++) {
      assertAny(input.substring(i, i + 1));
    }
    assertEof();
  }

  /** {@code [^]} matches unpaired surrogates. */
  @Test
  public void unpairedSurrogates() throws Exception {
    for (int c = Character.MIN_SURROGATE; c <= Character.MAX_SURROGATE; c++) {
      String surrogate = String.valueOf((char) c);
      scan(surrogate);
      assertAny(surrogate);
      assertEof();
    }
  }

  /** A supplementary character is one match of two {@code char}s. */
  @Test
  public void supplementaryCharacters() throws Exception {
    String first = new String(Character.toChars(Character.MIN_SUPPLEMENTARY_CODE_POINT));
    String last = new String(Character.toChars(Character.MAX_CODE_POINT));
    scan(first + "a" + last);
    assertAny(first);
    assertAny("a");
    assertAny(last);
    assertEof();
  }
}
