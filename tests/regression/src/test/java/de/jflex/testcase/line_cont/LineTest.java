// test: line

/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.line_cont;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import jflex.l10n.ErrorMessages;

@TestSpec(
    lex = "src/test/java/de/jflex/testcase/line_cont/line.flex",
    warnings = {
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 25),
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 25),
      @Warn(value = ErrorMessages.EMPTY_MATCH, line = 28)
    },
    sysout = "src/test/java/de/jflex/testcase/line_cont/line-flex.sysout",
    dump = true)
public class LineTest implements JFlexTest {}
