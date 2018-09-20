/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all rules of the specification for later access in RegExp -> NFA
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class RegExps {

  /** the spec line in which a regexp is used */
  List<Integer> lines;

  /** the lexical states in wich the regexp is used */
  List<List<Integer>> states;

  /** the regexp */
  List<RegExp> regExps;

  /** the action of a regexp */
  List<Action> actions;

  /** flag if it is a BOL regexp */
  List<Boolean> BOL;

  /** the lookahead expression */
  List<RegExp> look;

  /** the forward DFA entry point of the lookahead expression */
  List<Integer> look_entry;

  /**
   * Count of many general lookahead expressions there are. Need 2*gen_look_count additional DFA
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
   * @param regExp a {@link jflex.RegExp} object.
   * @param action a {@link jflex.Action} object.
   * @param isBOL a {@link java.lang.Boolean} object.
   * @param lookAhead a {@link jflex.RegExp} object.
   * @return a int.
   */
  public int insert(
      int line,
      List<Integer> stateList,
      RegExp regExp,
      Action action,
      Boolean isBOL,
      RegExp lookAhead) {
    if (Options.DEBUG) {
      Out.debug(
          "Inserting regular expression with statelist :" + Out.NL + stateList); // $NON-NLS-1$
      Out.debug("and action code :" + Out.NL + action.content + Out.NL); // $NON-NLS-1$
      Out.debug("expression :" + Out.NL + regExp); // $NON-NLS-1$
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
   * @param action a {@link jflex.Action} object.
   * @return a int.
   */
  public int insert(List<Integer> stateList, Action action) {

    if (Options.DEBUG) {
      Out.debug("Inserting eofrule with statelist :" + Out.NL + stateList); // $NON-NLS-1$
      Out.debug("and action code :" + Out.NL + action.content + Out.NL); // $NON-NLS-1$
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
   * @return a {@link jflex.RegExp} object.
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
   * Getter for the field <code>states</code>.
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
   * @return a {@link jflex.RegExp} object.
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
      Out.error(ErrorMessages.NO_LAST_ACTION);
      throw new GeneratorException();
    }
  }

  /**
   * getAction.
   *
   * @param num a int.
   * @return a {@link jflex.Action} object.
   */
  public Action getAction(int num) {
    while (num < actions.size() && actions.get(num) == null) num++;

    return actions.get(num);
  }

  /**
   * NFASize.
   *
   * @param macros a {@link jflex.Macros} object.
   * @return a int.
   */
  public int NFASize(Macros macros) {
    int size = 0;
    for (RegExp r : regExps) if (r != null) size += r.size(macros);

    for (RegExp r : look) if (r != null) size += r.size(macros);

    return size;
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

      int len1 = SemCheck.length(r1);
      int len2 = SemCheck.length(r2);

      if (len1 >= 0) {
        a.setLookAction(Action.FIXED_BASE, len1);
      } else if (len2 >= 0) {
        a.setLookAction(Action.FIXED_LOOK, len2);
      } else if (SemCheck.isFiniteChoice(r2)) {
        a.setLookAction(Action.FINITE_CHOICE, 0);
      } else {
        a.setLookAction(Action.GENERAL_LOOK, 0);
        look_entry.set(regExpNum, gen_look_count);
        gen_look_count++;
      }
    }
  }
}
