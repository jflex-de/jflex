/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.junit.jupiter.api.Test;

/**
 * Test for feature request <a href="https://github.com/jflex-de/jflex/issues/75">#75 Support
 * generics for %type and %extends</a>.
 *
 * <p>Test a {@code %type} that starts with type parameters. Also test a {@code %ctorarg} that uses
 * the type parameter of the class.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/generics/generics3.flex",
    // no warnings
    sysout = "src/test/java/de/jflex/testcase/generics/generics3.sysout",
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 1, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1",
      "1 [a0]:",
      "a0: return new Vector<A>();"
    },
    charClassesCount = 1,
    charClasses = {"{ [0-1114111] }"})
public class Generics3Test implements JFlexTest {

  /** The result of the scanning method is a vector of a type that is below both bounds. */
  @Test
  public void returnsVectorOfBoundedType() throws Exception {
    Generics3<String> scanner =
        new Generics3<>(new StringReader("ab"), new HashMap<Vector<Integer>, String>());

    Vector<? extends String> strings = scanner.yylex();
    assertNotNull(strings);
    assertTrue(strings.isEmpty());
    assertEquals("a", scanner.yytext());

    Vector<? extends Readable> readables = scanner.yylex();
    assertNotNull(readables);
    assertTrue(readables.isEmpty());
    assertEquals("b", scanner.yytext());

    strings = scanner.yylex();
    assertNull(strings);
    assertTrue(scanner.yyatEOF());
  }

  /** {@code <A extends String & Readable> Vector<A> yylex()} */
  @Test
  public void genericMethod() throws Exception {
    Method yylex = Generics3.class.getDeclaredMethod("yylex");

    TypeVariable<Method>[] parameters = yylex.getTypeParameters();
    assertEquals(1, parameters.length);
    assertEquals("A", parameters[0].getName());
    assertArrayEquals(new Type[] {String.class, Readable.class}, parameters[0].getBounds());

    assertEquals(Vector.class, yylex.getReturnType());
    ParameterizedType result = (ParameterizedType) yylex.getGenericReturnType();
    assertEquals(Vector.class, result.getRawType());
    assertArrayEquals(new Type[] {parameters[0]}, result.getActualTypeArguments());
  }

  /** {@code Generics3<T>(Reader, Map<Vector<Integer>, T>)} */
  @Test
  public void constructorUsesClassTypeParameter() throws Exception {
    TypeVariable<?>[] parameters = Generics3.class.getTypeParameters();
    assertEquals(1, parameters.length);
    assertEquals("T", parameters[0].getName());
    assertArrayEquals(new Type[] {Object.class}, parameters[0].getBounds());

    // no other constructor: the spec has no %debug or %standalone
    assertEquals(1, Generics3.class.getDeclaredConstructors().length);
    Constructor<?> constructor = Generics3.class.getDeclaredConstructor(Reader.class, Map.class);
    ParameterizedType map = (ParameterizedType) constructor.getGenericParameterTypes()[1];
    assertEquals(Map.class, map.getRawType());
    assertEquals(
        "java.util.Vector<java.lang.Integer>", map.getActualTypeArguments()[0].getTypeName());
    assertEquals(parameters[0], map.getActualTypeArguments()[1]);
  }
}
