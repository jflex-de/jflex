// test: cclmacros

package jflex.testcase.ccl_macros;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * test for issue <a href"https://github.com/jflex-de/jflex/issues/216">#216 Macros in character
 * classes are not supported</a>
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CclmacrosGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code cclmacros.flex} specification. */
  private final ScannerFactory<Cclmacros> scannerFactory = ScannerFactory.of(Cclmacros::new);

  private File testRuntimeDir = new File("javatests/jflex/testcase/ccl_macros");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "cclmacros-0.input"),
            new File(testRuntimeDir, "cclmacros-0.output"));
    compareSystemOutWith(golden);

    Cclmacros scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
