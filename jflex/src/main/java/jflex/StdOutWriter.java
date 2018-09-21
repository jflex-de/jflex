/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.awt.TextArea;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Convenience class for JFlex stdout, redirects output to a TextArea if in GUI mode.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class StdOutWriter extends PrintWriter {

  /** text area to write to if in gui mode, gui mode = (text != null) */
  private TextArea text;

  /**
   * approximation of the current column in the text area for auto wrapping at <code>wrap</code>
   * characters
   */
  private int col;

  /** auto wrap lines in gui mode at this value */
  private static final int wrap = 78;

  /** A StdOutWriter, attached to System.out, no gui mode */
  public StdOutWriter() {
    super(System.out, true);
  }

  /**
   * A StdOutWrite, attached to the specified output stream, no gui mode
   *
   * @param out a {@link java.io.OutputStream} object.
   */
  public StdOutWriter(OutputStream out) {
    super(out, true);
  }

  /**
   * Set the TextArea to write text to. Will continue to write to System.out if text is <code>null
   * </code>.
   *
   * @param text the TextArea to write to
   */
  public void setGUIMode(TextArea text) {
    this.text = text;
  }

  /**
   * Write a single character.
   *
   * @param c a int.
   */
  public void write(int c) {
    if (text != null) {
      text.append(String.valueOf((char) c));
      if (++col > wrap) println();
    } else super.write(c);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Write a portion of an array of characters.
   */
  public void write(char buf[], int off, int len) {
    if (text != null) {
      text.append(new String(buf, off, len));
      if ((col += len) > wrap) println();
    } else super.write(buf, off, len);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Write a portion of a string.
   */
  public void write(String s, int off, int len) {
    if (text != null) {
      text.append(s.substring(off, off + len));
      if ((col += len) > wrap) println();
    } else {
      super.write(s, off, len);
      flush();
    }
  }

  /** Begin a new line. Which actual character/s is/are written depends on the runtime platform. */
  public void println() {
    if (text != null) {
      text.append(Out.NL);
      col = 0;
    } else super.println();
  }
}
