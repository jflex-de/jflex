// test: eofmin

package jflex.testcase.eof_min;

import java.io.File;
import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import org.junit.Test;

/**
 * Regression test <a href="https://github.com/jflex-de/jflex/issues/82">#82 State minimizer ignores
 * EOF actions</a>
 *
 * <p>Should not get a warning for equivalent lex states. Should end up in different states for the
 * two inputs.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EofminGoldenTest extends AbstractGoldenTest<Eofmin> {

  private File testRuntimeDir = new File("javatests/jflex/testcase/eof_min");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eofmin-0.input"),
            new File(testRuntimeDir, "eofmin-0.output"));
    compareSystemOutWith(golden);

    Eofmin scanner = createScanner(golden.inputFile);
    while (scanner.yylex() != Eofmin.YYEOF) {}
    ;
  }

  @Test
  public void goldenTest1() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eofmin-1.input"),
            new File(testRuntimeDir, "eofmin-1.output"));
    compareSystemOutWith(golden);

    Eofmin scanner = createScanner(golden.inputFile);
    while (scanner.yylex() != Eofmin.YYEOF) {}
    ;
  }

  /** Creates a scanner conforming to the {@code eofmin.flex} specification. */
  @Override
  protected Eofmin createScanner(Reader reader) {
    return new Eofmin(reader);
  }
}
