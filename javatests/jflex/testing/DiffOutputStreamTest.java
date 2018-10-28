package jflex.testing;

import static jflex.testing.assertion.MoreAsserts.assertThrows;

import java.io.BufferedOutputStream;
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
    diff(in, out);
  }

  @Test
  public void testOneLine_differs() throws Exception {
    String in = "Hello world!\n";
    String out = "Hello\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  @Test
  public void testOneLine_differs2() throws Exception {
    String in = "Hello!\n";
    String out = "Hello world!\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  private static void diff(String in, String out) throws IOException {
    BufferedOutputStream diffStream =
        new BufferedOutputStream(new DiffOutputStream(new StringReader(in)));
    try {
      diffStream.write(out.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException impossible) {
    }
    diffStream.flush();
  }
}
