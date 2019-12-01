// test: eofclose

package jflex.testcase.eofclose;

import static jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * test eofclose directive
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class EofcloseGoldenTest extends AbstractGoldenTest {

  private File testRuntimeDir = new File("javatests/jflex/testcase/eofclose");

  @Test
  public void goldenTest0() throws Exception {
    File inputFile = new File(testRuntimeDir, "eofclose-0.input");
    Reader reader = Files.newReader(inputFile, StandardCharsets.UTF_8);
    Eofclose scanner = new Eofclose(reader);
    while (scanner.yylex() != Eofclose.YYEOF) {}
    assertThrows("The scanner should close the reader at EOF", IOException.class, reader::read);
  }
}
