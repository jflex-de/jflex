/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * jflex                                                         *
 * Copyright (C) 1998-2003  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package JFlex;

/**
 * CountEmitter
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class CountEmitter extends PackEmitter {
  private int numEntries;

  /**
   * @param name
   */
  protected CountEmitter(String name) {
    super(name);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see JFlex.PackEmitter#emitUnPack()
   */
  public void emitUnpack() {
    // TODO Auto-generated method stub

  }

  /**
   * @param i
   */
  public void setValTranslation(int i) {
    // TODO Auto-generated method stub
    
  }

  /**
   * @param count
   * @param value
   */
  public void emit(int count, int value) {
    numEntries+= 2;
    // TODO Auto-generated method stub
    
  }

  private void println(String s) {
    // TODO: implement
  }

  private void emitDynamicInitFunction() {
    println("");
    println("  /** ");
    println("   * Unpacks the split, compressed DFA transition table.");
    println("   *");
    println("   * @return the unpacked transition table");
    println("   */");
    println("  private static int [] yy_unpack() {");
    println("    int [] trans = new int["+numEntries+"];");
    println("    int offset = 0;");

    for (int i = 0; i < chunks; i++) {
      println("    offset = yy_unpack(yy_packed"+i+", offset, trans);");
    }

    println("    return trans;");
    println("  }");

    println("");
    println("  /** ");
    println("   * Unpacks the compressed DFA transition table.");
    println("   *");
    println("   * @param packed   the packed transition table");
    println("   * @return         the index of the last entry");
    println("   */");
    println("  private static int yy_unpack(String packed, int offset, int [] trans) {");
    println("    int i = 0;       /* index in packed string  */");
    println("    int j = offset;  /* index in unpacked array */");
    println("    int l = packed.length();");
    println("    while (i < l) {");
    println("      int count = packed.charAt(i++);");
    println("      int value = packed.charAt(i++);");
    println("      value--;");
    println("      do trans[j++] = value; while (--count > 0);");
    println("    }");
    println("    return j;");
    println("  }");
  }
}
