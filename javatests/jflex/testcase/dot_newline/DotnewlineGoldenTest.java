// test: dotnewline

package jflex.testcase.dot_newline;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * testing that dot (.) matches [^\n\r\u000B\u000C\u0085\u2028\u2029] and that \R matches "\r\n" |
 * [\n\r\u000B\u000C\u0085\u2028\u2029]
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class DotnewlineGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/dot_newline");

  /** scanner generated from {@code dotnewline.flex}. */
  private final ScannerFactory<Dotnewline> scannerFactory = ScannerFactory.of(Dotnewline::new);

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "dotnewline-0.input"),
            new File(testRuntimeDir, "dotnewline-0.output"));
    compareSystemOutWith(golden);

    Dotnewline scanner = scannerFactory.createScannerForFile(golden.inputFile);
    scanner.yylex();
  }
}
