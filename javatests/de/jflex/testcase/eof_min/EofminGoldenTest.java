// test: eofmin

package de.jflex.testcase.eof_min;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
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
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EofminGoldenTest extends AbstractGoldenTest {

  private final ScannerFactory<Eofmin> scannerFactory = ScannerFactory.of(Eofmin::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/eof_min");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eofmin-0.input"),
            new File(testRuntimeDir, "eofmin-0.output"));
    compareSystemOutWith(golden);

    Eofmin scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      scanner.yylex();
    }
  }

  @Test
  public void goldenTest1() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eofmin-1.input"),
            new File(testRuntimeDir, "eofmin-1.output"));
    compareSystemOutWith(golden);

    Eofmin scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      scanner.yylex();
    }
  }
}
