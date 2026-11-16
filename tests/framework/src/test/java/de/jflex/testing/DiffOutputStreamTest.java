/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.diff.DiffOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Test for {@link DiffOutputStream}. */
public class DiffOutputStreamTest {

  @Test
  public void oneLine_equal() throws Exception {
    assertNoDifference("Hello world!\n", "Hello world!\n");
  }

  @Test
  public void oneLine_expectedHasMore() {
    assertDiffers("Hello world!\n", "Hello\n");
  }

  @Test
  public void oneLine_outputHasMore() {
    assertDiffers("Hello!\n", "Hello world!\n");
  }

  @Test
  public void empty_equal() throws Exception {
    assertNoDifference("", "");
  }

  @Test
  public void empty_outputHasLine() {
    assertDiffers("", "Hello\n");
  }

  @Test
  public void empty_outputHasEmptyLines() {
    assertDiffers("", "\n\n");
  }

  @Test
  public void empty_expectedHasLine() {
    assertDiffers("Hello\n", "");
  }

  @Test
  public void emptyLine_equal() throws Exception {
    assertNoDifference("Hello\n\nworld!\n", "Hello\n\nworld!\n");
  }

  @Test
  public void emptyLine_missingInOutput() {
    assertDiffers("Hello\n\nworld!\n", "Hello\nworld!\n");
  }

  @Test
  public void emptyLine_extraInOutput() {
    assertDiffers("Hello\nworld!\n", "Hello\n\nworld!\n");
  }

  @Test
  public void trailingEmptyLines_equal() throws Exception {
    assertNoDifference("Hello\n\n\n", "Hello\n\n\n");
  }

  @Test
  public void trailingEmptyLine_extraInOutput() {
    assertDiffers("Hello\n", "Hello\n\n");
  }

  @Test
  public void trailingEmptyLines_extraInOutput() {
    assertDiffers("Hello\n", "Hello\n\n\n\n");
  }

  @Test
  public void trailingEmptyLine_missingInOutput() {
    assertDiffers("Hello\n\n", "Hello\n");
  }

  @Test
  public void lastLineWithoutNewline_equal() throws Exception {
    assertNoDifference("Hello\nworld!", "Hello\nworld!");
  }

  @Test
  public void lastLineWithoutNewline_differs() {
    assertDiffers("Hello\nworld!", "Hello\nmoon!");
  }

  @Test
  public void lastLineWithoutNewline_notExpected() {
    assertDiffers("Hello\n", "Hello\nworld!");
  }

  @Test
  public void lastLineWithoutNewline_missingInOutput() {
    assertDiffers("Hello\nworld!", "Hello\n");
  }

  @Test
  public void finalNewline_missingInOutput() {
    assertDiffers("Hello\nworld!\n", "Hello\nworld!");
  }

  @Test
  public void finalNewline_extraInOutput() {
    assertDiffers("Hello\nworld!", "Hello\nworld!\n");
  }

  @Test
  public void nonAscii_equal() throws Exception {
    assertNoDifference("héllo € 😀\n", "héllo € 😀\n");
  }

  /** The output has as many bytes as the expected line has characters. */
  @Test
  public void nonAscii_outputIsBytePrefix() {
    assertDiffers("éx\n", "é\n");
    assertDiffers("€ab\n", "€\n");
  }

  @Test
  public void carriageReturn_inLine_equal() throws Exception {
    assertNoDifference("Hello\rworld!\n", "Hello\rworld!\n");
  }

  @Test
  public void carriageReturn_crlf_equal() throws Exception {
    assertNoDifference("Hello\r\nworld!\r\n", "Hello\r\nworld!\r\n");
  }

  @Test
  public void carriageReturn_isNotALineSeparator() {
    assertDiffers("Hello\rworld!\n", "Hello\nworld!\n");
    assertDiffers("Hello\nworld!\n", "Hello\rworld!\n");
  }

  @Test
  public void carriageReturn_crlfIsNotLf() {
    assertDiffers("Hello\r\n", "Hello\n");
    assertDiffers("Hello\n", "Hello\r\n");
  }

  @Test
  public void longLine_equal() throws Exception {
    String line = longLine(10000);
    assertNoDifference(line + "\n", line + "\n");
  }

  @Test
  public void longLine_differs() {
    String line = longLine(10000);
    assertDiffers(line + "x\n", line + "y\n");
  }

