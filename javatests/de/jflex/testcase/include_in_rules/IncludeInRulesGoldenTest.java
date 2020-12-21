// test: IncludeInRules

package de.jflex.testcase.include_in_rules;

import de.jflex.testing.testsuite.golden.AbstractGoldenTest;
import de.jflex.testing.testsuite.golden.GoldenInOutFilePair;
import de.jflex.util.scanner.ScannerFactory;
import java.io.File;
import org.junit.Test;

/**
 * bug #116 [enable %include in rules section]
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/de/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class IncludeInRulesGoldenTest extends AbstractGoldenTest {

  /** Creates a scanner conforming to the {@code IncludeInRules.flex} specification. */
  private final ScannerFactory<IncludeInRulesScanner> scannerFactory =
      ScannerFactory.of(IncludeInRulesScanner::new);

  private final File testRuntimeDir = new File("javatests/de/jflex/testcase/include_in_rules");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "IncludeInRules-0.input"),
            new File(testRuntimeDir, "IncludeInRules-0.output"));
    compareSystemOutWith(golden);

    IncludeInRulesScanner scanner = scannerFactory.createScannerForFile(golden.inputFile);
    while (!scanner.yyatEOF()) {
      System.out.println(scanner.yylex());
    }
  }
}
