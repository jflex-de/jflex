/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jlex;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

/** Tests that the generator throws a warning about "hello" being swallowed by "[a-z]+". */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/caseless_jlex/caseless.flex",
    sysout = "src/test/java/de/jflex/testcase/caseless_jlex/caseless.sysout",
    quiet = true,
    jlexCompat = true)
public class GenerationTest implements JFlexTest {}
