/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Tests that the lex spec has an option to add arguments to the constructor ({@code %ctorarg}), and
 * to name the scan method ({@code %function}). See feature request <a
 * href="https://github.com/jflex-de/jflex/issues/156">#156 provide option to add arguments to
 * constructor and yylex</a>.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ctorarg/ctor-arg.flex",
    quiet = true,
    warnings = {@Warn(ErrorMessages.CTOR_DEBUG)},
    minimizedDfaStatesCount = 11,
    minimizedDfa = {
      "states 11, inputs 9, lexical states 1",
      "entry: 0 0",
      "0: 0-6 -> 1; 7 -> 2; 8 -> 1",
      "1 [a0]:",
      "2 [a0]: 5 -> 3",
      "3: 4 -> 4",
      "4: 2 -> 5",
      "5: 1 -> 6",
      "6: 6 -> 7",
      "7: 8 -> 8",
      "8: 3 -> 9",
      "9: 2 -> 10",
      "10 [a1]:",
      "a0: System.out.print(yytext());",
      "a1: /* without content */"
    },
    charClassesCount = 9,
    charClasses = {
      "{ [0-31]['!'-'d']['f'-'k']['n']['p'-'q']['t']['v'-1114111] }",
      "{ [' '] }",
      "{ ['e'] }",
      "{ ['l'] }",
      "{ ['m'] }",
      "{ ['o'] }",
      "{ ['r'] }",
      "{ ['s'] }",
      "{ ['u'] }"
    })
public class CtorArgTest implements JFlexTest {

  private static String unmatched(Ctorarg scanner) throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      assertEquals(Ctorarg.YYEOF, scanner.test());
    }
    return out.toString("UTF-8");
  }

  /** The constructor takes the {@code %ctorarg} arguments and the {@code %init} code sees them. */
  @Test
  public void constructorWithArguments() throws Exception {
    Object y = new Object();
    Ctorarg scanner = new Ctorarg(new StringReader(""), 42, y);
    assertEquals(42, scanner.x);
    assertSame(y, scanner.y);
  }

  @Test
  public void constructorParameterTypes() throws Exception {
    // throws if there is no such constructor
    Ctorarg.class.getDeclaredConstructor(Reader.class, int.class, Object.class);
  }

  /** The constructor for {@code main} does not run the {@code %init} code. */
  @Test
  public void readerOnlyConstructor() throws Exception {
    Ctorarg scanner = new Ctorarg(new StringReader(""));
    assertEquals(0, scanner.x);
    assertNull(scanner.y);
  }

  /** {@code %function test} names the scan method. */
  @Test
  public void scanMethodHasGivenName() throws Exception {
    assertEquals(int.class, Ctorarg.class.getMethod("test").getReturnType());
    assertThrows(NoSuchMethodException.class, () -> Ctorarg.class.getDeclaredMethod("yylex"));
  }

  @Test
  public void scansWithArguments() throws Exception {
    assertEquals("", unmatched(new Ctorarg(new StringReader("some rule"), 1, "y")));
    assertEquals(
        "no  rules", unmatched(new Ctorarg(new StringReader("no some rule rules"), 1, "y")));
  }

  @Test
  public void scansWithReaderOnly() throws Exception {
    assertEquals("x", unmatched(new Ctorarg(new StringReader("some rulexsome rule"))));
  }
}
