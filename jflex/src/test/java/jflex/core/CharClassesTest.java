/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import jflex.chars.Interval;
import jflex.core.unicode.UnicodeProperties;
import org.junit.Test;

/**
 * Test for {@link CharClasses}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class CharClassesTest {

  @Test
  public void testAdd1() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('h', 'o'));
    assertThat(set.toString()).isEqualTo("{ ['A'-'Z']['a'-'z'] }");
  }

  @Test
  public void testAdd2() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('i', 'n'));
    assertThat(set.toString()).isEqualTo("{ ['A'-'Z']['a'-'z'] }");
  }

  @Test
  public void testAdd3() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('a', 'n'));
    assertThat(set.toString()).isEqualTo("{ ['A'-'Z']['a'-'z'] }");
  }

  @Test
  public void testMergeLast() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'k'));
    assertThat(set.toString()).isEqualTo("{ ['a'-'k'] }");
    set.add('l');
    assertThat(set.toString()).isEqualTo("{ ['a'-'l'] }");
  }

  @Test
  public void testAddChar() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add('n');
    set.add('k');
    assertThat(set.toString()).isEqualTo("{ ['a'-'h']['k']['n'-'z'] }");
    set.add('i');
    assertThat(set.toString()).isEqualTo("{ ['a'-'i']['k']['n'-'z'] }");
    set.add('j');
    assertThat(set.toString()).isEqualTo("{ ['a'-'k']['n'-'z'] }");
    set.add(new Interval('l', 'm'));
    assertThat(set.toString()).isEqualTo("{ ['a'-'z'] }");
  }

  @Test
  public void testCopy() {
    IntCharSet set = IntCharSet.of(new Interval('a', 'z'));
    IntCharSet copy = IntCharSet.copyOf(set);
    Interval i = set.getNext();
    i.end = 'h';
    assertThat(set.toString()).isEqualTo("{ ['a'-'h'] }");
    assertThat(copy.toString()).isEqualTo("{ ['a'-'z'] }");
  }

  @Test
  public void testCaseless() {
    UnicodeProperties unicodeProperties;
    try {
      unicodeProperties = new UnicodeProperties("4.0");
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      fail("Unsupported default Unicode version: " + e);
      return;
    }

    IntCharSet set = IntCharSet.of(new Interval('a', 'c'));
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
    assertThat(set.getCaseless(unicodeProperties).toString())
        .isEqualTo("{ ['A'-'C']['H'-'O']['a'-'c']['h'-'o'][304-305][8490] }");
  }
}
