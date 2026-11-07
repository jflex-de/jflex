/*
 * Copyright (C) 2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite.golden;

import com.google.common.io.Files;
import de.jflex.testing.diff.DiffOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.After;

public abstract class AbstractGoldenTest {

  private DiffOutputStream output;

  /** The {@code System.out} of the test environment, to restore after the test. */
  private PrintStream originalSysOut;

  protected void compareSystemOutWith(GoldenInOutFilePair golden) throws FileNotFoundException {
    // in-memory output comparison
    output = new DiffOutputStream(Files.newReader(golden.outputFile, StandardCharsets.UTF_8));
    if (originalSysOut == null) {
      originalSysOut = System.out;
    }
    System.setOut(new PrintStream(output));
  }

  @After
  public void checkOuputEntirelyGenerated() throws IOException {
    if (output == null) {
      // possible if there was no input, and we have a "scanner compiles" test.
      return;
    }
    System.setOut(originalSysOut);
    // Checks that all expected output has been printed on System.out
    output.close();
  }
}
