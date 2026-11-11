/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_codepoint_escapes;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import jflex.scanner.ScannerException;

/**
 * Check that generation fails when a codepoint specified in <code>\\u{H+}</code> format is greater
 * than the maximum codepoint for the Unicode version.
 */
@TestSpec(
    lex =
        "src/test/java/de/jflex/testcase/unicode_codepoint_escapes/UnicodeCodePointEscapes-f-3.flex",
    sysout =
        "src/test/java/de/jflex/testcase/unicode_codepoint_escapes/UnicodeCodePointEscapes-f-3-flex.output",
    generatorThrows = GeneratorException.class,
    generatorThrowableCause = ScannerException.class)
public class UnicodeCodepointEscapes_failure3 implements JFlexTest {}
