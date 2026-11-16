/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_max_codepoint;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

/**
 * Test {@code %ignorecase} with {@code [^]} in JLex mode ({@code --jlex}), for a Unicode version
 * with the maximum code point U+FFFF.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/caseless_max_codepoint/caseless-any-jlex.flex",
    quiet = true,
    jlexCompat = true,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]:",
      "a0: return 0;",
      "a1: return 1;"
    },
    charClasses = {"{ [0-'J']['L'-'j']['l'-65535] }", "{ ['K']['k'] }"})
public class CaselessAnyJlexTest implements JFlexTest {}
