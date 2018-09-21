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
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Non-deterministic finite automata representation in JFlex.
 *
 * <p>Contains algorithms RegExp → NFA and NFA → DFA.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class NFA {

  /**
   * table[current_state][next_char] is the set of states that can be reached from current_state
   * with an input next_char
   */
  StateSet[][] table;

  /**
   * epsilon[current_state] is the set of states that can be reached from current_state via epsilon
   * edges
   */
  StateSet[] epsilon;

  /** isFinal[state] == true <=> state is a final state of the NFA */
  boolean[] isFinal;

  /**
   * action[current_state]: the action associated with the state current_state (null, if there is no
   * action for the state)
   */
  Action[] action;

  /** the number of states in this NFA */
  int numStates;

  /** the current maximum number of input characters */
  int numInput;

  /**
   * the number of lexical States. Lexical states have the indices 0..numLexStates-1 in the
   * transition table
   */
  int numLexStates;

  /** estimated size of the NFA (before actual construction) */
  int estSize = 256;

  Macros macros;
  CharClasses classes;

  LexScan scanner;
  RegExps regExps;

  // will be reused by several methods (avoids excessive object creation)
  private static StateSetEnumerator states = new StateSetEnumerator();
  private static StateSet tempStateSet = new StateSet();

  /** Constructor for NFA. */
  public NFA(int numInput, int estSize) {
    this.numInput = numInput;
    this.estSize = estSize;
    numStates = 0;
    epsilon = new StateSet[estSize];
    action = new Action[estSize];
    isFinal = new boolean[estSize];
    table = new StateSet[estSize][numInput];
  }

  /**
   * Construct new NFA.
   *
   * <p>Assumes that lookahead cases and numbers are already resolved in RegExps.
   *
   * @see RegExps#checkLookAheads()
   * @param numInput a int.
   * @param scanner a {@link LexScan} object.
   * @param regExps a {@link RegExps} object.
   * @param macros a {@link Macros} object.
   * @param classes a {@link CharClasses} object.
   */
  public NFA(int numInput, LexScan scanner, RegExps regExps, Macros macros, CharClasses classes) {
    this(numInput, regExps.NFASize(macros) + 2 * scanner.states.number());

    this.scanner = scanner;
    this.regExps = regExps;
    this.macros = macros;
    this.classes = classes;

    numLexStates = scanner.states.number();

    // ensureCapacity assumes correctly set up numStates.
    int new_num = numEntryStates();
    ensureCapacity(new_num);
    numStates = new_num;
  }

  /**
   * numEntryStates.
   *
   * @return a int.
   */
  public int numEntryStates() {
    return 2 * (numLexStates + regExps.gen_look_count);
  }

  /**
   * Add a standalone rule that has minimum priority, fires a transition on all single input
   * characters and has a "print yytext" action.
   */
  public void addStandaloneRule() {
    int start = numStates;
    int end = numStates + 1;

    for (int c = 0; c < classes.getNumClasses(); c++) addTransition(start, c, end);

    for (int i = 0; i < numLexStates * 2; i++) addEpsilonTransition(i, start);

    action[end] = new Action("System.out.print(yytext());", Integer.MAX_VALUE);
    isFinal[end] = true;
  }

  /**
   * Add a regexp to this NFA.
   *
   * @param regExpNum the number of the regexp to add.
   */
  public void addRegExp(int regExpNum) {

    if (Options.DEBUG)
      Out.debug(
          "Adding nfa for regexp " + regExpNum + " :" + Out.NL + regExps.getRegExp(regExpNum));

    IntPair nfa = insertNFA(regExps.getRegExp(regExpNum));

    List<Integer> lexStates = regExps.getStates(regExpNum);

    if (lexStates.isEmpty()) lexStates = scanner.states.getInclusiveStates();

    for (Integer stateNum : lexStates) {
      if (!regExps.isBOL(regExpNum)) addEpsilonTransition(2 * stateNum, nfa.start);

      addEpsilonTransition(2 * stateNum + 1, nfa.start);
    }

    if (regExps.getLookAhead(regExpNum) != null) {
      Action a = regExps.getAction(regExpNum);

      if (a.lookAhead() == Action.FINITE_CHOICE) {
        insertLookAheadChoices(nfa.end, a, regExps.getLookAhead(regExpNum));
        // remove the original action from the collection: it will never
        // be matched directly, only its copies will.
        scanner.actions.remove(a);
      } else {
        RegExp r1 = regExps.getRegExp(regExpNum);
        RegExp r2 = regExps.getLookAhead(regExpNum);

        IntPair look = insertNFA(r2);

        addEpsilonTransition(nfa.end, look.start);

        action[look.end] = a;
        isFinal[look.end] = true;

        if (a.lookAhead() == Action.GENERAL_LOOK) {
          // base forward pass
          IntPair forward = insertNFA(r1);
          // lookahead backward pass
          IntPair backward = insertNFA(r2.rev(macros));

          isFinal[forward.end] = true;
          action[forward.end] = new Action(Action.FORWARD_ACTION);

          isFinal[backward.end] = true;
          action[backward.end] = new Action(Action.BACKWARD_ACTION);

          int entry = 2 * (regExps.getLookEntry(regExpNum) + numLexStates);
          addEpsilonTransition(entry, forward.start);
          addEpsilonTransition(entry + 1, backward.start);

          a.setEntryState(entry);
        }
      }
    } else {
      action[nfa.end] = regExps.getAction(regExpNum);
      isFinal[nfa.end] = true;
    }
  }

  /**
   * Insert NFAs for the (finitely many) fixed length lookahead choices.
   *
   * @param lookAhead a lookahead of which isFiniteChoice is true
   * @param baseEnd the end state of the base expression NFA
   * @param a the action of the expression
   * @see SemCheck#isFiniteChoice(RegExp)
   */
  private void insertLookAheadChoices(int baseEnd, Action a, RegExp lookAhead) {
    if (lookAhead.type == sym.BAR) {
      RegExp2 r = (RegExp2) lookAhead;
      insertLookAheadChoices(baseEnd, a, r.r1);
      insertLookAheadChoices(baseEnd, a, r.r2);
    } else if (lookAhead.type == sym.MACROUSE) {
      RegExp1 r = (RegExp1) lookAhead;
      insertLookAheadChoices(baseEnd, a, macros.getDefinition((String) r.content));
    } else {
      int len = SemCheck.length(lookAhead);

      if (len >= 0) {
        // termination case
        IntPair look = insertNFA(lookAhead);

        addEpsilonTransition(baseEnd, look.start);

        Action x = a.copyChoice(len);
        action[look.end] = x;
        isFinal[look.end] = true;

        // add new copy to the collection of known actions such that
        // it can be checked for the NEVER_MATCH warning.
        scanner.actions.add(x);
      } else {
        // should never happen
        throw new Error(
            "When inserting lookahead expression: unknown expression type "
                + lookAhead.type
                + " in "
                + lookAhead); // $NON-NLS-1$ //$NON-NLS-2$
      }
    }
  }

  /**
   * Make sure the NFA can contain at least newNumStates states.
   *
   * @param newNumStates the minimu number of states.
   */
  private void ensureCapacity(int newNumStates) {
    int oldLength = epsilon.length;

    if (newNumStates < oldLength) return;

    int newStatesLength = Math.max(oldLength * 2, newNumStates);

    boolean[] newFinal = new boolean[newStatesLength];
    boolean[] newIsPush = new boolean[newStatesLength];
    Action[] newAction = new Action[newStatesLength];
    StateSet[][] newTable = new StateSet[newStatesLength][numInput];
    StateSet[] newEpsilon = new StateSet[newStatesLength];

    System.arraycopy(isFinal, 0, newFinal, 0, numStates);
    System.arraycopy(action, 0, newAction, 0, numStates);
    System.arraycopy(epsilon, 0, newEpsilon, 0, numStates);
    System.arraycopy(table, 0, newTable, 0, numStates);

    isFinal = newFinal;
    action = newAction;
    epsilon = newEpsilon;
    table = newTable;
  }

  /**
   * addTransition.
   *
   * @param start a int.
   * @param input a int.
   * @param dest a int.
   */
  public void addTransition(int start, int input, int dest) {
    Out.debug("Adding transition (" + start + ", " + input + ", " + dest + ")");

    int maxS = Math.max(start, dest) + 1;

    ensureCapacity(maxS);

    if (maxS > numStates) numStates = maxS;

    if (table[start][input] != null) table[start][input].addState(dest);
    else table[start][input] = new StateSet(estSize, dest);
  }

  /**
   * addEpsilonTransition.
   *
   * @param start a int.
   * @param dest a int.
   */
  public void addEpsilonTransition(int start, int dest) {
    int max = Math.max(start, dest) + 1;
    ensureCapacity(max);
    if (max > numStates) numStates = max;

    if (epsilon[start] != null) epsilon[start].addState(dest);
    else epsilon[start] = new StateSet(estSize, dest);
  }

  /**
   * Returns <code>true</code>, iff the specified set of states contains a final state.
   *
   * @param set the set of states that is tested for final states.
   */
  private boolean containsFinal(StateSet set) {
    states.reset(set);

    while (states.hasMoreElements()) if (isFinal[states.nextElement()]) return true;

    return false;
  }

  /**
   * Returns <code>true</code>, iff the specified set of states contains a pushback-state.
   *
   * @param set the set of states that is tested for pushback-states. private boolean
   *     containsPushback(StateSet set) { states.reset(set);
   *     <p>while ( states.hasMoreElements() ) if ( isPushback[states.nextElement()] ) return true;
   *     <p>return false; }
   */

  /**
   * Returns the action with highest priority in the specified set of states.
   *
   * @param set the set of states for which to determine the action
   */
  private Action getAction(StateSet set) {

    states.reset(set);

    Action maxAction = null;

    Out.debug("Determining action of : " + set);

    while (states.hasMoreElements()) {

      Action currentAction = action[states.nextElement()];

      if (currentAction != null) {
        if (maxAction == null) maxAction = currentAction;
        else maxAction = maxAction.getHigherPriority(currentAction);
      }
    }

    return maxAction;
  }

  /**
   * Calculates the epsilon closure for a specified set of states.
   *
   * <p>The epsilon closure for set a is the set of states that can be reached by epsilon edges from
   * a.
   *
   * @param startState the start state for the set of states to calculate the epsilon closure for
   * @return the epsilon closure of the specified set of states in this NFA
   */
  private StateSet closure(int startState) {

    // Out.debug("Calculating closure of "+set);

    StateSet notvisited = tempStateSet;
    StateSet closure = new StateSet(numStates, startState);

    notvisited.clear();
    notvisited.addState(startState);

    while (notvisited.containsElements()) {
      // Out.debug("closure is now "+closure);
      // Out.debug("notvisited is "+notvisited);
      int state = notvisited.getAndRemoveElement();
      // Out.debug("removed element "+state+" of "+notvisited);
      // Out.debug("epsilon[states] = "+epsilon[state]);
      notvisited.add(closure.complement(epsilon[state]));
      closure.add(epsilon[state]);
    }

    // Out.debug("Closure is : "+closure);

    return closure;
  }

  /** Returns the epsilon closure of a set of states */
  private StateSet closure(StateSet startStates) {
    StateSet result = new StateSet(numStates);

    if (startStates != null) {
      states.reset(startStates);
      while (states.hasMoreElements()) result.add(closure(states.nextElement()));
    }

    return result;
  }

  private void epsilonFill() {
    for (int i = 0; i < numStates; i++) {
      epsilon[i] = closure(i);
    }
  }

  /**
   * Calculates the set of states that can be reached from another set of states <code>start</code>
   * with an specified input character <code>input</code>
   *
   * @param start the set of states to start from
   * @param input the input character for which to search the next states
   * @return the set of states that are reached from <code>start</code> via <code>input</code>
   */
  private StateSet DFAEdge(StateSet start, int input) {
    // Out.debug(String.format("Calculating DFAEdge for state set "+start+" and input U+04X"),
    // input);

    tempStateSet.clear();

    states.reset(start);
    while (states.hasMoreElements()) tempStateSet.add(table[states.nextElement()][input]);

    StateSet result = new StateSet(tempStateSet);

    states.reset(tempStateSet);
    while (states.hasMoreElements()) result.add(epsilon[states.nextElement()]);

    // Out.debug("DFAEdge is : "+result);

    return result;
  }

  /**
   * Returns an DFA that accepts the same language as this NFA. This DFA is usually not minimal.
   *
   * @return a {@link jflex.DFA} object.
   */
  public DFA getDFA() {

    Map<StateSet, Integer> dfaStates = new HashMap<>(numStates);
    List<StateSet> dfaList = new ArrayList<>(numStates);

    DFA dfa = new DFA(numEntryStates(), numInput, numLexStates);

    int numDFAStates = 0;
    int currentDFAState = 0;

    Out.println("Converting NFA to DFA : ");

    epsilonFill();

    StateSet currentState, newState;

    // create the initial states of the DFA
    for (int i = 0; i < numEntryStates(); i++) {
      newState = epsilon[i];

      dfaStates.put(newState, numDFAStates);
      dfaList.add(newState);

      dfa.setEntryState(i, numDFAStates);

      dfa.setFinal(numDFAStates, containsFinal(newState));
      dfa.setAction(numDFAStates, getAction(newState));

      numDFAStates++;
    }

    numDFAStates--;

    if (Options.DEBUG)
      Out.debug(
          "DFA start states are :"
              + Out.NL
              + dfaStates
              + Out.NL
              + Out.NL
              + "ordered :"
              + Out.NL
              + dfaList);

    currentDFAState = 0;

    StateSet tempStateSet = NFA.tempStateSet;
    StateSetEnumerator states = NFA.states;

    // will be reused
    newState = new StateSet(numStates);

    while (currentDFAState <= numDFAStates) {

      currentState = dfaList.get(currentDFAState);

      for (int input = 0; input < numInput; input++) {

        // newState = DFAEdge(currentState, input);

        // inlining DFAEdge for performance:

        // Out.debug("Calculating DFAEdge for state set "+currentState+" and input '"+input+"'");

        tempStateSet.clear();
        states.reset(currentState);
        while (states.hasMoreElements()) tempStateSet.add(table[states.nextElement()][input]);

        newState.copy(tempStateSet);

        states.reset(tempStateSet);
        while (states.hasMoreElements()) newState.add(epsilon[states.nextElement()]);

        // Out.debug("DFAEdge is : "+newState);

        if (newState.containsElements()) {

          // Out.debug("DFAEdge for input "+(int)input+" and state set "+currentState+" is
          // "+newState);

          // Out.debug("Looking for state set "+newState);
          Integer nextDFAState = dfaStates.get(newState);

          if (nextDFAState != null) {
            // Out.debug("FOUND!");
            dfa.addTransition(currentDFAState, input, nextDFAState);
          } else {
            if (Options.progress) Out.print(".");
            // Out.debug("NOT FOUND!");
            // Out.debug("Table was "+dfaStates);
            numDFAStates++;

            // make a new copy of newState to store in dfaStates
            StateSet storeState = new StateSet(newState);

            dfaStates.put(storeState, numDFAStates);
            dfaList.add(storeState);

            dfa.addTransition(currentDFAState, input, numDFAStates);
            dfa.setFinal(numDFAStates, containsFinal(storeState));
            dfa.setAction(numDFAStates, getAction(storeState));
          }
        }
      }

      currentDFAState++;
    }

    if (Options.verbose) Out.println("");

    return dfa;
  }

  /** dumpTable. */
  public void dumpTable() {
    Out.dump(toString());
  }

  /**
   * toString.
   *
   * @return a {@link java.lang.String} object.
   */
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < numStates; i++) {
      result.append("State");
      if (isFinal[i]) {
        result.append("[FINAL");
        String l = action[i].lookString();
        if (!l.equals("")) {
          result.append(", ");
          result.append(l);
        }
        result.append("]");
      }
      result.append(" " + i + Out.NL);

      for (int input = 0; input < numInput; input++) {
        if (table[i][input] != null && table[i][input].containsElements())
          result
              .append("  with ")
              .append((int) input)
              .append(" in ")
              .append(table[i][input])
              .append(Out.NL);
      }

      if (epsilon[i] != null && epsilon[i].containsElements())
        result.append("  with epsilon in ").append(epsilon[i]).append(Out.NL);
    }

    return result.toString();
  }

  /**
   * writeDot.
   *
   * @param file a {@link java.io.File} object.
   */
  public void writeDot(File file) {
    try {
      PrintWriter writer = new PrintWriter(new FileWriter(file));
      writer.println(dotFormat());
      writer.close();
    } catch (IOException e) {
      Out.error(ErrorMessages.FILE_WRITE, file);
      throw new GeneratorException();
    }
  }

  /**
   * dotFormat.
   *
   * @return a {@link java.lang.String} object.
   */
  public String dotFormat() {
    StringBuilder result = new StringBuilder();

    result.append("digraph NFA {").append(Out.NL);
    result.append("rankdir = LR").append(Out.NL);

    for (int i = 0; i < numStates; i++) {
      if (isFinal[i]) {
        result.append(i);
        result.append(" [shape = doublecircle]");
        result.append(Out.NL);
      }
    }

    for (int i = 0; i < numStates; i++) {
      for (int input = 0; input < numInput; input++) {
        if (table[i][input] != null) {
          StateSetEnumerator states = table[i][input].states();

          while (states.hasMoreElements()) {
            int s = states.nextElement();
            result.append(i).append(" -> ").append(s);
            result
                .append(" [label=\"")
                .append(classes.toString(input))
                .append("\"]")
                .append(Out.NL);
          }
        }
      }
      if (epsilon[i] != null) {
        StateSetEnumerator states = epsilon[i].states();
        while (states.hasMoreElements()) {
          int s = states.nextElement();
          result.append(i).append(" -> ").append(s).append(" [style=dotted]").append(Out.NL);
        }
      }
    }

    result.append("}").append(Out.NL);

    return result.toString();
  }

  // -----------------------------------------------------------------------
  // Functions for constructing NFAs out of regular expressions.

  private void insertLetterNFA(boolean caseless, int ch, int start, int end) {
    if (caseless) {
      IntCharSet set = new IntCharSet(ch);
      IntCharSet caselessSet = set.getCaseless(scanner.getUnicodeProperties());
      for (Interval interval : caselessSet.getIntervals()) {
        for (int elem = interval.start; elem <= interval.end; ++elem) {
          addTransition(start, classes.getClassCode(elem), end);
        }
      }
    } else {
      addTransition(start, classes.getClassCode(ch), end);
    }
  }

  private IntPair insertStringNFA(boolean caseless, String str) {
    int start = numStates;
    int i = 0;
    for (int pos = 0; pos < str.length(); ++i) {
      int ch = str.codePointAt(pos);
      if (caseless) {
        IntCharSet set = new IntCharSet(ch);
        IntCharSet caselessSet = set.getCaseless(scanner.getUnicodeProperties());
        for (Interval interval : caselessSet.getIntervals()) {
          for (int elem = interval.start; elem <= interval.end; ++elem) {
            addTransition(i + start, classes.getClassCode(elem), i + start + 1);
          }
        }
      } else {
        addTransition(i + start, classes.getClassCode(ch), i + start + 1);
      }
      pos += Character.charCount(ch);
    }

    return new IntPair(start, i + start);
  }

  private void insertClassNFA(List<Interval> intervals, int start, int end) {
    // empty char class is ok:
    if (intervals == null) return;

    for (int aCl : classes.getClassCodes(intervals)) addTransition(start, aCl, end);
  }

  private void insertNotClassNFA(List<Interval> intervals, int start, int end) {

    for (int input : classes.getNotClassCodes(intervals)) addTransition(start, input, end);
  }

  /**
   * Constructs an NFA accepting the complement of the language of a given NFA.
   *
   * <p>Converts the NFA into a DFA, then negates that DFA. Exponential state blowup possible and
   * common.
   *
   * @param nfa the NFA to construct the complement for.
   * @return a pair of integers denoting the index of start and end state of the complement NFA.
   */
  private IntPair complement(IntPair nfa) {

    if (Options.DEBUG) {
      Out.debug("complement for " + nfa);
      Out.debug("NFA is :" + Out.NL + this);
    }

    int dfaStart = nfa.end + 1;

    // FIXME: only need epsilon closure of states reachable from nfa.start
    epsilonFill();

    Map<StateSet, Integer> dfaStates = new HashMap<>(numStates);
    List<StateSet> dfaList = new ArrayList<>(numStates);

    int numDFAStates = 0;
    int currentDFAState = 0;

    StateSet currentState, newState;

    newState = epsilon[nfa.start];
    dfaStates.put(newState, numDFAStates);
    dfaList.add(newState);

    if (Options.DEBUG) {
      Out.debug(
          "pos DFA start state is :"
              + Out.NL
              + dfaStates
              + Out.NL
              + Out.NL
              + "ordered :"
              + Out.NL
              + dfaList);
    }
    currentDFAState = 0;

    while (currentDFAState <= numDFAStates) {

      currentState = dfaList.get(currentDFAState);

      for (int input = 0; input < numInput; input++) {
        newState = DFAEdge(currentState, input);

        if (newState.containsElements()) {

          // Out.debug("DFAEdge for input "+(int)input+" and state set "+currentState+" is
          // "+newState);

          // Out.debug("Looking for state set "+newState);
          Integer nextDFAState = dfaStates.get(newState);

          if (nextDFAState != null) {
            // Out.debug("FOUND!");
            addTransition(dfaStart + currentDFAState, input, dfaStart + nextDFAState);
          } else {
            if (Options.dump) {
              Out.print("+");
              // Out.debug("NOT FOUND!");
              // Out.debug("Table was "+dfaStates);
            }
            numDFAStates++;

            dfaStates.put(newState, numDFAStates);
            dfaList.add(newState);

            addTransition(dfaStart + currentDFAState, input, dfaStart + numDFAStates);
          }
        }
      }

      currentDFAState++;
    }

    // We have a dfa accepting the positive regexp.

    // Now the complement:
    if (Options.DEBUG) {
      Out.debug("dfa finished, nfa is now :" + Out.NL + this);
    }

    int start = dfaStart + numDFAStates + 1;
    int error = dfaStart + numDFAStates + 2;
    int end = dfaStart + numDFAStates + 3;

    addEpsilonTransition(start, dfaStart);

    for (int i = 0; i < numInput; i++) addTransition(error, i, error);

    addEpsilonTransition(error, end);

    for (int s = 0; s <= numDFAStates; s++) {
      currentState = dfaList.get(s);

      currentDFAState = dfaStart + s;

      // if it was not a final state, it is now in the complement
      if (!currentState.isElement(nfa.end)) addEpsilonTransition(currentDFAState, end);

      // all inputs not present (formerly leading to an implicit error)
      // now lead to an explicit (final) state accepting everything.
      for (int i = 0; i < numInput; i++)
        if (table[currentDFAState][i] == null) addTransition(currentDFAState, i, error);
    }

    // eliminate transitions leading to dead states
    if (live == null || live.length < numStates) {
      live = new boolean[2 * numStates];
      visited = new boolean[2 * numStates];
    }

    removeDead(dfaStart, end);

    if (Options.DEBUG) {
      Out.debug("complement finished, nfa (" + start + "," + end + ") is now :" + this);
    }
    return new IntPair(start, end);
  }

  // "global" data for use in method removeDead only:
  // live[s] == false <=> no final state can be reached from s
  private boolean[] live; // = new boolean [estSize];
  private boolean[] visited; // = new boolean [estSize];

  private void removeDead(int start, int end) {
    // Out.debug("removeDead ("+start+")");

    if (visited[start] || live[start]) return;
    visited[start] = true;

    // Out.debug("not yet visited");

    if (closure(start).isElement(end)) live[start] = true;

    // Out.debug("is final :"+live[start]);

    for (int i = 0; i < numInput; i++) {
      StateSet nextState = closure(table[start][i]);
      StateSetEnumerator states = nextState.states();
      while (states.hasMoreElements()) {
        int next = states.nextElement();

        if (next != start) {
          removeDead(next, end);

          if (live[next]) live[start] = true;
          else table[start][i] = null;
        }
      }
    }

    StateSet nextState = closure(epsilon[start]);
    StateSetEnumerator states = nextState.states();
    while (states.hasMoreElements()) {
      int next = states.nextElement();

      if (next != start) {
        removeDead(next, end);

        if (live[next]) live[start] = true;
      }
    }

    // Out.debug("state "+start+" is live :"+live[start]);
  }

  /**
   * Constructs a two state NFA for char class regexps, such that the NFA has
   *
   * <p>exactly one start state, exactly one end state, no transitions leading out of the end state
   * no transitions leading into the start state
   *
   * <p>Assumes that regExp.isCharClass(macros) == true
   *
   * @param regExp the regular expression to construct the NFA for
   * @return a pair of integers denoting the index of start and end state of the NFA.
   */
  private void insertCCLNFA(RegExp regExp, int start, int end) {
    switch (regExp.type) {
      case sym.BAR:
        RegExp2 r = (RegExp2) regExp;
        insertCCLNFA(r.r1, start, end);
        insertCCLNFA(r.r2, start, end);
        return;

      case sym.CCLASS:
        insertClassNFA((List<Interval>) ((RegExp1) regExp).content, start, end);
        return;

      case sym.CCLASSNOT:
        insertNotClassNFA((List<Interval>) ((RegExp1) regExp).content, start, end);
        return;

      case sym.CHAR:
        insertLetterNFA(false, (Integer) ((RegExp1) regExp).content, start, end);
        return;

      case sym.CHAR_I:
        insertLetterNFA(true, (Integer) ((RegExp1) regExp).content, start, end);
        return;

      case sym.MACROUSE:
        insertCCLNFA(macros.getDefinition((String) ((RegExp1) regExp).content), start, end);
        return;
    }

    throw new Error("Unknown expression type " + regExp.type + " in NFA construction");
  }

  /**
   * Constructs an NFA for regExp such that the NFA has
   *
   * <p>exactly one start state, exactly one end state, no transitions leading out of the end state
   * no transitions leading into the start state
   *
   * @param regExp the regular expression to construct the NFA for
   * @return a pair of integers denoting the index of start and end state of the NFA.
   */
  public IntPair insertNFA(RegExp regExp) {

    IntPair nfa1, nfa2;
    int start, end;
    RegExp2 r;

    if (Options.DEBUG) {
      Out.debug("Inserting RegExp : " + regExp);
    }

    if (regExp.isCharClass(macros)) {
      start = numStates;
      end = numStates + 1;

      ensureCapacity(end + 1);
      if (end + 1 > numStates) numStates = end + 1;

      insertCCLNFA(regExp, start, end);

      return new IntPair(start, end);
    }

    switch (regExp.type) {
      case sym.BAR:
        r = (RegExp2) regExp;

        nfa1 = insertNFA(r.r1);
        nfa2 = insertNFA(r.r2);

        start = nfa2.end + 1;
        end = nfa2.end + 2;

        addEpsilonTransition(start, nfa1.start);
        addEpsilonTransition(start, nfa2.start);
        addEpsilonTransition(nfa1.end, end);
        addEpsilonTransition(nfa2.end, end);

        return new IntPair(start, end);

      case sym.CONCAT:
        r = (RegExp2) regExp;

        nfa1 = insertNFA(r.r1);
        nfa2 = insertNFA(r.r2);

        addEpsilonTransition(nfa1.end, nfa2.start);

        return new IntPair(nfa1.start, nfa2.end);

      case sym.STAR:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        start = nfa1.end + 1;
        end = nfa1.end + 2;

        addEpsilonTransition(nfa1.end, end);
        addEpsilonTransition(start, nfa1.start);

        addEpsilonTransition(start, end);
        addEpsilonTransition(nfa1.end, nfa1.start);

        return new IntPair(start, end);

      case sym.PLUS:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        start = nfa1.end + 1;
        end = nfa1.end + 2;

        addEpsilonTransition(nfa1.end, end);
        addEpsilonTransition(start, nfa1.start);

        addEpsilonTransition(nfa1.end, nfa1.start);

        return new IntPair(start, end);

      case sym.QUESTION:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        addEpsilonTransition(nfa1.start, nfa1.end);

        return new IntPair(nfa1.start, nfa1.end);

      case sym.BANG:
        return complement(insertNFA((RegExp) ((RegExp1) regExp).content));

      case sym.TILDE:
        return insertNFA(regExp.resolveTilde(macros));

      case sym.STRING:
        return insertStringNFA(false, (String) ((RegExp1) regExp).content);

      case sym.STRING_I:
        return insertStringNFA(true, (String) ((RegExp1) regExp).content);

      case sym.MACROUSE:
        return insertNFA(macros.getDefinition((String) ((RegExp1) regExp).content));
    }

    throw new Error("Unknown expression type " + regExp.type + " in NFA construction");
  }
}
