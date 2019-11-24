package jflex.testcase.large_input;

import static java.lang.Math.min;

import java.io.Reader;

/** A reader that returns the same content over and over again. */
public class RepeatContentReader extends Reader {

  private static final int MIN_INTERNAL_BUFFER_SIZE = 64 * 1024;

  /** The size of the content that this reader will provide. */
  private final long size;
  /** The content that this reader will return in loop. */
  private final char[] content;
  /** How many characters have been read so far. */
  private long read;

  /**
   * A reader thar returns {@code content} in loop, until the given {@code size} characters have
   * been read.
   *
   * @param size The number of characters that this reader will return.
   * @param content The content that this reader will return. If it's smaller than size, then it
   *     loops and reads the given content from the beginning again.
   */
  RepeatContentReader(long size, String content) {
    this.size = size;
    this.content = createInternalContent(content);
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

  private static char[] createInternalContent(String content) {
    char[] givenContent = content.toCharArray();
    if (givenContent.length > MIN_INTERNAL_BUFFER_SIZE) {
      return givenContent;
    }
    // round down the size
    int size = MIN_INTERNAL_BUFFER_SIZE / givenContent.length * givenContent.length;
    char[] myContent = new char[size];
    for (int destPos = 0; destPos < size; destPos += givenContent.length) {
      System.arraycopy(givenContent, 0, myContent, destPos, givenContent.length);
    }
    return myContent;
  }
}
