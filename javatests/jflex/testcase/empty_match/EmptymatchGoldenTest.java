// test: emptymatch

package jflex.testcase.empty_match;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Matching the empty string. May lead to non-termination if not used carefully.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EmptymatchGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code emptymatch.flex} specification. */
  private final ScannerFactory<Emptymatch> scannerFactory = ScannerFactory.of(Emptymatch::new);

  private File testRuntimeDir = new File("javatests/jflex/testcase/empty_match");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "emptymatch-0.input"),
            new File(testRuntimeDir, "emptymatch-0.output"));
    compareSystemOutWith(golden);

    Emptymatch scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
