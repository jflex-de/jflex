/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
