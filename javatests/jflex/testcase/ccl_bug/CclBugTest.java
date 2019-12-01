// test: ccl-bug

package jflex.testcase.ccl_bug;

import jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Test against <a href="https://github.com/jflex-de/jflex/issues/81>#81 {@code char[] ZZ_CMAP} is
 * incorrect</a>
 *
 * <p>Generated CclBug.java does not compile, because of missing ",".
 */
public class CclBugTest {

  /** Creates scanner generated from {@code ccl-bug.flex}. */
  ScannerFactory<CclBug> scannerFactory = ScannerFactory.of(reader -> new CclBug(reader));

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
