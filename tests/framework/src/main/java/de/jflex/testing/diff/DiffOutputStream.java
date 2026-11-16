/*
 * Copyright (C) 2018-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.diff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * An output stream that asserts that everything written to it is equal to the expected content.
 *
 * <p>The output is compared line by line as it is written. Encoding is expected to be UTF-8. {@link
 * #close()} compares an unterminated last line and checks that no expected content is missing. This
 * means the comparison is only complete once the stream is closed.
 *
 * <p>The output is not saved anywhere.
 */
public class DiffOutputStream extends OutputStream {

  /** The maximal number of characters of missing expected content to report. */
  private static final int MAX_REPORTED_MISSING_CHARS = 140;

  /** The golden content this OutputStream will compare against. */
  private final BufferedReader in;

  /** The bytes of the current output line, without line separator. */
  private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

  /** The number of the current line being compared, starting at 1. */
  private int line = 1;

  /**
   * Handles assertion failures when the content differs. By default, just throws the {@link
   * AssertionError}.
   */
  private ComparisonFailureHandler comparisonFailureHandler =
      failure -> {
        throw failure;
      };

  /** Whether the golden input has been closed. */
  private boolean closed;

  public DiffOutputStream(Reader in) {
    this(new BufferedReader(in));
  }

  @SuppressWarnings("WeakerAccess")
  public DiffOutputStream(BufferedReader in) {
    this.in = in;
  }

  @Override
  public void write(int b) throws IOException {
    if (closed) {
      throw new IOException("Stream closed");
    }
    if ((byte) b == '\n') {
      compareLine(true);
    } else {
      buf.write(b);
    }
  }

  /**
   * Compares the current output line with the next expected line and advances to the next line.
   *
   * @param terminated whether the output line ends in a newline
   */
  private void compareLine(boolean terminated) throws IOException {
    String actual = new String(buf.toByteArray(), StandardCharsets.UTF_8);
    // Advance first, so that the state is consistent if the failure handler throws.
    buf.reset();
    int lineNumber = line++;

    StringBuilder expected = new StringBuilder();
    boolean expectedTerminated = readExpectedLine(expected);
    if (!expectedTerminated && expected.length() == 0) {
      report(
          () ->
              fail(
                  "Unexpected output after the end of the expected content on line "
                      + lineNumber
                      + ": <"
                      + actual
                      + ">"));
    } else if (!actual.equals(expected.toString())) {
      report(
          () ->
              assertEquals(
                  expected.toString(), actual, "Content differs on line " + lineNumber + ":\n"));
    } else if (terminated != expectedTerminated) {
      report(
          () ->
              fail(
                  "Line "
                      + lineNumber
                      + " is equal, but the output "
                      + (terminated ? "has" : "does not have")
                      + " a newline at the end of the line and the expected content "
                      + (expectedTerminated ? "has one" : "ends without one")));
    }
  }

  /**
   * Reads the next line of the golden content into {@code expectedLine}, without line separator.
   * Only {@code \n} separates lines.
   *
   * @return whether the line ends in a newline. If not, the golden content is exhausted, and there
   *     was no further line if {@code expectedLine} is empty.
   */
  private boolean readExpectedLine(StringBuilder expectedLine) throws IOException {
    int c;
    while ((c = in.read()) != -1) {
      if (c == '\n') {
        return true;
      }
      expectedLine.append((char) c);
    }
    return false;
  }

  /** Returns the start of the expected content that has not been compared yet. */
  private String remainingContent() throws IOException {
    char[] extraInput = new char[MAX_REPORTED_MISSING_CHARS];
    int length = 0;
    int read;
    while (length < extraInput.length
        && (read = in.read(extraInput, length, extraInput.length - length)) != -1) {
      length += read;
    }
    String extraContent = new String(extraInput, 0, length);
    if (length == extraInput.length && in.read() != -1) {
      return extraContent + "\n[...]\n";
    }
    return extraContent;
  }

  /** Runs an assertion and passes its failure to the failure handler. */
  private void report(Runnable assertion) {
    try {
      assertion.run();
    } catch (AssertionError failure) {
      comparisonFailureHandler.handle(failure);
    }
  }

  /**
   * Compares the last line if it does not end in a newline, checks that all expected content has
   * been output, and closes the golden input.
   */
  @Override
  public void close() throws IOException {
    if (closed) {
      return;
    }
    try {
      if (buf.size() > 0) {
        compareLine(false);
      }
      String missing = remainingContent();
      if (!missing.isEmpty()) {
        report(
            () ->
                assertEquals(
                    missing,
                    "",
                    "Output ends before line " + line + " of the expected content:\n"));
      }
    } finally {
      closeWithoutCheck();
    }
  }

  /**
   * Closes the golden input without comparing the last line or checking for remaining content, for
   * callers that have already reported a failure, e.g. from a {@code finally} block.
   */
  public void closeWithoutCheck() {
    closed = true;
    try {
      in.close();
    } catch (IOException ignored) {
      // We're expecting to be called in finally, so there is not much more we can do.
    }
  }

  public void setComparisonFailureHandler(ComparisonFailureHandler comparisonFailureHandler) {
    this.comparisonFailureHandler = comparisonFailureHandler;
  }

  public interface ComparisonFailureHandler {
    void handle(AssertionError failure);
  }
}
