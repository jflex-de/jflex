/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import jflex.core.Action;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Tuple;
import net.jqwik.api.providers.ArbitraryProvider;
import net.jqwik.api.providers.TypeUsage;

/**
 * Generator for random {@link DFA} instances.
 *
 * <p>The DFAs are small, so that tests can afford to run brute force algorithms as checks. They are
 * in general partial, not minimal, and have states that cannot be reached from an entry state.
 * Every final state has an action, and only final states have actions.
 *
 * @see DFA
 */
public class DfaGen implements ArbitraryProvider {

  /** Max number of states. */
  static final int MAX_STATES = 6;

  /** Max number of inputs (character classes). */
  static final int MAX_INPUTS = 3;

  /** The actions of final states. */
  static final List<Action> ACTIONS = actions();

  private static List<Action> actions() {
    Action fixedLook = new Action("return 1;", 4);
    fixedLook.setLookAction(Action.Kind.FIXED_LOOK, 1);
    Action fixedLook2 = new Action("return 1;", 5);
    fixedLook2.setLookAction(Action.Kind.FIXED_LOOK, 2);
    Action fixedBase = new Action("return 1;", 6);
    fixedBase.setLookAction(Action.Kind.FIXED_BASE, 1);
    Action generalLook = new Action("return 1;", 7);
    generalLook.setLookAction(Action.Kind.GENERAL_LOOK, 0);
    generalLook.setEntryState(2);
    return Collections.unmodifiableList(
        Arrays.asList(
            new Action("return 1;", 1),
            // equivalent to the previous one
            new Action("return 1;", 2),
            new Action("return a[i]; // -> 1: \\", 3),
            new Action("f();\n  return 2;", 3),
            new Action("f();\\n  return 2;", 3),
            fixedLook,
            fixedLook2,
            fixedBase,
            generalLook,
            new Action(Action.Kind.FORWARD_ACTION),
            new Action(Action.Kind.BACKWARD_ACTION)));
  }

  @Override
  public boolean canProvideFor(TypeUsage targetType) {
    return targetType.isOfType(DFA.class);
  }

  @Override
  public Set<Arbitrary<?>> provideFor(TypeUsage targetType, SubtypeProvider subtypeProvider) {
    return Collections.singleton(dfas());
  }

  /** DFAs with up to {@link #MAX_STATES} states and {@link #MAX_INPUTS} inputs. */
  static Arbitrary<DFA> dfas() {
    return Combinators.combine(
            Arbitraries.integers().between(1, MAX_STATES),
            Arbitraries.integers().between(1, MAX_INPUTS),
            // lexical states
            Arbitraries.integers().between(1, 2),
            // general lookahead expressions, which have entry states of their own
            Arbitraries.integers().between(0, 1))
        .flatAs(
            (numStates, numInputs, numLexStates, numLook) -> {
              int numEntryStates = 2 * (numLexStates + numLook);
              Arbitrary<Integer> state = Arbitraries.integers().between(0, numStates - 1);
              Arbitrary<List<Integer>> targets =
                  Arbitraries.frequencyOf(
                          Tuple.of(1, Arbitraries.just(DFA.NO_TARGET)), Tuple.of(3, state))
                      .list()
                      .ofSize(numStates * numInputs);
              // index into ACTIONS, -1 for a state that is not final
              Arbitrary<List<Integer>> actions =
                  Arbitraries.frequencyOf(
                          Tuple.of(1, Arbitraries.just(-1)),
                          Tuple.of(1, Arbitraries.integers().between(0, ACTIONS.size() - 1)))
                      .list()
                      .ofSize(numStates);
              Arbitrary<List<Integer>> entryStates = state.list().ofSize(numEntryStates);

              return Combinators.combine(targets, actions, entryStates)
                  .as(
                      (t, a, e) -> {
                        DFA dfa = new DFA(numEntryStates, numInputs, numLexStates, numStates);
                        for (int s = 0; s < numStates; s++) {
                          for (int input = 0; input < numInputs; input++) {
                            dfa.table[s][input] = t.get(s * numInputs + input);
                          }
                          if (a.get(s) >= 0) {
                            dfa.setFinal(s, true);
                            dfa.setAction(s, ACTIONS.get(a.get(s)));
                          }
                        }
                        for (int i = 0; i < numEntryStates; i++) {
                          dfa.setEntryState(i, e.get(i));
                        }
                        return dfa;
                      });
            });
  }
}
