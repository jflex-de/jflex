// test: caseless

package jflex.testcase.caseless_jlex;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Tests {@code %ignorecase} with JLex semantics (char classes are caseless, in addition to strings
 * and chars.)
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CaselessGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code caseless.flex} spec. */
  private final ScannerFactory<Caseless> scannerFactory = ScannerFactory.of(Caseless::new);

  private final File testRuntimeDir = new File("javatests/jflex/testcase/caseless_jlex");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "caseless-0.input"),
            new File(testRuntimeDir, "caseless-0.output"));
    compareSystemOutWith(golden);

    Caseless scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }
}
