// test: lookmacro

package de.jflex.testcase.look_macro;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * test for bug <a href="https://github.com/jflex-de/jflex/issues/100">#100 (macro use in length
 * choice lookahead)</a>
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class LookmacroGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code lookmacro.flex} specification. */
  private final ScannerFactory<Lookmacro> scannerFactory = ScannerFactory.of(Lookmacro::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/look_macro");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "lookmacro-0.input"),
            new File(testRuntimeDir, "lookmacro-0.output"));
    compareSystemOutWith(golden);

    Lookmacro scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
