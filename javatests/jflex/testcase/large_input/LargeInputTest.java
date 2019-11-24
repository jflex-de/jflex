package jflex.testcase.large_input;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests that the scanner can parse a very large input.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/536">#536</a>.
 */
public class LargeInputTest {

  /** Tests a well-formed input content larger than MAX_INT (2^32-1). */
  @Test
  public void consumeLargeInput() throws Exception {
    final String content = "hello foo\n";
    long size = Integer.MAX_VALUE / content.length() * content.length();
    size += 3 * (long) content.length(); // a few more
    assertWithMessage("Tests an input content larger than MAX_INT (2^32-1)")
        .that(size)
        .isGreaterThan((long) Integer.MAX_VALUE + 1L);
    Reader largeContentReader = new RepeatContentReader(size, content);
    LargeInputScanner scanner = createScanner(largeContentReader);
    // FIX bug #536
    // This is not expected, only how JFlex < 1.8 behaves. This assertion only demonstrates the
    // test reproduces the bug.
    assertThrows(IllegalStateException.class, () -> readUntilEof(scanner));
  }

  @Test
  public void consumeSmallInput() throws Exception {
    long size = 50;
    Reader largeContentReader = new RepeatContentReader(size, "hello foo\n");
    LargeInputScanner scanner = createScanner(largeContentReader);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
  }

  @Test
  public void consumeUnterminatedLine() throws Exception {
    LargeInputScanner scanner = createScanner(CharSource.wrap("Hello\nworld").openBufferedStream());
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  private static void readUntilEof(LargeInputScanner scanner) throws IOException {
    while (scanner.yylex() != State.END_OF_FILE) {}
  }

  private static LargeInputScanner createScanner(Reader reader) {
    return new LargeInputScanner(reader);
  }
}
