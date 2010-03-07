/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.5                                                               *
 * Copyright (C) 1998-2004  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

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
    StringBuilder usercode = new StringBuilder("/* some *** comment */");
    assertTrue(!Emitter.endsWithJavadoc(usercode));
    usercode.append("import bla;  /** javadoc /* */  ");
    assertTrue(Emitter.endsWithJavadoc(usercode));
    usercode.append("bla");
    assertTrue(!Emitter.endsWithJavadoc(usercode));
  }
}
