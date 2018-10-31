package jflex.testing;

import static com.google.common.truth.Truth.assertThat;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

/** Test for {@link DiffOutputStreamTest}. */
public class DiffOutputStreamTest {

  @Test
  public void testOneLine_equal() throws Exception {
    String in = "Hello world!\n";
    @SuppressWarnings("RedundantStringConstructorCall")
    String out = new String(in);
    DiffOutputStream differ = diff(in, out);
    assertThat(differ.isCompleted()).isTrue();
  }

  @Test
  public void testOneLine_differ_inHasMore() throws Exception {
    String in = "Hello world!\n";
    String out = "Hello\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  @Test
  public void testOneLine_differs_outHasMore() throws Exception {
    String in = "Hello!\n";
    String out = "Hello world!\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  private static DiffOutputStream diff(String in, String out) throws IOException {
    DiffOutputStream diffStream = new DiffOutputStream(new StringReader(in));
    try {
      diffStream.write(out.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException impossible) {
    }
    diffStream.flush();
    return diffStream;
  }
}
