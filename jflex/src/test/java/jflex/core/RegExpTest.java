/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jflex.chars.Interval;
import jflex.core.unicode.CharClasses;
import jflex.core.unicode.IntCharSet;
import jflex.core.unicode.UnicodeProperties;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link jflex.core.RegExp}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class RegExpTest {

  @Test
  public void revString() {
    assertEquals("halb", RegExp.revString("blah"));
  }

  @Test
  public void testCharClass() {
    Macros m = new Macros();
    RegExp e1 = new RegExp1(sym.PRIMCLASS, IntCharSet.ofCharacterRange('a', 'z'));
    RegExp e2 = new RegExp1(sym.CHAR, 'Z');
    ArrayList<RegExp> l = new ArrayList<>();
    l.add(new RegExp1(sym.PRIMCLASS, IntCharSet.ofCharacterRange('0', '8')));
    l.add(new RegExp1(sym.PRIMCLASS, IntCharSet.ofCharacter('9')));
    RegExp e3 = new RegExp1(sym.CCLASS, l);
    m.insert("macro", e3);
    RegExp s = new RegExp1(sym.STAR, e1);
    RegExp u = new RegExp1(sym.MACROUSE, "macro");
    RegExp b = new RegExp2(sym.BAR, e2, u);
    e1 = e1.normaliseMacros(m);
    e2 = e2.normaliseMacros(m);
    b = b.normaliseMacros(m);
    s = s.normaliseMacros(m);
    u = u.normaliseMacros(m);
    e1 = e1.normaliseCCLs(null, 0);
    e2 = e2.normaliseCCLs(null, 0);
    b = b.normaliseCCLs(null, 0);
    s = s.normaliseCCLs(null, 0);
    u = u.normaliseCCLs(null, 0);
    assertTrue(e1.isCharClass());
    assertTrue(e2.isCharClass());
    assertTrue(b.isCharClass());
    assertFalse(s.isCharClass());
    assertTrue(u.isCharClass());
  }

  // --- caseless character classes (%ignorecase with --jlex)

  private static final int SMALL_DELTA = 0x03B4;
  private static final int CAPITAL_DELTA = 0x0394;

  /** The class with the single character {@code c}. */
  private static RegExp primClass(int c) {
    return new RegExp1(sym.PRIMCLASS, IntCharSet.ofCharacter(c));
  }

  /** The negated class {@code [^...]} of the content. */
  private static RegExp negated(RegExp content) {
    List<RegExp> l = new ArrayList<>();
    l.add(content);
    return new RegExp1(sym.CCLASSNOT, l);
  }

  /** The characters of the class expression {@code r} in a caseless spec. */
  private static IntCharSet caselessClass(RegExp r, String unicodeVersion, boolean jlex)
      throws Exception {
    UnicodeProperties props = new UnicodeProperties(unicodeVersion);
    CharClasses charClasses = new CharClasses(props.getMaximumCodePoint(), props);
    RegExp n = r.expandPreClasses(new HashMap<>(), charClasses, true, jlex).normaliseCCLs(null, 0);
    assertEquals(sym.PRIMCLASS, n.type);
    return (IntCharSet) ((RegExp1) n).content;
  }

  /** {@code [^δ]} in JLex mode is the negation of the caseless {@code [δ]}. */
  @Test
  public void caselessClass_jlex_negation() throws Exception {
    IntCharSet set = caselessClass(negated(primClass(SMALL_DELTA)), "12.1", true);
    assertFalse(set.contains(SMALL_DELTA));
    assertFalse(set.contains(CAPITAL_DELTA));
    assertTrue(set.contains('d'));
    assertTrue(set.contains('D'));
  }

  /** Character classes are not caseless without JLex mode. */
  @Test
  public void caselessClass_jflex_negation() throws Exception {
    IntCharSet set = caselessClass(negated(primClass(SMALL_DELTA)), "12.1", false);
    assertFalse(set.contains(SMALL_DELTA));
    assertTrue(set.contains(CAPITAL_DELTA));
  }

  /** {@code [a-c--[B]]} in JLex mode is the difference of the caseless classes. */
  @Test
  public void caselessClass_jlex_difference() throws Exception {
    RegExp range = new RegExp1(sym.PRIMCLASS, IntCharSet.ofCharacterRange('a', 'c'));
    RegExp difference =
        new RegExp1(sym.CCLASSOP, new RegExp2(sym.DIFFERENCE, range, primClass('B')));
    IntCharSet set = caselessClass(difference, "12.1", true);
    assertEquals(
        IntCharSet.of(
            Interval.ofCharacter('A'),
            Interval.ofCharacter('C'),
            Interval.ofCharacter('a'),
            Interval.ofCharacter('c')),
        set);
  }

  /**
   * {@code \W} in JLex mode is the negation of the caseless {@code \w}.
   *
   * <p>In Unicode 3.1, U+24D0 CIRCLED LATIN SMALL LETTER A is a word character, and its upper case
   * U+24B6 is not.
   */
  @Test
  public void caselessClass_jlex_negatedPreClass() throws Exception {
    int circledSmallA = 0x24D0;
    int circledCapitalA = 0x24B6;
    RegExp word = new RegExp1(sym.PRECLASS, sym.WORDCLASS);
    RegExp notWord = new RegExp1(sym.PRECLASS, sym.WORDCLASSNOT);

    IntCharSet wordSet = caselessClass(word, "3.1", true);
    assertTrue(wordSet.contains(circledSmallA));
    assertTrue(wordSet.contains(circledCapitalA));

    IntCharSet notWordSet = caselessClass(notWord, "3.1", true);
    assertFalse(notWordSet.contains(circledSmallA));
    assertFalse(notWordSet.contains(circledCapitalA));
    assertTrue(notWordSet.contains('#'));
    assertFalse(notWordSet.and(wordSet).containsElements());

    // without JLex mode, only the upper case is in \W
    IntCharSet jflexNotWordSet = caselessClass(notWord, "3.1", false);
    assertFalse(jflexNotWordSet.contains(circledSmallA));
    assertTrue(jflexNotWordSet.contains(circledCapitalA));
  }
}
