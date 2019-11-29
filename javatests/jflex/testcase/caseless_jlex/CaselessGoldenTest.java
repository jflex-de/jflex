// test: caseless

package jflex.testcase.caseless_jlex;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.testing.testsuite.golden.GoldenInOutFilePair;
import org.junit.Test;

/**
 * Tests {@code %ignorecase} with JLex semantics (char classes are caseless, in addition to strings
 * and chars.)
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class CaselessGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/caseless_jlex");

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

  private static Caseless createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }

  private static Caseless createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  /** Creates a scanner conforming to the {@code caseless.flex} spec. */
  private static Caseless createScanner(Reader reader) {
    return new Caseless(reader);
  }
}
