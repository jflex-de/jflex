/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import jflex.core.Action;
import jflex.core.NFA;
import jflex.core.SubsetConstruction;
import jflex.logging.Out;
import jflex.option.Options;

public class DfaFactory {
  /**
   * Returns a DFA that accepts the same language as the NFA.
   *
   * <p>This DFA is usually not minimal.
   *
   * @param options the options of this generator run
   * @param out the output of this generator run
   * @return a DFA that accepts the same language as the NFA.
   * @see SubsetConstruction
   */
  public static DFA createFromNfa(NFA nfa, Options options, Out out) {
    out.println("Converting NFA to DFA : ");

    int numEntryStates = nfa.numEntryStates();
    DFA dfa = new DFA(numEntryStates, nfa.numInput(), nfa.numLexStates());

    SubsetConstruction construction =
        new SubsetConstruction(
            nfa,
            new SubsetConstruction.Target() {
              @Override
              public void state(int state, int[] nfaStates) {
                if (state >= numEntryStates && options.progress()) out.print(".");

                boolean isFinal = false;
                Action action = null;
                for (int s : nfaStates) {
                  isFinal |= nfa.isFinal(s);
                  Action a = nfa.action(s);
                  if (a != null) action = action == null ? a : action.getHigherPriority(a);
                }
                dfa.setFinal(state, isFinal);
                dfa.setAction(state, action);
              }

              @Override
              public void transition(int from, int input, int to) {
                dfa.addTransition(from, input, to);
              }
            });

    // create the initial states of the DFA
    for (int i = 0; i < numEntryStates; i++) {
      dfa.setEntryState(i, construction.addEntryState(i));
    }
    construction.run();

    if (options.verbose()) out.println("");
    return dfa;
  }

  private DfaFactory() {}
}
