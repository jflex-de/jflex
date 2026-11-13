/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;

/**
 * Property-based tests for the canonical form of a {@link DFA} and for DFA isomorphism.
 *
 * @see DFA#canonical()
 * @see DFA#isIsomorphicTo(DFA)
 * @see DFA#canonicalLines()
 */
public class DfaQuickcheck {

  /** A copy has the same states with the same numbers, and is independent of the original. */
  @Property
  public void copyIsIdenticalAndIndependent(@ForAll DFA dfa, @ForAll Random random) {
    String before = dfa.toString();
    List<String> linesBefore = dfa.canonicalLines();

    DFA copy = dfa.copy();
    assertEquals(dfa, copy);
    assertEquals(dfa.isMinimized(), copy.isMinimized());
    assertEquals(dfa.lookaheadUsed(), copy.lookaheadUsed());

    change(copy, random);
    change(copy, random);
    copy.minimize();
    assertTrue(copy.isMinimized());
    assertEquals(copy, copy.copy());
    assertTrue(copy.copy().isMinimized());

    // Generator only produced DFAs without minimized flag set.
    assertFalse(dfa.isMinimized());
    assertEquals(before, dfa.toString());
    assertEquals(linesBefore, dfa.canonicalLines());
  }

  /** The canonical form does not depend on state numbering. */
  @Property
  public void renumberedIsIsomorphic(@ForAll DFA dfa, @ForAll Random random) {
    DFA renumbered = renumber(dfa, permutation(dfa.numStates(), random));

    assertTrue(dfa.isIsomorphicTo(renumbered));
    assertTrue(renumbered.isIsomorphicTo(dfa));
    assertEquals(dfa.canonicalLines(), renumbered.canonicalLines());
    assertEquals(dfa.canonical(), renumbered.canonical());
  }

  /** The canonical form is isomorphic to the DFA, and canonical itself. */
  @Property
  public void canonicalIsIdempotent(@ForAll DFA dfa) {
    DFA canonical = dfa.canonical();

    assertTrue(dfa.isIsomorphicTo(canonical));
    assertTrue(bruteForceIsomorphic(dfa, canonical));
    assertEquals(dfa.canonicalLines(), canonical.canonicalLines());
    assertEquals(canonical, canonical.canonical());
  }

  /**
   * {@link DFA#isIsomorphicTo(DFA)} and equality of {@link DFA#canonicalLines()} agree with a
   * search over all bijections between the reachable states. Compare DFAs with random state number
   * permutations and up to three random content changes.
   */
  @Property
  @StatisticsReport(StatisticsReport.StatisticsReportMode.OFF)
  public void isomorphicAgreesWithBruteForce(@ForAll DFA dfa, @ForAll Random random) {
    DFA other = dfa.copy();
    for (int changes = random.nextInt(3); changes > 0; changes--) {
      change(other, random);
    }
    other = renumber(other, permutation(other.numStates(), random));

    boolean isomorphic = bruteForceIsomorphic(dfa, other);
    // both outcomes have to occur for this property to mean something
    Statistics.collect(isomorphic);
    Statistics.coverage(
        coverage -> {
          coverage.check(true).percentage(p -> p > 10);
          coverage.check(false).percentage(p -> p > 10);
        });

    assertEquals(isomorphic, dfa.isIsomorphicTo(other));
    assertEquals(isomorphic, other.isIsomorphicTo(dfa));
    assertEquals(isomorphic, dfa.canonicalLines().equals(other.canonicalLines()));
  }

  /** The minimized DFA is unique up to state numbering. */
  @Property
  public void minimizedDoesNotDependOnStateNumbers(@ForAll DFA dfa, @ForAll Random random) {
    DFA minimized = dfa.copy();
    DFA renumbered = renumber(dfa, permutation(dfa.numStates(), random));
    minimized.minimize();
    renumbered.minimize();

    assertTrue(minimized.isIsomorphicTo(renumbered));
    assertEquals(minimized.canonicalLines(), renumbered.canonicalLines());
    assertTrue(minimized.canonical().isMinimized());
    assertTrue(minimized.canonical().numStates() <= dfa.canonical().numStates());
  }

