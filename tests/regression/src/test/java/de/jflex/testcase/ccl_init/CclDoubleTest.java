/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_init;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.scanner.ScannerException;

/**
 * Test that double char class declaration fails to generate.
 *
 * <p>See https://github.com/jflex-de/jflex/issues/986
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ccl_init/ccl2.flex",
    quiet = true,
    output = {
      "",
      "Error in file \"src/test/java/de/jflex/testcase/ccl_init/ccl2.flex\" (line 20): ",
      "Character set already declared.",
      "%unicode"
    },
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class)
public class CclDoubleTest implements JFlexTest {}
