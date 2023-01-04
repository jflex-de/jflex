/*
 * Copyright 2022, Gerwin Klein <lsf@jflex.de>.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_caseless;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * Test for issue <a href"https://github.com/jflex-de/jflex/issues/974">#974 Unexpected exception
 * encountered in JFlex</a>.
 *
 * <p>Tests that the generated scanner can handle case-insensitive character classes with characters
 * outside the current input char set.
 */
public class Ccl_caselessGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code ccl_caseless.flex} specification. */
  private final ScannerFactory<Ccl_caseless> scannerFactory = ScannerFactory.of(Ccl_caseless::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/ccl_caseless");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "ccl_caseless-0.input"),
            new File(testRuntimeDir, "ccl_caseless-0.output"));
    compareSystemOutWith(golden);

    Ccl_caseless scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
