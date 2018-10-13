package jflex.testing;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
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

  private void compile(RunNotifier notifier, List<? extends String> javaSourceFileNames)
      throws CompilerException {
    List<File> javaSourceFiles =
        javaSourceFileNames.stream().map(File::new).collect(Collectors.toList());
    compile(notifier, javaSourceFiles);
  }

  private void compile(RunNotifier notifier, Iterable<? extends File> javaSourceFiles)
      throws CompilerException {
    notifier.fireTestStarted(Description.createTestDescription(klass, "Compile java code"));
    JavacUtil.compile(javaSourceFiles);
  }
}
