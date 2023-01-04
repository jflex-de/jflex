/*
 * Copyright (C) 2014-2020 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2008-2020 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2017-2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

// test: no-unused
package de.jflex.testcase.unused_warning;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Test for the {@code --no-warn-unused} option. */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/unused_warning/no-unused.flex",
    sysout = "javatests/de/jflex/testcase/unused_warning/expected_nounused_sysout.txt",
    warnUnused = false)
public class NoUnusedTest {

  @Test
  public void ok() {}
}
