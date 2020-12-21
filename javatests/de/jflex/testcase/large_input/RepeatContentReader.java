package jflex.testcase.large_input;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.min;

import java.io.Reader;

/** A reader that returns the same content over and over again. */
public class RepeatContentReader extends Reader {

  /** Size of the precomputed buffer with the repeated content. */
  private static final int PREPARED_BUFFER_SIZE = 256 * 1024;

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
    checkArgument(size > 0);
    this.size = size;
    this.content = createInternalContent(size, content);
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

  private static char[] createInternalContent(long wantedSize, String content) {
    char[] givenContent = content.toCharArray();
    if (wantedSize <= givenContent.length) {
      // The given buffer is larger than what we will read.
      // Actually, we could even take the substring for memory efficiency. But from tests, the given
      // content is always small.
      return givenContent;
    }
    // wantedSize > content.size(): The reader will loop over the content.
    if (givenContent.length >= PREPARED_BUFFER_SIZE) {
      // The given content is large enough. Nothing to do.
      return givenContent;
    }
    // To maximize use of the buffer, we already prepare a repeated content of ~PREPARED_BUFFER_SIZE
    int size = PREPARED_BUFFER_SIZE / givenContent.length * givenContent.length;
    if (size > wantedSize) {
      // But we don't need so much if we read less.
      size = (int) (wantedSize / givenContent.length + 1) * givenContent.length;
    }
    char[] myContent = new char[size];
    for (int destPos = 0; destPos < size; destPos += givenContent.length) {
      System.arraycopy(givenContent, 0, myContent, destPos, givenContent.length);
    }
    return myContent;
  }
}
