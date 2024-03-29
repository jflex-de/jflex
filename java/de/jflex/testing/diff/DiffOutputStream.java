/*
 * Copyright (C) 2018-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing.diff;

import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.junit.ComparisonFailure;

/**
 * An output streams that asserts that every printed lined is equal to the one from the expected
 * input.
 *
 * <p>Each input line must be less than 2048 bytes.
 *
 * <p>The output is not saved anywhere.
 */
public class DiffOutputStream extends OutputStream {

  /** Constant for the Unicode {@code UTF-8}charset. */
  private static final String UTF_8 = "UTF-8";

  /** The golden content this OutputStream will compare against. */
  private final BufferedReader in;

  /** The internal buffer where actual data is stored. */
  private final byte[] buf = new byte[2048];

  /** The current lines being compared. Only {@code \n} serves as a line separator. */
  private int line = 1;

  /**
   * Handles assertion failures when the content differs. By default, just throws the {@link
   * ComparisonFailure}.
   */
  private ComparisonFailureHandler comparisonFailureHandler =
      failure -> {
        throw failure;
      };

  /**
   * The number of valid bytes in the buffer.
   *
   * <p>The useful buffer count is in range {@code 0} - {@code count} (excl) of {@link #buf}.
   */
  protected int count;

  public DiffOutputStream(Reader in) {
    this(new BufferedReader(in));
  }

  @SuppressWarnings("WeakerAccess")
  public DiffOutputStream(BufferedReader in) {
    this.in = in;
  }

  @Override
  public void write(int b) throws IOException {
    buf[count] = (byte) b;
    if (b == '\n') {
      String expectedLine = in.readLine();
      try {
        assertThatWrittenWasExpected(expectedLine);
      } catch (ComparisonFailure failure) {
        comparisonFailureHandler.handle(failure);
      }
      count = 0;
      line++;
    } else {
      count++;
    }
  }

  private void assertThatWrittenWasExpected(String expectedLine) throws IOException {
    if (Strings.isNullOrEmpty(expectedLine)) {
      failOnDifferentLine("");
      return;
    }
    byte[] expectedRaw = expectedLine.getBytes(UTF_8);
    if (count != expectedLine.length()) {
      failOnDifferentLine(expectedLine);
    }
    int length = count;
    for (int i = 0; i < length; i++) {
      if (buf[i] != expectedRaw[i]) {
        failOnDifferentLine(expectedLine);
      }
    }
  }

  private void failOnDifferentLine(String expectedLine) throws UnsupportedEncodingException {
    byte[] actualRaw = new byte[count];
    System.arraycopy(buf, 0, actualRaw, 0, count);
    String actualLine = new String(actualRaw, UTF_8);
    assertWithMessage("Content differs on line %s:\n", line)
        .that(actualLine)
        .isEqualTo(expectedLine);
  }

  public String remainingContent() {
    char[] extraInput = new char[140];
    try {
      int read = in.read(extraInput);
      if (read <= 0) {
        return "";
      }
      String extraContent = new String(extraInput, 0, read);
      if (read == extraInput.length) {
        return extraContent + "\n[...]\n";
      } else {
        return extraContent;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void close() throws IOException {
    assertWithMessage("All expected content has been output").that(remainingContent()).isEmpty();
    super.close();
  }

  public void setComparisonFailureHandler(ComparisonFailureHandler comparisonFailureHandler) {
    this.comparisonFailureHandler = comparisonFailureHandler;
  }

  public interface ComparisonFailureHandler {
    void handle(ComparisonFailure failure);
  }
}
