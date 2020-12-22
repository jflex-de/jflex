// test: count

package de.jflex.testcase.count;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * test line/column/char counting
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CountGoldenTest extends AbstractGoldenTest {

  private final ScannerFactory<Count> scannerFactory = ScannerFactory.of(Count::new);

  @Test
  public void goldenTest0() throws Exception {

    // The .input / .output Golden files
    File testRuntimeDir = new File("javatests/de/jflex/testcase/count");
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "count-0.input"), new File(testRuntimeDir, "count-0.output"));

    compareSystemOutWith(golden);

    // Scanner for test/cases/count/count.flex
    Count scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }
}
