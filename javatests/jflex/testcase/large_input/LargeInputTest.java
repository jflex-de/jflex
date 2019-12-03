package jflex.testcase.large_input;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.CharSource;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests that the scanner can parse a very large input.
 *
 * <p>See <a href="https://github.com/jflex-de/jflex/issues/536">#536</a>.
 */
public class LargeInputTest {

  /**
   * Tests a well-formed input content larger than MAX_INT (2^32-1). The scanner should never
   * encounter {@code zzchar < 0} and hence never throw {@link NegativeYyCharException}.
   */
  @Test
  public void consumeLargeInput() throws Exception {
    final String content = "One every character the `yychar` is incremented, but don't overflow!\n";
    long size = 4 * (long) content.length();
    Reader largeContentReader = new RepeatContentReader(size, content);
    LargeInputScanner scanner = createScanner(largeContentReader);
    // Pretend we move close to MAX_INT
    scanner.fakeRead(Integer.MAX_VALUE - 2 * content.length());
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.BEFORE_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.AFTER_2GB);
    assertThat(scanner.yylex()).isEqualTo(State.END_OF_FILE);
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

  private static LargeInputScanner createScanner(Reader reader) {
    return new LargeInputScanner(reader);
  }
}
