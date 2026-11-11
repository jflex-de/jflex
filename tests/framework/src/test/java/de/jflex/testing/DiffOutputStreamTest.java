/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.diff.DiffOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Test;

/** Test for {@link DiffOutputStreamTest}. */
public class DiffOutputStreamTest {

  @Test
  public void testOneLine_equal() throws Exception {
    String in = "Hello world!\n";
    @SuppressWarnings("RedundantStringConstructorCall")
    String out = new String(in);
    DiffOutputStream differ = diff(in, out);
    assertThat(differ.remainingContent()).isEmpty();
  }

  @Test
  public void testOneLine_differ_inHasMore() throws Exception {
    String in = "Hello world!\n";
    String out = "Hello\n";
    assertThrows(
        AssertionError.class,
        () -> diff(in, out),
        DiffOutputStream.class + " throws an exception when the content differs");
  }

  @Test
  public void testOneLine_differs_outHasMore() throws Exception {
    String in = "Hello!\n";
    String out = "Hello world!\n";
    assertThrows(
        AssertionError.class,
        () -> diff(in, out),
        DiffOutputStream.class + " throws an exception when the content differs");
  }

  @Test
  public void close_allContentOutput_closesInput() throws Exception {
    TrackedReader in = new TrackedReader("Hello world!\n");
    try (DiffOutputStream differ = new DiffOutputStream(in)) {
      differ.write("Hello world!\n".getBytes("UTF-8"));
    }
    assertThat(in.closed).isTrue();
  }

  @Test
  public void close_contentRemaining_failsAndClosesInput() throws Exception {
    TrackedReader in = new TrackedReader("Hello\nworld!\n");
    assertThrows(
        AssertionError.class,
        () -> {
          try (DiffOutputStream differ = new DiffOutputStream(in)) {
            differ.write("Hello\n".getBytes("UTF-8"));
          }
        },
        DiffOutputStream.class + " throws an exception when content was not output");
    assertThat(in.closed).isTrue();
  }

  @Test
  public void close_twice_hasNoEffect() throws Exception {
    try (DiffOutputStream differ = new DiffOutputStream(new TrackedReader(""))) {
      differ.close();
    }
  }

  @Test
  public void closeWithoutCheck_contentRemaining_closesInput() throws Exception {
    TrackedReader in = new TrackedReader("Hello world!\n");
    try (DiffOutputStream differ = new DiffOutputStream(in)) {
      differ.closeWithoutCheck();
    }
    assertThat(in.closed).isTrue();
  }

  /** A reader that records whether it was closed. */
  private static class TrackedReader extends StringReader {
    boolean closed;

    TrackedReader(String content) {
      super(content);
    }

    @Override
    public void close() {
      closed = true;
      super.close();
    }
  }

  private static DiffOutputStream diff(String in, String out) throws IOException {
    DiffOutputStream diffStream = new DiffOutputStream(new StringReader(in));
    try {
      diffStream.write(out.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException impossible) {
    }
    diffStream.flush();
    return diffStream;
  }
}
