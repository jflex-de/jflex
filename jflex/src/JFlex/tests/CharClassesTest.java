/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4                                                               *
 * Copyright (C) 1998-2003  Gerwin Klein <lsf@jflex.de>                    *
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

package JFlex.tests;

import JFlex.IntCharSet;
import JFlex.Interval;
import junit.framework.TestCase;

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

  /*
   * Test for void makeClass(IntCharSet)
   */
  public void testMakeClassIntCharSet() {
  }

  public void testGetClassCode() {
  }

  /*
   * Test for void makeClass(char)
   */
  public void testMakeClasschar() {
  }

  /*
   * Test for void makeClass(String)
   */
  public void testMakeClassString() {
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
    IntCharSet set = new IntCharSet(new Interval('a','c'));
    set.add(new Interval('h','o'));
    assertEquals("{ ['A'-'C']['H'-'O']['a'-'c']['h'-'o'] }", 
                 set.getCaseless().toString());
  }
}
