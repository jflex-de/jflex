// test: count

package jflex.testcase.count;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * test line/column/char counting
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CountGoldenTest extends AbstractGoldenTest {

  private ScannerFactory<Count> scannerFactory = ScannerFactory.of(Count::new);

  @Test
  public void goldenTest0() throws Exception {

    // The .input / .output Golden files
    File testRuntimeDir = new File("javatests/jflex/testcase/count");
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "count-0.input"), new File(testRuntimeDir, "count-0.output"));

    compareSystemOutWith(golden);

    // Scanner for test/cases/count/count.flex
    Count scanner = scannerFactory.createScanner(golden.inputFile);
    scanner.yylex();
  }
}
