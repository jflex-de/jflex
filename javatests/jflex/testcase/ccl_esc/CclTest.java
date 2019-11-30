// test: ccl-esc

package jflex.testcase.ccl_esc;

import static com.google.common.truth.Truth.assertThat;

import java.io.Reader;
import jflex.testing.testsuite.golden.AbstractGoldenTest;
import org.junit.Test;

/**
 * bug-test for [".."] style character escapes (<a
 * href="https://github.com/jflex-de/jflex/issues/48">#48</a>)
 */
public class CclTest extends AbstractGoldenTest<Ccl> {

  @Test
  public void tokenOther_a() throws Exception {
    Ccl scanner = createScanner("a");
    assertThat(scanner.yylex()).isEqualTo(Token.OTHER);
  }

  @Test
  public void tokenOther_hello() throws Exception {
    Ccl scanner = createScanner("hello");
    assertThat(scanner.yylex()).isEqualTo(Token.OTHER);
  }

  @Test
  public void tokenAB() throws Exception {
    Ccl scanner = createScanner("ab");
    assertThat(scanner.yylex()).isEqualTo(Token.AB);
  }

  /** Creates a scanner conforming to the {@code ccl.flex} specification. */
  @Override
  protected Ccl createScanner(Reader reader) {
    return new Ccl(reader);
  }
}
