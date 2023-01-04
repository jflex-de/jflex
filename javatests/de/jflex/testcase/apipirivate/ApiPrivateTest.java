/*
 * Copyright (C) 2018-2019 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.apipirivate;

import static com.google.common.truth.Truth.assertWithMessage;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import de.jflex.testing.testsuite.JFlexTestRunner;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.CompilerException;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test that JFlex can generate private classes.
 *
 * <p>See feature request <a href="https://github.com/jflex-de/jflex/issues/141">#141 Generate
 * cleaner interfaces</a>, {@code %apiprivate} option.
 */
@RunWith(JFlexTestRunner.class)
@TestSpec(lex = "javatests/de/jflex/testcase/apipirivate/private.flex")
public class ApiPrivateTest {

  @Test
  public void compile() throws CompilerException {
    ImmutableList<File> srcFiles =
        Stream.of(
                "javatests/de/jflex/testcase/apipirivate/PrivateScanner.java",
                "javatests/de/jflex/testcase/apipirivate/AttemptPrivateAccess.java")
            .map(File::new)
            .collect(ImmutableList.toImmutableList());
    try {
      JavacUtils.compile(srcFiles);
      fail("Class `PrivateScanner` should have private access");
    } catch (CompilerException e) {
      assertWithMessage(
              "While compiling %s",
              srcFiles.stream().map(File::getAbsolutePath).collect(joining(", ")))
          .that(e)
          .hasMessageThat()
          .contains("yylex() has private access in de.jflex.testcase.apipirivate.PrivateScanner");
    }
  }
}
