// test: ctorarg

package jflex.testcase.ctorarg;

import java.io.Reader;
import javax.annotation.Generated;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * Tests that the lex spec has an option to add arguments to constructor and yylex. See feature
 * request <a href="https://github.com/jflex-de/jflex/issues/156">#156 provide option to add
 * arguments to constructor and yylex</a>.
 */
@Generated("jflex.migration.Migrator")
public class CtorArgTest extends AbstractGoldenTest<Ctorarg> {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    createScanner("");
  }

  /** scanner generated from {@code ctor-arg.flex}. */
  @Override
  protected Ctorarg createScanner(Reader reader) {
    return new Ctorarg(reader);
  }
}
