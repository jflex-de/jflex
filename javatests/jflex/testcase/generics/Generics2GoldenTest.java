// test: generics2

package jflex.testcase.generics;

import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Test for feature request [ 1212181 ] Support generics for %type and %extends Tests %type,
 * %ctorarg
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class Generics2GoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code generics2.flex} specification. */
  private final ScannerFactory<Generics2> scannerFactory = ScannerFactory.of(Generics2::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
