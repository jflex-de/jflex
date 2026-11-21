/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

/**
 * The negation operator {@code !} nested 30 times.
 *
 * <p>The estimate of the number of NFA states must not multiply with each negation. With twice the
 * estimate of the content, 22 nested negations would take more than 1 GB for an NFA of 154 states,
 * and 30 would fail with a {@code NegativeArraySizeException}.
 *
 * <p>Only checks the generator. The scanner is not part of the test sources.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/negation/negation_nested.flex",
    noOutput = true,
    quiet = true,
    // the same as for "<" "ab" ">"
    minimizedDfaStatesCount = 6,
    minimizedDfa = {
      "states 6, inputs 5, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2; 2-4 -> 1",
      "1 [a0]:",
      "2 [a0]: 3 -> 3",
      "3: 4 -> 4",
      "4: 2 -> 5",
      "5 [a1]:",
      "a0: return 0;",
      "a1: return 1;"
    },
    charClassesCount = 5,
    charClasses = {
      "{ [0-';']['=']['?'-'`']['c'-1114111] }",
      "{ ['<'] }",
      "{ ['>'] }",
      "{ ['a'] }",
      "{ ['b'] }"
    })
public class NegationNestedTest implements JFlexTest {}
