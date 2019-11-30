// test: ccl

package jflex.testcase.ccl_esc;

import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * bug-test for [".."] style character escapes (<a
 * href="https://github.com/jflex-de/jflex/issues/48">#48</a>)
 *
 * <p>Note: This test was generated from {@code jflex-testsuite-maven-plugin} test cases. The test
 * relies on golden files for testing, expecting the scanner to output logs on the {@code
 * System.out}. Please migrate to proper unit tests, as describe in <a
 * href="https://github.com/jflex-de/jflex/tree/master/javatests/jflex/testcase">
 * //javatest/jflex/testcase</a>.
 */
// TODO Migrate this test to proper unit tests.
public class CclGoldenTest extends AbstractGoldenTest<Ccl> {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  @Override
  protected Ccl createScanner(Reader reader) {
    return new Ccl(reader);
  }
}
