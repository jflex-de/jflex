/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import jflex.unicode.UnicodeProperties;
import junit.framework.TestCase;

/**
 * CharClassesTest
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class CharClassesTest extends TestCase {

  /**
   * Constructor for CharClassesTest.
   *
   * @param arg0
   */
  public CharClassesTest(String arg0) {
    super(arg0);
  }

  public void testAdd1() {
    IntCharSet set = new IntCharSet(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('h', 'o'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }

  public void testAdd2() {
    IntCharSet set = new IntCharSet(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('i', 'n'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }

  public void testAdd3() {
    IntCharSet set = new IntCharSet(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add(new Interval('A', 'Z'));
    set.add(new Interval('a', 'n'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }

  public void testMergeLast() {
    IntCharSet set = new IntCharSet(new Interval('a', 'k'));
    assertEquals("{ ['a'-'k'] }", set.toString());
    set.add('l');
    assertEquals("{ ['a'-'l'] }", set.toString());
  }

  public void testAddChar() {
    IntCharSet set = new IntCharSet(new Interval('a', 'h'));
    set.add(new Interval('o', 'z'));
    set.add('n');
    set.add('k');
    assertEquals("{ ['a'-'h']['k']['n'-'z'] }", set.toString());
    set.add('i');
    assertEquals("{ ['a'-'i']['k']['n'-'z'] }", set.toString());
    set.add('j');
    assertEquals("{ ['a'-'k']['n'-'z'] }", set.toString());
    set.add(new Interval('l', 'm'));
    assertEquals("{ ['a'-'z'] }", set.toString());
  }

  public void testCopy() {
    IntCharSet set = new IntCharSet(new Interval('a', 'z'));
    IntCharSet copy = set.copy();
    Interval i = set.getNext();
    i.end = 'h';
    assertEquals("{ ['a'-'h'] }", set.toString());
    assertEquals("{ ['a'-'z'] }", copy.toString());
  }

  public void testCaseless() {
    UnicodeProperties unicodeProperties;
    try {
      unicodeProperties = new UnicodeProperties("4.0");
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported default Unicode version: " + e, false);
      return;
    }

    IntCharSet set = new IntCharSet(new Interval('a', 'c'));
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
    assertEquals(
        "{ ['A'-'C']['H'-'O']['a'-'c']['h'-'o'][304-305][8490] }",
        set.getCaseless(unicodeProperties).toString());
  }
}
