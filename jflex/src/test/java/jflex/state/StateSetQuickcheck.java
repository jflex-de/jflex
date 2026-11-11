/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

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
    assertThat(StateSet.size2nbits(StateSet.nbits2size(size))).isEqualTo(size);
  }

  @Property
  public void containsIsSubset(@ForAll StateSet s1, @ForAll StateSet s2) {
    boolean allElements = true;
    for (int e : s2) allElements &= s1.hasElement(e);
    assertWithMessage("s1.contains(s2) should be equal to " + allElements)
        .that(s1.contains(s2))
        .isEqualTo(allElements);

    allElements = true;
    for (int e : s1) allElements &= s2.hasElement(e);
    assertWithMessage("s2.contains(s1) should be equal to " + allElements)
        .that(s2.contains(s1))
        .isEqualTo(allElements);
  }

  @Property
  public void addIsUnion(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union = new StateSet(s1);
    union.add(s2);

    assertThat(union.contains(s1)).isTrue();
    assertThat(union.contains(s2)).isTrue();

    for (int i : union) {
      assertThat(s1.hasElement(i) || s2.hasElement(i)).isTrue();
    }
  }

  @Property
  public void addCommutes(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(s2);
    union2.add(s1);

    assertThat(union1).isEqualTo(union2);
  }

  @Property
  public void addEmpty(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(new StateSet());
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void addSelf(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(set);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void addIdemPotent(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(union1);
    union2.add(s2);
    assertThat(union2).isEqualTo(union1);
  }

  @Property
  public void intersect(
      @ForAll @IntRange(max = 100) StateSet s1, @ForAll @IntRange(max = 100) StateSet s2) {
    StateSet inter = new StateSet(s1);
    inter.intersect(s2);

    assertThat(s1.contains(inter)).isTrue();
    assertThat(s2.contains(inter)).isTrue();

    for (int i : s1) {
      assertThat(!s2.hasElement(i) || inter.hasElement(i)).isTrue();
    }
  }

  @Property
  public void intersectUnchanged(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s1,
      @ForAll @Size(max = 10) @IntRange(max = 100) StateSet s2) {
    // make s2 a subset of s1
    s1.add(s2);
    assertThat(s1.contains(s2)).isTrue();
    StateSet s2Pre = new StateSet(s2);
    s2.intersect(s1);
    assertThat(s2).isEqualTo(s2Pre);
  }

  @Property
  public void intersectCommutes(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet inter1 = new StateSet(s1);
    inter1.add(s2);
    StateSet inter2 = new StateSet(s2);
    inter2.add(s1);

    assertThat(inter1).isEqualTo(inter2);
  }

  @Property
  public void intersectEmpty(@ForAll StateSet set) {
    set.intersect(new StateSet());
    assertThat(set).isEqualTo(new StateSet());
  }

  @Property
  public void intersectSelf(@ForAll StateSet set) {
    StateSet setPre = new StateSet(set);
    set.intersect(set);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void containsItsElements(@ForAll StateSet set) {
    for (int i : set) assertThat(set.hasElement(i)).isTrue();
  }

  @Property
  public void removeRemoves(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s, @ForAll @IntRange(max = 100) int e) {
    s.remove(e);
    assertThat(s.hasElement(e)).isFalse();
  }

  @Property
  public void removeAdd(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s, @ForAll @IntRange(max = 100) int e) {
    Assume.that(s.hasElement(e));
    StateSet sPre = new StateSet(s);
    s.remove(e);
    assertThat(sPre.contains(s)).isTrue();
    assertThat(s).isNotEqualTo(sPre);
    s.addState(e);
    assertThat(s).isEqualTo(sPre);
  }

  @Property
  public void removeAddResize(
      @ForAll @Size(max = 90) @IntRange(max = 100) StateSet s,
      @ForAll @IntRange(max = 100) int e,
      @ForAll("offsets") int largeOffset) {
    Assume.that(s.hasElement(e));
    StateSet sPre = new StateSet(s);
    s.remove(e);
    assertThat(sPre.contains(s)).isTrue();
    assertThat(s).isNotEqualTo(sPre);
    s.addState(e);
    assertThat(s).isEqualTo(sPre);

    // add larger state value to force resize
    int largerState;
    try {
      largerState = Math.addExact(s.getCurrentMaxState(), largeOffset);
    } catch (ArithmeticException arithmeticException) {
      largerState = Integer.MAX_VALUE;
    }
    s.addState(largerState);
    assertThat(s).contains(largerState);
    s.remove(largerState);
    assertThat(s).isEqualTo(sPre);
  }

  @Property
  public void clearMakesEmpty(@ForAll StateSet set) {
    set.clear();
    assertThat(set).isEmpty();
    assertThat(set).isEqualTo(new StateSet());
  }

  @Property
  public void addStateAdds(@ForAll StateSet set, @ForAll @IntRange(max = 34) int e) {
    set.addState(e);
    assertThat(set.hasElement(e)).isTrue();
  }

  @Property
  public void addStateDoesNotRemove(@ForAll StateSet set, @ForAll("stateOrMax") int e) {
    StateSet setPre = new StateSet(set);
    set.addState(e);
    assertThat(set.contains(setPre)).isTrue();
    assertThat(set.hasElement(e)).isTrue();

    // add an out of range value to increase coverage of contains

    // Larger than the default elements of set. Needs to be adjusted if set gets an IntRange.
    int offset = StateSetGen.MAX_RANGE + 1;

    //  if no overflow then offset + e, else overflow so use MAX_VALUE
    int newValue = (Integer.MAX_VALUE - offset) >= e ? offset + e : Integer.MAX_VALUE;
    Assume.that(!set.hasElement(newValue));
    set.addState(newValue);
    assertThat(set.contains(setPre)).isTrue();
    assertThat(set.hasElement(newValue)).isTrue();
    assertThat(setPre.contains(set)).isFalse();
  }

  @Property
  public void addStateAdd(@ForAll StateSet set, @ForAll @IntRange(max = 34) int e) {
    StateSet set2 = new StateSet(set);
    set.addState(e);
    set2.add(new StateSet(10, e));
    assertThat(set).isEqualTo(set2);
  }

  @Property
  public void complementNoOriginalElements(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet comp = s1.complement(s2);
    assertThat(comp).isNotNull();

    // no elements of s1 are in the complement
    comp.intersect(s1);
    assertThat(comp).isEmpty();
  }

  @Property
  public void complementElements(@ForAll StateSet s1, @ForAll StateSet s2) {
    StateSet comp = s1.complement(s2);
    assertThat(comp).isNotNull();

    // only elements of s2 are in the complement
    assertThat(s2.contains(comp)).isTrue();

    // ensure that comp does not contain s1
    if (s1.containsElements()) { // if s1 is {}, then it will always be contained in comp
      assertThat(comp.contains(s1)).isFalse();
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

    assertThat(union1).isEqualTo(union0);
  }

  @Property
  public void containsElements(@ForAll StateSet s, @ForAll @IntRange(max = 34) int e) {
    s.addState(e);
    assertThat(s.containsElements()).isTrue();

    // remove each added element, ot ensure containsElements continues to work as elements are
    // removed
    while (s.containsElements()) {
      s.getAndRemoveElement();
    }
    assertThat(s.containsElements()).isFalse();
  }

  @Property
  public void containsNoElements(@ForAll StateSet s) {
    s.clear();
    assertThat(s.containsElements()).isFalse();
  }

  @Property
  public void copy(@ForAll StateSet set) {
    StateSet copy = set.copy();
    assertThat(copy).isEqualTo(set);
    Assume.that(set.containsElements());
    set.clear();
    assertThat(copy).isNotEqualTo(set);
  }

  @Property
  public void copyInto(@ForAll StateSet s1, @ForAll StateSet s2) {
    s1.copy(s2);
    assertThat(s1).isEqualTo(s2);
    Assume.that(s1.containsElements());
    s1.clear();
    assertThat(s1).isNotEqualTo(s2);
  }

  @Property
  public void hashCode(@ForAll StateSet s1, @ForAll StateSet s2) {
    // two StateSet objects with different memory backing, but equal content should have same
    // hashCode
    s1.copy(s2);
    assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
  }

  @Property
  public void getAndRemoveRemoves(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    int e = set.getAndRemoveElement();
    assertThat(set.hasElement(e)).isFalse();
  }

  @Property
  public void getAndRemoveIsElement(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    assertThat(setPre.hasElement(e)).isTrue();
  }

  @Property
  public void getAndRemoveAdd(@ForAll StateSet set) {
    Assume.that(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    set.addState(e);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void enumerator(@ForAll StateSet set) {
    StateSetEnumerator s = set.states();
    for (int e : set) {
      assertThat(s.hasMoreElements()).isTrue();
      assertThat(s.nextElement()).isEqualTo(e);
    }
  }
}
