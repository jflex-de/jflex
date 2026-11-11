/*
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import jflex.base.Pair;
import jflex.chars.Interval;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Size;

/**
 * Property-based tests for {@link CharClasses}
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 * @see IntCharSet
 */
public class CharClassesQuickcheck {

  // TODO(lsf): add testing for caseless; needs UnicodeProperties

  @Provide
  Arbitrary<Integer> intChars() {
    return IntCharGen.intChars();
  }

  @Property
  public void invariants(@ForAll CharClasses c) {
    assertTrue(c.invariants());
  }

  @Property
  public void maxCharCode(@ForAll CharClasses c) {
    // currently only testing with maxChar
    assertEquals(CharClasses.maxChar, c.getMaxCharCode());
  }

  @Property
  public void addSingle(
      @ForAll CharClasses classes, @ForAll("intChars") int c1, @ForAll("intChars") int c2) {
    Assume.that(c1 != c2);
    classes.makeClass(c1, false);
    assertTrue(classes.invariants());
    assertNotEquals(classes.getClassCode(c2), classes.getClassCode(c1));
  }

  @Property
  public void addSingleSingleton(@ForAll CharClasses classes, @ForAll("intChars") int c) {
    classes.makeClass(c, false);
    IntCharSet set = classes.getCharClass(classes.getClassCode(c));
    assertEquals(IntCharSet.ofCharacter(c), set);
  }

  @Property
  public void addSet(
      @ForAll CharClasses classes,
      @ForAll @IntRange(max = CharClasses.maxChar) IntCharSet set,
      @ForAll("intChars") int c) {

    Assume.that(!set.contains(c));

    classes.makeClass(set, false);
    assertTrue(classes.invariants());

    int[] classCodes = classes.getClassCodes(set, false);
    int cCode = classes.getClassCode(c);
    for (int i : classCodes) {
      assertNotEquals(cCode, i);
    }
  }

  @Property
  public void addSetParts(
      @ForAll CharClasses classes, @ForAll @IntRange(max = CharClasses.maxChar) IntCharSet set) {

    classes.makeClass(set, false);

    int[] classCodes = classes.getClassCodes(set, false);
    IntCharSet allParts = new IntCharSet();
    for (int i : classCodes) {
      allParts.add(classes.getCharClass(i));
    }
    assertEquals(set, allParts);
  }

  @Property
  public void addSetComplement(
      @ForAll CharClasses classes, @ForAll @IntRange(max = CharClasses.maxChar) IntCharSet set) {

    classes.makeClass(set, false);

    int[] notCodes = classes.getClassCodes(set, true);
    IntCharSet others = new IntCharSet();
    for (int i : notCodes) {
      others.add(classes.getCharClass(i));
    }
    assertEquals(IntCharSet.complementOf(set), others);
  }

  @Property
  public void addString(@ForAll CharClasses classes, @ForAll String s, @ForAll("intChars") int c) {

    Assume.that(s.indexOf(c) < 0);

    classes.makeClass(s, false);
    assertTrue(classes.invariants());

    int cCode = classes.getClassCode(c);
    for (int i = 0; i < s.length(); ) {
      int ch = s.codePointAt(i);
      assertNotEquals(cCode, classes.getClassCode(ch));
      i += Character.charCount(ch);
    }
  }

  @Property
  public void normaliseSingle(
      @ForAll CharClasses classes, @ForAll @IntRange(max = CharClasses.maxChar) int c) {
    CharClasses preClasses = CharClasses.copyOf(classes);

    classes.normalise();
    assertTrue(classes.invariants());

    IntCharSet classNew = classes.getCharClass(classes.getClassCode(c));
    IntCharSet classOld = preClasses.getCharClass(preClasses.getClassCode(c));
    assertEquals(classOld, classNew);
  }

  private static int translateBlocks(Pair<int[], List<CMapBlock>> table, int input) {
    int top = table.fst[input >> CMapBlock.BLOCK_BITS];
    int offset = input & (CMapBlock.BLOCK_SIZE - 1);
    return table.snd.get(top).block[offset];
  }

  @Property(tries = 20)
  public void computeTablesEq(
      @ForAll CharClasses classes,
      @ForAll @Size(100) List<@IntRange(max = CharClasses.maxChar) Integer> inputs) {
    Pair<int[], List<CMapBlock>> table = classes.computeTables();
    for (int input : inputs) {
      assertEquals(classes.getClassCode(input), translateBlocks(table, input));
    }
  }

  private static int translateFlat(Pair<int[], int[]> table, int input) {
    int top = table.fst[input >> CMapBlock.BLOCK_BITS];
    int offset = input & (CMapBlock.BLOCK_SIZE - 1);
    return offset == input ? table.snd[offset] : table.snd[top | offset];
  }

  @Property(tries = 20)
  public void getTablesEq(
      @ForAll CharClasses classes,
      @ForAll @Size(100) List<@IntRange(max = CharClasses.maxChar) Integer> inputs) {
    Pair<int[], int[]> table = classes.getTables();
    for (int input : inputs) {
      assertEquals(classes.getClassCode(input), translateFlat(table, input));
    }
  }

  @Property
  public void classCodesUnion(@ForAll CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();
    IntCharSet union = new IntCharSet();
    for (CharClassInterval i : intervals) {
      union.add(new Interval(i.start, i.end));
    }
    assertEquals(IntCharSet.allChars(), union);
  }

  @Property
  public void classCodesCode(@ForAll CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();

    for (CharClassInterval i : intervals) {
      IntCharSet set = IntCharSet.ofCharacterRange(i.start, i.end);
      IntCharSet ccl = classes.getCharClass(i.charClass);
      assertTrue(ccl.contains(set));
    }
  }

  @Property
  public void classCodesDisjointOrdered(@ForAll CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();

    for (int i = 0; i < intervals.length - 1; i++) {
      assertEquals(intervals[i + 1].start, intervals[i].end + 1);
    }
  }
}
