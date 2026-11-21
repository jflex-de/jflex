/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

/**
 * The negation operator {@code !} with a long expression (48k characters).
 *
 * <p>The generator used to estimate the number of NFA states for {@code !r} as the square of the
 * number for {@code r}. For spec of the form here the square can be larger than an {@code int}, and
 * the generator would fail with a {@code NegativeArraySizeException}.
 *
 * <p>Only checks the generator. The scanner is not part of the test sources.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/negation/negation_large.flex",
    noOutput = true,
    quiet = true,
    // one state per character of the string, plus 5 more
    minimizedDfaStatesCount = 48005,
    // one class for each of the 10 characters in the spec, and one for all others
    charClassesCount = 11,
    distinctCharClasses = {"abcdefgh<>x"},
    sameCharClass = {"x\n\u0000i\uFFFF😀"})
public class NegationLargeTest implements JFlexTest {}
