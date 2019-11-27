// test: ctorarg

package jflex.testcase.ctorarg;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Tests scanner generated from {@code ctor-arg.flex}.
 *
 * <p>test feature request <a href="https://github.com/jflex-de/jflex/issues/156">#156 provide
 * option to add arguments to constructor and yylex<a/>.
 *
 * <p>Positive test case: arguments are specified.
 */
@Generated("jflex.migration.Migrator")
public class CtorArgTest extends AbstractGoldenTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  private static Ctorarg createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  private static Ctorarg createScanner(Reader reader) {
    return new Ctorarg(reader);
  }
}
