// test: fixedlook

package jflex.testcase.fixed_look;

import java.io.File;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * test fixed length lookahead, e.g. r1 / "some text"
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class FixedlookGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code fixedlook.flex} specification. */
  private final ScannerFactory<Fixedlook> scannerFactory = ScannerFactory.of(Fixedlook::new);

  private File testRuntimeDir = new File("javatests/jflex/testcase/fixed_look");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "fixedlook-0.input"),
            new File(testRuntimeDir, "fixedlook-0.output"));
    compareSystemOutWith(golden);

    Fixedlook scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  @Test
  public void goldenTest1() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "fixedlook-1.input"),
            new File(testRuntimeDir, "fixedlook-1.output"));
    compareSystemOutWith(golden);

    Fixedlook scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  @Test
  public void goldenTest2() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "fixedlook-2.input"),
            new File(testRuntimeDir, "fixedlook-2.output"));
    compareSystemOutWith(golden);

    Fixedlook scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  @Test
  public void goldenTest3() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "fixedlook-3.input"),
            new File(testRuntimeDir, "fixedlook-3.output"));
    compareSystemOutWith(golden);

    Fixedlook scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
