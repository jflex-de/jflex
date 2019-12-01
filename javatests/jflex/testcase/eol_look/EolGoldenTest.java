// test: eol

package jflex.testcase.eol_look;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * testing finite choice lookahead, esp eol ($)
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EolGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code eol.flex} specification. */
  private final ScannerFactory<Eol> scannerFactory = ScannerFactory.of(Eol::new);

  private File testRuntimeDir = new File("javatests/jflex/testcase/eol_look");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "eol-0.input"), new File(testRuntimeDir, "eol-0.output"));
    compareSystemOutWith(golden);

    Eol scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }
}
