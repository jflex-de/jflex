// test: caseless

package jflex.testcase.caseless_jflex;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import org.junit.Test;

/**
 * Tests {@code %ignorecase} with jflex semantics (only strings and chars are caseless).
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CaselessGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/caseless_jflex");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "caseless-0.input"),
            new File(testRuntimeDir, "caseless-0.output"));
    compareSystemOutWith(golden);

    Caseless scanner = createScanner(golden.inputFile);
    scanner.yylex();
  }

  /** Scanner generated from {@code caseless.flex}. */
  private static Caseless createScanner(File inputFile) throws FileNotFoundException {
    return new Caseless(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }
}
