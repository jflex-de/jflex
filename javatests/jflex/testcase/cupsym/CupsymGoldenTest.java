// test: cupsym

package jflex.testcase.cupsym;

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
 * Tests scanner generated from {@code cupsym.flex}.
 *
 * <p>bug <a href "https://github.com/jflex-de/jflex/issues/58">#58 %cupsym doesn't affect all
 * code</a>
 *
 * <p>If %cupdebug is used, the code generated for getTokenName uses "sym" as the cup symbol name
 * even if %cupsym is specified. The comment correctly uses the cupSymbol variable, but the
 * generated code does not.
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class CupsymGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/cupsym");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "cupsym-0.input"),
            new File(testRuntimeDir, "cupsym-0.output"));
    compareSystemOutWith(golden);

    Cupsym scanner = createScanner(golden.inputFile);
    scanner.debug_next_token();
  }

  private static Cupsym createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }

  private static Cupsym createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  private static Cupsym createScanner(Reader reader) {
    return new Cupsym(reader);
  }
}
