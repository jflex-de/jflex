package jflex.testing.testsuite;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.core.LexGenerator;
import jflex.testing.javac.CompilerException;
import jflex.testing.javac.JavacUtil;
import jflex.testing.testsuite.annotations.NoExceptionThrown;
import jflex.testing.testsuite.annotations.TestSpec;
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
    if (spec.generatorThrows() != NoExceptionThrown.class) {
      try {
        generateLexer(notifier);
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
      }
    } else {
      String lexerJavaFileName = generateLexer(notifier);
      buildLexer(notifier, lexerJavaFileName);
    }
    super.run(notifier);
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
