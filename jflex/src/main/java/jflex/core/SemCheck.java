/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.core;

import java.io.File;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * Performs simple semantic analysis on regular expressions.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public final class SemCheck {

  /** Prevent instantiation of static-only class */
  private SemCheck() {}

  /**
   * Performs semantic analysis for all expressions.
   *
   * <p>Currently checks for empty expressions only.
   *
   * @param rs the reg exps to be checked
   * @param f the spec file containing the rules
   */
  public static void check(RegExps rs, File f) {
    int num = rs.getNum();
    for (int i = 0; i < num; i++) {
      RegExp r = rs.getRegExp(i);
      RegExp l = rs.getLookAhead(i);
      Action a = rs.getAction(i);

      if (r != null && maybeEmtpy(r)) {
        if (l != null) {
          if (a == null) {
            Out.error(jflex.l10n.ErrorMessages.EMPTY_MATCH_LOOK);
          } else {
            Out.error(f, jflex.l10n.ErrorMessages.EMPTY_MATCH_LOOK, a.priority - 1, -1);
          }
        } else {
          if (a == null) {
            Out.warning(jflex.l10n.ErrorMessages.EMPTY_MATCH);
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
   * @param re a {@link RegExp} object.
   * @return a boolean.
   */
  public static boolean maybeEmtpy(RegExp re) {
    RegExp2 r;

    switch (re.type) {
      case sym.BAR:
        r = (RegExp2) re;
        return maybeEmtpy(r.r1) || maybeEmtpy(r.r2);

      case sym.CONCAT:
        r = (RegExp2) re;
        return maybeEmtpy(r.r1) && maybeEmtpy(r.r2);

      case sym.STAR:
      case sym.QUESTION:
        return true;

      case sym.PLUS:
        RegExp1 r1 = (RegExp1) re;
        return maybeEmtpy((RegExp) r1.content);

      case sym.CHAR:
      case sym.CHAR_I:
      case sym.PRIMCLASS:
      case sym.TILDE:
        return false;

      case sym.STRING:
      case sym.STRING_I:
        String content = (String) ((RegExp1) re).content;
        return content.length() == 0;

      case sym.BANG:
        RegExp1 r3 = (RegExp1) re;
        return !maybeEmtpy((RegExp) r3.content);

      default:
        throw new RegExpException(re);
    }
  }

  /**
   * Returns length if expression has fixed length, -1 otherwise.
   *
   * <p>Negation operators are treated as always variable length.
   *
   * @param re a {@link RegExp} object.
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

      case sym.CHAR:
      case sym.CHAR_I:
      case sym.PRIMCLASS:
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

      default:
        throw new RegExpException(re);
    }
  }

  /**
   * Returns true iff the expression is a finite choice of fixed length expressions.
   *
   * <p>Negation operators are treated as always variable length.
   *
   * @param re a {@link RegExp} object.
   * @return a boolean.
   */
  public static boolean isFiniteChoice(RegExp re) {
    RegExp2 r;

    switch (re.type) {
      case sym.BAR:
        r = (RegExp2) re;
        return isFiniteChoice(r.r1) && isFiniteChoice(r.r2);

      case sym.CONCAT:
        r = (RegExp2) re;
        int l1 = length(r.r1);
        if (l1 < 0) return false;
        int l2 = length(r.r2);
        return l2 >= 0;

      case sym.STAR:
      case sym.PLUS:
      case sym.QUESTION:
      case sym.TILDE:
      case sym.BANG:
        return false;

      case sym.CHAR:
      case sym.CHAR_I:
      case sym.PRIMCLASS:
      case sym.STRING:
      case sym.STRING_I:
        return true;

      default:
        throw new RegExpException(re);
    }
  }
}
