// test: ctorarg

package jflex.testcase.ctorarg;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Tests scanner generated from {@code ctorarg.flex}.
 *
 * <p>Tests that the lex spec has an option to add arguments to constructor and yylex. See feature request #156.
 */
@Generated("jflex.migration.Migrator")
public class CtorArgPositiveTest extends AbstractGoldenTest {

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