  /**
   * {@link DFA#hasDeadStates()} agrees with the fixpoint implementation, and does not depend on how
   * the states are numbered.
   */
  @Property
  @StatisticsReport(StatisticsReport.StatisticsReportMode.OFF)
  public void hasDeadStatesAgreesWithFixpoint(@ForAll DFA dfa, @ForAll Random random) {
    boolean dead = hasDeadStates(dfa);
    // both outcomes have to occur for this property to mean something
    Statistics.collect(dead);
    Statistics.coverage(
        coverage -> {
          coverage.check(true).percentage(p -> p > 10);
          coverage.check(false).percentage(p -> p > 10);
        });

    assertEquals(dead, dfa.hasDeadStates());
    assertEquals(dead, dfa.canonical().hasDeadStates());
    assertEquals(dead, renumber(dfa, permutation(dfa.numStates(), random)).hasDeadStates());
  }

  /**
   * Minimized DFAs without dead states are isomorphic iff they accept the same language with the
   * same actions. Use DFAs and a DFA permutation with additionally up to two random changes. With
   * dead states, isomorphic DFAs still accept the same language, but DFAs that accept the same
   * language may not be isomorphic.
   */
  @Property
  @StatisticsReport(StatisticsReport.StatisticsReportMode.OFF)
  public void minimizedIsomorphicIffSameLanguage(@ForAll DFA dfa, @ForAll Random random) {
    DFA a = dfa.copy();
    DFA b = dfa.copy();
    for (int changes = random.nextInt(3); changes > 0; changes--) {
      change(b, random);
    }
    b = renumber(b, permutation(b.numStates(), random));
    a.minimize();
    b.minimize();

    boolean sameLanguage = sameLanguage(a, b);
    boolean isomorphic = a.isIsomorphicTo(b);
    boolean noDeadStates = !a.hasDeadStates() && !b.hasDeadStates();
    Statistics.collect(noDeadStates, sameLanguage);
    Statistics.coverage(
        coverage -> {
          coverage.check(true, true).percentage(p -> p > 5);
          coverage.check(true, false).percentage(p -> p > 5);
        });

    if (isomorphic) {
      assertTrue(sameLanguage);
    }
    if (noDeadStates) {
      assertEquals(sameLanguage, isomorphic);
    }
  }

  /**
   * Removing the transitions to dead states does not change the language, and the minimized result
   * is isomorphic to the minimized DFA iff that had no dead states.
   */
  @Property
  public void minimizedWithDeadStatesIsNotIsomorphic(@ForAll DFA dfa) {
    DFA minimized = dfa.copy();
    DFA pruned = withoutDeadTransitions(dfa);
    minimized.minimize();
    pruned.minimize();

    assertEquals(hasDeadStates(dfa), minimized.hasDeadStates());
    assertFalse(pruned.hasDeadStates());
    assertTrue(sameLanguage(minimized, pruned));
    assertEquals(!minimized.hasDeadStates(), minimized.isIsomorphicTo(pruned));
  }

