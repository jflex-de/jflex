package jflex.testing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import jflex.LexGenerator;
import jflex.testing.annotations.TestSpec;
import org.junit.runner.Description;
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
    generateLexer(notifier);
    super.run(notifier);
  }

  private void generateLexer(RunNotifier notifier) {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Generate Lexer"));
    LexGenerator.generate(new File(spec.lex()));
  }
}
