// test: negation
/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.negation;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * Test for bug #567 (r|!r does not always match everything).
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class NegationGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code negation.flex} specification. */
  private final ScannerFactory<Negation> scannerFactory = ScannerFactory.of(Negation::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/negation");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "negation-0.input"),
            new File(testRuntimeDir, "negation-0.output"));
    compareSystemOutWith(golden);

    Negation scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  @Test
  public void goldenTest1() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "negation-1.input"),
            new File(testRuntimeDir, "negation-1.output"));
    compareSystemOutWith(golden);

    Negation scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
