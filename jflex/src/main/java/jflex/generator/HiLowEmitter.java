/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.generator;

/**
 * HiLowEmitter
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class HiLowEmitter extends PackEmitter {

  /** number of entries in expanded array */
  private int numEntries;

  /**
   * Create new emitter for values in [0, 0xFFFFFFFF] using hi/low encoding.
   *
   * @param name the name of the generated array
   */
  public HiLowEmitter(String name) {
    super(name);
  }

  /**
   * Emits hi/low pair unpacking code for the generated array.
   *
   * @see PackEmitter#emitUnpack()
   */
  @Override
  public void emitUnpack() {
    // close last string chunk:
    println("\";");
    nl();
    println("  private static int [] zzUnpack" + name + "() {");
    println("    int [] result = new int[" + numEntries + "];");
    println("    int offset = 0;");

    for (int i = 0; i < chunks; i++) {
      println(
          "    offset = zzUnpack"
              + name
              + "("
              + constName()
              + "_PACKED_"
              + i
              + ", offset, result);");
    }

    println("    return result;");
    println("  }");

    nl();
    println(
        "  private static int zzUnpack" + name + "(String packed, int offset, int [] result) {");
    println("    int i = 0;  /* index in packed string  */");
    println("    int j = offset;  /* index in unpacked array */");
    println("    int l = packed.length();");
    println("    while (i < l) {");
    println("      int high = packed.charAt(i++) << 16;");
    println("      result[j++] = high | packed.charAt(i++);");
    println("    }");
    println("    return j;");
    println("  }");
  }

  /**
   * Emit one value using two characters.
   *
   * @param val the value to emit; {@code 0 <= val <= 0xFFFFFFFF}
   */
  public void emit(int val) {
    numEntries += 1;
    breaks();
    emitUC(val >> 16);
    emitUC(val & 0xFFFF);
  }
}
