/*
 * Copyright 2026, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.testing.testsuite.annotations.Warn;
import java.io.StringReader;
import jflex.l10n.ErrorMessages;
import org.junit.jupiter.api.Test;

/**
 * Tests that {@code %cupdebug} together with {@code %ctorarg} generates a scanner that compiles.
 *
 * <p>The debug {@code main} emitted for {@code %cupdebug} constructs the scanner with a reader
 * only, so the emitter must also generate the constructor without the {@code %ctorarg} parameters,
 * as it already did for {@code %standalone} and {@code %debug}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/ctorarg/ctor-arg-cupdebug.flex",
    quiet = true,
    warnings = {@Warn(ErrorMessages.CTOR_DEBUG)},
    minimizedDfaStatesCount = 10,
    minimizedDfa = {
      "states 10, inputs 9, lexical states 1",
      "entry: 0 0",
      "0: 7 -> 1",
      "1: 5 -> 2",
      "2: 4 -> 3",
      "3: 2 -> 4",
      "4: 1 -> 5",
      "5: 6 -> 6",
      "6: 8 -> 7",
      "7: 3 -> 8",
      "8: 2 -> 9",
      "9 [a0]:",
      "a0: /* without content */"
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
public class CtorArgCupDebugTest implements JFlexTest {

  /** The constructor takes the {@code %ctorarg} arguments and the {@code %init} code sees them. */
  @Test
  public void constructorWithArguments() throws Exception {
    Object y = new Object();
    CtorargCupdebug scanner = new CtorargCupdebug(new StringReader("some rule"), 42, y);
    assertEquals(42, scanner.x);
    assertSame(y, scanner.y);
    assertEquals(sym.EOF, scanner.next_token().sym);
  }

  /** The constructor for the debug {@code main} does not run the {@code %init} code. */
  @Test
  public void readerOnlyConstructor() throws Exception {
    CtorargCupdebug scanner = new CtorargCupdebug(new StringReader("some rulesome rule"));
    assertEquals(0, scanner.x);
    assertNull(scanner.y);
    assertEquals(sym.EOF, scanner.next_token().sym);
  }
}
