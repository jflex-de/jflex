package jflex.testing;

import static com.google.common.base.Preconditions.checkNotNull;
import static junit.framework.TestCase.fail;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.LexGenerator;
import jflex.testing.annotations.NoExceptionThrown;
import jflex.testing.annotations.TestSpec;
import jflex.testing.assertion.MoreAsserts;
import jflex.testing.javac.CompilerException;
import jflex.testing.javac.JavacUtil;
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
      MoreAsserts.assertThrows(
          "@TestCase indicates that the jflex generation must throw a "
              + spec.generatorThrows().getSimpleName(),
          spec.generatorThrows(),
          () -> generateLexer(notifier));
    } else {
      String lexerJavaFileName = generateLexer(notifier);
      buildLexer(notifier, lexerJavaFileName);
    }
    super.run(notifier);
  }

  private String generateLexer(RunNotifier notifier) {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Generate Lexer"));
    String lexerJavaFileName = LexGenerator.generate(new File(spec.lex()));

    // The generator didn't throw an exception but we expected one
    if (spec.generatorThrows() != NoExceptionThrown.class) {
      fail(
          String.format(
              "Text expected to throw %s but JFlex didn't throw anything",
              spec.generatorThrows().getName()));
    }
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
