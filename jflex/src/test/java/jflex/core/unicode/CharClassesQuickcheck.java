/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.9.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assume.assumeTrue;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.ArrayList;
import java.util.List;
import jflex.base.Pair;
import jflex.chars.Interval;
import org.junit.runner.RunWith;

/**
 * Property-based tests for {@link CharClasses}
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.0-SNAPSHOT
 * @see IntCharSet
 */
@RunWith(JUnitQuickcheck.class)
public class CharClassesQuickcheck {
  UnicodeProperties unicodeProperties;

  // TODO(lsf): add testing for caseless; needs UnicodeProperties

  @Property
  public void invariants(CharClasses c) {
    assertThat(c.invariants()).isTrue();
  }

  @Property
  public void maxCharCode(CharClasses c) {
    // currently only testing with maxChar
    assertThat(c.getMaxCharCode()).isEqualTo(CharClasses.maxChar);
  }

  @Property
  public void addSingle(
      CharClasses classes,
      @From(IntCharGen.class) int c1,
      @From(IntCharGen.class) int c2,
      boolean caseless) throws UnicodeProperties.UnsupportedUnicodeVersionException {

    assumeTrue(c1 != c2);

    if (caseless) {
      classesInit(classes);
    }

    classes.makeClass(c1, caseless);
    assertThat(classes.invariants()).isTrue();
    assertThat(classes.getClassCode(c1)).isNotEqualTo(classes.getClassCode(c2));
  }

  @Property
  public void addSingleSingleton(
      CharClasses classes, @From(IntCharGen.class) int c) {
    classes.makeClass(c, false);
    IntCharSet set = classes.getCharClass(classes.getClassCode(c));
    assertThat(set).isEqualTo(IntCharSet.ofCharacter(c));
  }

  @Property
  public void addSet(
      CharClasses classes,
      @InRange(maxInt = CharClasses.maxChar) IntCharSet set,
      @From(IntCharGen.class) int c,
      boolean caseless) throws UnicodeProperties.UnsupportedUnicodeVersionException {

    assumeTrue(!set.contains(c));

    if (caseless) {
      classesInit(classes);
    }

    classes.makeClass(set, caseless);
    assertThat(classes.invariants()).isTrue();

    int[] classCodes = classes.getClassCodes(set, false);
    int cCode = classes.getClassCode(c);
    for (int i : classCodes) {
      assertThat(i).isNotEqualTo(cCode);
    }
  }

  @Property
  public void addSetParts(
      CharClasses classes, @InRange(maxInt = CharClasses.maxChar) IntCharSet set, boolean caseless) throws UnicodeProperties.UnsupportedUnicodeVersionException {

    if (caseless) {
      classesInit(classes);
      set = set.getCaseless(unicodeProperties);
    }

    classes.makeClass(set, caseless);

    int[] classCodes = classes.getClassCodes(set, false);
    IntCharSet allParts = new IntCharSet();
    for (int i : classCodes) {
      allParts.add(classes.getCharClass(i));
    }
    assertThat(allParts).isEqualTo(set);
  }

  @Property
  public void addSetComplement(
      CharClasses classes, @InRange(maxInt = CharClasses.maxChar) IntCharSet set, boolean caseless) throws UnicodeProperties.UnsupportedUnicodeVersionException {

    if (caseless) {
      classesInit(classes);
      set = set.getCaseless(unicodeProperties);
    }

    classes.makeClass(set, caseless);

    int[] notCodes = classes.getClassCodes(set, true);
    IntCharSet others = new IntCharSet();
    for (int i : notCodes) {
      others.add(classes.getCharClass(i));
    }
    assertThat(others).isEqualTo(IntCharSet.complementOf(set));
  }

  @Property
  public void addString(
      CharClasses classes, String s, @From(IntCharGen.class) int c, boolean caseless) throws UnicodeProperties.UnsupportedUnicodeVersionException {

    assumeTrue(s.indexOf(c) < 0);

    if (caseless) {
      classesInit(classes);
    }

    classes.makeClass(s, caseless);
    assertThat(classes.invariants()).isTrue();

    int cCode = classes.getClassCode(c);
    for (int i = 0; i < s.length(); ) {
      int ch = s.codePointAt(i);
      assertThat(classes.getClassCode(ch)).isNotEqualTo(cCode);
      i += Character.charCount(ch);
    }
  }

  @Property
  public void normaliseSingle(
      CharClasses classes, @InRange(minInt = 0, maxInt = CharClasses.maxChar) int c) {
    CharClasses preClasses = CharClasses.copyOf(classes);

    classes.normalise();
    assertThat(classes.invariants()).isTrue();

    IntCharSet classNew = classes.getCharClass(classes.getClassCode(c));
    IntCharSet classOld = preClasses.getCharClass(preClasses.getClassCode(c));
    assertThat(classNew).isEqualTo(classOld);
  }

  private static int translateBlocks(Pair<int[], List<CMapBlock>> table, int input) {
    int top = table.fst[input >> CMapBlock.BLOCK_BITS];
    int offset = input & (CMapBlock.BLOCK_SIZE - 1);
    return table.snd.get(top).block[offset];
  }

  @Property(trials = 20)
  public void computeTablesEq(
      CharClasses classes,
      @Size(min = 100, max = 100)
          ArrayList<@InRange(minInt = 0, maxInt = CharClasses.maxChar) Integer> inputs) {
    Pair<int[], List<CMapBlock>> table = classes.computeTables();
    for (int input : inputs) {
      assertThat(translateBlocks(table, input)).isEqualTo(classes.getClassCode(input));
    }
  }

  private static int translateFlat(Pair<int[], int[]> table, int input) {
    int top = table.fst[input >> CMapBlock.BLOCK_BITS];
    int offset = input & (CMapBlock.BLOCK_SIZE - 1);
    return offset == input ? table.snd[offset] : table.snd[top | offset];
  }

  @Property(trials = 20)
  public void getTablesEq(
      CharClasses classes,
      @Size(min = 100, max = 100)
          ArrayList<@InRange(minInt = 0, maxInt = CharClasses.maxChar) Integer> inputs) {
    Pair<int[], int[]> table = classes.getTables();
    for (int input : inputs) {
      assertThat(translateFlat(table, input)).isEqualTo(classes.getClassCode(input));
    }
  }

  @Property
  public void classCodesUnion(CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();
    IntCharSet union = new IntCharSet();
    for (CharClassInterval i : intervals) {
      union.add(new Interval(i.start, i.end));
    }
    assertThat(union).isEqualTo(IntCharSet.allChars());
  }

  @Property
  public void classCodesCode(CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();

    for (CharClassInterval i : intervals) {
      IntCharSet set = IntCharSet.ofCharacterRange(i.start, i.end);
      IntCharSet ccl = classes.getCharClass(i.charClass);
      assertThat(ccl.contains(set)).isTrue();
    }
  }

  @Property
  public void classCodesDisjointOrdered(CharClasses classes) {
    CharClassInterval[] intervals = classes.getIntervals();

    for (int i = 0; i < intervals.length - 1; i++) {
      assertThat(intervals[i].end + 1).isEqualTo(intervals[i + 1].start);
    }
  }

  private void classesInit(CharClasses classes) throws UnicodeProperties.UnsupportedUnicodeVersionException {
    // init classes
    unicodeProperties = new UnicodeProperties();
    classes.init(CharClasses.maxChar, new ILexScan() {
      @Override
      public UnicodeProperties getUnicodeProperties() {
        return unicodeProperties;
      }
    });
  }
}
