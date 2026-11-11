/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jflex.chars.Interval;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;

/**
 * Property-based tests for {@link IntCharSet}
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see IntCharSet
 */
public class IntCharSetQuickcheck {

  @Property
  public void addIsUnion(@ForAll IntCharSet s1, @ForAll IntCharSet s2) {
    IntCharSet union = IntCharSet.copyOf(s1);
    union.add(s2);

    assertTrue(union.invariants());

    assertTrue(IntCharSet.isSubSet(s1, union));
    assertTrue(IntCharSet.isSubSet(s2, union));

    for (int i : union) {
      assertTrue(s1.contains(i) || s2.contains(i));
    }
  }

  @Property
  public void andIsIntersection(
      @ForAll @IntRange(max = 100) IntCharSet s1, @ForAll @IntRange(max = 100) IntCharSet s2) {
    IntCharSet inter = s1.and(s2);

    assertTrue(inter.invariants());

    assertTrue(IntCharSet.isSubSet(inter, s1));
    assertTrue(IntCharSet.isSubSet(inter, s2));

    for (int i : s1) {
      assertTrue(!s2.contains(i) || inter.contains(i));
    }
  }

  @Property
  public void andCommutes(@ForAll IntCharSet s1, @ForAll IntCharSet s2) {
    assertEquals(s2.and(s1), s1.and(s2));
  }

  @Property
  public void addSelf(@ForAll IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.add(setPre);
    assertEquals(setPre, set);
  }

  @Property
  public void addIdemPotent(@ForAll IntCharSet s1, @ForAll IntCharSet s2) {
    IntCharSet union1 = IntCharSet.copyOf(s1);
    union1.add(s2);
    IntCharSet union2 = IntCharSet.copyOf(union1);
    union2.add(s2);
    assertEquals(union1, union2);
  }

  @Property
  public void subIsDifference(@ForAll IntCharSet s1, @ForAll IntCharSet s2) {
    IntCharSet diff = IntCharSet.copyOf(s1);
    // use intersection to ensure that argument of sub is contained in s1
    diff.sub(s1.and(s2));

    assertTrue(diff.invariants());

    assertTrue(IntCharSet.isSubSet(diff, s1));
    assertFalse(diff.and(s2).containsElements());

    // union of the diff and s2 should be equal to union of s1 and s2
    diff.add(s2);
    IntCharSet s3 = IntCharSet.copyOf(s1);
    s3.add(s2);
    assertEquals(s3, diff);
  }

  @Property
  public void containsItsElements(@ForAll IntCharSet set) {
    for (int i : set) assertTrue(set.contains(i));
  }

  @Property
  public void allCharsContainsEverything(@ForAll IntCharSet set) {
    assertTrue(IntCharSet.allChars().contains(set));
  }

  @Property
  public void addSubEq(@ForAll IntCharSet s1, @ForAll IntCharSet s2) {
    IntCharSet s1Pre = IntCharSet.copyOf(s1);
    IntCharSet inter = s1.and(s2);

    s1.sub(inter);
    s1.add(inter);

    assertEquals(s1Pre, s1);
  }

  @Property
  public void addEmpty(@ForAll IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.add(new IntCharSet());
    assertEquals(setPre, set);
  }

  @Property
  public void subEmpty(@ForAll IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.sub(new IntCharSet());
    assertEquals(setPre, set);
  }

  @Property
  public void andEmpty(@ForAll IntCharSet set) {
    assertEquals(new IntCharSet(), set.and(new IntCharSet()));
  }

  @Property
  public void addAll(@ForAll IntCharSet set) {
    set.add(IntCharSet.allChars());
    assertEquals(IntCharSet.allChars(), set);
  }

  @Property
  public void subSelf(@ForAll IntCharSet set) {
    set.sub(IntCharSet.copyOf(set));
    assertEquals(new IntCharSet(), set);
  }

  @Property
  public void andAll(@ForAll IntCharSet set) {
    assertEquals(set, set.and(IntCharSet.allChars()));
  }

  @Property
  public void andSelf(@ForAll IntCharSet set) {
    assertEquals(set, set.and(set));
  }

  @Property
  public void complement(@ForAll IntCharSet set) {
    IntCharSet comp = IntCharSet.allChars();
    comp.sub(set);

    assertTrue(comp.invariants());
    assertFalse(comp.and(set).containsElements());

    comp.add(set);
    assertEquals(IntCharSet.allChars(), comp);
  }

  @Property
  public void invariants(
      @ForAll @Size(max = 15) @IntRange(max = CharClasses.maxChar) IntCharSet set) {
    assertTrue(set.invariants());
  }

  @Property
  public void addConsistent(
      @ForAll @Size(max = 10) @IntRange(max = CharClasses.maxChar) IntCharSet set,
      @ForAll @IntRange(max = CharClasses.maxChar) int c) {
    IntCharSet set2 = IntCharSet.copyOf(set);
    set.add(c);
    set2.add(Interval.ofCharacter(c));
    assertEquals(set2, set);
  }

  @Property
  public void singleInterval(@ForAll @IntRange(max = CharClasses.maxChar) Interval i) {
    IntCharSet set1 = IntCharSet.of(i);
    IntCharSet set2 = new IntCharSet();
    set2.add(i);
    assertEquals(set2, set1);
  }

  @Property
  public void complementUnion(@ForAll IntCharSet set) {
    set.add(IntCharSet.complementOf(set));
    assertEquals(IntCharSet.allChars(), set);
  }

  @Property
  public void complementIntersection(@ForAll IntCharSet set) {
    assertEquals(new IntCharSet(), set.and(IntCharSet.complementOf(set)));
  }
}
