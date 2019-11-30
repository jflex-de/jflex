// test: ccl-bug

package jflex.testcase.ccl_bug;

import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Test against <a href="https://github.com/jflex-de/jflex/issues/81>#81 {@code char[] ZZ_CMAP} is
 * incorrect</a>
 *
 * <p>Generated CclBug.java does not compile, because of missing ",".
 */
public class CclBugTest extends AbstractGoldenTest<CclBug> {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  /** Creates scanner generated from {@code ccl-bug.flex}. */
  @Override
  protected CclBug createScanner(Reader reader) {
    return new CclBug(reader);
  }
}
