/*
 * Copyright (C) 2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.spoon_feed_reader;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.CharSource;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Test demonstrating a fix for issue <a href="https://github.com/jflex-de/jflex/issues/538">#538 A
 * spoon-feeding Reader can result in the scanning buffer not being fully populated</a>.
 *
 * <p>The scanner, which doesn't recognize surrogate chars in its non-fallback rule, is fed input
 * text "12345678𐌀" using a spoon-feeding reader that splits the input between the high and low
 * surrogate chars for "𐌀".
 */
public class SpoonFeedReaderTest {

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/spoon_feed_reader");

  /** Using the current {@code skeleton.default}, the scanner should output all input chars. */
  @Test
  public void testFixedSpoonReaderWithSurrogate() throws Exception {
    SpoonFeedReader scanner = ScannerFactory.of(SpoonFeedReader::new).createForReader(reader());
    assertThat(scanner.yylex()).isEqualTo("12345678𐌀");
  }

  /**
   * Using the {@code problematic.skeleton.default} (the default skeleton at the time of writing),
   * the scanner will output all input chars except the "𐌀" because its non-fallback rule doesn't
   * recognize unpaired surrogates. This test just reproduces the problem described in the bug.
   */
  @Test
  public void testFailingSpoonReaderWithSurrogate() throws Exception {
    SpoonFeedReaderF scanner = ScannerFactory.of(SpoonFeedReaderF::new).createForReader(reader());
    assertThat(scanner.yylex()).isEqualTo("12345678");
  }

  private static Reader reader() throws IOException {
    String input = "12345678𐌀";
    int maxChars = 9; // Hard-coded to split surrogate characters in the input "12345678𐌀"
    return new SpoonFeedMaxCharsReaderWrapper(maxChars, CharSource.wrap(input).openStream());
  }
}
