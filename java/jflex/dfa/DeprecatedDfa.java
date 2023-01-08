/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import java.util.Arrays;
import jflex.exceptions.GeneratorException;
import jflex.logging.Out;
import jflex.option.Options;

/** Deprecated DFA class, only used for testing. */
@Deprecated
public class DeprecatedDfa extends DFA {

  DeprecatedDfa(int numEntryStates, int numInputs, int numLexStates, int numStates) {
    super(numEntryStates, numInputs, numLexStates, numStates);
  }

  /**
   * Much simpler, but slower and less memory efficient minimization algorithm than {@link
   * DFA#minimize()}.
   *
   * <p>This implementation is only useful for testing.
   *
   * @return the equivalence relation on states.
   * @deprecated Use {@link DFA#minimize()} instead.
   */
  @Deprecated
  public boolean[][] old_minimize() {

    int i;
    int j;
    int c;

    if (numStates() == 0) {
      throw new GeneratorException(new IllegalStateException("DFA has no states"));
    }

    if (Options.no_minimize) {
      throw new UnsupportedOperationException(
          "Options.no_minimize is set. Minimization is not allowed in this case");
    }

    // equiv[i][j] == true <=> state i and state j are equivalent
    boolean[][] equiv = new boolean[numStates()][];

    // list[i][j] contains all pairs of states that have to be marked "not equivalent"
    // if states i and j are recognized to be not equivalent
    StatePairList[][] list = new StatePairList[numStates()][];

    // construct a triangular matrix equiv[i][j] with j < i
    // and mark pairs (final state, not final state) as not equivalent
    for (i = 1; i < numStates(); i++) {
      list[i] = new StatePairList[i];
      equiv[i] = new boolean[i];
      for (j = 0; j < i; j++) {
        // i and j are equivalent, iff :
        // i and j are both final and their actions are equivalent and have same pushback behaviour
        // or
        // i and j are both not final

        if (isFinal[i] && isFinal[j]) {
          equiv[i][j] = action(i).isEquiv(action(j));
        } else {
          equiv[i][j] = !isFinal[j] && !isFinal[i];
        }
      }
    }

    for (i = 1; i < numStates(); i++) {

      for (j = 0; j < i; j++) {

        if (equiv[i][j]) {

          for (c = 0; c < numInput(); c++) {

            if (equiv[i][j]) {

              int p = table[i][c];
              int q = table[j][c];
              if (p < q) {
                int t = p;
                p = q;
                q = t;
              }
              if (p >= 0) {
                if (p != q && (q == -1 || !equiv[p][q])) {
                  equiv[i][j] = false;
                  if (list[i][j] != null) list[i][j].markAll(list, equiv);
                }
                if (DFA.DFA_DEBUG) {
                  printTable(equiv);
                }
              } // if (p >= 0) ..
            } // if (equiv[i][j]
          } // for (char c = 0; c < numInput ..

          // if i and j are still marked equivalent..

          if (equiv[i][j]) {

            for (c = 0; c < numInput(); c++) {

              int p = table[i][c];
              int q = table[j][c];
              if (p < q) {
                int t = p;
                p = q;
                q = t;
              }

              if (p != q && p >= 0 && q >= 0) {
                if (list[p][q] == null) {
                  list[p][q] = new StatePairList();
                }
                list[p][q].addPair(i, j);
              }
            }
          }
        } // of first if (equiv[i][j])
      } // of for j
    } // of for i
    // }

    if (DFA.DFA_DEBUG) {
      printTable(equiv);
    }
    return equiv;
  }

  /**
   * Prints the equivalence table.
   *
   * @param equiv Equivalence table from {@link #old_minimize()}
   */
  void printTable(boolean[][] equiv) {
    Out.dump("Equivalence table is : ");
    StringBuilder line = new StringBuilder();
    for (int i = 1; i < numStates(); i++) {
      line.setLength(0);
      line.append(i).append(" :");
      for (int j = 0; j < i; j++) {
        if (equiv[i][j]) {
          line.append(" E");
        } else {
          line.append(" x");
        }
      }
      Out.dump(line.toString());
    }
  }

  public static DeprecatedDfa copyOf(DFA dfa) {
    DeprecatedDfa copy =
        new DeprecatedDfa(
            dfa.entryState.length, dfa.numInput(), dfa.numLexStates(), dfa.numStates());
    copy.table = new int[dfa.table.length][dfa.numInput()];
    for (int i = 0; i < dfa.table.length; i++) {
      System.arraycopy(dfa.table[i], 0, copy.table[i], 0, copy.numInput());
    }
    copy.isFinal = Arrays.copyOf(dfa.isFinal, dfa.isFinal.length);
    copy.entryState = Arrays.copyOf(dfa.entryState, dfa.entryState.length);
    // Sets action and usedActions
    for (int i = 0; i < dfa.numStates(); i++) {
      copy.setAction(i, dfa.action(i));
    }

    return copy;
  }
}
