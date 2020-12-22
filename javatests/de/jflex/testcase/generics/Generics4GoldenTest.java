// test: generics4

package de.jflex.testcase.generics;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * Test for feature request <a href="https://github.com/jflex-de/jflex/issues/75">#75 Support
 * generics for %type and %extends</a>.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class Generics4GoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code generics4.flex} specification. */
  private final ScannerFactory<Generics4> scannerFactory = ScannerFactory.of(Generics4::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/generics");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "generics4-0.input"),
            new File(testRuntimeDir, "generics4-0.output"));
    compareSystemOutWith(golden);

    Generics4 scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }
}
