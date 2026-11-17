/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.apipirivate;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.jflex.testing.testsuite.JFlexTest;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.CompilerException;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
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
@TestSpec(lex = "src/test/java/de/jflex/testcase/apipirivate/private.flex")
public class ApiPrivateTest implements JFlexTest {

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
