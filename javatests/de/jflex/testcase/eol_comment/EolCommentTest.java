// test: eolcomment

package de.jflex.testcase.eol_comment;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * bug <a href="https://github.com/jflex-de/jflex/issues/61">#61 comment in jflex file</a>.
 *
 * <p>end of line comments (//) at end of action did not compile. (Generated closing bracket is on
 * same line)
 */
public class EolCommentTest {

  /** Creates a scanner conforming to the {@code eolcomment.flex} specification. */
  private final ScannerFactory<EolComment> scannerFactory = ScannerFactory.of(EolComment::new);

  /** Tests that the scanner was successfully generated and can be instantiated. */
  @Test
  public void canInstantiateScanner() throws Exception {
    scannerFactory.createScannerWithContent("");
  }
}
