// test: ccl-bug

package jflex.testcase.ccl_bug;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Test against <a href="https://github.com/jflex-de/jflex/issues/81>#81 {@code char[] ZZ_CMAP} is
 * incorrect</a>
 *
 * <p>Generated CclBug.java does not compile, because of missing ",".
 */
// TODO Migrate this test to proper unit tests.
@Generated("jflex.migration.Migrator")
public class CclBugTest extends AbstractGoldenTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  private static CclBug createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  /** Creates scanner generated from {@code ccl-bug.flex}. */
  private static CclBug createScanner(Reader reader) {
    return new CclBug(reader);
  }
}
