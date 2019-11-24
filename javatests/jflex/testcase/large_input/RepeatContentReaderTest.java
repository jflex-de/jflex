package jflex.testcase.large_input;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/** Test {@link RepeatContentReader}. */
public class RepeatContentReaderTest {
  @Test
  public void read_sizeLessThanContent() throws IOException {
    int length = 11;
    Reader reader = new RepeatContentReader(length, "The content is larger than the declared size");
    String read = CharStreams.toString(reader);
    assertThat(read).hasLength(length);
    assertThat(read).isEqualTo("The content");
  }

  @Test
  public void read_sizeMoreThanContent() throws IOException {
    int length = 25;
    Reader reader = new RepeatContentReader(length, "foo bar!");
    String read = CharStreams.toString(reader);
    assertThat(read).hasLength(length);
    assertThat(read).isEqualTo("foo bar!foo bar!foo bar!f");
  }

  @Test
  public void read_offset() throws IOException {
    Reader reader = new RepeatContentReader(25, "abc");
    char[] cbuf = new char[7];
    int read = reader.read(cbuf, 1, 2);
    assertThat(read).isEqualTo(2);
    assertThat(new String(cbuf)).isEqualTo("\0ab\0\0\0\0");
  }

  @Test
  public void read_offsetLoop() throws IOException {
    Reader reader = new RepeatContentReader(25, "abc");
    char[] cbuf = new char[7];
    int read = reader.read(cbuf, 1, 7 - 1);
    assertThat(read).isEqualTo(6);
    assertThat(new String(cbuf)).isEqualTo("\0abcabc");
  }
}
