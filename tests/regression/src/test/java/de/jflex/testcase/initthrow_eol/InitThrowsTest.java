/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.initthrow_eol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Regression test against bug <a href="https://github.com/jflex-de/jflex/issues/52">#52 single-line
 * %initthrow option fails</a> (for extra space before eol).
 *
 * <p>The {@code %init} code of the spec calls {@code Reader.ready()}, which can throw an {@code
 * IOException}, so the scanner only compiles if its constructor declares the {@code %initthrow}
 * exception.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/initthrow_eol/initthrow.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 6,
    minimizedDfa = {
      "states 6, inputs 5, lexical states 1",
      "entry: 0 0",
      "0: 1 -> 1",
      "1: 3 -> 2",
      "2: 2 -> 3",
      "3: 2 -> 4",
      "4: 4 -> 5",
      "5 [a0]:",
      "a0:"
    },
    charClassesCount = 5,
    charClasses = {
      "{ [0-'c']['e'-'l']['n'-'t']['v'-'x']['z'-1114111] }",
      "{ ['d'] }",
      "{ ['m'] }",
      "{ ['u'] }",
      "{ ['y'] }"
    })
public class InitThrowsTest implements JFlexTest {

  @Test
  public void canInstantiateScanner() throws Exception {
    Initthrow scanner = new Initthrow(new StringReader("dummy"));
    assertEquals(Initthrow.YYEOF, scanner.yylex());
  }

  /** The constructor declares the exception of {@code %initthrow}, and only that. */
  @Test
  public void constructorDeclaresException() throws Exception {
    assertEquals(
        Arrays.asList(IOException.class),
        Arrays.asList(Initthrow.class.getDeclaredConstructor(Reader.class).getExceptionTypes()));
  }

  @Test
  public void initCodeThrowsDeclaredException() throws Exception {
    Reader reader = new StringReader("");
    reader.close();
    assertThrows(IOException.class, () -> new Initthrow(reader));
  }

  @Test
  public void initCodeThrowsForReaderThatIsNotReady() throws Exception {
    Reader reader =
        new StringReader("") {
          @Override
          public boolean ready() {
            return false;
          }
        };
    UnsupportedOperationException e =
        assertThrows(UnsupportedOperationException.class, () -> new Initthrow(reader));
    assertEquals("Input reader is not ready", e.getMessage());
  }
}
