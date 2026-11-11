/*
 * Copyright (C) 2014-2020 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2020 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

// test: no-unused
package de.jflex.testcase.unused_warning;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

/** Test for the {@code --warn-unused} option. */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/unused_warning/unused.flex",
    sysout = "src/test/java/de/jflex/testcase/unused_warning/expected_unused_sysout.txt",
    warnUnused = true)
public class UnusedTest implements JFlexTest {}
