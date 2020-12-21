package jflex.testcase.ccl_16;

import com.google.common.io.CharSource;
import java.io.IOException;
import org.junit.Test;

/**
 * Test for following bug: If {@code %16bit} is used with the {@code ccl.flex}, the scanner fails on
 * the input with an {@link ArrayOutOfBoundsException}. Turned out to be an overflow in {@code
 * ZZ_PACKED_CMAP} (using {@code \u10000}).
 */
public class Ccl16Test {

  @Test
  public void ok() throws Exception {
    CclScanner scanner = createScanner("some content\n" + "ignored anyway\n");
    // just assert that there is no exception
    scanner.yylex();
  }

  private static CclScanner createScanner(String content) throws IOException {
    return new CclScanner(CharSource.wrap(content).openStream());
  }
}
