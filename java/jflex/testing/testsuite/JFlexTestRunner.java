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
package jflex.testing.testsuite;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.fail;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import jflex.core.Out;
import jflex.generator.LexGenerator;
import jflex.testing.diff.DiffOutputStream;
import jflex.testing.testsuite.annotations.NoExceptionThrown;
import jflex.testing.testsuite.annotations.TestSpec;
import jflex.util.javac.CompilerException;
import jflex.util.javac.JavacUtil;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JFlexTestRunner extends BlockJUnit4ClassRunner {

  private final Class<?> klass;
  private final TestSpec spec;

  public JFlexTestRunner(Class<?> testClass) throws InitializationError {
    super(testClass);
    this.klass = testClass;
    this.spec = checkNotNull(testClass.getAnnotation(TestSpec.class));
  }

  @Override
  public Description getDescription() {
    return Description.createTestDescription(klass, "JFlex test case");
  }

  @Override
  public void run(RunNotifier notifier) {
    Optional<DiffOutputStream> diffSysOut = injectDiffSysOut();
    if (spec.generatorThrows() != NoExceptionThrown.class) {
      try {
        generateLexer(notifier);
        fail(
            "@TestCase indicates that the jflex generation throws a "
                + spec.generatorThrows().getSimpleName()
                + " but nothing was thrown");
      } catch (AssertionError e) {
        throw e;
      } catch (Throwable e) {
        assertWithMessage(
                "@TestCase indicates that the jflex generation must throw a "
                    + spec.generatorThrows().getSimpleName())
            .that(e)
            .isInstanceOf(spec.generatorThrows());
        if (spec.generatorThrowableCause() == Void.class) {
          assertWithMessage(
                  "@TestCase indicates that there is no cause for the generator exception")
              .that(e.getCause())
              .isNull();
        } else if (spec.generatorThrowableCause() != NoExceptionThrown.class) {
          assertWithMessage(
                  "@TestCase indicates that cause of the generator exception is "
                      + spec.generatorThrowableCause())
              .that(e.getCause())
              .isInstanceOf(spec.generatorThrowableCause());
        }
      } finally {
        if (diffSysOut.isPresent()) {
          assertWithMessage("Some content from expected output %s was not read", spec.sysout())
              .that(diffSysOut.get().remainingContent())
              .isEmpty();
        }
      }
    } else {
      String lexerJavaFileName = generateLexer(notifier);
      buildLexer(notifier, lexerJavaFileName);
    }
    super.run(notifier);
  }

  private Optional<DiffOutputStream> injectDiffSysOut() {
    if (!Strings.isNullOrEmpty(spec.sysout())) {
      File sysoutFile = new File(spec.sysout());
      try {
        DiffOutputStream diffSysOut =
            new DiffOutputStream(Files.newReader(sysoutFile, Charsets.UTF_8));
        Out.setOutputStream(diffSysOut);
        return Optional.of(diffSysOut);
      } catch (FileNotFoundException e) {
        throw new AssertionError(
            "The golden sysout was not found: " + sysoutFile.getAbsolutePath(), e);
      }
    }
    return Optional.empty();
  }

  private String generateLexer(RunNotifier notifier) {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Generate Lexer"));
    String lexerJavaFileName = LexGenerator.generate(new File(spec.lex()));
    return checkNotNull(lexerJavaFileName);
  }

  private void buildLexer(RunNotifier notifier, String lexerJavaFileName) {
    try {
      compile(notifier, ImmutableList.of(lexerJavaFileName));
    } catch (CompilerException e) {
      notifier.fireTestFailure(
          new Failure(Description.createTestDescription(klass, "Failed to compile lexer"), e));
    }
  }

  private void compile(RunNotifier notifier, ImmutableList<String> javaFileNames)
      throws CompilerException {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Compile java code"));
    JavacUtil.compile(javaFileNames);
  }
}
