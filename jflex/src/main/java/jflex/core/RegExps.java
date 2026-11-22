/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jflex.base.Build;
import jflex.core.unicode.CharClasses;
import jflex.core.unicode.IntCharSet;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * Stores all rules of the specification for later access in the RegExp to NFA conversion.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class RegExps {

  /** the spec line in which a regexp is used */
  private final List<Integer> lines;

  /** the lexical states in which the regexp is used */
  private final List<List<Integer>> states;

  /** the regexp */
  private List<RegExp> regExps;

  /** the action of a regexp */
  private final List<Action> actions;

  /** flag if it is a BOL regexp */
  private final List<Boolean> BOL;

  /** the lookahead expression */
  private List<RegExp> look;

  /** the forward DFA entry point of the lookahead expression */
  private final List<Integer> look_entry;

  /**
   * Count of how many general lookahead expressions there are. Need 2*gen_look_count additional DFA
   * entry points.
   */
  int gen_look_count;

  /** Constructor for RegExps. */
  public RegExps() {
    states = new ArrayList<>();
    regExps = new ArrayList<>();
    actions = new ArrayList<>();
    BOL = new ArrayList<>();
    look = new ArrayList<>();
    lines = new ArrayList<>();
    look_entry = new ArrayList<>();
  }

  /**
   * insert.
   *
   * @param line a int.
   * @param stateList a {@link java.util.List} object.
   * @param regExp a {@link RegExp} object.
   * @param action a {@link Action} object.
   * @param isBOL a {@link java.lang.Boolean} object.
   * @param lookAhead a {@link RegExp} object.
   * @return a int.
   */
  public int insert(
      int line,
      List<Integer> stateList,
      RegExp regExp,
      Action action,
      Boolean isBOL,
      RegExp lookAhead) {
    if (Build.DEBUG) {
      Out.debug("Inserting regular expression with statelist :" + Out.NL + stateList);
      Out.debug("and action code :" + Out.NL + (action == null ? "null" : action.content) + Out.NL);
      Out.debug("expression :" + Out.NL + regExp);
    }

    states.add(stateList);
    regExps.add(regExp);
    actions.add(action);
    BOL.add(isBOL);
    look.add(lookAhead);
    lines.add(line);
    look_entry.add(null);

    return states.size() - 1;
  }

  /**
   * insert.
   *
   * @param stateList a {@link java.util.List} object.
   * @param action a {@link Action} object.
   * @return a int.
   */
  public int insert(List<Integer> stateList, Action action) {

    if (Build.DEBUG) {
      Out.debug("Inserting eofrule with statelist :" + Out.NL + stateList);
      Out.debug("and action code :" + Out.NL + (action == null ? "null" : action.content) + Out.NL);
    }

    states.add(stateList);
    regExps.add(null);
    actions.add(action);
    BOL.add(null);
    look.add(null);
    lines.add(null);
    look_entry.add(null);

    return states.size() - 1;
  }

  /**
   * addStates.
   *
   * @param regNum a int.
   * @param newStates a {@link java.util.List} object.
   */
  public void addStates(int regNum, List<Integer> newStates) {
    states.get(regNum).addAll(newStates);
  }

  /**
   * getNum.
   *
   * @return a int.
   */
  public int getNum() {
    return states.size();
  }

  /**
   * isBOL.
   *
   * @param num a int.
   * @return a boolean.
   */
  public boolean isBOL(int num) {
    return BOL.get(num);
  }

  /**
   * getLookAhead.
   *
   * @param num a int.
   * @return a {@link RegExp} object.
   */
  public RegExp getLookAhead(int num) {
    return look.get(num);
  }

  /**
   * isEOF.
   *
   * @param num a int.
   * @return a boolean.
   */
  public boolean isEOF(int num) {
    return BOL.get(num) == null;
  }

  /**
   * Getter for the field {@code states}.
   *
   * @param num a int.
   * @return a {@link java.util.List} object.
   */
  public List<Integer> getStates(int num) {
    return states.get(num);
  }

  /**
   * getRegExp.
   *
   * @param num a int.
   * @return a {@link RegExp} object.
   */
  public RegExp getRegExp(int num) {
    return regExps.get(num);
  }

  /**
   * getLine.
   *
   * @param num a int.
   * @return a int.
   */
  public int getLine(int num) {
    return lines.get(num);
  }

  /**
   * getLookEntry.
   *
   * @param num a int.
   * @return a int.
   */
  public int getLookEntry(int num) {
    return look_entry.get(num);
  }

  /** checkActions. */
  public void checkActions() {
    if (actions.get(actions.size() - 1) == null) {
      throw new GeneratorException(ErrorMessages.NO_LAST_ACTION);
    }
  }

  /**
   * getAction.
   *
   * @param num a int.
   * @return a {@link Action} object.
   */
  public Action getAction(int num) {
    while (num < actions.size() && actions.get(num) == null) num++;

    return actions.get(num);
  }

  /**
   * The approximate number of NFA states that {@link NFA#addRegExp(int)} creates for all
   * expressions.
   *
   * <p>Assumes that lookahead cases are already resolved, see {@link #checkLookAheads()}.
   *
   * @param macros a {@link Macros} object.
   * @return the number of states, exact up to negation as {@link RegExp#size(Macros)}.
   */
  public long NFASize(Macros macros) {
    long size = 0;
    for (int i = 0; i < regExps.size(); i++) {
      RegExp r = regExps.get(i);
      RegExp lookAhead = look.get(i);

      if (r != null) size += r.size(macros);
      if (lookAhead == null) continue;

      Action a = getAction(i);
      if (a != null && a.lookAhead() == Action.Kind.FINITE_CHOICE) {
        size += choicesSize(lookAhead, macros);
      } else {
        size += lookAhead.size(macros);
        // the forward and the backward pass have their own NFAs
        if (a != null && a.lookAhead() == Action.Kind.GENERAL_LOOK) {
          size += lookAhead.size(macros);
          if (r != null) size += r.size(macros);
        }
      }
    }

    return size;
  }

  /** The number of NFA states for a finite choice lookahead. One NFA per choice. */
  private static long choicesSize(RegExp lookAhead, Macros macros) {
    if (lookAhead.type != sym.BAR) return lookAhead.size(macros);

    RegExp2 r = (RegExp2) lookAhead;
    return choicesSize(r.r1, macros) + choicesSize(r.r2, macros);
  }

  /** checkLookAheads. */
  public void checkLookAheads() {
    for (int i = 0; i < regExps.size(); i++) lookAheadCase(i);
  }

  /**
   * Determine which case of lookahead expression regExpNum points to (if any). Set case data in
   * corresponding action. Increment count of general lookahead expressions for entry points of the
   * two additional DFAs. Register DFA entry point in RegExps
   *
   * <p>Needs to be run before adding any regexps/rules to be able to reserve the correct amount of
   * space of lookahead DFA entry points.
   *
   * @param regExpNum the number of the regexp in RegExps.
   */
  private void lookAheadCase(int regExpNum) {
    if (getLookAhead(regExpNum) != null) {
      RegExp r1 = getRegExp(regExpNum);
      RegExp r2 = getLookAhead(regExpNum);

      Action a = getAction(regExpNum);

      if (a == null) {
        throw new NullPointerException("Action is null, this should not be possible");
      }

      int len1 = SemCheck.length(r1);
      int len2 = SemCheck.length(r2);

      if (len1 >= 0) {
        a.setLookAction(Action.Kind.FIXED_BASE, len1);
      } else if (len2 >= 0) {
        a.setLookAction(Action.Kind.FIXED_LOOK, len2);
      } else if (SemCheck.isFiniteChoice(r2)) {
        a.setLookAction(Action.Kind.FINITE_CHOICE, 0);
      } else {
        a.setLookAction(Action.Kind.GENERAL_LOOK, 0);
        look_entry.set(regExpNum, gen_look_count);
        gen_look_count++;
      }
    }
  }

  /**
   * Expand all macro calls in regexp and lookahead rules.
   *
   * <p>A macro can define an expression with lookahead ({@code m = a / b}). If such an expression
   * is the entire expression of a rule after expansion, the rule becomes a lookahead rule.
   * Lookahead in any other position is reported as error.
   *
   * @param m the macro definitions, already expanded
   * @param f the spec file for error reporting
   * @param out the output of this generator run, for error reporting
   */
  public void normaliseMacros(Macros m, File f, Out out) {
    List<RegExp> newRegExps = new ArrayList<RegExp>();
    List<RegExp> newLook = new ArrayList<RegExp>();

    for (int i = 0; i < regExps.size(); i++) {
      RegExp r = regExps.get(i);
      RegExp l = look.get(i);

      if (r != null) r = r.normaliseMacros(m);
      if (l != null) l = l.normaliseMacros(m);

      // the action stores which file the rule came from
      Action a = getAction(i);
      File file = a == null ? f : a.file;

      // a lookahead from a macro at the top level of a rule without lookahead
      if (r != null && r.type == sym.LOOKAHEAD && l == null) {
        if (actions.get(i) == null) {
          // "|" action not allowed, the action stores the lookahead kind and cannot be shared
          out.error(file, ErrorMessages.LOOKAHEAD_NEEDS_ACTION, lines.get(i), -1);
        }
        RegExp2 lookExp = (RegExp2) r;
        r = lookExp.r1;
        l = lookExp.r2;
      }

      if (RegExp.containsLookAhead(r) || RegExp.containsLookAhead(l)) {
        out.error(file, ErrorMessages.LOOKAHEAD_NOT_TOP_LEVEL, lines.get(i), -1);
      }

      newRegExps.add(r);
      newLook.add(l);
    }

    this.regExps = newRegExps;
    this.look = newLook;
  }

  /**
   * Normalise all character class expressions in regexp and lookahead rules.
   *
   * @param f the spec file for error reporting
   */
  public void normaliseCCLs(File f) {
    List<RegExp> newRegExps = new ArrayList<RegExp>();
    List<RegExp> newLook = new ArrayList<RegExp>();

    for (int i = 0; i < regExps.size(); i++) {
      Action a = getAction(i);
      File file = a == null ? f : a.file;
      int line = a == null ? -1 : a.line - 1;
      RegExp r;
      r = regExps.get(i);
      newRegExps.add(r == null ? r : r.normaliseCCLs(file, line));
      r = look.get(i);
      newLook.add(r == null ? r : r.normaliseCCLs(file, line));
    }

    this.regExps = newRegExps;
    this.look = newLook;
  }

  /**
   * Replace all predefined character classes with primitive IntCharSet classes.
   *
   * <p>With {@code caseless} in JLex mode ({@code jlex}), this makes all character classes
   * caseless.
   *
   * <p>Assumes normalised expressions.
   */
  public void expandPreClasses(
      Map<Integer, IntCharSet> preclassCache,
      CharClasses charClasses,
      boolean caseless,
      boolean jlex) {
    List<RegExp> newRegExps = new ArrayList<RegExp>();
    List<RegExp> newLook = new ArrayList<RegExp>();

    for (RegExp r : regExps)
      newRegExps.add(
          r == null ? r : r.expandPreClasses(preclassCache, charClasses, caseless, jlex));
    for (RegExp r : look)
      newLook.add(r == null ? r : r.expandPreClasses(preclassCache, charClasses, caseless, jlex));

    this.regExps = newRegExps;
    this.look = newLook;
  }

  /**
   * Print the list of regExps to the dump output.
   *
   * @param out the output of this generator run
   */
  public void dump(Out out) {
    out.dump("RegExp rules:");
    for (RegExp r : regExps) {
      if (r != null) out.dump(r.toString());
    }
  }

  /**
   * Make character class partitions for all classes mentioned in the spec.
   *
   * <p>Assumes normalised expressions.
   */
  public void makeCCLs(CharClasses classes, boolean caseless) {
    for (RegExp r : regExps) if (r != null) r.makeCCLs(classes, caseless);
    for (RegExp r : look) if (r != null) r.makeCCLs(classes, caseless);
  }
}
