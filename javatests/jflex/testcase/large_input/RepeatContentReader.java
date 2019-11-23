package jflex.testcase.large_input;

import static java.lang.Math.min;

import java.io.Reader;

public class RepeatContentReader extends Reader {
  /** The size of the content that this reader will provide. */
  private final long size;
  /** The content that this reader will return in loop. */
  private final char[] content;
  /** How many characters have been read so far. */
  private long read;

  public RepeatContentReader(long size, String content) {
    this.size = size;
    this.content = content.toCharArray();
  }

  @Override
  public int read(char[] cbuf, int off, int len) {
    if (read >= size) {
      return -1;
    }
    long maxLen =
        min(
            len, // Client asks len characters
            size - read // but we might reach the end before.
            );
    // Let's copy characters until the end of the buffer.
    int srcPos = (int) (read % content.length);
    int length = (int) min(content.length - srcPos, maxLen);

    System.arraycopy(content, srcPos, cbuf, /*destPos=*/ off, length);
    read += length;
    return length;
  }

  @Override
  public void close() {}
}
