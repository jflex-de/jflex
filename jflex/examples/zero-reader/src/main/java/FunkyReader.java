/*
 * JFlex ZeroReader example
 *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.IOException;
import java.io.Reader;

/**
 * Reader that returns 0 chars read every once in a while.
 *
 * <p>This is a demonstration of a problematic Reader that does not implement the Reader
 * specification correctly. Do not use.
 */
public class FunkyReader extends Reader {

  boolean do_zero;
  Reader reader;

  public FunkyReader(Reader r) {
    this.reader = r;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    if (!do_zero) {
      do_zero = true;
      return reader.read(cbuf, off, Math.min(10, len));
    } else {
      do_zero = false;
      return 0;
    }
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
