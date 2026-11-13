/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.dfa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import jflex.core.Action;
import jflex.logging.Out;
import org.junit.jupiter.api.Test;

public class DfaTest {

  @Test
  @SuppressWarnings("deprecation")
  public void copyOf() {
    DFA dfa1 = new DFA(2, 1, 1);
    DFA dfa2 = DeprecatedDfa.copyOf(dfa1);
    assertEquals(dfa1, dfa2);
  }

  @Test
  public void copy() {
    DFA dfa = example();
    DFA copy = dfa.copy();

    assertEquals(dfa, copy);
    assertEquals(dfa.numStates(), copy.numStates());
    assertEquals(dfa.toString(), copy.toString());
    assertEquals(dfa.canonicalLines(), copy.canonicalLines());

    // the copy is independent of the original
    copy.addTransition(0, 0, 3);
    copy.setEntryState(1, 0);
    copy.setFinal(3, true);
    copy.setAction(3, new Action("return 3;", 3));
    assertEquals(DFA.NO_TARGET, dfa.table(0, 0));
    assertEquals(2, dfa.entryState(1));
    assertFalse(dfa.isFinal(3));
    assertEquals(example().canonicalLines(), dfa.canonicalLines());
    assertFalse(dfa.isIsomorphicTo(copy));
  }

  /**
   * A DFA for one lexical state over three inputs. Entry state is state 2. State 3 is unreachable.
   */
  private static DFA example() {
    Action fixedLook = new Action("f();\n  return \"\\n\";", 2);
    fixedLook.setLookAction(Action.Kind.FIXED_LOOK, 1);

    DFA dfa = new DFA(2, 3, 1);
    dfa.setEntryState(0, 2);
    dfa.setEntryState(1, 2);
    dfa.addTransition(2, 0, 0);
    dfa.addTransition(2, 1, 0);
    dfa.addTransition(2, 2, 1);
    dfa.addTransition(1, 1, 1);
    dfa.addTransition(3, 0, 2);
    dfa.setFinal(0, true);
    dfa.setAction(0, new Action("return a[i];", 1));
    dfa.setFinal(1, true);
    dfa.setAction(1, fixedLook);
    return dfa;
  }

  @Test
  public void canonical() {
    DFA canonical = example().canonical();

    // the unreachable state is gone
    assertEquals(3, canonical.numStates());
    assertEquals(0, canonical.entryState(0));
    assertEquals(0, canonical.entryState(1));
    assertEquals(1, canonical.table(0, 0));
    assertEquals(1, canonical.table(0, 1));
    assertEquals(2, canonical.table(0, 2));
    assertEquals(2, canonical.table(2, 1));
    assertEquals(DFA.NO_TARGET, canonical.table(1, 0));
    assertFalse(canonical.isFinal(0));
    assertEquals("return a[i];", canonical.action(1).content);
    assertEquals(Action.Kind.FIXED_LOOK, canonical.action(2).lookAhead());
  }

  @Test
  public void canonicalLines() {
    assertEquals(
        Arrays.asList(
            "states 3, inputs 3, lexical states 1",
            "entry: 0 0",
            "0: 0-1 -> 1; 2 -> 2",
            "1 [a0]:",
            "2 [a1]: 1 -> 2",
            "a0: return a[i];",
            // action string is escaped
            "a1 FIXED_LOOK len 1: f();\\n  return \"\\\\n\";"),
        example().canonicalLines());
  }

  @Test
  public void canonicalLines_lookahead() {
    Action generalLook = new Action("return 1;", 1);
    generalLook.setLookAction(Action.Kind.GENERAL_LOOK, 0);
    generalLook.setEntryState(2);

    // one lexical state, and the forward and backward DFA of one general lookahead expression
    DFA dfa = new DFA(4, 2, 1);
    dfa.setEntryState(0, 0);
    dfa.setEntryState(1, 0);
    dfa.setEntryState(2, 3);
    dfa.setEntryState(3, 2);
    dfa.addTransition(0, 0, 1);
    dfa.addTransition(3, 0, 4);
    dfa.addTransition(2, 1, 5);
    dfa.setFinal(1, true);
    dfa.setAction(1, generalLook);
    dfa.setFinal(4, true);
    dfa.setAction(4, new Action(Action.Kind.FORWARD_ACTION));
    dfa.setFinal(5, true);
    dfa.setAction(5, new Action(Action.Kind.BACKWARD_ACTION));

    assertEquals(
        Arrays.asList(
            "states 6, inputs 2, lexical states 1",
            "entry: 0 0 1 2",
            "0: 0 -> 3",
            "1: 0 -> 4",
            "2: 1 -> 5",
            "3 [a0]:",
            "4 [a1]:",
            "5 [a2]:",
            "a0 GENERAL_LOOK entry 2: return 1;",
            "a1 FORWARD_ACTION:",
            "a2 BACKWARD_ACTION:"),
        dfa.canonicalLines());
  }

  @Test
  public void toCanonicalString() {
    DFA dfa = new DFA(2, 1, 1);
    dfa.addTransition(0, 0, 0);
    assertEquals(
        "states 1, inputs 1, lexical states 1"
            + Out.NL
            + "entry: 0 0"
            + Out.NL
            + "0: 0 -> 0"
            + Out.NL,
        dfa.toCanonicalString());
  }

