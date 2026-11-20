/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests that the scanner can parse a very large input.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/536">#536</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/large_input/large_input.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 4,
    charClassesCount = 3,
    charClasses = {
      "{ [0-9][14-132][134-8231][8234-55295][57344-1114111] }",
      "{ [10] }",
      "{ [11-13][133][8232-8233][55296-57343] }"
    })
public class LargeInputTest implements JFlexTest {

  private LargeInputScanner scanner;

  /** Asserts that the next match is a line with this text. */
  private void assertLine(State state, String text) throws IOException {
    assertEquals(state, scanner.yylex());
    assertEquals(text, scanner.yytext());
  }

  /**
   * Tests a well-formed input content larger than MAX_INT (2^32-1). The scanner should never
   * encounter {@code zzchar < 0} and hence never throw {@link NegativeYyCharException}.
   */
  @Test
  public void consumeLargeInput() throws Exception {
    final String line = "For every character `yychar` is incremented, but it does not overflow!";
    final String content = line + "\n";
    long size = 4 * (long) content.length();
    Reader largeContentReader = new RepeatContentReader(size, content);
    scanner = createScanner(largeContentReader);
    // Pretend we move close to MAX_INT
    scanner.fakeRead(Integer.MAX_VALUE - 2 * content.length());
    assertLine(State.BEFORE_2GB, line);
    assertLine(State.BEFORE_2GB, line);
    assertLine(State.BEFORE_2GB, line);
    assertLine(State.AFTER_2GB, line);
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  @Test
  public void consumeSmallInput() throws Exception {
    long size = 50;
    Reader largeContentReader = new RepeatContentReader(size, "hello foo\n");
    scanner = createScanner(largeContentReader);
    for (int i = 0; i < 5; i++) {
      assertLine(State.BEFORE_2GB, "hello foo");
    }
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  @Test
  public void consumeUnterminatedLine() throws Exception {
    scanner = createScanner(new BufferedReader(new StringReader("Hello\nworld")));
    assertLine(State.BEFORE_2GB, "Hello");
    assertLine(State.BEFORE_2GB, "world");
    assertEquals(State.END_OF_FILE, scanner.yylex());
  }

  private static LargeInputScanner createScanner(Reader reader) {
    return new LargeInputScanner(reader);
  }
}
