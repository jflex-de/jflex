// test: ccl-bug

package jflex.testcase.ccl_bug;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Tests scanner generated from {@code ccl-bug.flex}.
 *
 * <p>#1498726 char [] ZZ_CMAP is incorrect Generated Yylex.java does not compile, because of
 * missing ","
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class CclBugGoldenTest extends AbstractGoldenTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  private static CclBug createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, Charset.forName("UTF-8")));
  }

  private static CclBug createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  private static CclBug createScanner(Reader reader) {
    return new CclBug(reader);
  }
}