  @Test
  public void isIsomorphicTo() {
    DFA dfa = example();
    assertTrue(dfa.isIsomorphicTo(dfa));
    assertTrue(dfa.isIsomorphicTo(dfa.canonical()));

    // equivalent actions: same code and lookahead, but priority is ignored
    DFA sameAction = example();
    sameAction.setAction(0, new Action("return a[i];", 7));
    assertTrue(dfa.isIsomorphicTo(sameAction));

    DFA otherAction = example();
    otherAction.setAction(0, new Action("return a[j];", 1));
    assertFalse(dfa.isIsomorphicTo(otherAction));

    DFA otherTransition = example();
    otherTransition.addTransition(2, 1, 1);
    assertFalse(dfa.isIsomorphicTo(otherTransition));

    DFA otherEntry = example();
    otherEntry.setEntryState(1, 0);
    assertFalse(dfa.isIsomorphicTo(otherEntry));

    // changes in unreachable states are irrelevant
    DFA otherUnreachable = example();
    otherUnreachable.addTransition(3, 1, 3);
    assertTrue(dfa.isIsomorphicTo(otherUnreachable));
  }

  @Test
  public void hasDeadStates() {
    assertFalse(example().hasDeadStates());

    // transition to a new state without transitions
    DFA dead = example();
    dead.addTransition(0, 0, 4);
    assertTrue(dead.hasDeadStates());

    // loop of non-final states
    DFA loop = example();
    loop.addTransition(0, 0, 4);
    loop.addTransition(4, 1, 5);
    loop.addTransition(5, 1, 4);
    assertTrue(loop.hasDeadStates());
    loop.setFinal(5, true);
    loop.setAction(5, new Action("return 1;", 1));
    assertFalse(loop.hasDeadStates());

    // unreachable dead states are fine
    DFA unreachable = example();
    unreachable.addTransition(3, 1, 4);
    assertFalse(unreachable.hasDeadStates());
  }

  /** The entry state of a lexical state without rules is only dead if it has a transition. */
  @Test
  public void hasDeadStates_entryState() {
    DFA noTransitions = new DFA(2, 1, 1);
    assertFalse(noTransitions.hasDeadStates());

    // the second lexical state has no rules
    DFA dfa = new DFA(4, 1, 2);
    dfa.setEntryState(2, 2);
    dfa.setEntryState(3, 2);
    dfa.addTransition(0, 0, 1);
    dfa.setFinal(1, true);
    dfa.setAction(1, new Action("return 1;", 1));
    assertFalse(dfa.hasDeadStates());

    dfa.addTransition(2, 0, 2);
    assertTrue(dfa.hasDeadStates());
  }

  /**
   * Two minimized DFAs for the same language are not isomorphic if one of them has a dead state.
   * (Minimization does not eliminate dead states.)
   */
  @Test
  public void hasDeadStates_minimized() {
    DFA dfa = new DFA(2, 2, 1);
    dfa.addTransition(0, 0, 1);
    dfa.setFinal(1, true);
    dfa.setAction(1, new Action("return 1;", 1));

    DFA dead = new DFA(2, 2, 1);
    dead.addTransition(0, 0, 1);
    dead.addTransition(0, 1, 2);
    dead.setFinal(1, true);
    dead.setAction(1, new Action("return 1;", 1));

    dfa.minimize();
    dead.minimize();

    assertFalse(dfa.hasDeadStates());
    assertTrue(dead.hasDeadStates());
    assertEquals(3, dead.numStates());
    assertFalse(dfa.isIsomorphicTo(dead));
  }

  /** The number of inputs and of lexical states is part of isomorphism. */
  @Test
  public void isIsomorphicTo_dimensions() {
    DFA dfa = new DFA(4, 2, 2);
    dfa.addTransition(0, 0, 0);
    DFA otherLexStates = new DFA(4, 2, 1);
    otherLexStates.addTransition(0, 0, 0);
    DFA otherInputs = new DFA(4, 3, 2);
    otherInputs.addTransition(0, 0, 0);
    DFA otherEntryStates = new DFA(6, 2, 2);
    otherEntryStates.addTransition(0, 0, 0);

    assertTrue(dfa.isIsomorphicTo(dfa));
    assertFalse(dfa.isIsomorphicTo(otherLexStates));
    assertFalse(dfa.isIsomorphicTo(otherInputs));
    assertFalse(dfa.isIsomorphicTo(otherEntryStates));
    assertNotEquals(dfa.canonicalLines(), otherLexStates.canonicalLines());
    assertNotEquals(dfa.canonicalLines(), otherInputs.canonicalLines());
    assertNotEquals(dfa.canonicalLines(), otherEntryStates.canonicalLines());
  }

  /** The canonical form after minimization does not depend on the order of states. */
  @Test
  public void canonicalLines_minimized() {
    // states 1 and 2 are equivalent
    DFA dfa = new DFA(2, 2, 1);
    dfa.setEntryState(0, 0);
    dfa.setEntryState(1, 0);
    dfa.addTransition(0, 0, 1);
    dfa.addTransition(0, 1, 2);
    dfa.addTransition(1, 0, 3);
    dfa.addTransition(2, 0, 3);
    dfa.setFinal(3, true);
    dfa.setAction(3, new Action("return 1;", 1));

    // the same DFA with states 0 and 3 swapped
    DFA swapped = new DFA(2, 2, 1);
    swapped.setEntryState(0, 3);
    swapped.setEntryState(1, 3);
    swapped.addTransition(3, 0, 1);
    swapped.addTransition(3, 1, 2);
    swapped.addTransition(1, 0, 0);
    swapped.addTransition(2, 0, 0);
    swapped.setFinal(0, true);
    swapped.setAction(0, new Action("return 1;", 1));

    dfa.minimize();
    swapped.minimize();

    assertEquals(
        Arrays.asList(
            "states 3, inputs 2, lexical states 1",
            "entry: 0 0",
            "0: 0-1 -> 1",
            "1: 0 -> 2",
            "2 [a0]:",
            "a0: return 1;"),
        dfa.canonicalLines());
    assertEquals(dfa.canonicalLines(), swapped.canonicalLines());
    assertTrue(dfa.isIsomorphicTo(swapped));
  }
}
