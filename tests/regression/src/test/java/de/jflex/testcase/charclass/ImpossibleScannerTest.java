/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.charclass;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;

/**
 * Test that an impossible macro throws a {@link GeneratorException} rather than a broken scanner.
 *
 * <p>See https://github.com/jflex-de/jflex/issues/106
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/charclass/char-range-failing.flex",
    quiet = true,
    output = {
      "",
      "Error in file \"src/test/java/de/jflex/testcase/charclass/char-range-failing.flex\" (line"
          + " 10): ",
      "Impossible character class range (end is less than start)",
      "BadRange = [b-a]",
      "            ^"
    },
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = Void.class)
public class ImpossibleScannerTest implements JFlexTest {}
