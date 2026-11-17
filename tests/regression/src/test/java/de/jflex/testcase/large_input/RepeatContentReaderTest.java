/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import org.junit.jupiter.api.Test;

/** Test {@link RepeatContentReader}. */
public class RepeatContentReaderTest {
  @Test
  public void read_sizeLessThanContent() throws IOException {
    int length = 11;
    Reader reader = new RepeatContentReader(length, "The content is larger than the declared size");
    String read = readAll(reader);
    assertEquals(length, read.length());
    assertEquals("The content", read);
  }

  @Test
  public void read_sizeMoreThanContent() throws IOException {
    int length = 25;
    Reader reader = new RepeatContentReader(length, "foo bar!");
    String read = readAll(reader);
    assertEquals(length, read.length());
    assertEquals("foo bar!foo bar!foo bar!f", read);
  }

  @Test
  public void read_offset() throws IOException {
    try (Reader reader = new RepeatContentReader(25, "abc")) {
      char[] cbuf = new char[7];
      int read = reader.read(cbuf, 1, 2);
      assertEquals(2, read);
      assertEquals("\0ab\0\0\0\0", new String(cbuf));
    }
  }

  @Test
  public void read_offsetLoop() throws IOException {
    try (Reader reader = new RepeatContentReader(25, "abc")) {
      char[] cbuf = new char[7];
      int read = reader.read(cbuf, 1, 7 - 1);
      assertEquals(6, read);
      assertEquals("\0abcabc", new String(cbuf));
    }
  }

  private static String readAll(Reader reader) throws IOException {
    StringBuilder result = new StringBuilder();
    char[] buffer = new char[1024];
    int n;
    while ((n = reader.read(buffer, 0, buffer.length)) != -1) {
      result.append(buffer, 0, n);
    }
    return result.toString();
  }
}
