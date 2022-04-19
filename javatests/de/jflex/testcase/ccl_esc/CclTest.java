// test: ccl-esc

package de.jflex.testcase.ccl_esc;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * bug-test for [".."] style character escapes (<a
 * href="https://github.com/jflex-de/jflex/issues/48">#48</a>)
 */
public class CclTest {
  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  private final ScannerFactory<Ccl> scannerFactory = ScannerFactory.of(Ccl::new);

  @Test
  public void tokenOther_a() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("a");
    assertThat(scanner.yylex()).isEqualTo(Token.OTHER);
  }

  @Test
  public void tokenOther_hello() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("hello");
    assertThat(scanner.yylex()).isEqualTo(Token.OTHER);
  }

  @Test
  public void tokenAB() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("ab");
    assertThat(scanner.yylex()).isEqualTo(Token.AB);
  }
}
