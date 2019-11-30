// test: ctorarg

package jflex.testcase.ctorarg;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Tests scanner generated from {@code ctor-arg.flex}.
 *
 * <p>Tests that the lex spec has an option to add arguments to constructor and yylex. See feature
 * request <a href="https://github.com/jflex-de/jflex/issues/156">#156 provide option to add
 * arguments to constructor and yylex</a>.
 */
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
