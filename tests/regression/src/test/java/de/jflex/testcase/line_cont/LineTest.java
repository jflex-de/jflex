// test: line

/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.line_cont;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;

@TestSpec(
    lex = "src/test/java/de/jflex/testcase/line_cont/line.flex",
    sysout = "src/test/java/de/jflex/testcase/line_cont/line-flex.sysout",
    dump = true)
public class LineTest implements JFlexTest {}
