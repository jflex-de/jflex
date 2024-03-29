/*
 * Copyright (C) 2019 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.spoon_feed_reader;

import java.io.IOException;
import java.io.Reader;

/**
 * Wraps a reader to return at most the configured number of chars from {@link
 * #read(char[],int,int)}.
 */
public class SpoonFeedMaxCharsReaderWrapper extends Reader {
  private final Reader in;
  private final int maxChars;

  public SpoonFeedMaxCharsReaderWrapper(int maxChars, Reader in) {
    this.in = in;
    this.maxChars = maxChars;
  }

  @Override
  public void close() throws IOException {
    in.close();
  }

  /** Returns the configured number of chars if available. */
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return in.read(cbuf, off, Math.min(maxChars, len));
  }
}
