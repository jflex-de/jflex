/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex ZeroReader wrapper                                                *
 *                                                                         *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.io.IOException;
import java.io.Reader;

/**
 * A Reader wrapper that guards against Readers that sometimes return 0 characters instead of
 * blocking.
 *
 * <p>If 0 character returns are rare, efficiency loss will be minimal.
 */
public class ZeroReader extends Reader {

  /** The underlying Reader that is being wrapped. */
  private Reader reader;

  /**
   * A new ZeroReader wrapper for a Reader that does not always block appropriately in
   * read(char[],int,int).
   *
   * @param reader the original Reader to wrap
   */
  public ZeroReader(Reader reader) {
    this.reader = reader;
  }

  /**
   * Read len characters from the underlying Reader into the buffer cbuf at position off. Blocks
   * until input is available.
   *
   * <p>Relies on the method char read() of the underlying Reader to block until at least one
   * character is available.
   *
   * @param buf the buffer to write into
   * @param off the offset from where to write
   * @param len the maximum number of characters to write
   * @return -1 for end of stream, number of characters read otherwise. Returns 0 if and only if len
   *     is less than or equal to 0.
   * @throws IOException if the underlying reader throws one or if the offset is outside the
   *     provided buffer.
   */
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    int n = reader.read(cbuf, off, len);

    if (n != 0 || len <= 0) {
      return n;
    } else {
      if (off < cbuf.length) {
        // Returns the character read, as an integer in the range 0 to 65535
        // (0x00-0xffff), or -1 if the end of the stream has been reached
        int c = reader.read();
        if (c == -1) return -1;
        cbuf[off] = (char) c;
        return 1;
      } else throw new IOException("Offset outside buffer");
    }
  }

  /**
   * Closes the underlying Reader.
   *
   * @throws IOException if the underlying Reader does.
   */
  @Override
  public void close() throws IOException {
    reader.close();
  }
}
