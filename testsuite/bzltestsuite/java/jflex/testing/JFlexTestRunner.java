package jflex.testing;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.LexGenerator;
import jflex.testing.annotations.TestSpec;
import jflex.testing.exception.CompilerException;
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
    String lexerJavaFileName = generateLexer(notifier);
    try {
      compile(notifier, ImmutableList.of(lexerJavaFileName));
    } catch (CompilerException e) {
      notifier.fireTestFailure(
          new Failure(Description.createTestDescription(klass, "Failed to compile java code"), e));
    }
    super.run(notifier);
  }

  private String generateLexer(RunNotifier notifier) {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Generate Lexer"));
    String lexerJavaFileName = LexGenerator.generate(new File(spec.lex()));
    return lexerJavaFileName;
  }

  private void compile(RunNotifier notifier, ImmutableList<String> javaFileNames)
      throws CompilerException {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Compile java code"));
    JavacUtil.compile(javaFileNames);
  }
}