  /** Identity permutation. */
  private static int[] identity(int n) {
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
      result[i] = i;
    }
    return result;
  }

  /** A random permutation of {@code 0..n-1}. */
  private static int[] permutation(int n, Random random) {
    int[] result = identity(n);
    for (int i = n - 1; i > 0; i--) {
      int j = random.nextInt(i + 1);
      int tmp = result[i];
      result[i] = result[j];
      result[j] = tmp;
    }
    return result;
  }

  /** A copy of the DFA in which state {@code s} has the number {@code number[s]}. */
  private static DFA renumber(DFA dfa, int[] number) {
    DFA result =
        new DFA(dfa.entryState.length, dfa.numInput(), dfa.numLexStates(), dfa.numStates());
    for (int state = 0; state < dfa.numStates(); state++) {
      for (int input = 0; input < dfa.numInput(); input++) {
        int target = dfa.table(state, input);
        result.table[number[state]][input] = target >= 0 ? number[target] : DFA.NO_TARGET;
      }
      result.setFinal(number[state], dfa.isFinal(state));
      result.setAction(number[state], dfa.action(state));
    }
    for (int i = 0; i < dfa.entryState.length; i++) {
      result.setEntryState(i, number[dfa.entryState(i)]);
    }
    return result;
  }

  /**
   * Changes one transition, finality or action of one state, or one entry state. The new value can
   * be the old one.
   */
  private static void change(DFA dfa, Random random) {
    int state = random.nextInt(dfa.numStates());
    switch (random.nextInt(4)) {
      case 0:
        dfa.table[state][random.nextInt(dfa.numInput())] = random.nextInt(dfa.numStates() + 1) - 1;
        break;
      case 1:
        if (dfa.isFinal(state)) {
          dfa.setFinal(state, false);
          dfa.setAction(state, null);
        } else {
          dfa.setFinal(state, true);
          dfa.setAction(state, DfaGen.ACTIONS.get(random.nextInt(DfaGen.ACTIONS.size())));
        }
        break;
      case 2:
        if (dfa.isFinal(state)) {
          dfa.setAction(state, DfaGen.ACTIONS.get(random.nextInt(DfaGen.ACTIONS.size())));
        }
        break;
      default:
        dfa.setEntryState(random.nextInt(dfa.entryState.length), state);
    }
  }

  /**
   * The states that can be reached from an entry state. As a fixpoint computation instead of the
   * backwards search in DFA, so there is implementation cross checking.
   */
  private static List<Integer> reachable(DFA dfa) {
    boolean[] reached = new boolean[dfa.numStates()];
    for (int entry : dfa.entryState) {
      reached[entry] = true;
    }
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int state = 0; state < dfa.numStates(); state++) {
        for (int input = 0; input < dfa.numInput(); input++) {
          int target = dfa.table(state, input);
          if (reached[state] && target >= 0 && !reached[target]) {
            reached[target] = true;
            changed = true;
          }
        }
      }
    }
    List<Integer> result = new ArrayList<>();
    for (int state = 0; state < dfa.numStates(); state++) {
      if (reached[state]) result.add(state);
    }
    return result;
  }

  /**
   * {@code live[s]} iff a final state can be reached from state s. As a fixpoint computation
   * instead of the backwards search in DFA, so there is implementation cross checking.
   */
  private static boolean[] live(DFA dfa) {
    boolean[] live = new boolean[dfa.numStates()];
    boolean changed = true;
    while (changed) {
      changed = false;
      for (int state = 0; state < dfa.numStates(); state++) {
        boolean isLive = dfa.isFinal(state);
        for (int input = 0; input < dfa.numInput(); input++) {
          int target = dfa.table(state, input);
          isLive |= target >= 0 && live[target];
        }
        if (isLive && !live[state]) {
          live[state] = true;
          changed = true;
        }
      }
    }
    return live;
  }

  /**
   * Same as {@link DFA#hasDeadStates()}, but computed from the fixpoints {@link #reachable(DFA)}
   * and {@link #live(DFA)} to cross check the implementation.
   */
  private static boolean hasDeadStates(DFA dfa) {
    boolean[] live = live(dfa);
    for (int state : reachable(dfa)) {
      for (int input = 0; input < dfa.numInput(); input++) {
        int target = dfa.table(state, input);
        if (target >= 0 && !live[target]) {
          return true;
        }
      }
    }
    return false;
  }

  /** A copy of the DFA without the transitions to states that cannot reach a final state. */
  private static DFA withoutDeadTransitions(DFA dfa) {
    DFA result = dfa.copy();
    boolean[] live = live(dfa);
    for (int state = 0; state < dfa.numStates(); state++) {
      for (int input = 0; input < dfa.numInput(); input++) {
        int target = dfa.table(state, input);
        if (target >= 0 && !live[target]) {
          result.table[state][input] = DFA.NO_TARGET;
        }
      }
    }
    return result;
  }

  /**
   * Whether both DFAs accept the same language with the same actions from each entry state.
   * Reachability search over the cross product DFA. Aborts if there is any behaviour mismatch. The
   * DFAs accept the same language if they behave the same for all reachable states.
   */
  private static boolean sameLanguage(DFA a, DFA b) {
    if (a.numInput() != b.numInput() || a.entryState.length != b.entryState.length) {
      return false;
    }
    int width = b.numStates() + 1;
    boolean[] seen = new boolean[(a.numStates() + 1) * width];
    List<int[]> todo = new ArrayList<>();
    for (int i = 0; i < a.entryState.length; i++) {
      todo.add(new int[] {a.entryState(i), b.entryState(i)});
    }
    while (!todo.isEmpty()) {
      int[] pair = todo.remove(todo.size() - 1);
      int stateA = pair[0];
      int stateB = pair[1];
      if (seen[(stateA + 1) * width + stateB + 1]) {
        continue;
      }
      seen[(stateA + 1) * width + stateB + 1] = true;

      boolean finalA = stateA >= 0 && a.isFinal(stateA);
      boolean finalB = stateB >= 0 && b.isFinal(stateB);
      if (finalA != finalB || finalA && !a.action(stateA).isEquiv(b.action(stateB))) {
        return false;
      }
      for (int input = 0; input < a.numInput(); input++) {
        int targetA = stateA >= 0 ? a.table(stateA, input) : -1;
        int targetB = stateB >= 0 ? b.table(stateB, input) : -1;
        if (targetA >= 0 || targetB >= 0) {
          todo.add(new int[] {targetA, targetB});
        }
      }
    }
    return true;
  }

  /**
   * The specification of {@link DFA#isIsomorphicTo(DFA)}. Tries bijections between the reachable
   * states of the DFAs until it finds an isomphism.
   */
  private static boolean bruteForceIsomorphic(DFA a, DFA b) {
    if (a.numInput() != b.numInput()
        || a.numLexStates() != b.numLexStates()
        || a.entryState.length != b.entryState.length) {
      return false;
    }
    List<Integer> statesA = reachable(a);
    List<Integer> statesB = reachable(b);
    if (statesA.size() != statesB.size()) {
      return false;
    }
    return existsIsomorphism(
        a, b, statesA, statesB, new int[a.numStates()], new boolean[statesB.size()], 0);
  }

  /**
   * Whether the mapping {@code map} of the first {@code k} states of {@code statesA} can be
   * extended to an isomorphism.
   */
  private static boolean existsIsomorphism(
      DFA a,
      DFA b,
      List<Integer> statesA,
      List<Integer> statesB,
      int[] map,
      boolean[] used,
      int k) {
    if (k == statesA.size()) {
      return isIsomorphism(a, b, statesA, map);
    }
    for (int i = 0; i < statesB.size(); i++) {
      if (!used[i]) {
        used[i] = true;
        map[statesA.get(k)] = statesB.get(i);
        if (existsIsomorphism(a, b, statesA, statesB, map, used, k + 1)) {
          return true;
        }
        used[i] = false;
      }
    }
    return false;
  }

  /**
   * Whether the bijection {@code map} from the (reachable) states of a to the states of b is a DFA
   * isomorphism. It checks entry states, finality, actions, and for transitions {@code map[s] -c->
   * map[t]} iff {@code s -c-> t}.
   */
  private static boolean isIsomorphism(DFA a, DFA b, List<Integer> statesA, int[] map) {
    for (int i = 0; i < a.entryState.length; i++) {
      if (map[a.entryState(i)] != b.entryState(i)) {
        return false;
      }
    }
    for (int state : statesA) {
      int image = map[state];
      if (a.isFinal(state) != b.isFinal(image)) {
        return false;
      }
      if (a.isFinal(state) && !a.action(state).isEquiv(b.action(image))) {
        return false;
      }
      for (int input = 0; input < a.numInput(); input++) {
        int targetA = a.table(state, input);
        int targetB = b.table(image, input);
        if (targetA < 0 ? targetB >= 0 : targetB != map[targetA]) {
          return false;
        }
      }
    }
    return true;
  }
}
