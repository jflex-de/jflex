/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.dfa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import jflex.core.Action;
import jflex.core.EOFActions;
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;

/**
 * Deterministic finite automata representation in JFlex. Contains minimization algorithm.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class DFA {

  /** The initial number of states */
  private static final int STATES = 500;

  /** The code for "no target state" in the transition table. */
  public static final int NO_TARGET = -1;

  // Build.DEBUG is too high-level for enabling debug output in minimisation
  static final boolean DFA_DEBUG = false;

  /**
   * {@code table[current_state][character]} is the next state for {@code current_state} with input
   * {@code character}, {@code NO_TARGET} if there is no transition for this input in {@code
   * current_state}
   */
  int[][] table;

  /** {@code isFinal[state] == true} if the state {@code state} is a final state. */
  boolean[] isFinal;

  /** The maximum number of input characters */
  private final int numInput;

  /** The number of lexical states (2*numLexStates <= entryState.length) */
  private final int numLexStates;

  /** The number of states in this DFA */
  private int numStates;

  /** {@code entryState[i]} is the start-state of lexical state i or lookahead DFA i. */
  int[] entryState;

  /**
   * {@code action[state]} is the action that is to be carried out in state {@code state}, {@code
   * null} if there is no action.
   */
  private Action[] action;

  /** all actions that are used in this DFA */
  private final Map<Action, Action> usedActions = new HashMap<>();

  /** True iff this DFA contains general lookahead */
  private boolean lookaheadUsed;

  /** Whether the DFA is minimized. */
  private boolean minimized;

  /** Constructor for a deterministic finite automata. */
  public DFA(int numEntryStates, int numInputs, int numLexStates) {
    this(numEntryStates, numInputs, numLexStates, 0);
  }

  DFA(int numEntryStates, int numInputs, int numLexStates, int numStates) {
    this.numInput = numInputs;
    this.numLexStates = numLexStates;
    this.numStates = numStates;

    int statesNeeded = Math.max(numEntryStates, STATES);

    table = new int[statesNeeded][numInput];
    isFinal = new boolean[statesNeeded];
    action = new Action[statesNeeded];
    entryState = new int[numEntryStates];

    for (int i = 0; i < statesNeeded; i++) {
      for (int j = 0; j < numInput; j++) {
        table[i][j] = NO_TARGET;
      }
    }
  }

  /**
   * Sets the state of the entry.
   *
   * @param eState entry state.
   * @param trueState whether it is the current state.
   */
  public void setEntryState(int eState, int trueState) {
    entryState[eState] = trueState;
    minimized = false;
  }

  private void ensureStateCapacity(int newNumStates) {
    int oldLength = isFinal.length;

    if (newNumStates < oldLength) return;

    int newLength = oldLength * 2;
    while (newLength <= newNumStates) newLength *= 2;

    boolean[] newFinal = new boolean[newLength];
    Action[] newAction = new Action[newLength];
    int[][] newTable = new int[newLength][numInput];

    System.arraycopy(isFinal, 0, newFinal, 0, numStates);
    System.arraycopy(action, 0, newAction, 0, numStates);
    System.arraycopy(table, 0, newTable, 0, oldLength);

    int i, j;

    for (i = oldLength; i < newLength; i++) {
      for (j = 0; j < numInput; j++) {
        newTable[i][j] = NO_TARGET;
      }
    }

    isFinal = newFinal;
    action = newAction;
    table = newTable;
    minimized = false;
  }

  /**
   * Sets the action.
   *
   * @param state a int.
   * @param stateAction a {@link Action} object.
   */
  public void setAction(int state, Action stateAction) {
    action[state] = stateAction;
    if (stateAction != null) {
      usedActions.put(stateAction, stateAction);
      lookaheadUsed |= stateAction.isGenLookAction();
      minimized = false;
    }
  }

  /**
   * setFinal.
   *
   * @param state a int.
   * @param isFinalState a boolean.
   */
  public void setFinal(int state, boolean isFinalState) {
    isFinal[state] = isFinalState;
    minimized = false;
  }

  /**
   * addTransition.
   *
   * @param start a int.
   * @param input a int.
   * @param dest a int.
   */
  public void addTransition(int start, int input, int dest) {
    int max = Math.max(start, dest) + 1;
    ensureStateCapacity(max);
    if (max > numStates) numStates = max;

    // Out.debug("Adding DFA transition (" + start + ", " + (int) input + ", " + dest + ")");

    table[start][input] = dest;
    minimized = false;
  }

  public boolean lookaheadUsed() {
    return lookaheadUsed;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < numStates; i++) {
      result.append("State ");
      if (isFinal[i]) {
        result.append("[FINAL");
        String l = action[i].lookString();
        if (!Objects.equals(l, "")) {
          result.append(", ");
          result.append(l);
        }
        result.append("] ");
      }
      result.append(i).append(":").append(Out.NL);

      for (int j = 0; j < numInput; j++) {
        if (table[i][j] >= 0)
          result.append("  with ").append(j).append(" in ").append(table[i][j]).append(Out.NL);
      }
    }

    return result.toString();
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(table);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DFA)) {
      return false;
    }
    return Arrays.equals(isFinal, ((DFA) obj).isFinal)
        && Arrays.equals(entryState, ((DFA) obj).entryState)
        && Arrays.equals(action, ((DFA) obj).action)
        && Objects.equals(usedActions, ((DFA) obj).usedActions)
        && tableEquals(table, ((DFA) obj).table);
  }

  private static boolean tableEquals(int[][] a, int[][] b) {
    for (int i = 0; i < a.length; i++) {
      if (!Arrays.equals(a[i], b[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Writes a dot-file representing this DFA.
   *
   * @param file output file.
   */
  public void writeDot(File file) {
    try {
      PrintWriter writer =
          new PrintWriter(
              new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
      writer.println(dotFormat());
      writer.close();
    } catch (IOException e) {
      Out.error(ErrorMessages.FILE_WRITE, file);
      throw new GeneratorException(e);
    }
  }

  /**
   * Returns a gnu representation of the DFA.
   *
   * @return a representation in the dot format.
   */
  private String dotFormat() {
    StringBuilder result = new StringBuilder();

    result.append("digraph DFA {").append(Out.NL);
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
        if (table[i][input] >= 0) {
          result.append(i).append(" -> ").append(table[i][input]);
          result.append(" [label=\"[").append(input).append("]\"]").append(Out.NL);
          // result.append(" [label=\"[").append(classes.toString(input)).append("]\"]\n");
        }
      }
    }

    result.append("}").append(Out.NL);

    return result.toString();
  }

  /** Checks that all actions can actually be matched in this DFA. */
  public void checkActions(LexScan scanner, LexParse parser) {
    EOFActions eofActions = parser.getEOFActions();

    for (Action a : scanner.actions())
      if (!Objects.equals(a, usedActions.get(a)) && !eofActions.isEOFAction(a))
        Out.warning(scanner.file(), ErrorMessages.NEVER_MATCH, a.priority - 1, -1);
  }

  /**
   * Implementation of Hopcroft's O(n log n) minimization algorithm, follows description by D.
   * Gries.
   *
   * <p>Time: {@code O(n log n)} Space: {@code O(c n), size < 4*(5*c*n + 13*n + 3*c) byte}
   */
  public void minimize() {
    if (minimized) {
      // Already minimized
      return;
    }

    if (numStates == 0) {
      Out.error(ErrorMessages.ZERO_STATES);
      throw new GeneratorException(new IllegalStateException("DFA has 0 states"));
    }

    if (Options.no_minimize) {
      Out.println("minimization skipped.");
      return;
    }

    // the algorithm needs the DFA to be total, so we add an error state 0,
    // and translate the rest of the states by +1
    final int n = numStates + 1;

    // block information:
    // [0..n-1] stores which block a state belongs to,
    // [n..2*n-1] stores how many elements each block has
    int[] block = new int[2 * n];

    // implements a doubly linked list of states (these are the actual blocks)
    int[] b_forward = new int[2 * n];
    int[] b_backward = new int[2 * n];

    // the last of the blocks currently in use (in [n..2*n-1])
    // (end of list marker, points to the last used block)
    int lastBlock = n; // at first we start with one empty block
    final int b0 = n; // the first block

    // the circular doubly linked list L of pairs (B_i, c)
    // (B_i, c) in L iff l_forward[(B_i-n)*numInput+c] > 0 // numeric value of block 0 = n!
    int[] l_forward = new int[n * numInput + 1];
    int[] l_backward = new int[n * numInput + 1];
    int anchorL = n * numInput; // list anchor

    // inverse of the transition table
    // if t = inv_delta[s][c] then { inv_delta_set[t], inv_delta_set[t+1], .. inv_delta_set[k] }
    // is the set of states, with inv_delta_set[k] = -1 and inv_delta_set[j] >= 0 for t <= j < k
    int[][] inv_delta = new int[n][numInput];
    int[] inv_delta_set = new int[2 * n * numInput];

    // twin stores two things:
    // twin[0]..twin[numSplit-1] is the list of blocks that have been split
    // twin[B_i] is the twin of block B_i
    int[] twin = new int[2 * n];
    int numSplit;

    // SD[B_i] is the the number of states s in B_i with delta(s,a) in B_j
    // if SD[B_i] == block[B_i], there is no need to split
    int[] SD = new int[2 * n]; // [only SD[n..2*n-1] is used]

    // for fixed (B_j,a), the D[0]..D[numD-1] are the inv_delta(B_j,a)
    int[] D = new int[n];
    int numD;

    // initialize inverse of transition table
    int lastDelta = 0;
    int[] inv_lists = new int[n]; // holds a set of lists of states
    int[] inv_list_last = new int[n]; // the last element
    for (int c = 0; c < numInput; c++) {
      // clear "head" and "last element" pointers
      for (int s = 0; s < n; s++) {
        inv_list_last[s] = -1;
        inv_delta[s][c] = -1;
      }

      // the error state has a transition for each character into itself
      inv_delta[0][c] = 0;
      inv_list_last[0] = 0;

      // accumulate states of inverse delta into lists (inv_delta serves as head of list)
      for (int s = 1; s < n; s++) {
        int t = table[s - 1][c] + 1;

        if (inv_list_last[t] == -1) { // if there are no elements in the list yet
          inv_delta[t][c] = s; // mark t as first and last element
          inv_list_last[t] = s;
        } else {
          inv_lists[inv_list_last[t]] = s; // link t into chain
          inv_list_last[t] = s; // and mark as last element
        }
      }

      // now move them to inv_delta_set in sequential order,
      // and update inv_delta accordingly
      for (int s = 0; s < n; s++) {
        int i = inv_delta[s][c];
        inv_delta[s][c] = lastDelta;
        int j = inv_list_last[s];
        boolean go_on = (i != -1);
        while (go_on) {
          go_on = (i != j);
          inv_delta_set[lastDelta++] = i;
          i = inv_lists[i];
        }
        inv_delta_set[lastDelta++] = -1;
      }
    } // of initialize inv_delta

    if (DFA_DEBUG) {
      printInvDelta(inv_delta, inv_delta_set);
    }
    // initialize blocks

    // make b0 = {0}  where 0 = the additional error state
    b_forward[b0] = 0;
    b_backward[b0] = 0;
    b_forward[0] = b0;
    b_backward[0] = b0;
    block[0] = b0;
    block[b0] = 1;

    for (int s = 1; s < n; s++) {
      // System.out.println("Checking state ["+(s-1)+"]");
      // search the blocks if it fits in somewhere
      // (fit in = same pushback behavior, same finalness, same lookahead behavior, same action)
      int b = b0 + 1; // no state can be equivalent to the error state
      boolean found = false;
      while (!found && b <= lastBlock) {
        // get some state out of the current block
        int t = b_forward[b];
        // System.out.println("  picking state ["+(t-1)+"]");

        // check, if s could be equivalent with t
        if (isFinal[s - 1]) {
          found = isFinal[t - 1] && action[s - 1].isEquiv(action[t - 1]);
        } else {
          found = !isFinal[t - 1];
        }

        if (found) { // found -> add state s to block b
          // System.out.println("Found! Adding to block "+(b-b0));
          // update block information
          block[s] = b;
          block[b]++;

          // chain in the new element
          int last = b_backward[b];
          b_forward[last] = s;
          b_forward[s] = b;
          b_backward[b] = s;
          b_backward[s] = last;
        }

        b++;
      }

      if (!found) { // fits in nowhere -> create new block
        // System.out.println("not found, lastBlock = "+lastBlock);

        // update block information
        block[s] = b;
        block[b]++;

        // chain in the new element
        b_forward[b] = s;
        b_forward[s] = b;
        b_backward[b] = s;
        b_backward[s] = b;

        lastBlock++;
      }
    } // of initialize blocks

    if (DFA_DEBUG) {
      printBlocks(block, b_forward, b_backward, lastBlock);
    }

    // initialize worklist L
    // first, find the largest block B_max, then, all other (B_i,c) go into the list
    int B_max = b0;
    int B_i;
    for (B_i = b0 + 1; B_i <= lastBlock; B_i++) if (block[B_max] < block[B_i]) B_max = B_i;

    // L = empty
    l_forward[anchorL] = anchorL;
    l_backward[anchorL] = anchorL;

    // set up the first list element
    if (B_max == b0) B_i = b0 + 1;
    else B_i = b0; // there must be at least two blocks

    int index = (B_i - b0) * numInput; // (B_i, 0)
    while (index < (B_i + 1 - b0) * numInput) {
      int last = l_backward[anchorL];
      l_forward[last] = index;
      l_forward[index] = anchorL;
      l_backward[index] = last;
      l_backward[anchorL] = index;
      index++;
    }

    // now do the rest of L
    while (B_i <= lastBlock) {
      if (B_i != B_max) {
        index = (B_i - b0) * numInput;
        while (index < (B_i + 1 - b0) * numInput) {
          int last = l_backward[anchorL];
          l_forward[last] = index;
          l_forward[index] = anchorL;
          l_backward[index] = last;
          l_backward[anchorL] = index;
          index++;
        }
      }
      B_i++;
    }
    // end of setup L

    // start of "real" algorithm
    // int step = 0;
    // System.out.println("max_steps = "+(n*numInput));
    // while L not empty
    while (l_forward[anchorL] != anchorL) {
      if (DFA_DEBUG) {
        // System.out.println("step : "+(step++));
        printL(l_forward, l_backward, anchorL);
      }

      // pick and delete (B_j, a) in L:

      // pick
      int B_j_a = l_forward[anchorL];
      // delete
      l_forward[anchorL] = l_forward[B_j_a];
      l_backward[l_forward[anchorL]] = anchorL;
      l_forward[B_j_a] = 0;
      // take B_j_a = (B_j-b0)*numInput+c apart into (B_j, a)
      int B_j = b0 + B_j_a / numInput;
      int a = B_j_a % numInput;

      if (DFA_DEBUG) {
        printL(l_forward, l_backward, anchorL);

        System.out.println("picked (" + B_j + "," + a + ")");
        printL(l_forward, l_backward, anchorL);
      }

      // determine splittings of all blocks wrt (B_j, a)
      // i.e. D = inv_delta(B_j,a)
      numD = 0;
      int s = b_forward[B_j];
      while (s != B_j) {
        // System.out.println("splitting wrt. state "+s);
        int t = inv_delta[s][a];
        // System.out.println("inv_delta chunk "+t);
        while (inv_delta_set[t] != -1) {
          // System.out.println("D+= state "+inv_delta_set[t]);
          D[numD++] = inv_delta_set[t++];
        }
        s = b_forward[s];
      }

      // clear the twin list
      numSplit = 0;

      if (DFA_DEBUG) {
        System.out.println("splitting blocks according to D");
      }

      // clear SD and twins (only those B_i that occur in D)
      for (int indexD = 0; indexD < numD; indexD++) { // for each s in D
        s = D[indexD];
        B_i = block[s];
        SD[B_i] = -1;
        twin[B_i] = 0;
      }

      // count how many states of each B_i occurring in D go with a into B_j
      // Actually we only check, if *all* t in B_i go with a into B_j.
      // In this case SD[B_i] == block[B_i] will hold.
      for (int indexD = 0; indexD < numD; indexD++) { // for each s in D
        s = D[indexD];
        B_i = block[s];

        // only count, if we haven't checked this block already
        if (SD[B_i] < 0) {
          SD[B_i] = 0;
          int t = b_forward[B_i];
          while (t != B_i
              && (t != 0 || block[0] == B_j)
              && (t == 0 || block[table[t - 1][a] + 1] == B_j)) {
            SD[B_i]++;
            t = b_forward[t];
          }
        }
      }

      // split each block according to D
      for (int indexD = 0; indexD < numD; indexD++) { // for each s in D
        s = D[indexD];
        B_i = block[s];

        // System.out.println("checking if block "+(B_i-b0)+" must be split because of state "+s);

        if (SD[B_i] != block[B_i]) {
          // System.out.println("state "+(s-1)+" must be moved");
          int B_k = twin[B_i];
          if (B_k == 0) {
            // no twin for B_i yet -> generate new block B_k, make it B_i's twin
            B_k = ++lastBlock;
            // System.out.println("creating block "+(B_k-n));
            // printBlocks(block,b_forward,b_backward,lastBlock-1);
            b_forward[B_k] = B_k;
            b_backward[B_k] = B_k;

            twin[B_i] = B_k;

            // mark B_i as split
            twin[numSplit++] = B_i;
          }
          // move s from B_i to B_k

          // remove s from B_i
          b_forward[b_backward[s]] = b_forward[s];
          b_backward[b_forward[s]] = b_backward[s];

          // add s to B_k
          int last = b_backward[B_k];
          b_forward[last] = s;
          b_forward[s] = B_k;
          b_backward[s] = last;
          b_backward[B_k] = s;

          block[s] = B_k;
          block[B_k]++;
          block[B_i]--;

          SD[B_i]--; // there is now one state less in B_i that goes with a into B_j
          // printBlocks(block, b_forward, b_backward, lastBlock);
          // System.out.println("finished move");
        }
      } // of block splitting

      if (DFA_DEBUG) {
        printBlocks(block, b_forward, b_backward, lastBlock);

        System.out.println("updating L");
      }
      // update L
      for (int indexTwin = 0; indexTwin < numSplit; indexTwin++) {
        B_i = twin[indexTwin];
        int B_k = twin[B_i];
        for (int c = 0; c < numInput; c++) {
          int B_i_c = (B_i - b0) * numInput + c;
          int B_k_c = (B_k - b0) * numInput + c;
          if (l_forward[B_i_c] > 0) {
            // (B_i,c) already in L --> put (B_k,c) in L
            int last = l_backward[anchorL];
            l_backward[anchorL] = B_k_c;
            l_forward[last] = B_k_c;
            l_backward[B_k_c] = last;
            l_forward[B_k_c] = anchorL;
          } else {
            // put the smaller block in L
            if (block[B_i] <= block[B_k]) {
              int last = l_backward[anchorL];
              l_backward[anchorL] = B_i_c;
              l_forward[last] = B_i_c;
              l_backward[B_i_c] = last;
              l_forward[B_i_c] = anchorL;
            } else {
              int last = l_backward[anchorL];
              l_backward[anchorL] = B_k_c;
              l_forward[last] = B_k_c;
              l_backward[B_k_c] = last;
              l_forward[B_k_c] = anchorL;
            }
          }
        }
      }
    }

    if (DFA_DEBUG) {
      System.out.println("Result");
      printBlocks(block, b_forward, b_backward, lastBlock);
    }

    // transform the transition table

    // trans[i] is the state j that will replace state i, i.e.
    // states i and j are equivalent
    int[] trans = new int[numStates];

    // kill[i] is true iff state i is redundant and can be removed
    boolean[] kill = new boolean[numStates];

    // move[i] is the amount line i has to be moved in the transition table
    // (because states j < i have been removed)
    int[] move = new int[numStates];

    // fill arrays trans[] and kill[] (in O(n))
    for (int b = n + 1; b <= lastBlock; b++) { // b0 contains the error state
      // get the state with smallest value in current block
      int s = b_forward[b];
      int min_s = s; // there are no empty blocks!
      for (; s != b; s = b_forward[s]) if (min_s > s) min_s = s;
      // now fill trans[] and kill[] for this block
      // (and translate states back to partial DFA)
      min_s--;
      for (s = b_forward[b] - 1; s != b - 1; s = b_forward[s + 1] - 1) {
        trans[s] = min_s;
        kill[s] = s != min_s;
      }
    }

    // fill array move[] (in O(n))
    int amount = 0;
    for (int i = 0; i < numStates; i++) {
      if (kill[i]) amount++;
      else move[i] = amount;
    }

    int i, j;
    // j is the index in the new transition table
    // the transition table is transformed in place (in O(c n))
    for (i = 0, j = 0; i < numStates; i++) {

      // we only copy lines that have not been removed
      if (!kill[i]) {

        // translate the target states
        for (int c = 0; c < numInput; c++) {
          if (table[i][c] >= 0) {
            table[j][c] = trans[table[i][c]];
            table[j][c] -= move[table[j][c]];
          } else {
            table[j][c] = table[i][c];
          }
        }

        isFinal[j] = isFinal[i];
        action[j] = action[i];

        j++;
      }
    }

    numStates = j;

    // translate lexical states
    for (i = 0; i < entryState.length; i++) {
      entryState[i] = trans[entryState[i]];
      entryState[i] -= move[entryState[i]];
    }
    minimized = true;
  }

  public boolean isMinimized() {
    return minimized;
  }

  /** Returns a representation of this DFA. */
  public String toString(int[] a) {
    StringBuilder r = new StringBuilder("{");
    for (int i = 0; i < a.length - 1; i++) {
      r.append(a[i]).append(",");
    }
    if (a.length > 0) {
      r.append(a[a.length - 1]);
    }
    r.append("}");
    return r.toString();
  }

  private void printBlocks(int[] b, int[] b_f, int[] b_b, int last) {
    Out.dump("block     : " + toString(b));
    Out.dump("b_forward : " + toString(b_f));
    Out.dump("b_backward: " + toString(b_b));
    Out.dump("lastBlock : " + last);
    final int n = numStates + 1;
    for (int i = n; i <= last; i++) {
      Out.dump("Block " + (i - n) + " (size " + b[i] + "):");
      String line = "{";
      int s = b_f[i];
      while (s != i) {
        line = line + (s - 1);
        int t = s;
        s = b_f[s];
        if (s != i) {
          line = line + ",";
          if (b[s] != i)
            Out.dump("consistency error for state " + (s - 1) + " (block " + b[s] + ")");
        }
        if (b_b[s] != t)
          Out.dump(
              "consistency error for b_back in state "
                  + (s - 1)
                  + " (back = "
                  + b_b[s]
                  + ", should be = "
                  + t
                  + ")");
      }
      Out.dump(line + "}");
    }
  }

  private void printL(int[] l_f, int[] l_b, int anchor) {
    String l = "L = {";
    int bc = l_f[anchor];
    while (bc != anchor) {
      int b = bc / numInput;
      int c = bc % numInput;
      l += "(" + b + "," + c + ")";
      int old_bc = bc;
      bc = l_f[bc];
      if (bc != anchor) l += ",";
      if (l_b[bc] != old_bc) Out.dump("consistency error for (" + b + "," + c + ")");
    }
    Out.dump(l + "}");
  }

  /**
   * Prints the inverse of transition table.
   *
   * @param inv_delta an array of int.
   * @param inv_delta_set an array of int.
   */
  private void printInvDelta(int[][] inv_delta, int[] inv_delta_set) {
    Out.dump("Inverse of transition table: ");
    for (int s = 0; s < numStates + 1; s++) {
      Out.dump("State [" + (s - 1) + "]");
      for (int c = 0; c < numInput; c++) {
        String line = "With <" + c + "> in {";
        int t = inv_delta[s][c];
        while (inv_delta_set[t] != -1) {
          line += inv_delta_set[t++] - 1;
          if (inv_delta_set[t] != -1) line += ",";
        }
        if (inv_delta_set[inv_delta[s][c]] != -1) Out.dump(line + "}");
      }
    }
  }

  public int numInput() {
    return numInput;
  }

  public int numStates() {
    return numStates;
  }

  public int numLexStates() {
    return numLexStates;
  }

  public int entryState(int i) {
    return entryState[i];
  }

  public boolean isFinal(int i) {
    return isFinal[i];
  }

  public int table(int i, int j) {
    return table[i][j];
  }

  public Action action(int i) {
    return action[i];
  }
}
