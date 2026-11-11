/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.Tuple;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;

/**
 * Property-based tests for {@link StateSet}
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see StateSet
 */
public class StateSetQuickcheck {

  @Provide
  Arbitrary<Integer> offsets() {
    return OffsetGen.offsets();
  }

  /** A small state, and sometimes the largest possible one. */
  @Provide
  Arbitrary<Integer> stateOrMax() {
    return Arbitraries.frequencyOf(
        Tuple.of(19, Arbitraries.integers().between(0, 34)),
        Tuple.of(1, Arbitraries.just(Integer.MAX_VALUE)));
  }

  @Property
  public void size2nbits(@ForAll @IntRange(min = 1, max = 56) int size) {
    assertEquals(size, StateSet.size2nbits(StateSet.nbits2size(size)));
  }

  @Property
  public void containsIsSubset(@ForAll StateSet s1, @ForAll StateSet s2) {
    boolean allElements = true;
    for (int e : s2) allElements &= s1.hasElement(e);
    assertEquals(allElements, s1.contains(s2), "s1.contains(s2) should be equal to " + allElements);

    allElements = true;
    for (int e : s1) allElements &= s2.hasElement(e);
    assertEquals(allElements, s2.contains(s1), "s2.contains(s1) should be equal to " + allElements);
  }

  @Property
  public void addIsUnion(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union = new StateSet(s1);
    union.add(s2);

    assertTrue(union.contains(s1));
    assertTrue(union.contains(s2));

    for (int i : union) {
      assertTrue(s1.hasElement(i) || s2.hasElement(i));
    }
  }

  @Property
  public void addCommutes(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(s2);
    union2.add(s1);

    assertEquals(union2, union1);
  }

  @Property
  public void addEmpty(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(new StateSet());
    assertEquals(setPre, set);
  }

  @Property
  public void addSelf(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(set);
    assertEquals(setPre, set);
  }

  @Property
  public void addIdemPotent(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(union1);
    union2.add(s2);
    assertEquals(union1, union2);
  }

  @Property
  public void intersect(
      @ForAll @IntRange(max = 100) StateSet s1, @ForAll @IntRange(max = 100) StateSet s2) {
    StateSet inter = new StateSet(s1);
    inter.intersect(s2);

    assertTrue(s1.contains(inter));
    assertTrue(s2.contains(inter));

    for (int i : s1) {
      assertTrue(!s2.hasElement(i) || inter.hasElement(i));
    }
  }

  @Property
  public void intersectUnchanged(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s1,
      @ForAll @Size(max = 10) @IntRange(max = 100) StateSet s2) {
    // make s2 a subset of s1
    s1.add(s2);
    assertTrue(s1.contains(s2));
    StateSet s2Pre = new StateSet(s2);
    s2.intersect(s1);
    assertEquals(s2Pre, s2);
  }

  @Property
  public void intersectCommutes(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet inter1 = new StateSet(s1);
    inter1.add(s2);
    StateSet inter2 = new StateSet(s2);
    inter2.add(s1);

    assertEquals(inter2, inter1);
  }

  @Property
  public void intersectEmpty(@ForAll StateSet set) {
    set.intersect(new StateSet());
    assertEquals(new StateSet(), set);
  }

  @Property
  public void intersectSelf(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.intersect(set);
    assertEquals(setPre, set);
  }

  @Property
  public void containsItsElements(@ForAll StateSet set) {
    for (int i : set) assertTrue(set.hasElement(i));
  }

  @Property
  public void removeRemoves(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s, @ForAll @IntRange(max = 100) int e) {
    s.remove(e);
    assertFalse(s.hasElement(e));
  }

  @Property
  public void removeAdd(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s, @ForAll @IntRange(max = 100) int e) {
    Assume.that(s.hasElement(e));
    StateSet sPre = new StateSet(s);
    s.remove(e);
    assertTrue(sPre.contains(s));
    assertNotEquals(sPre, s);
    s.addState(e);
    assertEquals(sPre, s);
  }

  @Property
  public void removeAddResize(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s,
      @ForAll @IntRange(max = 100) int e,
      @ForAll("offsets") int largeOffset) {
    Assume.that(s.hasElement(e));
    StateSet sPre = new StateSet(s);
    s.remove(e);
    assertTrue(sPre.contains(s));
    assertNotEquals(sPre, s);
    s.addState(e);
    assertEquals(sPre, s);

    // add larger state value to force resize
    int largerState;
    try {
      largerState = Math.addExact(s.getCurrentMaxState(), largeOffset);
    } catch (ArithmeticException arithmeticException) {
      largerState = Integer.MAX_VALUE;
    }
    s.addState(largerState);
    assertTrue(s.hasElement(largerState));
    s.remove(largerState);
    assertEquals(sPre, s);
  }

