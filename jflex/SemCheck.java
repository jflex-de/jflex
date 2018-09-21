/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

import java.io.File;

/**
 * Performs simple semantic analysis on regular expressions.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class SemCheck {

  // stored globally since they are used as constants in all checks
  private static Macros macros;

  /**
   * Performs semantic analysis for all expressions.
   *
   * <p>Currently checks for empty expressions only.
   *
   * @param rs the reg exps to be checked
   * @param m the macro table (in expanded form)
   * @param f the spec file containing the rules
   */
  public static void check(RegExps rs, Macros m, File f) {
    macros = m;
    int num = rs.getNum();
    for (int i = 0; i < num; i++) {
      RegExp r = rs.getRegExp(i);
      RegExp l = rs.getLookAhead(i);
      Action a = rs.getAction(i);

      if (r != null && maybeEmtpy(r)) {
        if (l != null) {
          if (a == null) {
            Out.error(ErrorMessages.EMPTY_MATCH_LOOK);
          } else {
            Out.error(f, ErrorMessages.EMPTY_MATCH_LOOK, a.priority - 1, -1);
          }
        } else {
          if (a == null) {
            Out.warning(ErrorMessages.EMPTY_MATCH);
          } else {
            Out.warning(f, ErrorMessages.EMPTY_MATCH, a.priority - 1, -1);
          }
        }
      }
    }
  }

  /**
   * Checks if the expression potentially matches the empty string.
   *
   * @param re a {@link jflex.RegExp} object.
   * @return a boolean.
   */
  public static boolean maybeEmtpy(RegExp re) {
    RegExp2 r;

    switch (re.type) {
      case sym.BAR:
        {
          r = (RegExp2) re;
          return maybeEmtpy(r.r1) || maybeEmtpy(r.r2);
        }

      case sym.CONCAT:
        {
          r = (RegExp2) re;
          return maybeEmtpy(r.r1) && maybeEmtpy(r.r2);
        }

      case sym.STAR:
      case sym.QUESTION:
        return true;

      case sym.PLUS:
        {
          RegExp1 r1 = (RegExp1) re;
          return maybeEmtpy((RegExp) r1.content);
        }

      case sym.CCLASS:
      case sym.CCLASSNOT:
      case sym.CHAR:
      case sym.CHAR_I:
        return false;

      case sym.STRING:
      case sym.STRING_I:
        {
          String content = (String) ((RegExp1) re).content;
          return content.length() == 0;
        }

      case sym.TILDE:
        return false;

      case sym.BANG:
        {
          RegExp1 r1 = (RegExp1) re;
          return !maybeEmtpy((RegExp) r1.content);
        }

      case sym.MACROUSE:
        return maybeEmtpy(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error(
        "Unknown expression type " + re.type + " in " + re); // $NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Returns length if expression has fixed length, -1 otherwise.
   *
   * <p>Negation operators are treated as always variable length.
   *
   * @param re a {@link jflex.RegExp} object.
   * @return a int.
   */
  public static int length(RegExp re) {
    RegExp2 r;

    switch (re.type) {
      case sym.BAR:
        {
          r = (RegExp2) re;
          int l1 = length(r.r1);
          if (l1 < 0) return -1;
          int l2 = length(r.r2);

          if (l1 == l2) return l1;
          else return -1;
        }

      case sym.CONCAT:
        {
          r = (RegExp2) re;
          int l1 = length(r.r1);
          if (l1 < 0) return -1;
          int l2 = length(r.r2);
          if (l2 < 0) return -1;
          return l1 + l2;
        }

      case sym.STAR:
      case sym.PLUS:
      case sym.QUESTION:
        return -1;

      case sym.CCLASS:
      case sym.CCLASSNOT:
      case sym.CHAR:
      case sym.CHAR_I:
        return 1;

      case sym.STRING:
      case sym.STRING_I:
        {
          String content = (String) ((RegExp1) re).content;
          return content.length();
        }

      case sym.TILDE:
      case sym.BANG:
        // too hard to calculate at this level, use safe approx
        return -1;

      case sym.MACROUSE:
        return length(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error(
        "Unknown expression type " + re.type + " in " + re); // $NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * Returns true iff the expression is a finite choice of fixed length expressions.
   *
   * <p>Negation operators are treated as always variable length.
   *
   * @param re a {@link jflex.RegExp} object.
   * @return a boolean.
   */
  public static boolean isFiniteChoice(RegExp re) {
    RegExp2 r;

    switch (re.type) {
      case sym.BAR:
        {
          r = (RegExp2) re;
          return isFiniteChoice(r.r1) && isFiniteChoice(r.r2);
        }

      case sym.CONCAT:
        {
          r = (RegExp2) re;
          int l1 = length(r.r1);
          if (l1 < 0) return false;
          int l2 = length(r.r2);
          return l2 >= 0;
        }

      case sym.STAR:
      case sym.PLUS:
      case sym.QUESTION:
        return false;

      case sym.CCLASS:
      case sym.CCLASSNOT:
      case sym.CHAR:
      case sym.CHAR_I:
        return true;

      case sym.STRING:
      case sym.STRING_I:
        {
          return true;
        }

      case sym.TILDE:
      case sym.BANG:
        return false;

      case sym.MACROUSE:
        return isFiniteChoice(macros.getDefinition((String) ((RegExp1) re).content));
    }

    throw new Error(
        "Unknown expression type " + re.type + " in " + re); // $NON-NLS-1$ //$NON-NLS-2$
  }
}
