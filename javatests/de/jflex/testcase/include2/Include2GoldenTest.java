// test: include2

package jflex.testcase.include2;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * tests yychar and zzAtBOL in included files using yyPushStream and yyPopStream bugs
 * <http://sourceforge.net/p/jflex/bugs/105> and <http://sourceforge.net/p/jflex/bugs/106>
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class Include2GoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code include2.flex} specification. */
  private final ScannerFactory<Include2> scannerFactory = ScannerFactory.of(Include2::new);

  private final File testRuntimeDir = new File("javatests/jflex/testcase/include2");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "include2-0.input"),
            new File(testRuntimeDir, "include2-0.output"));
    compareSystemOutWith(golden);

    Include2 scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
