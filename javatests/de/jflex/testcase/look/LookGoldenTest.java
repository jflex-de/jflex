// test: look

package de.jflex.testcase.look;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * test for bug <a href="https://github.com/jflex-de/jflex/issues/74">#74 (yytext() return longer
 * than expected with lookahead)</a>
 *
 * <p>(this tests fixed lookahead/base only)
 *
 * <p>the pattern ab / cde { System.out.println(yytext()); return 2; } abcd / f {
 * System.out.println(yytext()); return 7; }
 *
 * <p>with string abcde
 *
 * <p>prints out "abcd", but returns 2.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class LookGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code look.flex} specification. */
  private final ScannerFactory<Look> scannerFactory = ScannerFactory.of(Look::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/look");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "look-0.input"), new File(testRuntimeDir, "look-0.output"));
    compareSystemOutWith(golden);

    Look scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }

  @Test
  public void goldenTest1() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "look-1.input"), new File(testRuntimeDir, "look-1.output"));
    compareSystemOutWith(golden);

    Look scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
