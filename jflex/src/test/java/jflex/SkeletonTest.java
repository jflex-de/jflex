/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.3                                                             *
 * Copyright (C) 1998-2008  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;


import java.io.File;

import junit.framework.TestCase;

/**
 * SkeletonTest
 * 
 * @author Gerwin Klein
 * @version $Revision$, $Date$
 */
public class SkeletonTest extends TestCase {

  /**
   * Constructor for SkeletonTest.
   * @param arg0 test name
   */
  public SkeletonTest(String arg0) {
    super(arg0);
  }

  public void testReplace() {
    assertEquals(Skeleton.replace("bla ", "blub", "bla blub bla "), 
                 "blubblub blub");
  }

  public void testMakePrivate() {
    Skeleton.makePrivate(); 
    for (int i=0; i < Skeleton.line.length; i++) {
      assertEquals(Skeleton.line[i].indexOf("public"), -1);
    }
  }

  public void testDefault() {
    Skeleton.readSkelFile(new File("src/main/jflex/skeleton.nested"));
    assertTrue(jflex.Skeleton.line[3].indexOf("java.util.Stack") > 0);
    Skeleton.readDefault();
    assertEquals(jflex.Skeleton.line[3].indexOf("java.util.Stack"), -1);
  }
}
