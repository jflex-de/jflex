package de.jflex.testcase.ccl_init;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.util.scanner.ScannerFactory;
import org.junit.Test;

/**
 * Test for <a href="https://github.com/jflex-de/jflex/issues/986">char class directives delete
 * existing classes #986</a>.
 */
public class CclInitTest {
  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  private final ScannerFactory<Ccl> scannerFactory = ScannerFactory.of(Ccl::new);

  @Test
  public void test_init() throws Exception {
    Ccl scanner = scannerFactory.createScannerWithContent("cab");
    assertThat(scanner.yylex()).isEqualTo(1);
    assertThat(scanner.yylex()).isEqualTo(0);
    assertThat(scanner.yylex()).isEqualTo(-1);
  }
}
