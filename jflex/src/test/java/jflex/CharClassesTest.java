/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.2                                                             *
 * Copyright (C) 1998-2008  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import junit.framework.TestCase;
import jflex.unicode.UnicodeProperties;

/**
 * CharClassesTest
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class CharClassesTest extends TestCase {

  /**
   * Constructor for CharClassesTest.
   * @param arg0
   */
  public CharClassesTest(String arg0) {
    super(arg0);
  }

  public void testAdd1() {
    IntCharSet set = new IntCharSet(new Interval('a','h'));
    set.add(new Interval('o','z'));
    set.add(new Interval('A','Z'));
    set.add(new Interval('h','o'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }

  public void testAdd2() {
    IntCharSet set = new IntCharSet(new Interval('a','h'));
    set.add(new Interval('o','z'));
    set.add(new Interval('A','Z'));
    set.add(new Interval('i','n'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }

  public void testAdd3() {
    IntCharSet set = new IntCharSet(new Interval('a','h'));
    set.add(new Interval('o','z'));
    set.add(new Interval('A','Z'));
    set.add(new Interval('a','n'));
    assertEquals("{ ['A'-'Z']['a'-'z'] }", set.toString());
  }
  
  public void testMergeLast() {
    IntCharSet set = new IntCharSet(new Interval('a','k'));
    assertEquals("{ ['a'-'k'] }", set.toString());
    set.add('l');
    assertEquals("{ ['a'-'l'] }", set.toString());
  }

  public void testAddChar() {
    IntCharSet set = new IntCharSet(new Interval('a','h'));
    set.add(new Interval('o','z'));
    set.add('n');
    set.add('k');
    assertEquals("{ ['a'-'h']['k']['n'-'z'] }", set.toString());
    set.add('i');
    assertEquals("{ ['a'-'i']['k']['n'-'z'] }", set.toString());    
    set.add('j');
    assertEquals("{ ['a'-'k']['n'-'z'] }", set.toString());    
    set.add(new Interval('l','m'));
    assertEquals("{ ['a'-'z'] }", set.toString());    
  }

  public void testCopy() {
    IntCharSet set = new IntCharSet(new Interval('a','z'));
    IntCharSet copy = set.copy();
    Interval i = set.getNext();
    i.end = 'h';
    assertEquals("{ ['a'-'h'] }", set.toString());
    assertEquals("{ ['a'-'z'] }", copy.toString());
  }

  public void testCaseless() {
    try {
      IntCharSet.setUnicodeProperties(new UnicodeProperties("4.0"));
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      assertTrue("Unsupported default Unicode version: " + e, false);
    }

    IntCharSet set = new IntCharSet(new Interval('a','c'));
    set.add(new Interval('h','o'));

    // From <http://unicode.org/Public/4.0-Update1/UnicodeData-4.0.1.txt>:
    //
    // 0049;LATIN CAPITAL LETTER I;Lu;0;L;;;;;N;;;;0069;
    // 0069;LATIN SMALL LETTER I;Ll;0;L;;;;;N;;;0049;;0049
    // 0130;LATIN CAPITAL LETTER I WITH DOT ABOVE;Lu;0;L;0049 0307;;;;N;LATIN CAPITAL LETTER I DOT;;;0069;
    // 0131;LATIN SMALL LETTER DOTLESS I;Ll;0;L;;;;;N;;;0049;;0049
    //
    // 006B;LATIN SMALL LETTER K;Ll;0;L;;;;;N;;;004B;;004B
    // 212A;KELVIN SIGN;Lu;0;L;004B;;;;N;DEGREES KELVIN;;;006B;
    assertEquals("{ ['A'-'C']['H'-'O']['a'-'c']['h'-'o'][304-305][8490] }",
                 set.getCaseless().toString());
  }
}
