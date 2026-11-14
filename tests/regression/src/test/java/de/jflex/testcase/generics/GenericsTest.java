/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.junit.jupiter.api.Test;

/**
 * Test for feature request <a href="https://github.com/jflex-de/jflex/issues/75">#75 Support
 * generics for %type and %extends</a>.
 *
 * <p>Tests a type parameter in {@code %class} that is the {@code %type} of the scanning method.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/generics/generics.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/generics/generics.sysout",
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 1, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1",
      "1 [a0]:",
      "a0: return token;"
    },
    charClassesCount = 1,
    charClasses = {"{ [0-1114111] }"})
public class GenericsTest implements JFlexTest {

  /** The scanning method returns {@code T}: the assignments need no cast. */
  @Test
  public void returnsTypeArgument() throws Exception {
    Generics<String> strings = new Generics<>(new StringReader("ab"));
    strings.token = "token";
    String s = strings.yylex();
    assertEquals("token", s);
    assertEquals("a", strings.yytext());
    s = strings.yylex();
    assertEquals("token", s);
    assertEquals("b", strings.yytext());
    s = strings.yylex();
    assertNull(s);
    assertTrue(strings.yyatEOF());

    Generics<Integer> integers = new Generics<>(new StringReader("a"));
    integers.token = 7;
    Integer i = integers.yylex();
    assertEquals(Integer.valueOf(7), i);
    i = integers.yylex();
    assertNull(i);
    assertTrue(integers.yyatEOF());
  }

  /** The value at the end of input is {@code null}, as for every {@code %type} of objects. */
  @Test
  public void nullAtEof() throws Exception {
    Generics<String> scanner = new Generics<>(new StringReader(""));
    scanner.token = "token";
    String s = scanner.yylex();
    assertNull(s);
    assertTrue(scanner.yyatEOF());
  }

  /** {@code class Generics<T>} with {@code T yylex()}. */
  @Test
  public void typeParameter() throws Exception {
    TypeVariable<?>[] parameters = Generics.class.getTypeParameters();
    assertEquals(1, parameters.length);
    assertEquals("T", parameters[0].getName());
    assertArrayEquals(new Type[] {Object.class}, parameters[0].getBounds());
    assertSame(Generics.class, parameters[0].getGenericDeclaration());

    assertEquals(parameters[0], Generics.class.getDeclaredMethod("yylex").getGenericReturnType());
    assertEquals(Object.class, Generics.class.getDeclaredMethod("yylex").getReturnType());
  }
}
