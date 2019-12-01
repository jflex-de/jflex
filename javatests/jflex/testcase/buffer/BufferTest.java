package jflex.testcase.buffer;

import static java.util.stream.Collectors.joining;

import com.google.common.io.CharSource;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Test for buffer expansion in {@code yy_refill()}.
 *
 * <p>See https://github.com/jflex-de/jflex/issues/62
 */
public class BufferTest {
  @Test
  public void test() throws Exception {
    String longString = Stream.generate(() -> " \r").limit(16400).collect(joining());
    EatAllScanner scanner = createScanner(longString);
    scanner.yylex();
  }

  private static EatAllScanner createScanner(String content) {
    try {
      return new EatAllScanner(CharSource.wrap(content).openStream());
    } catch (IOException impossible) {
      throw new RuntimeException(impossible);
    }
  }
}
