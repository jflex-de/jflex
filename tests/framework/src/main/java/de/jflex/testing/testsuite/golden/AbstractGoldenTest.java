/*
 * Copyright (C) 2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.testsuite.golden;

import com.google.common.io.Files;
import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.diff.DiffOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;

public abstract class AbstractGoldenTest {

  private DiffOutputStream output;

  /** Capture System.out to isolate concurrent thread output. */
  private SystemOutCapture capture;

  protected void compareSystemOutWith(GoldenInOutFilePair golden) throws IOException {
    // Check the output of a previous comparison in the same test before replacing it.
    checkOuputEntirelyGenerated();
    // in-memory output comparison
    output = new DiffOutputStream(Files.newReader(golden.outputFile, StandardCharsets.UTF_8));
    // The golden file is UTF-8, so print in UTF-8 independently of the platform charset.
    capture = SystemOutCapture.start(new PrintStream(output, true, "UTF-8"));
  }

  @AfterEach
  public void checkOuputEntirelyGenerated() throws IOException {
    if (output == null) {
      // possible if there was no input, and we have a "scanner compiles" test.
      return;
    }
    capture.close();
    // Checks that all expected output has been printed on System.out
    output.close();
  }
}
