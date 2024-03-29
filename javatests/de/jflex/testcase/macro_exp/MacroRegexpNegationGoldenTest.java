// test: macro
/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * bug <a href="https://github.com/jflex-de/jflex/issues/57">#57 [Macro error on regexp negation
 * (!)]</a>
 *
 * <p>wrong warnings for negated macros
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class MacroRegexpNegationGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code macro.flex} specification. */
  private final ScannerFactory<Macro> scannerFactory = ScannerFactory.of(Macro::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
