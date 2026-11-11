// test: ctorarg-f
/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.scanner.ScannerException;

/** Tests that JFlex refuses to generate a parser if its argument name is not a Java identifier. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ctorarg/ctor-bad-arg.flex",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class,
    sysout = "src/test/java/de/jflex/testcase/ctorarg/ctor-bad-arg.output")
public class CtorBadArgTest implements JFlexTest {}
