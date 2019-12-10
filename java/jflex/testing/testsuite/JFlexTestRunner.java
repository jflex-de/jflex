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

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Optional;
import jflex.core.Options;
import jflex.core.Out;
import jflex.generator.LexGenerator;
import jflex.testing.diff.DiffOutputStream;
import jflex.testing.testsuite.annotations.NoExceptionThrown;
import jflex.testing.testsuite.annotations.TestSpec;
import jflex.util.javac.CompilerException;
import jflex.util.javac.JavacUtils;
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
    this.spec =
        checkNotNull(
            testClass.getAnnotation(TestSpec.class),
            "A test running with %s must have a @%s",
            JFlexTestRunner.class.getName(),
            TestSpec.class.getName());
  }

  @Override
  public Description getDescription() {
    return Description.createTestDescription(klass, "JFlex test case");
  }

  @Override
  public void run(RunNotifier notifier) {
    String lexerJavaFileName = generateLexer(notifier);

    if (lexerJavaFileName != null) {
      buildLexer(notifier, lexerJavaFileName);
    }

    // The lexer must be generated before the other tests are executed, as they can try to
    // compile this generated code.
    super.run(notifier);
  }

  private String generateLexer(RunNotifier notifier) {
    Description desc = Description.createTestDescription(klass, "Generate Lexer");
    notifier.fireTestStarted(desc);

    Optional<DiffOutputStream> diffSysOut = injectDiffSysOut();
    Optional<DiffOutputStream> diffSysErr = injectDiffSysErr();

    String lexerJavaFileName;
    if (spec.generatorThrows() == NoExceptionThrown.class) {
      lexerJavaFileName = invokeJflex();
    } else {
      lexerJavaFileName = null;
      try {
        generateLexerWithExpectedException();
      } catch (Exception e) {
        notifier.fireTestFailure(new Failure(desc, e));
        return null;
      }
    }

    try {
      assertSystemStream(diffSysOut, "System.out");
      assertSystemStream(diffSysErr, "System.err");
    } catch (AssertionError e) {
      notifier.fireTestFailure(new Failure(desc, e));
    }

    notifier.fireTestFinished(desc);
    return lexerJavaFileName;
  }

  private static void assertSystemStream(Optional<DiffOutputStream> diffStream, String streamName) {
    diffStream.ifPresent(
        s ->
            assertWithMessage("Content printed to %s", streamName)
                .that(s.remainingContent())
                .isEmpty());
  }

  private String generateLexerWithExpectedException() {
    try {
      invokeJflex();
    } catch (Throwable e) {
      assertWithMessage(
              "@TestCase indicates that the jflex generation must throw a "
                  + spec.generatorThrows().getSimpleName())
          .that(e)
          .isInstanceOf(spec.generatorThrows());
      if (spec.generatorThrowableCause() == Void.class) {
        assertWithMessage("@TestCase indicates that there is no cause for the generator exception")
            .that(e.getCause())
            .isNull();
      } else if (spec.generatorThrowableCause() != NoExceptionThrown.class) {
        assertWithMessage(
                "@TestCase indicates that cause of the generator exception is "
                    + spec.generatorThrowableCause())
            .that(e.getCause())
            .isInstanceOf(spec.generatorThrowableCause());
      }
      // Expected
      return null;
    }
    throw new AssertionError(
        "@TestCase indicates that the jflex generation throws a "
            + spec.generatorThrows().getSimpleName()
            + " but nothing was thrown");
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

  private Optional<DiffOutputStream> injectDiffSysErr() {
    if (!Strings.isNullOrEmpty(spec.syserr())) {
      File syserrFile = new File(spec.syserr());
      try {
        DiffOutputStream diffSysErr =
            new DiffOutputStream(Files.newReader(syserrFile, Charsets.UTF_8));
        System.setErr(new PrintStream(diffSysErr));
        return Optional.of(diffSysErr);
      } catch (FileNotFoundException e) {
        throw new AssertionError(
            "The golden syserr was not found: " + syserrFile.getAbsolutePath(), e);
      }
    }
    return Optional.empty();
  }

  private String invokeJflex() {
    Options.verbose = !spec.quiet();
    Options.jlex = spec.jlexCompat();
    String lexerJavaFileName = LexGenerator.generate(new File(spec.lex()));
    return checkNotNull(lexerJavaFileName);
  }

  private void buildLexer(RunNotifier notifier, String lexerJavaFileName) {
    Description desc = Description.createTestDescription(klass, "Compile java code");
    notifier.fireTestStarted(desc);
    try {
      JavacUtils.compile(ImmutableList.of(new File(lexerJavaFileName)));
      notifier.fireTestFinished(desc);
    } catch (CompilerException e) {
      notifier.fireTestFailure(new Failure(desc, e));
    }
  }
}
