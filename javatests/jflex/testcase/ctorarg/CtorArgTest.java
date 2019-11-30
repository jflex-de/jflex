// test: ctorarg

package jflex.testcase.ctorarg;

import jflex.testing.testsuite.golden.AbstractGoldenTest;
import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Tests that the lex spec has an option to add arguments to constructor and yylex. See feature
 * request <a href="https://github.com/jflex-de/jflex/issues/156">#156 provide option to add
 * arguments to constructor and yylex</a>.
 */
public class CtorArgTest extends AbstractGoldenTest {

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }

  /** scanner generated from {@code ctor-arg.flex}. */
  private ScannerFactory<Ctorarg> scannerFactory = ScannerFactory.of(Ctorarg::new);
}
