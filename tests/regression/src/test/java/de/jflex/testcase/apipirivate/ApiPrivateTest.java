/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.apipirivate;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.CompilerException;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Test that JFlex can generate private classes.
 *
 * <p>See feature request <a href="https://github.com/jflex-de/jflex/issues/141">#141 Generate
 * cleaner interfaces</a>, {@code %apiprivate} option.
 */
@TestSpec(
    lex = "src/test/java/de/jflex/testcase/apipirivate/private.flex",
    noOutput = true,
    quiet = true,
    minimizedDfaStatesCount = 2,
    minimizedDfa = {
      "states 2, inputs 1, lexical states 1",
      "entry: 0 0",
      "0: 0 -> 1",
      "1 [a0]:",
      "a0: /* no action */"
    },
    charClassesCount = 1,
    charClasses = {"{ [0-1114111] }"})
public class ApiPrivateTest implements JFlexTest {

  /** The API methods that {@code %apiprivate} makes private. */
  private static final List<String> API_METHODS =
      Arrays.asList(
          "yyatEOF",
          "yybegin",
          "yycharat",
          "yyclose",
          "yylength",
          "yylex",
          "yypushback",
          "yyreset",
          "yystate",
          "yytext");

  private static final List<String> API_CONSTANTS = Arrays.asList("YYEOF", "YYINITIAL");

  @Test
  public void methodsArePrivate() {
    List<String> names = new ArrayList<>();
    for (Method method : PrivateScanner.class.getDeclaredMethods()) {
      assertTrue(Modifier.isPrivate(method.getModifiers()), method + " should be private");
      names.add(method.getName());
    }
    for (String name : API_METHODS) {
      assertTrue(names.contains(name), "The scanner should have the method " + name);
    }
  }

  @Test
  public void fieldsArePrivate() {
    List<String> names = new ArrayList<>();
    for (Field field : PrivateScanner.class.getDeclaredFields()) {
      assertTrue(Modifier.isPrivate(field.getModifiers()), field + " should be private");
      names.add(field.getName());
    }
    for (String name : API_CONSTANTS) {
      assertTrue(names.contains(name), "The scanner should have the constant " + name);
    }
  }

  @Test
  public void classAndConstructorArePublic() throws Exception {
    assertTrue(Modifier.isPublic(PrivateScanner.class.getModifiers()));
    Constructor<?>[] constructors = PrivateScanner.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);
    assertEquals(
        Collections.singletonList(Reader.class),
        Arrays.asList(constructors[0].getParameterTypes()));
    assertTrue(Modifier.isPublic(constructors[0].getModifiers()));
  }

  @Test
  public void compile() throws CompilerException {
    List<File> srcFiles =
        Stream.of(
                "src/test/java/de/jflex/testcase/apipirivate/PrivateScanner.java",
                "src/test/java/de/jflex/testcase/apipirivate/AttemptPrivateAccess.java")
            .map(File::new)
            .collect(Collectors.toList());
    try {
      JavacUtils.compile(srcFiles);
      fail("Class `PrivateScanner` should have private access");
    } catch (CompilerException e) {
      assertTrue(
          e.getMessage()
              .contains(
                  "yylex() has private access in de.jflex.testcase.apipirivate.PrivateScanner"),
          "While compiling "
              + srcFiles.stream().map(File::getAbsolutePath).collect(joining(", "))
              + ": "
              + e.getMessage());
    }
  }
}
