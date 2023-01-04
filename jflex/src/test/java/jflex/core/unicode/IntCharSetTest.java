/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core.unicode;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import jflex.chars.Interval;
import org.junit.Test;

public class IntCharSetTest {

  @Test
  public void add_overlappingIntervals1() {
    IntCharSet set = new IntCharSet();
    set.add(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('h', 'o'));
    assertThat(set).isEqualTo(IntCharSet.of(new Interval('A', 'Z'), new Interval('a', 'z')));
  }

  @Test
  public void add_overlappingIntervals2() {
    IntCharSet set = new IntCharSet();
    set.add(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('i', 'n'));
    assertThat(set).isEqualTo(IntCharSet.of(new Interval('A', 'Z'), new Interval('a', 'z')));
  }

  @Test
  public void add_overlappingIntervals3() {
    IntCharSet set = new IntCharSet();
    set.add(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('a', 'n'));
    assertThat(set).isEqualTo(IntCharSet.of(new Interval('A', 'Z'), new Interval('a', 'z')));
  }

  @Test
  public void add_lastCharacter() {
    IntCharSet set = IntCharSet.ofCharacterRange('a', 'k');
    assertThat(set).isEqualTo(IntCharSet.ofCharacterRange('a', 'k'));
    set.add('l');
    assertThat(set).isEqualTo(IntCharSet.ofCharacterRange('a', 'l'));
  }

  @Test
  public void add_firstCharacter() {
    IntCharSet set = new IntCharSet();
    set.add(new Interval('o', 'z'));
    set.add('n');
    assertThat(set).isEqualTo(IntCharSet.ofCharacterRange('n', 'z'));
  }

  @Test
  public void add_disjointCharacter() {
    IntCharSet set = new IntCharSet();
    set.add(new Interval('x', 'z'));
    set.add(new Interval('a', 'c'));
    set.add('n');
    assertThat(set)
        .isEqualTo(
            IntCharSet.of(
                new Interval('a', 'c'), Interval.ofCharacter('n'), new Interval('x', 'z')));
  }

  @Test
  public void copy() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'z'));
    IntCharSet copy = IntCharSet.copyOf(set);
    Interval i = set.getIntervals().get(0);
    i.end = 'X';
    assertThat(copy).isNotEqualTo(set);
  }

  @Test
  public void getCaseless() throws Exception {
    UnicodeProperties unicodeProperties = new UnicodeProperties("4.0");

    IntCharSet set = new IntCharSet();
    set.add(new Interval('a', 'c'));
    set.add(new Interval('h', 'o'));

    // From <http://unicode.org/Public/4.0-Update1/UnicodeData-4.0.1.txt>:
    //
    // 0049;LATIN CAPITAL LETTER I;Lu;0;L;;;;;N;;;;0069;
    // 0069;LATIN SMALL LETTER I;Ll;0;L;;;;;N;;;0049;;0049
    // 0130;LATIN CAPITAL LETTER I WITH DOT ABOVE;Lu;0;L;0049 0307;;;;N;LATIN CAPITAL LETTER I
    // DOT;;;0069;
    // 0131;LATIN SMALL LETTER DOTLESS I;Ll;0;L;;;;;N;;;0049;;0049
    //
    // 006B;LATIN SMALL LETTER K;Ll;0;L;;;;;N;;;004B;;004B
    // 212A;KELVIN SIGN;Lu;0;L;004B;;;;N;DEGREES KELVIN;;;006B;
    assertWithMessage(
            "The caseless version should be ['A'-'C']['H'-'O']['a'-'c']['h'-'o'][304-305][8490]")
        .that(set.getCaseless(unicodeProperties))
        .isEqualTo(
            IntCharSet.of(
                new Interval('A', 'C'),
                new Interval('H', 'O'),
                new Interval('a', 'c'),
                new Interval('h', 'o'),
                new Interval(0x130, 0x131),
                Interval.ofCharacter(0x212A)));
  }

  @Test
  public void testToString() {
    IntCharSet set =
        IntCharSet.of(
            new Interval('A', 'C'),
            new Interval('H', 'O'),
            new Interval('a', 'c'),
            new Interval('h', 'o'),
            new Interval('İ', 'ı'), // http://www.fileformat.info/info/unicode/char/130/
            Interval.ofCharacter('K') // http://www.fileformat.info/info/unicode/char/212A/
            );
    assertThat(set.toString()).isEqualTo("{ ['A'-'C']['H'-'O']['a'-'c']['h'-'o'][304-305][8490] }");
  }

  @Test
  public void addIntCharSet() {
    IntCharSet set = IntCharSet.ofCharacterRange(0, 10);
    set.add(IntCharSet.of(new Interval(11, 20), new Interval(41, 42)));
    assertThat(set).isEqualTo(IntCharSet.of(new Interval(0, 20), new Interval(41, 42)));
  }

  @Test
  public void sub() {
    IntCharSet a = IntCharSet.of(new Interval(1, 7), new Interval(10, 42));
    a.sub(IntCharSet.ofCharacterRange(4, 7));
    a.sub(IntCharSet.ofCharacterRange(10, 41));
    assertThat(a).isEqualTo(IntCharSet.of(new Interval(1, 3), Interval.ofCharacter(42)));
  }

  @Test
  public void contains() {
    IntCharSet a = IntCharSet.of(new Interval(3, 7), new Interval(10, 15));
    IntCharSet b = IntCharSet.ofCharacterRange(4, 6);
    IntCharSet c = IntCharSet.ofCharacterRange(1, 5);
    IntCharSet d = IntCharSet.ofCharacterRange(1, 20);
    IntCharSet e = IntCharSet.of(new Interval(4, 6), new Interval(10, 15));

    assertThat(a.contains(b)).isTrue();
    assertThat(a.contains(e)).isTrue();
    assertThat(a.contains(c)).isFalse();
    assertThat(a.contains(d)).isFalse();
  }
}
