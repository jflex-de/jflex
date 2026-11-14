/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.testing.SystemOutCapture;
import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Vector;
import org.junit.jupiter.api.Test;

/**
 * Test for feature request <a href="https://github.com/jflex-de/jflex/issues/75">#75 Support
 * generics for %type and %extends</a>.
 *
 * <p>Tests a wildcard in {@code %ctorarg}, and the second constructor without the {@code %ctorarg}
 * parameters that JFlex generates for the {@code main} method of a {@code %standalone} scanner.
 *
 * <p>Also test {@code %standalone}.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/generics/generics4.flex",
    // warning about the second constructor
    sysout = "src/test/java/de/jflex/testcase/generics/generics4.sysout",
    quiet = true,
    minimizedDfaStatesCount = 3,
    minimizedDfa = {
      "states 3, inputs 2, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1; 1 -> 2",
      "1 [a0]:",
      "2 [a1]:",
      "a0: System.out.print(yytext());",
      "a1: /* print everything else */ return 1;"
    },
    charClassesCount = 2,
    charClasses = {"{ [0-'@']['['-'`']['{'-1114111] }", "{ ['A'-'Z']['a'-'z'] }"})
public class Generics4Test implements JFlexTest {

  /** {@code [a-zA-Z]} */
  private static final int LETTER = 1;

  /** Scans the input to the end. Returns the matched text. */
  private static String scanAll(Generics4<?> scanner) throws Exception {
    StringBuilder matched = new StringBuilder();
    while (scanner.yylex() != Generics4.YYEOF) {
      assertEquals(1, scanner.yylength());
      matched.append(scanner.yytext());
    }
    assertTrue(scanner.yyatEOF());
    return matched.toString();
  }

  @Test
  public void standalonePrintsUnmatchedInput() throws Exception {
    String input = "int x = 42;\n// \u00E9, \u00DF and \uD83D\uDE00\r\n\tfoo(bar);\n";
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String matched;
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      matched = scanAll(new Generics4<String>(new StringReader(input)));
    }
    assertEquals("intxandfoobar", matched);
    assertEquals("  = 42;\n// \u00E9, \u00DF  \uD83D\uDE00\r\n\t();\n", out.toString("UTF-8"));
  }

  /** Each call of the scanning method prints the unmatched text before its match. */
  @Test
  public void standalonePrintsBeforeMatch() throws Exception {
    Generics4<String> scanner = new Generics4<>(new StringReader("12a3"));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      assertEquals(LETTER, scanner.yylex());
      assertEquals("a", scanner.yytext());
      assertEquals("12", out.toString("UTF-8"));

      assertEquals(Generics4.YYEOF, scanner.yylex());
      assertEquals("123", out.toString("UTF-8"));
    }
    assertTrue(scanner.yyatEOF());
  }

  @Test
  public void standaloneEmptyInput() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (SystemOutCapture capture = SystemOutCapture.start(new PrintStream(out, true, "UTF-8"))) {
      assertEquals("", scanAll(new Generics4<String>(new StringReader(""))));
    }
    assertEquals("", out.toString("UTF-8"));
  }

  /** The {@code %ctorarg} constructor takes a map with any subtype of String as values. */
  @Test
  public void wildcardConstructorArgument() throws Exception {
    HashMap<Vector<Integer>, String> strings = new HashMap<>();
    Generics4<Integer> scanner = new Generics4<>(new StringReader("ab"), strings);
    assertEquals(LETTER, scanner.yylex());
    assertEquals("a", scanner.yytext());
    assertEquals(LETTER, scanner.yylex());
    assertEquals("b", scanner.yytext());
    assertEquals(Generics4.YYEOF, scanner.yylex());

    HashMap<Vector<Integer>, ? extends String> wildcard = strings;
    scanner = new Generics4<>(new StringReader(""), wildcard);
    assertEquals(Generics4.YYEOF, scanner.yylex());
  }

  /** The constructor with the {@code %ctorarg} parameter and the one that the warning is about. */
  @Test
  public void constructors() throws Exception {
    assertEquals(2, Generics4.class.getDeclaredConstructors().length);

    Constructor<?> withArgument = Generics4.class.getConstructor(Reader.class, HashMap.class);
    assertEquals(
        "java.util.HashMap<java.util.Vector<java.lang.Integer>, ? extends java.lang.String>",
        withArgument.getGenericParameterTypes()[1].getTypeName());

    Constructor<?> withoutArgument = Generics4.class.getConstructor(Reader.class);
    assertEquals(1, withoutArgument.getParameterCount());

    assertEquals(1, Generics4.class.getTypeParameters().length);
    assertEquals("T", Generics4.class.getTypeParameters()[0].getName());
  }
}