  @Property
  public void clearMakesEmpty(@ForAll StateSet set) {
    set.clear();
    assertFalse(set.containsElements());
    assertEquals(new StateSet(), set);
  }

  @Property
  public void addStateAdds(@ForAll StateSet set, @ForAll @IntRange(max = 34) int e) {
    set.addState(e);
    assertTrue(set.hasElement(e));
  }

  @Property
  public void addStateDoesNotRemove(@ForAll StateSet set, @ForAll("stateOrMax") int e) {
    StateSet setPre = new StateSet(set);
    set.addState(e);
    assertTrue(set.contains(setPre));
    assertTrue(set.hasElement(e));

    // add an out of range value to increase coverage of contains

    // Larger than the default elements of set. Needs to be adjusted if set gets an IntRange.
    int offset = StateSetGen.MAX_RANGE + 1;

    //  if no overflow then offset + e, else overflow so use MAX_VALUE
    int newValue = (Integer.MAX_VALUE - offset) >= e ? offset + e : Integer.MAX_VALUE;
    Assume.that(!set.hasElement(newValue));
    set.addState(newValue);
    assertTrue(set.contains(setPre));
    assertTrue(set.hasElement(newValue));
    assertFalse(setPre.contains(set));
  }

  @Property
  public void addStateAdd(@ForAll StateSet set, @ForAll @IntRange(max = 34) int e) {
    StateSet set2 = new StateSet(set);
    set.addState(e);
    set2.add(new StateSet(10, e));
    assertEquals(set2, set);
  }

  @Property
  public void complementNoOriginalElements(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet comp = s1.complement(s2);
    assertNotNull(comp);

    // no elements of s1 are in the complement
    comp.intersect(s1);
    assertFalse(comp.containsElements());
  }

  @Property
  public void complementElements(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet comp = s1.complement(s2);
    assertNotNull(comp);

    // only elements of s2 are in the complement
    assertTrue(s2.contains(comp));

    // ensure that comp does not contain s1
    if (s1.containsElements()) { // if s1 is {}, then it will always be contained in comp
      assertFalse(comp.contains(s1));
    }
  }

  @Property
  public void complementUnion(@ForAll StateSet s1, @ForAll StateSet s2) {
    // complement creates no elements outside s1 union s2
    StateSet union0 = new StateSet(s1);
    union0.add(s2);

    StateSet comp = s1.complement(s2);

    StateSet union1 = new StateSet(comp);
    union1.add(s1);

    assertEquals(union0, union1);
  }

  @Property
  public void containsElements(@ForAll StateSet s, @ForAll @IntRange(max = 34) int e) {
    s.addState(e);
    assertTrue(s.containsElements());

    // remove each added element, ot ensure containsElements continues to work as elements are
    // removed
    while (s.containsElements()) {
      s.getAndRemoveElement();
    }
    assertFalse(s.containsElements());
  }

  @Property
  public void containsNoElements(@ForAll StateSet s) {
    s.clear();
    assertFalse(s.containsElements());
  }

  @Property
  public void copy(@ForAll StateSet set) {
    StateSet copy = set.copy();
    assertEquals(set, copy);
    Assume.that(set.containsElements());
    set.clear();
    assertNotEquals(set, copy);
  }

  @Property
  public void copyInto(@ForAll StateSet s1, @ForAll StateSet s2) {
    s1.copy(s2);
    assertEquals(s2, s1);
    Assume.that(s1.containsElements());
    s1.clear();
    assertNotEquals(s2, s1);
  }

  @Property
  public void hashCode(@ForAll StateSet s1, @ForAll StateSet s2) {
    // two StateSet objects with different memory backing, but equal content should have same
    // hashCode
    s1.copy(s2);
    assertEquals(s2.hashCode(), s1.hashCode());
  }

  @Property
  public void getAndRemoveRemoves(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    int e = set.getAndRemoveElement();
    assertFalse(set.hasElement(e));
  }

  @Property
  public void getAndRemoveIsElement(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    assertTrue(setPre.hasElement(e));
  }

  @Property
  public void getAndRemoveAdd(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    set.addState(e);
    assertEquals(setPre, set);
  }

  @Property
  public void enumerator(@ForAll StateSet set) {
    StateSetEnumerator s = set.states();
    for (int e : set) {
      assertTrue(s.hasMoreElements());
      assertEquals(e, s.nextElement());
    }
  }
}
