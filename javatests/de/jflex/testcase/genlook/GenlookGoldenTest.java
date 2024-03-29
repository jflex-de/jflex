// test: genlook

/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * testing general lookahead expression case (e.g. a+a/a+)
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class GenlookGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code genlook.flex} specification. */
  private final ScannerFactory<Genlook> scannerFactory = ScannerFactory.of(Genlook::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/genlook");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "genlook-0.input"),
            new File(testRuntimeDir, "genlook-0.output"));
    compareSystemOutWith(golden);

    Genlook scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
