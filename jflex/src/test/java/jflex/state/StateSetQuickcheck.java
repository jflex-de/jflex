/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.state;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.junit.Assume.assumeTrue;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.Also;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

/**
 * Property-based tests for {@link StateSet}
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see StateSet
 */
@RunWith(JUnitQuickcheck.class)
public class StateSetQuickcheck {

  @Property
  public void size2nbits(@InRange(minInt = 1, maxInt = 56) int size) {
    assertThat(StateSet.size2nbits(StateSet.nbits2size(size))).isEqualTo(size);
  }

  @Property
  public void containsIsSubset(StateSet s1, StateSet s2) {
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
  public void addIsUnion(StateSet s1, StateSet s2) {
    StateSet union = new StateSet(s1);
    union.add(s2);

    assertThat(union.contains(s1)).isTrue();
    assertThat(union.contains(s2)).isTrue();

    for (int i : union) {
      assertThat(s1.hasElement(i) || s2.hasElement(i)).isTrue();
    }
  }

  @Property
  public void addCommutes(StateSet s1, StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(s2);
    union2.add(s1);

    assertThat(union1).isEqualTo(union2);
  }

  @Property
  public void addEmpty(StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(new StateSet());
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void addSelf(StateSet set) {
    StateSet setPre = new StateSet(set);
    set.add(set);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void addIdemPotent(StateSet s1, StateSet s2) {
    StateSet union1 = new StateSet(s1);
    union1.add(s2);
    StateSet union2 = new StateSet(union1);
    union2.add(s2);
    assertThat(union2).isEqualTo(union1);
  }

  @Property
  public void intersect(@InRange(maxInt = 100) StateSet s1, @InRange(maxInt = 100) StateSet s2) {
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
      @Size(max = 90) @InRange(minInt = 0, maxInt = 100) StateSet s1,
      @Size(max = 10) @InRange(minInt = 0, maxInt = 100) StateSet s2) {
    assumeTrue(s1.contains(s2));
    StateSet s2Pre = new StateSet(s2);
    s2.intersect(s1);
    assertThat(s2).isEqualTo(s2Pre);
  }

  @Property
  public void intersectCommutes(StateSet s1, StateSet s2) {
    StateSet inter1 = new StateSet(s1);
    inter1.add(s2);
    StateSet inter2 = new StateSet(s2);
    inter2.add(s1);

    assertThat(inter1).isEqualTo(inter2);
  }

  @Property
  public void intersectEmpty(StateSet set) {
    set.intersect(new StateSet());
    assertThat(set).isEqualTo(new StateSet());
  }

  @Property
  public void intersectSelf(StateSet set) {
    StateSet setPre = new StateSet(set);
    set.intersect(set);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void containsItsElements(StateSet set) {
    for (int i : set) assertThat(set.hasElement(i)).isTrue();
  }

  @Property
  public void removeRemoves(
      @Size(max = 90) @InRange(minInt = 0, maxInt = 100) StateSet s,
      @InRange(minInt = 0, maxInt = 100) int e) {
    s.remove(e);
    assertThat(s.hasElement(e)).isFalse();
  }

  @Property
  public void removeAdd(
      @Size(max = 90) @InRange(minInt = 0, maxInt = 100) StateSet s,
      @InRange(minInt = 0, maxInt = 100) int e) {
    assumeTrue(s.hasElement(e));
    StateSet sPre = new StateSet(s);
    s.remove(e);
    assertThat(sPre.contains(s)).isTrue();
    assertThat(s).isNotEqualTo(sPre);
    s.addState(e);
    assertThat(s).isEqualTo(sPre);
  }

  @Property
  public void removeAddResize(
      @Size(max = 90) @InRange(minInt = 0, maxInt = 100) StateSet s,
      @InRange(minInt = 0, maxInt = 100) int e,
      @From(OffsetGen.class) int largeOffset) {
    assumeTrue(s.hasElement(e));
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
  public void clearMakesEmpty(StateSet set) {
    set.clear();
    assertThat(set).isEmpty();
    assertThat(set).isEqualTo(new StateSet());
  }

  @Property
  public void addStateAdds(StateSet set, @InRange(minInt = 0, maxInt = 34) int e) {
    set.addState(e);
    assertThat(set.hasElement(e)).isTrue();
  }

  @Property
  public void addStateDoesNotRemove(
      StateSet set, @Also("2147483647") @InRange(minInt = 0, maxInt = 34) int e) {
    StateSet setPre = new StateSet(set);
    set.addState(e);
    assertThat(set.contains(setPre)).isTrue();
    assertThat(set.hasElement(e)).isTrue();

    // add an out of range value to increase coverage of contains

    // offset to StateSetGen.maxRange + 1
    int offset =
        1001; // note this effected by InRange, so this needs to be adjusted based on annotations
    // on set, default is set

    //  if no overflow then offset + e, else overflow so use MAX_VALUE
    int newValue = (Integer.MAX_VALUE - offset) >= e ? offset + e : Integer.MAX_VALUE;
    assumeThat(set.hasElement(newValue), equalTo(false));
    set.addState(newValue);
    assertThat(set.contains(setPre)).isTrue();
    assertThat(set.hasElement(newValue)).isTrue();
    assertThat(setPre.contains(set)).isFalse();
  }

  @Property
  public void addStateAdd(StateSet set, @InRange(minInt = 0, maxInt = 34) int e) {
    StateSet set2 = new StateSet(set);
    set.addState(e);
    set2.add(new StateSet(10, e));
    assertThat(set).isEqualTo(set2);
  }

  @Property
  public void complementNoOriginalElements(StateSet s1, StateSet s2) {
    StateSet comp = s1.complement(s2);
    if (comp == null) {
      fail("complement returned null");
      return; // unreachable; makes non-nullable check happy
    }

    // no elements of s1 are in the complement
    comp.intersect(s1);
    assertThat(comp).isEmpty();
  }

  @Property
  public void complementElements(StateSet s1, StateSet s2) {
    StateSet comp = s1.complement(s2);
    if (comp == null) {
      fail("complement returned null");
      return; // unreachable; makes non-nullable check happy
    }

    // only elements of s2 are in the complement
    assertThat(s2.contains(comp)).isTrue();

    // ensure that comp does not contain s1
    if (s1.containsElements()) { // if s1 is {}, then it will always be contained in comp
      assertThat(comp.contains(s1)).isFalse();
    }
  }

  @Property
  public void complementUnion(StateSet s1, StateSet s2) {
    // complement creates no elements outside s1 union s2
    StateSet union0 = new StateSet(s1);
    union0.add(s2);

    StateSet comp = s1.complement(s2);

    StateSet union1 = new StateSet(comp);
    union1.add(s1);

    assertThat(union1).isEqualTo(union0);
  }

  @Property
  public void containsElements(StateSet s, @InRange(minInt = 0, maxInt = 34) int e) {
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
  public void containsNoElements(StateSet s) {
    s.clear();
    assertThat(s.containsElements()).isFalse();
  }

  @Property
  public void copy(StateSet set) {
    StateSet copy = set.copy();
    assertThat(copy).isEqualTo(set);
    assumeTrue(set.containsElements());
    set.clear();
    assertThat(copy).isNotEqualTo(set);
  }

  @Property
  public void copyInto(StateSet s1, StateSet s2) {
    s1.copy(s2);
    assertThat(s1).isEqualTo(s2);
    assumeTrue(s1.containsElements());
    s1.clear();
    assertThat(s1).isNotEqualTo(s2);
  }

  @Property
  public void hashCode(StateSet s1, StateSet s2) {
    // two StateSet objects with different memory backing, but equal content should have same
    // hashCode
    s1.copy(s2);
    assertThat(s1.hashCode()).isEqualTo(s2.hashCode());
  }

  @Property
  public void getAndRemoveRemoves(StateSet set) {
    assumeTrue(set.containsElements());
    int e = set.getAndRemoveElement();
    assertThat(set.hasElement(e)).isFalse();
  }

  @Property
  public void getAndRemoveIsElement(StateSet set) {
    assumeTrue(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    assertThat(setPre.hasElement(e)).isTrue();
  }

  @Property
  public void getAndRemoveAdd(StateSet set) {
    assumeTrue(set.containsElements());
    StateSet setPre = set.copy();
    int e = set.getAndRemoveElement();
    set.addState(e);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void enumerator(StateSet set) {
    StateSetEnumerator s = set.states();
    for (int e : set) {
      assertThat(s.hasMoreElements()).isTrue();
      assertThat(s.nextElement()).isEqualTo(e);
    }
  }
}
