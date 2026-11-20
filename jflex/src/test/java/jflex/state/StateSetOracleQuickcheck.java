/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;

/**
 * Checks {@link StateSet} against {@link DenseStateSet}, the bitmap implementation that it
 * replaced, and checks how much memory a {@link StateSet} uses.
 *
 * @see StateSet
 */
public class StateSetOracleQuickcheck {

  enum Kind {
    ADD_STATE,
    REMOVE,
    CLEAR,
    ADD,
    INTERSECT,
    COMPLEMENT,
    COPY_FROM,
    COPY,
    GET_AND_REMOVE
  }

  /** An operation on a set, with a state and a set as operands for the operations that take one. */
  static final class Op {
    final Kind kind;
    final int state;
    final List<Integer> operand;

    /** whether the operand has unused memory */
    final boolean slack;

    Op(Kind kind, int state, List<Integer> operand, boolean slack) {
      this.kind = kind;
      this.state = state;
      this.operand = operand;
      this.slack = slack;
    }

    @Override
    public String toString() {
      return kind + "(" + state + ", " + operand + (slack ? " with slack)" : ")");
    }
  }

  /**
   * States from ranges of different size. The smaller the range, the more elements two sets have in
   * common.
   */
  @Provide
  Arbitrary<Integer> states() {
    return Arbitraries.frequencyOf(
        Tuple.of(3, Arbitraries.integers().between(0, 70)),
        Tuple.of(4, Arbitraries.integers().between(0, 2_000)),
        Tuple.of(2, Arbitraries.integers().between(0, 70_000)),
        Tuple.of(1, Arbitraries.integers().between(69_000, 70_000)));
  }

  @Provide
  Arbitrary<List<Integer>> elements() {
    return Arbitraries.oneOf(
        states().list().ofMaxSize(8),
        states().list().ofMaxSize(120),
        Arbitraries.integers().between(0, 300).list().ofMaxSize(120));
  }

  @Provide
  Arbitrary<List<Op>> ops() {
    return Combinators.combine(
            Arbitraries.of(Kind.class), states(), elements(), Arbitraries.of(true, false))
        .as(Op::new)
        .list()
        .ofMaxSize(25);
  }

  /** A set with the elements and some unused entries at the end of its array. */
  private static StateSet withSlack(Iterable<Integer> elements) {
    TreeSet<Integer> sorted = new TreeSet<>();
    for (int e : elements) sorted.add(e);
    StateSet set = new StateSet();
    set.elems = new int[sorted.size() + 3];
    for (int e : sorted) set.elems[set.numElems++] = e;
    return set;
  }

  private static StateSet build(List<Integer> elements, boolean slack) {
    if (slack) return withSlack(elements);
    StateSet set = new StateSet();
    elements.forEach(set::addState);
    return set;
  }

  private static DenseStateSet oracle(List<Integer> elements) {
    DenseStateSet set = new DenseStateSet();
    elements.forEach(set::addState);
    return set;
  }

  private static List<Integer> elements(StateSet set) {
    List<Integer> result = new ArrayList<>();
    StateSetEnumerator states = set.states();
    while (states.hasMoreElements()) result.add(states.nextElement());
    assertFalse(states.hasNext());
    return result;
  }

  private static List<Integer> elements(DenseStateSet set) {
    List<Integer> result = new ArrayList<>();
    for (int e : set) result.add(e);
    return result;
  }

  /** The internal invariants of a set. */
  private static void assertWellFormed(StateSet set) {
    assertTrue(set.elems != null, "elems is never null");
    assertTrue(set.numElems >= 0 && set.numElems <= set.elems.length, "numElems in range");
    for (int i = 1; i < set.numElems; i++) {
      assertTrue(set.elems[i - 1] < set.elems[i], "elems ascending and distinct");
    }
  }

  /** The set has the elements of the oracle, and is consistent with itself. */
  private static void assertSame(DenseStateSet expected, StateSet actual) {
    assertWellFormed(actual);

    List<Integer> elements = elements(expected);
    assertEquals(elements, elements(actual));
    assertEquals(expected.containsElements(), actual.containsElements());
    assertEquals(expected.toString(), actual.toString());

    // equality and hash code do not depend on unused memory
    for (StateSet other : new StateSet[] {new StateSet(actual), withSlack(elements)}) {
      assertTrue(actual.equals(other));
      assertTrue(other.equals(actual));
      assertEquals(other.hashCode(), actual.hashCode());
      assertTrue(actual.contains(other));
      assertTrue(other.contains(actual));
    }
  }

