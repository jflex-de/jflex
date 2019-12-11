// test: line

package jflex.testcase.line_cont;

import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class LineGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code line.flex} specification. */
  private final ScannerFactory<Line> scannerFactory = ScannerFactory.of(Line::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
