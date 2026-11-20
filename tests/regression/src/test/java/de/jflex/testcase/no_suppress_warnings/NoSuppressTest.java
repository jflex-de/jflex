/*
 * Copyright 2023, Gerwin Klein.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.no_suppress_warnings;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.jupiter.api.Test;

/**
 * Tests {@code %no_suppress_warnings}. The spec has its own {@code @SuppressWarnings} annotation
 * for the scanner class, so the scanner does not compile if JFlex emits one as well.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/no_suppress_warnings/no_suppress.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 3, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1-2 -> 2",
      "1 [a0]:",
      "2 [a1]:",
      "a0: return 0;",
      "a1: return 1;"
    },
    charClassesCount = 3,
    charClasses = {"{ [0-'`']['c'-1114111] }", "{ ['a'] }", "{ ['b'] }"})
public class NoSuppressTest implements JFlexTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void ok() throws Exception {}
}
