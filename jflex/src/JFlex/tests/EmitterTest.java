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

import JFlex.Emitter;
import junit.framework.TestCase;

/**
 * Some unit tests for the jflex Emitter class
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class EmitterTest extends TestCase {

  /**
   * Constructor for EmitterTest.
   * @param name  the test name
   */
  public EmitterTest(String name) {
    super(name);
  }

  public void testJavadoc() {
    StringBuffer usercode = new StringBuffer("/* some *** comment */");
    assertFalse(Emitter.endsWithJavadoc(usercode));
    usercode.append("import bla;  /** javadoc /* */  ");
    assertTrue(Emitter.endsWithJavadoc(usercode));
    usercode.append("bla");
    assertFalse(Emitter.endsWithJavadoc(usercode));
  }
}
