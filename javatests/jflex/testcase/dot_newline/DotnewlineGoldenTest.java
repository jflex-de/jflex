// test: dotnewline

package jflex.testcase.dot_newline;

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
 * Tests scanner generated from {@code dotnewline.flex}.
 *
 * <p>testing that dot (.) matches [^\n\r\u000B\u000C\u0085\u2028\u2029] and that \R matches "\r\n"
 * | [\n\r\u000B\u000C\u0085\u2028\u2029]
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class DotnewlineGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/dot_newline");

  @Test
  public void goldenTest0() throws Exception {
    GoldenInOutFilePair golden =
        new GoldenInOutFilePair(
            new File(testRuntimeDir, "dotnewline-0.input"),
            new File(testRuntimeDir, "dotnewline-0.output"));
    compareSystemOutWith(golden);

    Dotnewline scanner = createScanner(golden.inputFile);
    scanner.yylex();
  }

  private static Dotnewline createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }

  private static Dotnewline createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  private static Dotnewline createScanner(Reader reader) {
    return new Dotnewline(reader);
  }
}
