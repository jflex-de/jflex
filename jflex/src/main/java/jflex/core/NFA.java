/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import jflex.base.Build;
import jflex.base.IntPair;
import jflex.chars.Interval;
import jflex.core.unicode.CharClasses;
import jflex.core.unicode.IntCharSet;
import jflex.logging.Out;
import jflex.option.Options;

/**
 * Non-deterministic finite automata representation in JFlex.
 *
 * <p>Contains algorithms RegExp → NFA.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public final class NFA {

  /**
   * {@code transitions[state][0..numTransitions[state]-1]} are the transitions of {@code state} on
   * inputs, each one as {@code (input << 32) | target}, see {@link #input(long)} and {@link
   * #target(long)}. In no particular order, and the same transition can be there more than once.
   * {@code null} if the state never had a transition.
   *
   * <p>Most states have very few transitions, and the NFA can have a lot of states: a table of all
   * states and inputs, or a bit set of target states per transition, is far too large.
   */
  private long[][] transitions;

  private int[] numTransitions;

  /**
   * {@code epsilon[state][0..numEpsilon[state]-1]} are the states that can be reached from {@code
   * state} via one epsilon edge. In no particular order, and the same state can be there more than
   * once. {@code null} if the state never had an epsilon edge.
   */
  private int[][] epsilon;

  private int[] numEpsilon;

  /** isFinal[state] == true <=> state is a final state of the NFA */
  private boolean[] isFinal;

  /**
   * action[current_state]: the action associated with the state current_state (null, if there is no
   * action for the state)
   */
  private Action[] action;

  /** the number of states in this NFA */
  private int numStates;

  /** the current maximum number of input characters */
  private final int numInput;

  /**
   * the number of lexical States. Lexical states have the indices 0..numLexStates-1 in the
   * transition table
   */
  private int numLexStates;

  private CharClasses classes;

  private LexScan scanner;
  private final Options options;
  private final Out out;
  private RegExps regExps;

  /**
   * Constructor for NFA.
   *
   * @param options the options of this generator run
   * @param out the output of this generator run
   */
  public NFA(int numInput, int estSize, Options options, Out out) {
    this.options = options;
    this.out = out;
    this.numInput = numInput;
    numStates = 0;
    epsilon = new int[estSize][];
    numEpsilon = new int[estSize];
    action = new Action[estSize];
    isFinal = new boolean[estSize];
    transitions = new long[estSize][];
    numTransitions = new int[estSize];
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
    this(
        numInput,
        regExps.NFASize(macros) + 2 * scanner.states.number(),
        scanner.options(),
        scanner.out());

    this.scanner = scanner;
    this.regExps = regExps;
    this.classes = classes;

    numLexStates = scanner.states.number();

    // ensureCapacity assumes correctly set up numStates.
    int new_num = numEntryStates();
    ensureCapacity(new_num);
    numStates = new_num;
  }

  /** Returns the input of a transition in {@link #transitions(int)}. */
  public static int input(long transition) {
    return (int) (transition >>> 32);
  }

  /** Returns the target state of a transition in {@link #transitions(int)}. */
  public static int target(long transition) {
    return (int) transition;
  }

  /**
   * Returns the number of transitions on inputs of the state, where transitions that were added
   * more than once count more than once.
   */
  public int numTransitions(int state) {
    return numTransitions[state];
  }

  /**
   * Returns the transitions on inputs of the state.
   *
   * @return an array where the first {@link #numTransitions(int)} elements are the transitions,
   *     with {@link #input(long)} and {@link #target(long)}. They are in no particular order, and
   *     the same transition can be there more than once. The array must not be changed, and is only
   *     valid until the NFA changes. Can be null if there are no transitions.
   */
  public long[] transitions(int state) {
    return transitions[state];
  }

  /**
   * Returns the number of epsilon edges of the state, where edges that were added more than once
   * count more than once.
   */
  public int numEpsilon(int state) {
    return numEpsilon[state];
  }

  /**
   * Returns the states that can be reached from the state via one epsilon edge.
   *
   * @return an array where the first {@link #numEpsilon(int)} elements are the states. They are in
   *     no particular order, and the same state can be there more than once. The array must not be
   *     changed, and is only valid until the NFA changes. Can be null if there are no edges.
   */
  public int[] epsilon(int state) {
    return epsilon[state];
  }

  public int numEntryStates() {
    return 2 * (numLexStates + regExps.gen_look_count);
  }

  public int numInput() {
    return numInput;
  }

  public int numLexStates() {
    return numLexStates;
  }

  public int numStates() {
    return numStates;
  }

  /** Returns true iff the state is a final state of the NFA. */
  public boolean isFinal(int state) {
    return isFinal[state];
  }

  /** Returns the action of the state, null if it has none. */
  public Action action(int state) {
    return action[state];
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

    action[end] = new Action("System.out.print(yytext());");
    isFinal[end] = true;
  }

  /**
   * Add a regexp to this NFA.
   *
   * @param regExpNum the number of the regexp to add.
   */
  public void addRegExp(int regExpNum) {

    if (Build.DEBUG)
      Out.debug(
          "Adding nfa for regexp " + regExpNum + " :" + Out.NL + regExps.getRegExp(regExpNum));

    IntPair nfa = insertNFA(regExps.getRegExp(regExpNum));

    List<Integer> lexStates = regExps.getStates(regExpNum);

    if (lexStates.isEmpty()) lexStates = scanner.states.getInclusiveStates();

    for (Integer stateNum : lexStates) {
      if (!regExps.isBOL(regExpNum)) addEpsilonTransition(2 * stateNum, nfa.start());

      addEpsilonTransition(2 * stateNum + 1, nfa.start());
    }

    if (regExps.getLookAhead(regExpNum) != null) {
      Action a = regExps.getAction(regExpNum);

      if (a != null && a.lookAhead() == Action.Kind.FINITE_CHOICE) {
        insertLookAheadChoices(nfa.end(), a, regExps.getLookAhead(regExpNum));
        // remove the original action from the collection: it will never
        // be matched directly, only its copies will.
        scanner.actions.remove(a);
      } else {
        RegExp r1 = regExps.getRegExp(regExpNum);
        RegExp r2 = regExps.getLookAhead(regExpNum);

        IntPair look = insertNFA(r2);

        addEpsilonTransition(nfa.end(), look.start());

        action[look.end()] = a;
        isFinal[look.end()] = true;

        if (a != null && a.lookAhead() == Action.Kind.GENERAL_LOOK) {
          // base forward pass
          IntPair forward = insertNFA(r1);
          // lookahead backward pass
          IntPair backward = insertNFA(r2.rev());

          isFinal[forward.end()] = true;
          action[forward.end()] = new Action(Action.Kind.FORWARD_ACTION);

          isFinal[backward.end()] = true;
          action[backward.end()] = new Action(Action.Kind.BACKWARD_ACTION);

          int entry = 2 * (regExps.getLookEntry(regExpNum) + numLexStates);
          addEpsilonTransition(entry, forward.start());
          addEpsilonTransition(entry + 1, backward.start());

          a.setEntryState(entry);
        }
      }
    } else {
      action[nfa.end()] = regExps.getAction(regExpNum);
      isFinal[nfa.end()] = true;
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
    } else {
      int len = SemCheck.length(lookAhead);

      if (len >= 0) {
        // termination case
        IntPair look = insertNFA(lookAhead);

        addEpsilonTransition(baseEnd, look.start());

        Action x = a.copyChoice(len);
        action[look.end()] = x;
        isFinal[look.end()] = true;

        // add new copy to the collection of known actions such that
        // it can be checked for the NEVER_MATCH warning.
        scanner.actions.add(x);
      } else {
        // should never happen
        throw new RegExpException(lookAhead);
      }
    }
  }

  /**
   * Make sure the NFA can contain at least newNumStates states.
   *
   * @param newNumStates the minimum number of states.
   */
  private void ensureCapacity(int newNumStates) {
    int oldLength = epsilon.length;

    if (newNumStates < oldLength) return;

    int newStatesLength = Math.max(oldLength * 2, newNumStates);

    isFinal = Arrays.copyOf(isFinal, newStatesLength);
    action = Arrays.copyOf(action, newStatesLength);
    epsilon = Arrays.copyOf(epsilon, newStatesLength);
    numEpsilon = Arrays.copyOf(numEpsilon, newStatesLength);
    transitions = Arrays.copyOf(transitions, newStatesLength);
    numTransitions = Arrays.copyOf(numTransitions, newStatesLength);
  }

  public void addTransition(int start, int input, int dest) {
    Out.debug("Adding transition (" + start + ", " + input + ", " + dest + ")");

    // Trying to insert a transition for a character that is not in the input
    // char set. Ignore it. This can happen for case insensitive matching.
    if (input == -1) return;

    int maxS = Math.max(start, dest) + 1;

    ensureCapacity(maxS);

    if (maxS > numStates) numStates = maxS;

    long[] t = transitions[start];
    int n = numTransitions[start];
    if (t == null) {
      t = transitions[start] = new long[1];
    } else if (n == t.length) {
      t = transitions[start] = Arrays.copyOf(t, 2 * n);
    }
    t[n] = ((long) input << 32) | dest;
    numTransitions[start] = n + 1;
  }

  public void addEpsilonTransition(int start, int dest) {
    int max = Math.max(start, dest) + 1;
    ensureCapacity(max);
    if (max > numStates) numStates = max;

    int[] e = epsilon[start];
    int n = numEpsilon[start];
    if (e == null) {
      e = epsilon[start] = new int[2];
    } else if (n == e.length) {
      e = epsilon[start] = Arrays.copyOf(e, 2 * n);
    }
    e[n] = dest;
    numEpsilon[start] = n + 1;
  }

  /** Returns the distinct transitions on inputs of the state, by input and then by target. */
  private long[] sortedTransitions(int state) {
    return sortedDistinct(
        numTransitions[state] == 0
            ? new long[0]
            : Arrays.copyOf(transitions[state], numTransitions[state]));
  }

  /** Returns the distinct states with an epsilon edge from the state, in ascending order. */
  private int[] sortedEpsilon(int state) {
    long[] sorted = new long[numEpsilon[state]];
    for (int i = 0; i < sorted.length; i++) sorted[i] = epsilon[state][i];
    sorted = sortedDistinct(sorted);
    int[] result = new int[sorted.length];
    for (int i = 0; i < result.length; i++) result[i] = (int) sorted[i];
    return result;
  }

  /** Sorts the array and returns it without duplicates. */
  private static long[] sortedDistinct(long[] values) {
    Arrays.sort(values);
    int n = 0;
    for (int i = 0; i < values.length; i++) {
      if (i == 0 || values[i] != values[i - 1]) values[n++] = values[i];
    }
    return n == values.length ? values : Arrays.copyOf(values, n);
  }

  public void dumpTable() {
    out.dump(toString());
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < numStates; i++) {
      result.append("State");
      if (isFinal[i]) {
        result.append("[FINAL");
        String l = action[i].lookString();
        if (!Objects.equals(l, "")) {
          result.append(", ");
          result.append(l);
        }
        result.append("]");
      }
      result.append(" ").append(i).append(Out.NL);

      long[] sorted = sortedTransitions(i);
      for (int from = 0; from < sorted.length; ) {
        // all targets of one input
        int input = input(sorted[from]);
        result.append("  with ").append(input).append(" in {").append(target(sorted[from++]));
        while (from < sorted.length && input(sorted[from]) == input) {
          result.append(", ").append(target(sorted[from++]));
        }
        result.append("}").append(Out.NL);
      }

      if (numEpsilon[i] > 0) {
        result.append("  with epsilon in {");
        String separator = "";
        for (int s : sortedEpsilon(i)) {
          result.append(separator).append(s);
          separator = ", ";
        }
        result.append("}").append(Out.NL);
      }
    }

    return result.toString();
  }

  /**
   * Writes a dot-file representing this NFA.
   *
   * @param file output file.
   * @throws IOException if the file cannot be written
   */
  public void writeDot(File file) throws IOException {
    try (PrintWriter writer =
        new PrintWriter(
            new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
      writer.println(dotFormat());
    }
  }

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
      for (long transition : sortedTransitions(i)) {
        result.append(i).append(" -> ").append(target(transition));
        result
            .append(" [label=\"")
            .append(classes.toString(input(transition)))
            .append("\"]")
            .append(Out.NL);
      }
      for (int s : sortedEpsilon(i)) {
        result.append(i).append(" -> ").append(s).append(" [style=dotted]").append(Out.NL);
      }
    }

    result.append("}").append(Out.NL);

    return result.toString();
  }

  // -----------------------------------------------------------------------
  // Functions for constructing NFAs out of regular expressions.

  private void insertLetterNFA(boolean caseless, int ch, int start, int end) {
    if (caseless) {
      IntCharSet set = IntCharSet.ofCharacter(ch);
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
        IntCharSet set = IntCharSet.ofCharacter(ch);
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

    return IntPair.create(start, i + start);
  }

  private void insertClassNFA(IntCharSet set, int start, int end) {
    for (int aCl : classes.getClassCodes(set, false)) {
      addTransition(start, aCl, end);
    }
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

    if (Build.DEBUG) {
      Out.debug("complement for " + nfa);
      Out.debug("NFA is :" + Out.NL + this);
    }

    int dfaStart = nfa.end() + 1;

    // The DFA of the positive regexp. Only what is reachable from nfa.start() matters, and the
    // states of the DFA are added to this NFA when the DFA is complete: the subset construction
    // needs an NFA that does not change.

    // dfaTable.get(s)[input] is the DFA state after s with input, -1 if there is none
    List<int[]> dfaTable = new ArrayList<>();
    // dfaFinal.get(s) iff the DFA state s contains nfa.end()
    List<Boolean> dfaFinal = new ArrayList<>();

    SubsetConstruction construction =
        new SubsetConstruction(
            this,
            new SubsetConstruction.Target() {
              @Override
              public void state(int state, int[] nfaStates) {
                if (state > 0 && options.dump()) out.print("+");

                int[] row = new int[numInput];
                Arrays.fill(row, -1);
                dfaTable.add(row);
                dfaFinal.add(Arrays.binarySearch(nfaStates, nfa.end()) >= 0);
              }

              @Override
              public void transition(int from, int input, int to) {
                dfaTable.get(from)[input] = to;
              }
            },
            nfa.end());
    construction.addEntryState(nfa.start());
    construction.run();

    // the number of the last DFA state
    int numDFAStates = dfaTable.size() - 1;

    // We have a dfa accepting the positive regexp.

    // Now the complement:
    if (Build.DEBUG) {
      Out.debug("dfa finished, nfa is now :" + Out.NL + this);
    }

    int start = dfaStart + numDFAStates + 1;
    int error = dfaStart + numDFAStates + 2;
    int end = dfaStart + numDFAStates + 3;

    addEpsilonTransition(start, dfaStart);

    for (int i = 0; i < numInput; i++) addTransition(error, i, error);

    addEpsilonTransition(error, end);

    for (int s = 0; s <= numDFAStates; s++) {
      int currentDFAState = dfaStart + s;

      // if it was not a final state, it is now in the complement
      if (!dfaFinal.get(s)) addEpsilonTransition(currentDFAState, end);

      // all inputs not present (formerly leading to an implicit error)
      // now lead to an explicit (final) state accepting everything.
      int[] row = dfaTable.get(s);
      for (int i = 0; i < numInput; i++) {
        addTransition(currentDFAState, i, row[i] >= 0 ? dfaStart + row[i] : error);
      }
    }

    // eliminate transitions that cannot reach final states
    removeDead(dfaStart, end);

    if (Build.DEBUG) {
      Out.debug("complement finished, nfa (" + start + "," + end + ") is now :" + this);
    }
    return IntPair.create(start, end);
  }

  /**
   * Find all states from (numerically) {@code start} to @{@code end} that (transitively) cannot
   * reach reach {@code end}, and remove the transitions leading to those states.
   *
   * <p>After a complement operation, there may be dead states left over in the NFA, which could
   * lead the scanning engine into a situation where it is trying to perform lookahead even though
   * no final state can ever be reached.
   *
   * <p>Precondition: all states that potentially lead to {@code end} are within the interval @{code
   * [start,end]}. This is satisfied by DFA generation in the complement operation.
   *
   * <p>Precondition: end state has no outgoing transitions
   *
   * @param start the first state from which to compute live states
   * @param end the state that if it can be reached makes a state live
   * @see NFA#complement(IntPair)
   */
  private void removeDead(int start, int end) {
    if (Build.DEBUG) {
      Out.debug("removeDead (" + start + "," + end + ") " + Out.NL + this);
    }

    // All states in question are in [start,end], state s has the index s - start below.
    int n = end - start + 1;

    // the states that are reachable from start, and a stack of those among them that have not been
    // expanded yet
    boolean[] reachable = new boolean[n];
    int[] stack = new int[n];
    int top = 0;

    reachable[0] = true;
    stack[top++] = start;

    // The edges between reachable states in reverse: the states with an edge to state t are
    // pred[predStart[t-start]] .. pred[predStart[t-start+1]-1]. First count them, then fill.
    int[] predStart = new int[n + 1];

    while (top > 0) {
      int state = stack[--top];
      for (int i = 0; i < numTransitions[state] + numEpsilon[state]; i++) {
        int t = successor(state, i);
        predStart[t - start + 1]++;
        if (!reachable[t - start]) {
          reachable[t - start] = true;
          stack[top++] = t;
        }
      }
    }

    for (int i = 0; i < n; i++) predStart[i + 1] += predStart[i];
    int[] pred = new int[predStart[n]];
    int[] next = Arrays.copyOf(predStart, n);
    for (int state = start; state <= end; state++) {
      if (!reachable[state - start]) continue;
      for (int i = 0; i < numTransitions[state] + numEpsilon[state]; i++) {
        pred[next[successor(state, i) - start]++] = state;
      }
    }

    // the states that can reach end: search backwards from end
    boolean[] live = new boolean[n];
    if (reachable[end - start]) {
      live[end - start] = true;
      stack[top++] = end;
    }
    while (top > 0) {
      int t = stack[--top] - start;
      for (int i = predStart[t]; i < predStart[t + 1]; i++) {
        int state = pred[i];
        if (!live[state - start]) {
          live[state - start] = true;
          stack[top++] = state;
        }
      }
    }

    // now remove all transitions to non-live states
    for (int state = start; state <= end; state++) {
      if (!reachable[state - start]) continue;

      long[] t = transitions[state];
      int kept = 0;
      for (int i = 0; i < numTransitions[state]; i++) {
        if (live[target(t[i]) - start]) t[kept++] = t[i];
      }
      numTransitions[state] = kept;

      int[] e = epsilon[state];
      kept = 0;
      for (int i = 0; i < numEpsilon[state]; i++) {
        if (live[e[i] - start]) e[kept++] = e[i];
      }
      numEpsilon[state] = kept;
    }

    if (Build.DEBUG) {
      Out.debug("Removed dead states " + Out.NL + this);
    }
  }

  /**
   * Returns the target of the i-th edge of the state, where the transitions on inputs come first
   * and the epsilon edges after them.
   */
  private int successor(int state, int i) {
    int n = numTransitions[state];
    return i < n ? target(transitions[state][i]) : epsilon[state][i - n];
  }

  /**
   * Constructs a two state NFA for char class regexps, such that the NFA has
   *
   * <ul>
   *   <li>exactly one start state,
   *   <li>exactly one end state,
   *   <li>no transitions leading out of the end state,
   *   <li>no transitions leading into the start state.
   * </ul>
   *
   * <p>Assumes that regExp.isCharClass(macros) == true
   *
   * @param regExp the regular expression to construct the NFA for
   */
  private void insertCCLNFA(RegExp regExp, int start, int end) {
    switch (regExp.type) {
      case sym.BAR:
        RegExp2 r = (RegExp2) regExp;
        insertCCLNFA(r.r1, start, end);
        insertCCLNFA(r.r2, start, end);
        return;

      case sym.PRIMCLASS:
        insertClassNFA((IntCharSet) ((RegExp1) regExp).content, start, end);
        return;

      case sym.CHAR:
        insertLetterNFA(false, (Integer) ((RegExp1) regExp).content, start, end);
        return;

      case sym.CHAR_I:
        insertLetterNFA(true, (Integer) ((RegExp1) regExp).content, start, end);
        return;

      default:
        throw new RegExpException(regExp);
    }
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

    if (Build.DEBUG) {
      Out.debug("Inserting RegExp : " + regExp);
    }

    if (regExp.isCharClass()) {
      start = numStates;
      end = numStates + 1;

      ensureCapacity(end + 1);
      numStates = end + 1;

      insertCCLNFA(regExp, start, end);

      return IntPair.create(start, end);
    }

    switch (regExp.type) {
      case sym.BAR:
        r = (RegExp2) regExp;

        nfa1 = insertNFA(r.r1);
        nfa2 = insertNFA(r.r2);

        start = nfa2.end() + 1;
        end = nfa2.end() + 2;

        addEpsilonTransition(start, nfa1.start());
        addEpsilonTransition(start, nfa2.start());
        addEpsilonTransition(nfa1.end(), end);
        addEpsilonTransition(nfa2.end(), end);

        return IntPair.create(start, end);

      case sym.CONCAT:
        r = (RegExp2) regExp;

        nfa1 = insertNFA(r.r1);
        nfa2 = insertNFA(r.r2);

        addEpsilonTransition(nfa1.end(), nfa2.start());

        return IntPair.create(nfa1.start(), nfa2.end());

      case sym.STAR:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        start = nfa1.end() + 1;
        end = nfa1.end() + 2;

        addEpsilonTransition(nfa1.end(), end);
        addEpsilonTransition(start, nfa1.start());

        addEpsilonTransition(start, end);
        addEpsilonTransition(nfa1.end(), nfa1.start());

        return IntPair.create(start, end);

      case sym.PLUS:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        start = nfa1.end() + 1;
        end = nfa1.end() + 2;

        addEpsilonTransition(nfa1.end(), end);
        addEpsilonTransition(start, nfa1.start());

        addEpsilonTransition(nfa1.end(), nfa1.start());

        return IntPair.create(start, end);

      case sym.QUESTION:
        nfa1 = insertNFA((RegExp) ((RegExp1) regExp).content);

        addEpsilonTransition(nfa1.start(), nfa1.end());

        return IntPair.create(nfa1.start(), nfa1.end());

      case sym.BANG:
        return complement(insertNFA((RegExp) ((RegExp1) regExp).content));

      case sym.TILDE:
        return insertNFA(regExp.resolveTilde());

      case sym.STRING:
        return insertStringNFA(false, (String) ((RegExp1) regExp).content);

      case sym.STRING_I:
        return insertStringNFA(true, (String) ((RegExp1) regExp).content);

      default:
        throw new RegExpException(regExp);
    }
  }
}