  @Property(tries = 2000)
  public void sameAsDenseStateSet(@ForAll("ops") List<Op> ops) {
    StateSet actual = new StateSet();
    DenseStateSet expected = new DenseStateSet();

    for (Op op : ops) {
      StateSet operand = build(op.operand, op.slack);
      DenseStateSet expectedOperand = oracle(op.operand);
      assertSame(expectedOperand, operand);

      assertEquals(expected.hasElement(op.state), actual.hasElement(op.state));
      assertEquals(expected.contains(expectedOperand), actual.contains(operand));
      assertEquals(expectedOperand.contains(expected), operand.contains(actual));
      assertEquals(expected.equals(expectedOperand), actual.equals(operand));
      assertEquals(expected.equals(expectedOperand), operand.equals(actual));

      switch (op.kind) {
        case ADD_STATE:
          actual.addState(op.state);
          expected.addState(op.state);
          break;
        case REMOVE:
          actual.remove(op.state);
          expected.remove(op.state);
          break;
        case CLEAR:
          actual.clear();
          expected.clear();
          break;
        case ADD:
          actual.add(operand);
          expected.add(expectedOperand);
          break;
        case INTERSECT:
          actual.intersect(operand);
          expected.intersect(expectedOperand);
          break;
        case COMPLEMENT:
          actual = actual.complement(operand);
          expected = expected.complement(expectedOperand);
          break;
        case COPY_FROM:
          actual.copy(operand);
          expected.copy(expectedOperand);
          break;
        case COPY:
          actual = actual.copy();
          expected = expected.copy();
          break;
        case GET_AND_REMOVE:
          if (expected.containsElements()) {
            // which element is not specified
            int e = actual.getAndRemoveElement();
            assertTrue(expected.hasElement(e));
            expected.remove(e);
          }
          break;
      }

      assertSame(expected, actual);
      // the operand is unchanged
      assertSame(expectedOperand, operand);
    }
  }

  /** The array of a set that only grows has room for less than twice its elements. */
  @Property
  public void growsByAtMostTwice(
      @ForAll("elements") List<Integer> elements,
      @ForAll("elements") List<Integer> more,
      @ForAll boolean slack) {
    StateSet set = new StateSet();

    for (int e : elements) {
      set.addState(e);
      assertWellFormed(set);
      assertTrue(set.elems.length < 2 * set.numElems, "array too large");
    }

    set.add(build(more, slack));
    assertWellFormed(set);
    assertTrue(set.numElems == 0 || set.elems.length < 2 * set.numElems, "array too large");
  }

  /** A copy has no unused memory. */
  @Property
  public void copyIsCompact(@ForAll("elements") List<Integer> elements, @ForAll boolean slack) {
    StateSet set = build(elements, slack);

    for (StateSet copy : new StateSet[] {new StateSet(set), set.copy()}) {
      assertWellFormed(copy);
      assertEquals(elements(set), elements(copy));
      assertEquals(copy.numElems, copy.elems.length);
    }
  }

  /** The hash code has to tell apart sets of states that are close to each other. */
  @Example
  public void hashCodeOfSmallSets() {
    Set<Integer> hashes = new HashSet<>();
    int sets = 0;
    for (int a = 0; a < 200; a++) {
      for (int b = a + 1; b < 200; b++) {
        StateSet set = new StateSet(a);
        set.addState(b);
        hashes.add(set.hashCode());
        sets++;
      }
    }
    assertTrue(hashes.size() > sets - sets / 100, hashes.size() + " hash codes for " + sets);
  }

  @Example
  public void nullOperands() {
    StateSet set = new StateSet(3);
    set.addState(70_000);

    set.add(null);
    assertEquals("{3, 70000}", set.toString());
    assertNull(set.complement(null));

    StateSet copy = set.copy();
    copy.intersect(null);
    assertFalse(copy.containsElements());

    copy = set.copy();
    copy.copy(null);
    assertFalse(copy.containsElements());

    assertFalse(set.equals(null));
  }

  /** Operations with the set itself as operand. */
  @Property
  public void selfOperands(@ForAll("elements") List<Integer> elements, @ForAll boolean slack) {
    StateSet set = build(elements, slack);
    DenseStateSet expected = oracle(elements);

    set.add(set);
    assertSame(expected, set);
    set.intersect(set);
    assertSame(expected, set);
    set.copy(set);
    assertSame(expected, set);
    assertTrue(set.contains(set));
    assertFalse(set.complement(set).containsElements());
  }
}
