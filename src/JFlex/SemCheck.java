/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4                                                               *
 * Copyright (C) 1998-2004  Gerwin Klein <lsf@jflex.de>                    *
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

import java.util.*;
import java.io.File;

/**
 * Performs simple semantic analysis on regular expressions.
 *
 * (used for checking if trailing contexts are legal)
 *
 * @author Gerwin Klein
 * @version JFlex 1.4, $Revision$, $Date$
 */
public final class SemCheck {

  // stored globally since they are used as constants in all checks
  private static Macros macros;
  private static char maxChar;


  /**
   * Performs semantic analysis for all expressions.
   *
   * Currently: illegal lookahead check only
   * [fixme: more checks possible]
   *
   * @param rs   the reg exps to be checked
   * @param m    the macro table (in expanded form)
   * @param max  max character of the used charset (for negation)
   * @param f    the spec file containing the rules [fixme]
   */
  public static void check(RegExps rs, Macros m, char max, File f) {
    macros = m;
    maxChar = max;
    
    boolean errors = false;
    int num = rs.getNum();
    for (int i = 0; i < num; i++) {
      RegExp r = rs.getRegExp(i);
      RegExp l = rs.getLookAhead(i);
     
      if (!checkLookAhead(r,l)) {
        errors = true;
        Out.error(f, ErrorMessages.LOOKAHEAD_ERROR, rs.getLine(i), -1);
      }
    }
    
    if (errors) throw new GeneratorException();
  }


  /**
   * Checks for illegal lookahead expressions. 
   * 
   * Lookahead in JFlex only works when the first expression has fixed
   * length or when the intersection of the last set of the first expression
   * and the first set of the second expression is empty.
   *
   * @param r1   first regexp
   * @param r2   second regexp (the lookahead)
   *
   * @return true iff JFlex can generate code for the lookahead expression
   */
  private static boolean checkLookAhead(RegExp r1, RegExp r2) {
    return r2 == null || length(r1) > 0 || !(last(r1).and(first(r2)).containsElements());
  }


  /**
   * Returns length if expression has fixed length, -1 otherwise.   
   */
  private static int length(RegExp re) {
    RegExp2 r;

    switch (re.type) {      

    case sym.BAR: {
      r = (RegExp2) re;
      int l1 = length(r.r1);
      if (l1 < 0) return -1;
      int l2 = length(r.r2);

      if (l1 == l2) 
        return l1;
      else
        return -1;
    }

    case sym.CONCAT: {
      r = (RegExp2) re;
      int l1 = length(r.r1);
      if (l1 < 0) return -1;
      int l2 = length(r.r2);
      if (l2 < 0) return -1;
      return l1+l2;
    }

    case sym.STAR:
    case sym.PLUS:
    case sym.QUESTION:
      return -1;

    case sym.CCLASS:
    case sym.CCLASSNOT:
    case sym.CHAR:
      return 1;

    case sym.STRING: {
      String content = (String) ((RegExp1) re).content;
      return content.length();
    }

    case sym.MACROUSE:      
      return length(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error("Unkown expression type "+re.type+" in "+re);   //$NON-NLS-1$ //$NON-NLS-2$
  }
  

  /**
   * Returns true iff the matched language contains epsilon
   */
  private static boolean containsEpsilon(RegExp re) {
    RegExp2 r;

    switch (re.type) {      

    case sym.BAR:
      r = (RegExp2) re;
      return containsEpsilon(r.r1) || containsEpsilon(r.r2);

    case sym.CONCAT:
      r = (RegExp2) re;
      if (containsEpsilon(r.r1))
        return containsEpsilon(r.r2);
      else
        return false;         

    case sym.STAR:
    case sym.QUESTION:
      return true;

    case sym.PLUS:
      return containsEpsilon( (RegExp) ((RegExp1)re).content );

    case sym.CCLASS:     
    case sym.CCLASSNOT:
    case sym.CHAR:
      return false;

    case sym.STRING:
      return ((String) ((RegExp1) re).content).length() <= 0;

    case sym.MACROUSE:
      return containsEpsilon(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error("Unkown expression type "+re.type+" in "+re); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Returns the first set of an expression. 
   *
   * (the first-character-projection of the language)
   */
  private static IntCharSet first(RegExp re) {
    RegExp2 r;

    switch (re.type) {      

    case sym.BAR:
      r = (RegExp2) re;
      return first(r.r1).add(first(r.r2));

    case sym.CONCAT:
      r = (RegExp2) re;
      if ( containsEpsilon(r.r1) ) 
        return first(r.r1).add(first(r.r2));      
      else
        return first(r.r1);

    case sym.STAR:
    case sym.PLUS:
    case sym.QUESTION:
      return first((RegExp) ((RegExp1)re).content);

    case sym.CCLASS:
      return new IntCharSet((Vector) ((RegExp1) re).content);

    case sym.CCLASSNOT:
      IntCharSet all = new IntCharSet(new Interval( (char) 0,maxChar));
      IntCharSet set = new IntCharSet((Vector) ((RegExp1) re).content);
      all.sub(set);
      return all;

    case sym.CHAR:
      return new IntCharSet(((Character) ((RegExp1) re).content).charValue());

    case sym.STRING:
      String content = (String) ((RegExp1) re).content;
      if (content.length() > 0)
        return new IntCharSet(content.charAt(0));
      else
        return new IntCharSet();

    case sym.MACROUSE:      
      return first(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error("Unkown expression type "+re.type+" in "+re); //$NON-NLS-1$ //$NON-NLS-2$
  }


  /**
   * Returns the last set of the expression
   *
   * (the last-charater-projection of the language)
   */
  private static IntCharSet last(RegExp re) {

    RegExp2 r;

    switch (re.type) {      

    case sym.BAR:
      r = (RegExp2) re;
      return last(r.r1).add(last(r.r2));

    case sym.CONCAT:
      r = (RegExp2) re;
      if ( containsEpsilon(r.r2) )
        return last(r.r1).add(last(r.r2));
      else
        return last(r.r2);

    case sym.STAR:
    case sym.PLUS:
    case sym.QUESTION:
      return last((RegExp) ((RegExp1)re).content);

    case sym.CCLASS:
      return new IntCharSet((Vector) ((RegExp1) re).content);

    case sym.CCLASSNOT:
      IntCharSet all = new IntCharSet(new Interval( (char) 0,maxChar));
      IntCharSet set = new IntCharSet((Vector) ((RegExp1) re).content);
      all.sub(set);
      return all;

    case sym.CHAR:
      return new IntCharSet(((Character) ((RegExp1) re).content).charValue());

    case sym.STRING:
      String content = (String) ((RegExp1) re).content;
      if (content.length() > 0)
        return new IntCharSet(content.charAt(content.length()-1));
      else
        return new IntCharSet();

    case sym.MACROUSE:      
      return last(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error("Unkown expression type "+re.type+" in "+re); //$NON-NLS-1$ //$NON-NLS-2$
  }
}
