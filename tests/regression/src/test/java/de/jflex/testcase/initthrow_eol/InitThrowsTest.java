// test: initthrow

/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.initthrow_eol;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Regression test against bug <a href="https://github.com/jflex-de/jflex/issues/52">#52 single-line
 * %initthrow option fails</a> (for extra space before eol)
 */
public class InitThrowsTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    new Initthrow(new StringReader(""));
  }

  /**
   * Tests that the scanner constrcutor throws an UnsupportedOperationException if the reader is not
   * ready.
   */
  @Test
  public void canInstantiateScanner_throwsInInit() throws Exception {
    Reader reader = new StringReader("");
    reader.close();
    assertThrows(IOException.class, () -> new Initthrow(reader));
  }
}
