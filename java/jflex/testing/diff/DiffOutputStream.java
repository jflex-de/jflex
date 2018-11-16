package jflex.testing.diff;

import static com.google.common.truth.Truth.assertWithMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * An output streams that asserts that every printed lined is equal to the one from the expected
 * input.
 *
 * <p>Each input line must be less than 2048 bytes.
 *
 * <p>The output is not saved anywhere.
 */
public class DiffOutputStream extends OutputStream {

  /** Constant for the Unicode {@code UTF-8}charset. */
  private static final String UTF_8 = "UTF-8";

  /** The golden content this OutputStream will compare against. */
  private final BufferedReader in;

  /** The internal buffer where actual data is stored. */
  private byte buf[] = new byte[2048];

  /** The current lines being compared. Only {@code \n} serves as a line separator. */
  private int line = 1;

  /**
   * The number of valid bytes in the buffer.
   *
   * <p>The useful buffer count is in range {@code 0} - {@code count} (excl) of {@link #buf}.
   */
  protected int count;

  public DiffOutputStream(Reader in) {
    this(new BufferedReader(in));
  }

  @SuppressWarnings("WeakerAccess")
  public DiffOutputStream(BufferedReader in) {
    this.in = in;
  }

  @Override
  public void write(int b) throws IOException {
    buf[count] = (byte) b;
    if (b == '\n') {
      String expectedLine = in.readLine();
      assertThatWrittenWasExpected(expectedLine);
      count = 0;
      line++;
    } else {
      count++;
    }
  }

  private void assertThatWrittenWasExpected(String expectedLine) throws IOException {
    byte[] expectedRaw = expectedLine.getBytes(UTF_8);
    if (count != expectedLine.length()) {
      failOnDifferentLine(expectedLine);
    }
    int length = count;
    for (int i = 0; i < length; i++) {
      if (buf[i] != expectedRaw[i]) {
        failOnDifferentLine(expectedLine);
      }
    }
  }

  private void failOnDifferentLine(String expectedLine) throws UnsupportedEncodingException {
    byte[] actualRaw = new byte[count];
    System.arraycopy(buf, 0, actualRaw, 0, count);
    String actualLine = new String(actualRaw, UTF_8);
    assertWithMessage("Content differs on line %s:\n", line)
        .that(actualLine)
        .isEqualTo(expectedLine);
  }

  public boolean isCompleted() {
    char[] extraInput = new char[64];
    try {
      int read = in.read(extraInput);
      assertWithMessage("There is still content in the expected input: " + new String(extraInput))
          .that(read)
          .isLessThan(0);
      return read == -1;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
