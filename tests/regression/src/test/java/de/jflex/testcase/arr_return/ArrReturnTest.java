/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.arr_return;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/** Tests a scan method that returns an array ({@code %type int []}). */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/arr_return/arr.flex",
    sysout = "src/test/java/de/jflex/testcase/arr_return/arr-flex.output",
    minimizedDfaStatesCount = 5,
    charClassesCount = 4,
    charClasses = {
      "{ [0-'a']['c'-'k']['m'-'t']['v'-1114111] }",
      "{ ['b'] }",
      "{ ['l'] }",
      "{ ['u'] }"
    },
    sameCharClass = {"az\uD83D\uDE00", "bb"},
    distinctCharClasses = {"blua"})
public class ArrReturnTest implements JFlexTest {

  /** The scan method has the array type of {@code %type int []} as its return type. */
  @Test
  public void returnType() throws Exception {
    assertEquals(int[].class, Arr.class.getDeclaredMethod("yylex").getReturnType());
  }

  @Test
  public void returnsArrays() throws Exception {
    Arr scanner = new Arr(new StringReader("blubblub"));
    int[] first = scanner.yylex();
    assertArrayEquals(new int[2], first);
    assertEquals("blub", scanner.yytext());
    int[] second = scanner.yylex();
    assertArrayEquals(new int[2], second);
    assertNotSame(first, second);
    assertNull(scanner.yylex(), "null at the end of input");
  }
}
