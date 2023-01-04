// test: unicode-version-f
/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_version_f;

import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import jflex.exceptions.GeneratorException;
import org.junit.Test;
import org.junit.runner.RunWith;

/** Specifies invalid unicode version with %unicode directive */
@RunWith(JFlexTestRunner.class)
@TestSpec(
    lex = "javatests/de/jflex/testcase/unicode_version_f/unicode-version-f.flex",
    generatorThrows = GeneratorException.class,
    sysout = "javatests/de/jflex/testcase/unicode_version_f/unicode-version-f.output")
public class UnicodeInvalidVersionTest {

  @Test
  public void ok() {}
}
