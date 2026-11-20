/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eofclose;

import java.io.StringReader;

/** A reader that knows if it was closed. */
class CloseTrackingReader extends StringReader {

  private boolean closed;

  CloseTrackingReader(String content) {
    super(content);
  }

  boolean isClosed() {
    return closed;
  }

  @Override
  public void close() {
    closed = true;
    super.close();
  }
}
