// test: Over2GbInput

package jflex.testcase.large_input;

import java.io.File;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests that the scanner can parse a very large input.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/536">#536</a>.
 */
public class LargeInputTest {

  private File testRuntimeDir = new File("jflex/testcase/large_input");

  @Test
  public void consumeLargeInput() throws Exception {
    Reader largeContentReader = new RepeatContentReader((long) Integer.MAX_VALUE + 1L, "Hello ");
    LargeInputScanner scanner = createScanner(largeContentReader);
    scanner.yylex();
  }

  private static LargeInputScanner createScanner(Reader reader) {
    return new LargeInputScanner(reader);
  }
}