  @Test
  public void write_differs_reportsLineAndContent() {
    AssertionError failure = assertDiffers("Hello\nworld!\n", "Hello\nmoon!\n");
    assertContains(failure.getMessage(), "line 2");
    assertContains(failure.getMessage(), "world!");
    assertContains(failure.getMessage(), "moon!");
  }

  @Test
  public void write_outputAfterExpectedEnd_reportsExtraOutput() {
    AssertionError failure = assertDiffers("Hello\n", "Hello\nworld!\n");
    assertContains(failure.getMessage(), "Unexpected output");
    assertContains(failure.getMessage(), "line 2");
    assertContains(failure.getMessage(), "world!");
  }

  @Test
  public void close_contentRemaining_reportsMissingContent() {
    AssertionError failure = assertDiffers("Hello\nworld!\n", "Hello\n");
    assertContains(failure.getMessage(), "line 2");
    assertContains(failure.getMessage(), "world!");
  }

  @Test
  public void close_muchContentRemaining_reportsStartOfMissingContent() {
    AssertionError failure = assertDiffers("Hello\n" + longLine(1000) + "\n", "Hello\n");
    assertContains(failure.getMessage(), longLine(140) + "\n[...]");
    assertFalse(failure.getMessage().contains(longLine(141)), failure.getMessage());
  }

  @Test
  public void close_allContentOutput_closesInput() throws Exception {
    TrackedReader in = new TrackedReader("Hello world!\n");
    try (DiffOutputStream differ = new DiffOutputStream(in)) {
      differ.write(utf8("Hello world!\n"));
    }
    assertTrue(in.closed);
  }

  @Test
  public void close_contentRemaining_failsAndClosesInput() {
    TrackedReader in = new TrackedReader("Hello\nworld!\n");
    assertThrows(
        AssertionError.class,
        () -> {
          try (DiffOutputStream differ = new DiffOutputStream(in)) {
            differ.write(utf8("Hello\n"));
          }
        },
        DiffOutputStream.class + " throws an exception when content was not output");
    assertTrue(in.closed);
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
    assertTrue(in.closed);
  }

  @Test
  public void write_afterClose_fails() throws Exception {
    DiffOutputStream differ = new DiffOutputStream(new StringReader("Hello\n"));
    differ.write(utf8("Hello\n"));
    differ.close();
    assertThrows(IOException.class, () -> differ.write(utf8("world!")));
  }

  @Test
  public void failureHandler_collectsAllFailures() throws Exception {
    List<AssertionError> failures = new ArrayList<>();
    try (DiffOutputStream differ = new DiffOutputStream(new StringReader("a\nb\nc\nd\ne\n"))) {
      differ.setComparisonFailureHandler(failures::add);
      differ.write(utf8("a\nX\nc\nY"));
    }
    assertEquals(3, failures.size(), failures.toString());
    assertContains(failures.get(0).getMessage(), "line 2");
    assertContains(failures.get(1).getMessage(), "line 4");
    assertContains(failures.get(2).getMessage(), "line 5");
  }

  /** The stream compares the following lines correctly after a failure that was thrown. */
  @Test
  public void failureHandler_throws_continuesWithNextLine() throws Exception {
    DiffOutputStream differ = new DiffOutputStream(new StringReader("a\nb\nc\n"));
    differ.write(utf8("a\n"));
    assertThrows(AssertionError.class, () -> differ.write(utf8("X\n")));
    differ.write(utf8("c\n"));
    differ.close();
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

  /** Asserts that writing {@code output} and closing the stream reports no difference. */
  private static void assertNoDifference(String expected, String output) throws IOException {
    try (DiffOutputStream differ = new DiffOutputStream(new StringReader(expected))) {
      differ.write(utf8(output));
    }
  }

  /**
   * Asserts that writing {@code output} and closing the stream reports a difference.
   *
   * @return the reported failure
   */
  private static AssertionError assertDiffers(String expected, String output) {
    return assertThrows(
        AssertionError.class,
        () -> assertNoDifference(expected, output),
        DiffOutputStream.class + " throws an exception when the content differs");
  }

  private static void assertContains(String text, String part) {
    assertTrue(text.contains(part), "<" + text + "> must contain <" + part + ">");
  }

  private static byte[] utf8(String content) {
    return content.getBytes(StandardCharsets.UTF_8);
  }

  private static String longLine(int length) {
    char[] line = new char[length];
    Arrays.fill(line, 'x');
    return new String(line);
  }
}
