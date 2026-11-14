/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.junit.jupiter.api.Test;

/**
 * Test for feature request <a href="https://github.com/jflex-de/jflex/issues/75">#75 Support
 * generics for %type and %extends</a>.
 *
 * <p>Tests type arguments in {@code %type} and {@code %ctorarg}, in a class without type
 * parameters.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/generics/generics2.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/generics/generics2.sysout",
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 1, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1",
      "1 [a0]:",
      "a0: return codePoint();"
    },
    charClassesCount = 1,
    charClasses = {"{ [0-1114111] }"})
public class Generics2Test implements JFlexTest {

  private static Vector<Integer> vector(int element) {
    return new Vector<>(Collections.singleton(element));
  }

  /** The scanning method returns {@code Vector<Integer>}. */
  @Test
  public void returnsVectorOfInteger() throws Exception {
    Map<Vector<Integer>, String> names = new HashMap<>();
    names.put(vector('a'), "letter a");
    Generics2 scanner = new Generics2(new StringReader("ab"), names);

    Vector<Integer> token = scanner.yylex();
    assertEquals(vector('a'), token);
    Integer first = token.get(0);
    assertEquals(Integer.valueOf('a'), first);
    assertEquals("letter a", scanner.names().get(token));

    token = scanner.yylex();
    assertEquals(vector('b'), token);
    assertNull(scanner.names().get(token));

    token = scanner.yylex();
    assertNull(token);
    assertTrue(scanner.yyatEOF());
  }

  /** The constructor passes its argument to the {@code %init} code. */
  @Test
  public void constructorArgument() throws Exception {
    Map<Vector<Integer>, String> names = new HashMap<>();
    Generics2 scanner = new Generics2(new StringReader(""), names);
    assertSame(names, scanner.names());
    Vector<Integer> token = scanner.yylex();
    assertNull(token);
  }

  /** The type arguments are in the signatures, not only in the code of the methods. */
  @Test
  public void signatures() throws Exception {
    Method yylex = Generics2.class.getDeclaredMethod("yylex");
    assertEquals(Vector.class, yylex.getReturnType());
    assertEquals("java.util.Vector<java.lang.Integer>", yylex.getGenericReturnType().getTypeName());
    assertEquals(0, yylex.getTypeParameters().length);

    // no other constructor: the spec has no %debug or %standalone
    Constructor<?>[] constructors = Generics2.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);
    Constructor<Generics2> constructor =
        Generics2.class.getDeclaredConstructor(Reader.class, Map.class);
    assertEquals(
        "java.util.Map<java.util.Vector<java.lang.Integer>, java.lang.String>",
        constructor.getGenericParameterTypes()[1].getTypeName());

    assertEquals(0, Generics2.class.getTypeParameters().length);
  }
}
