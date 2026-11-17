/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The subset construction NFA → DFA.
 *
 * <p>The sets of NFA states are kept sparse instead of as bit sets over all NFA states, so that the
 * cost of a DFA state depends on the number of NFA states and transitions it actually has, and not
 * on the size of the whole NFA:
 *
 * <ul>
 *   <li>A DFA state is identified by its <em>important</em> NFA states only: those that have a
 *       transition on an input or that are final. All other NFA states of an epsilon closure
 *       influence neither the transitions nor the action of the DFA state, so that sets which only
 *       differ in such states are the same DFA state.
 *   <li>The transitions of a DFA state are computed in one pass over the transitions of its NFA
 *       states, instead of one pass over all its NFA states per input.
 *   <li>The DFA state for a set of NFA target states (before taking the epsilon closure) is
 *       remembered, so that the closure is only computed once per distinct set of targets. Many
 *       inputs and many DFA states lead to the same set of targets.
 *   <li>Epsilon closures are only computed for the NFA states that are the target of a transition.
 * </ul>
 *
 * <p>DFA states are numbered from 0 in the order they are found, with inputs in ascending order.
 *
 * <p>The NFA must not change between the construction of this object and the end of {@link #run()}.
 *
 * @see NFA
 */
public final class SubsetConstruction {

  /** Receives the DFA. */
  public interface Target {
    /**
     * A new DFA state. Called before any transition from or to the state.
     *
     * @param state the number of the DFA state, the number of DFA states so far.
     * @param nfaStates the important NFA states of the DFA state in ascending order, see {@link
     *     SubsetConstruction}. Must not be changed.
     */
    void state(int state, int[] nfaStates);

    /**
     * A transition of the DFA. The transitions of a state come in ascending order of the input.
     *
     * @param from the DFA state the transition starts in
     * @param input the input of the transition
     * @param to the DFA state the transition leads to
     */
    void transition(int from, int input, int to);
  }

  /** A set of NFA states as map key: a sorted array of distinct states. */
  private static final class Key {
    final int[] states;
    final int hash;

    Key(int[] states) {
      this.states = states;
      this.hash = Arrays.hashCode(states);
    }

    @Override
    public int hashCode() {
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof Key
          && hash == ((Key) obj).hash
          && Arrays.equals(states, ((Key) obj).states);
    }
  }

  private static final int[] NO_STATES = new int[0];

  private final NFA nfa;
  private final Target target;

  /** An NFA state that is important in addition to those of the NFA, -1 if there is none. */
  private final int importantState;

  /**
   * {@code closure[s]} are the important states in the epsilon closure of NFA state {@code s} in
   * ascending order. Computed on demand, most NFA states are never the target of a transition.
   */
  private final int[][] closure;

  /** {@code mark[s] == epoch} iff NFA state {@code s} is in the set that is being collected. */
  private final int[] mark;

  private int epoch;

  /** Stack of NFA states for the search in {@link #closure(int)}. */
  private int[] stack = new int[64];

  /** The NFA states collected so far, {@code buffer[0..size-1]}. */
  private int[] buffer = new int[64];

  /** The DFA state of each set of important NFA states. */
  private final Map<Key, Integer> dfaStates = new HashMap<>();

  /** The DFA state that each set of NFA target states leads to. */
  private final Map<Key, Integer> targetStates = new HashMap<>();

  /** {@code dfaList[i]} are the important NFA states of DFA state {@code i}. */
  private int[][] dfaList = new int[64][];

  private int numDfaStates;

  /**
   * Prepares the subset construction for an NFA.
   *
   * @param nfa the NFA to convert
   * @param target receives the DFA
   */
  public SubsetConstruction(NFA nfa, Target target) {
    this(nfa, target, -1);
  }

  /**
   * Prepares the subset construction for an NFA.
   *
   * @param nfa the NFA to convert
   * @param target receives the DFA
   * @param importantState an NFA state that is reported in {@link Target#state(int, int[])} even if
   *     it has no transitions on an input and is not final.
   */
  SubsetConstruction(NFA nfa, Target target, int importantState) {
    this.nfa = nfa;
    this.target = target;
    this.importantState = importantState;

    closure = new int[nfa.numStates()][];
    mark = new int[nfa.numStates()];
  }

  /**
   * Adds a new DFA state for the epsilon closure of an NFA state. The state is new even if there is
   * a DFA state for the same set of NFA states already.
   *
   * @param nfaState the NFA state to start in
   * @return the number of the new DFA state.
   */
  public int addEntryState(int nfaState) {
    return newDfaState(new Key(closure(nfaState)));
  }

  /** Returns the number of DFA states so far. */
  public int numStates() {
    return numDfaStates;
  }

  private boolean important(int state) {
    return nfa.numTransitions(state) > 0
        || nfa.isFinal(state)
        || nfa.action(state) != null
        || state == importantState;
  }

  /** Sets {@code buffer[index]}, growing the buffer if necessary. */
  private void add(int index, int state) {
    if (index == buffer.length) buffer = Arrays.copyOf(buffer, 2 * index);
    buffer[index] = state;
  }

  /**
   * Returns the important states of the epsilon closure of the NFA state.
   *
   * @param state an NFA state
   * @return the important NFA states that are reachable from {@code state} by epsilon transitions,
   *     including {@code state} itself, in ascending order.
   */
  private int[] closure(int state) {
    int[] result = closure[state];
    if (result != null) return result;

    // A state that is not important and has one epsilon edge has the closure of the state the edge
    // leads to. Follow such edges, and share the closure: large alternatives have long chains of
    // such states that end in the same state, and its closure can be large.
    int last = state;
    epoch++;
    while (closure[last] == null && !important(last) && nfa.numEpsilon(last) == 1) {
      mark[last] = epoch;
      int next = nfa.epsilon(last)[0];
      if (mark[next] == epoch) {
        // a cycle of such states
        closure[last] = NO_STATES;
        break;
      }
      last = next;
    }

    result = closure[last];
    if (result == null) {
      result = closure[last] = search(last);
    }
    for (int s = state; s != last; s = nfa.epsilon(s)[0]) {
      closure[s] = result;
    }
    return result;
  }

  /** Computes {@link #closure(int)} with a search over the epsilon edges. */
  private int[] search(int state) {
    if (nfa.numEpsilon(state) == 0) {
      return important(state) ? new int[] {state} : NO_STATES;
    }

    int size = 0;
    int top = 0;
    epoch++;
    mark[state] = epoch;
    stack[top++] = state;
    while (top > 0) {
      int s = stack[--top];
      if (important(s)) add(size++, s);
      int[] eps = nfa.epsilon(s);
      for (int i = nfa.numEpsilon(s) - 1; i >= 0; i--) {
        int t = eps[i];
        if (mark[t] != epoch) {
          mark[t] = epoch;
          if (top == stack.length) stack = Arrays.copyOf(stack, 2 * top);
          stack[top++] = t;
        }
      }
    }
    int[] result = Arrays.copyOf(buffer, size);
    Arrays.sort(result);
    return result;
  }

  /**
   * Returns the important states of the epsilon closure of a set of NFA states.
   *
   * @param targets NFA states
   * @return the union of {@link #closure(int)} of the states, in ascending order.
   */
  private int[] closure(int[] targets) {
    if (targets.length == 1) return closure(targets[0]);

    // The closures have to be there before the epoch starts, closure(int) uses the marks as well.
    for (int t : targets) closure(t);

    int size = 0;
    epoch++;
    for (int t : targets) {
      for (int s : closure[t]) {
        if (mark[s] != epoch) {
          mark[s] = epoch;
          add(size++, s);
        }
      }
    }
    int[] result = Arrays.copyOf(buffer, size);
    Arrays.sort(result);
    return result;
  }

  /** Adds a new DFA state for the set of important NFA states, and returns its number. */
  private int newDfaState(Key set) {
    int state = numDfaStates++;

    if (state == dfaList.length) dfaList = Arrays.copyOf(dfaList, 2 * state);
    dfaList[state] = set.states;
    dfaStates.put(set, state);

    target.state(state, set.states);
    return state;
  }

  /**
   * Finds all DFA states that are reachable from the entry states, and their transitions.
   *
   * @see #addEntryState(int)
   */
  public void run() {
    // the transitions of the NFA states of the current DFA state, as in NFA#transitions(int)
    long[] pairs = new long[256];

    for (int current = 0; current < numDfaStates; current++) {
      int numPairs = 0;
      for (int s : dfaList[current]) {
        int n = nfa.numTransitions(s);
        if (n == 0) continue;
        if (numPairs + n > pairs.length) {
          pairs = Arrays.copyOf(pairs, Math.max(2 * pairs.length, numPairs + n));
        }
        System.arraycopy(nfa.transitions(s), 0, pairs, numPairs, n);
        numPairs += n;
      }
      // by input, and by target for each input
      Arrays.sort(pairs, 0, numPairs);

      int[] lastTargets = null;
      int lastState = -1;

      int from = 0;
      while (from < numPairs) {
        int input = NFA.input(pairs[from]);

        // the distinct targets for this input
        int size = 0;
        int to = from;
        while (to < numPairs && NFA.input(pairs[to]) == input) {
          int t = NFA.target(pairs[to++]);
          if (size == 0 || buffer[size - 1] != t) add(size++, t);
        }
        from = to;

        int next;
        if (lastTargets != null && equals(lastTargets, buffer, size)) {
          // usually, many inputs of a state lead to the same targets
          next = lastState;
        } else {
          Key targets = new Key(Arrays.copyOf(buffer, size));
          Integer known = targetStates.get(targets);
          if (known != null) {
            next = known;
          } else {
            Key set = new Key(closure(targets.states));
            known = dfaStates.get(set);
            next = known != null ? known : newDfaState(set);
            targetStates.put(targets, next);
          }
          lastTargets = targets.states;
          lastState = next;
        }

        target.transition(current, input, next);
      }
    }
  }

  /** Returns true iff {@code a} has the same elements as {@code b[0..size-1]}. */
  private static boolean equals(int[] a, int[] b, int size) {
    if (a.length != size) return false;
    for (int i = 0; i < size; i++) {
      if (a[i] != b[i]) return false;
    }
    return true;
  }
}
