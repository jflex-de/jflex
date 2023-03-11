/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import static com.google.common.truth.Truth.assertThat;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import jflex.chars.Interval;
import org.junit.runner.RunWith;

/**
 * Property-based tests for {@link IntCharSet}
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 * @see IntCharSet
 */
@RunWith(JUnitQuickcheck.class)
public class IntCharSetQuickcheck {

  @Property
  public void addIsUnion(IntCharSet s1, IntCharSet s2) {
    IntCharSet union = IntCharSet.copyOf(s1);
    union.add(s2);

    assertThat(union.invariants()).isTrue();

    assertThat(IntCharSet.isSubSet(s1, union)).isTrue();
    assertThat(IntCharSet.isSubSet(s2, union)).isTrue();

    for (int i : union) {
      assertThat(s1.contains(i) || s2.contains(i)).isTrue();
    }
  }

  @Property
  public void andIsIntersection(
      @InRange(maxInt = 100) IntCharSet s1, @InRange(maxInt = 100) IntCharSet s2) {
    IntCharSet inter = s1.and(s2);

    assertThat(inter.invariants()).isTrue();

    assertThat(IntCharSet.isSubSet(inter, s1)).isTrue();
    assertThat(IntCharSet.isSubSet(inter, s2)).isTrue();

    for (int i : s1) {
      assertThat(!s2.contains(i) || inter.contains(i)).isTrue();
    }
  }

  @Property
  public void andCommutes(IntCharSet s1, IntCharSet s2) {
    assertThat(s1.and(s2)).isEqualTo(s2.and(s1));
  }

  @Property
  public void addSelf(IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.add(setPre);
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void addIdemPotent(IntCharSet s1, IntCharSet s2) {
    IntCharSet union1 = IntCharSet.copyOf(s1);
    union1.add(s2);
    IntCharSet union2 = IntCharSet.copyOf(union1);
    union2.add(s2);
    assertThat(union2).isEqualTo(union1);
  }

  @Property
  public void subIsDifference(IntCharSet s1, IntCharSet s2) {
    IntCharSet diff = IntCharSet.copyOf(s1);
    // use intersection to ensure that argument of sub is contained in s1
    diff.sub(s1.and(s2));

    assertThat(diff.invariants()).isTrue();

    assertThat(IntCharSet.isSubSet(diff, s1)).isTrue();
    assertThat(diff.and(s2).containsElements()).isFalse();

    // union of the diff and s2 should be equal to union of s1 and s2
    diff.add(s2);
    IntCharSet s3 = IntCharSet.copyOf(s1);
    s3.add(s2);
    assertThat(diff).isEqualTo(s3);
  }

  @Property
  public void containsItsElements(IntCharSet set) {
    for (int i : set) assertThat(set.contains(i)).isTrue();
  }

  @Property
  public void allCharsContainsEverything(IntCharSet set) {
    assertThat(IntCharSet.allChars().contains(set)).isTrue();
  }

  @Property
  public void addSubEq(IntCharSet s1, IntCharSet s2) {
    IntCharSet s1Pre = IntCharSet.copyOf(s1);
    IntCharSet inter = s1.and(s2);

    s1.sub(inter);
    s1.add(inter);

    assertThat(s1).isEqualTo(s1Pre);
  }

  @Property
  public void addEmpty(IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.add(new IntCharSet());
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void subEmpty(IntCharSet set) {
    IntCharSet setPre = IntCharSet.copyOf(set);
    set.sub(new IntCharSet());
    assertThat(set).isEqualTo(setPre);
  }

  @Property
  public void andEmpty(IntCharSet set) {
    assertThat(set.and(new IntCharSet())).isEqualTo(new IntCharSet());
  }

  @Property
  public void addAll(IntCharSet set) {
    set.add(IntCharSet.allChars());
    assertThat(set).isEqualTo(IntCharSet.allChars());
  }

  @Property
  public void subSelf(IntCharSet set) {
    set.sub(IntCharSet.copyOf(set));
    assertThat(set).isEqualTo(new IntCharSet());
  }

  @Property
  public void andAll(IntCharSet set) {
    assertThat(set.and(IntCharSet.allChars())).isEqualTo(set);
  }

  @Property
  public void andSelf(IntCharSet set) {
    assertThat(set.and(set)).isEqualTo(set);
  }

  @Property
  public void complement(IntCharSet set) {
    IntCharSet comp = IntCharSet.allChars();
    comp.sub(set);

    assertThat(comp.invariants()).isTrue();
    assertThat(comp.and(set).containsElements()).isFalse();

    comp.add(set);
    assertThat(comp).isEqualTo(IntCharSet.allChars());
  }

  @Property
  public void invariants(@Size(max = 15) @InRange(maxInt = CharClasses.maxChar) IntCharSet set) {
    assertThat(set.invariants()).isTrue();
  }

  @Property
  public void addConsistent(
      @Size(max = 10) @InRange(maxInt = CharClasses.maxChar) IntCharSet set,
      @InRange(maxInt = CharClasses.maxChar) int c) {
    IntCharSet set2 = IntCharSet.copyOf(set);
    set.add(c);
    set2.add(Interval.ofCharacter(c));
    assertThat(set).isEqualTo(set2);
  }

  @Property
  public void singleInterval(@InRange(maxInt = CharClasses.maxChar) Interval i) {
    IntCharSet set1 = IntCharSet.of(i);
    IntCharSet set2 = new IntCharSet();
    set2.add(i);
    assertThat(set1).isEqualTo(set2);
  }

  @Property
  public void complementUnion(IntCharSet set) {
    set.add(IntCharSet.complementOf(set));
    assertThat(set).isEqualTo(IntCharSet.allChars());
  }

  @Property
  public void complementIntersection(IntCharSet set) {
    assertThat(set.and(IntCharSet.complementOf(set))).isEqualTo(new IntCharSet());
  }
}
