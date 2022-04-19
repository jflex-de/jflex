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
package de.jflex.testing.testsuite;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import de.jflex.testing.diff.DiffOutputStream;
import de.jflex.testing.testsuite.annotations.NoExceptionThrown;
import de.jflex.testing.testsuite.annotations.TestSpec;
import de.jflex.util.javac.JavacUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import jflex.core.OptionUtils;
import jflex.generator.LexGenerator;
import jflex.logging.Out;
import jflex.option.Options;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class JFlexTestRunner extends BlockJUnit4ClassRunner {

  private final Class<?> klass;
  private final TestSpec spec;
  private final PrintStream originalSysOut = System.out;
  private final PrintStream originalSysErr = System.err;

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

    try {
      String lexerJavaFileName;
      if (spec.generatorThrows() == NoExceptionThrown.class) {
        lexerJavaFileName = invokeJflex();
      } else {
        lexerJavaFileName = null;
        generateLexerWithExpectedException();
      }

      assertSystemStream(diffSysOut, "System.out");
      assertSystemStream(diffSysErr, "System.err");

      return lexerJavaFileName;
    } catch (Throwable th) {
      notifier.fireTestFailure(new Failure(desc, th));
      return null;
    } finally {
      if (diffSysOut.isPresent()) {
        System.setOut(originalSysOut);
      }
      if (diffSysErr.isPresent()) {
        System.setErr(originalSysErr);
      }
      notifier.fireTestFinished(desc);
    }
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
                "@TestCase indicates that cause of the generator exception is %s but it was %s\n",
                spec.generatorThrowableCause().getSimpleName(), e.getCause())
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
            new DiffOutputStream(Files.newReader(sysoutFile, StandardCharsets.UTF_8));
        diffSysOut.setComparisonFailureHandler(
            failure -> {
              throw new Error("System.out differs: " + spec.sysout(), failure);
            });
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
            new DiffOutputStream(Files.newReader(syserrFile, StandardCharsets.UTF_8));
        diffSysErr.setComparisonFailureHandler(
            failure -> {
              throw new Error("System.err differs: " + spec.syserr(), failure);
            });
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
    if (Options.encoding == null) {
      OptionUtils.setDefaultOptions();
    }
    Options.jlex = spec.jlexCompat();
    Options.dump = spec.dump();
    Options.verbose = !spec.quiet();
    Options.unused_warning = spec.warnUnused();
    LexGenerator lexGenerator = new LexGenerator(new File(spec.lex()));
    String lexerJavaFileName = checkNotNull(lexGenerator.generate());
    if (spec.minimizedDfaStatesCount() > 0) {
      assertWithMessage("There should be %d minimized states in the DFA")
          .that(lexGenerator.minimizedDfaStatesCount())
          .isEqualTo(spec.minimizedDfaStatesCount());
    }
    return lexerJavaFileName;
  }

  private void buildLexer(RunNotifier notifier, String lexerJavaFileName) {
    Description desc = Description.createTestDescription(klass, "Compile java code");
    notifier.fireTestStarted(desc);
    try {
      JavacUtils.compile(ImmutableList.of(new File(lexerJavaFileName)));
      notifier.fireTestFinished(desc);
    } catch (Throwable th) {
      notifier.fireTestFailure(new Failure(desc, th));
    } finally {
      notifier.fireTestFinished(desc);
    }
  }
}
