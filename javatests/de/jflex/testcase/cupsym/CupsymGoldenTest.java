// test: cupsym

package de.jflex.testcase.cupsym;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * bug <a href "https://github.com/jflex-de/jflex/issues/58">#58 %cupsym doesn't affect all code</a>
 *
 * <p>If %cupdebug is used, the code generated for getTokenName uses "sym" as the cup symbol name
 * even if %cupsym is specified. The comment correctly uses the cupSymbol variable, but the
 * generated code does not.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CupsymGoldenTest extends AbstractGoldenTest {

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/cupsym");

  /** scanner generated from {@code cupsym.flex}. */
  private final ScannerFactory<Cupsym> scannerFactory = ScannerFactory.of(Cupsym::new);

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "cupsym-0.input"),
            new File(testRuntimeDir, "cupsym-0.output"));
    compareSystemOutWith(golden);

    Cupsym scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.debug_next_token();
  }
}
