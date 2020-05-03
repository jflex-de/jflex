/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.generator;

import java.util.Locale;
import jflex.logging.Out;

/**
 * Encodes {@code int} arrays as strings.
 *
 * <p>Also splits up strings when longer than 64K in UTF8 encoding. Subclasses emit unpacking code.
 *
 * <p>Usage protocol: {@code p.emitInit();}<br>
 * {@code for each data: p.emitData(data);}<br>
 * {@code p.emitUnpack();}
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public abstract class PackEmitter {

  /** name of the generated array (mixed case, no yy prefix) */
  protected String name;

  /** current UTF8 length of generated string in current chunk */
  private int UTF8Length;

  /** position in the current line */
  private int linepos;

  /** max number of entries per line */
  private static final int maxEntries = 16;

  /** output buffer */
  protected StringBuilder out = new StringBuilder();

  /** number of existing string chunks */
  protected int chunks;

  /** maximum size of chunks */
  // String constants are stored as UTF8 with 2 bytes length
  // field in class files. One Unicode char can be up to 3
  // UTF8 bytes. 64K max and two chars safety.
  private static final int maxSize = 0xFFFF - 6;

  /** indent for string lines */
  private static final String indent = "    ";

  /**
   * Create new emitter for an array.
   *
   * @param name the name of the generated array
   */
  public PackEmitter(String name) {
    this.name = name;
  }

  /**
   * Convert array name into all uppercase internal scanner constant name.
   *
   * @return {@code name} as a internal constant name.
   * @see PackEmitter#name
   */
  protected String constName() {
    return "ZZ_" + name.toUpperCase(Locale.ENGLISH);
  }

  /**
   * Return current output buffer.
   *
   * @return a {@link java.lang.String} object.
   */
  @Override
  public String toString() {
    return out.toString();
  }

  /** Emit declaration of decoded member and open first chunk. */
  public void emitInit() {
    out.append("  private static final int [] ");
    out.append(constName());
    out.append(" = zzUnpack");
    out.append(name);
    out.append("();");
    nl();
    nextChunk();
  }

  /**
   * Emit single unicode character.
   *
   * <p>Updates length, position, etc.
   *
   * @param i the character to emit.
   */
  public void emitUC(int i) {
    if (i < 0 || i > 0xFFFF) {
      throw new IllegalArgumentException("character value expected");
    }

    // cast ok because of prec
    char c = (char) i;

    printUC(c);
    UTF8Length += Utf8Length(c);
    linepos++;
  }

  /** Execute line/chunk break if necessary. Leave space for at least two chars. */
  public void breaks() {
    if (UTF8Length >= maxSize) {
      // close current chunk
      out.append("\";");
      nl();

      nextChunk();
    } else {
      if (linepos >= maxEntries) {
        // line break
        out.append("\"+");
        nl();
        out.append(indent);
        out.append("\"");
        linepos = 0;
      }
    }
  }

  /** Emit the unpacking code. */
  public abstract void emitUnpack();

  /** emit next chunk */
  private void nextChunk() {
    nl();
    out.append("  private static final String ");
    out.append(constName());
    out.append("_PACKED_");
    out.append(chunks);
    out.append(" =");
    nl();
    out.append(indent);
    out.append("\"");

    UTF8Length = 0;
    linepos = 0;
    chunks++;
  }

  /** emit newline */
  protected void nl() {
    out.append(Out.NL);
  }

  /**
   * Append a unicode/octal escaped character to {@code out} buffer.
   *
   * @param c the character to append
   */
  private void printUC(char c) {
    if (c > 255) {
      out.append("\\u");
      if (c < 0x1000) out.append("0");
      out.append(Integer.toHexString(c));
    } else {
      out.append("\\");
      out.append(Integer.toOctalString(c));
    }
  }

  /**
   * Calculates the number of bytes a Unicode character would have in UTF8 representation in a class
   * file.
   *
   * @param value the char code of the Unicode character
   * @return length of UTF8 representation.
   */
  private static int Utf8Length(int value) {
    // see JVM spec section 4.4.7, p 111
    if (value == 0) return 2;
    if (value <= 0x7F) return 1;

    // workaround for javac bug (up to jdk 1.3):
    if (value < 0x0400) return 2;
    if (value <= 0x07FF) return 3;

    // correct would be:
    // if (value <= 0x7FF) return 2;
    return 3;
  }

  // convenience
  /**
   * println.
   *
   * @param s a {@link java.lang.String} object.
   */
  protected void println(String s) {
    out.append(s);
    nl();
  }
}
