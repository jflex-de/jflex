/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static jflex.core.RegExp.revString;

import java.util.ArrayList;
import junit.framework.TestCase;

/**
 * Unit tests for JFlex.RegExp
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class RegExpTests extends TestCase implements sym {

  /**
   * Constructor for RegExpTests.
   *
   * @param name the test name
   */
  public RegExpTests(String name) {
    super(name);
  }

  public void testrevString() {
    assertEquals("halb", revString("blah"));
  }

  public void testCharClass() {
    Macros m = new Macros();
    RegExp e1 = new RegExp1(PRIMCLASS, IntCharSet.ofCharacterRange('a', 'z'));
    RegExp e2 = new RegExp1(CHAR, 'Z');
    ArrayList<RegExp> l = new ArrayList<RegExp>();
    l.add(new RegExp1(PRIMCLASS, IntCharSet.ofCharacterRange('0', '8')));
    l.add(new RegExp1(PRIMCLASS, IntCharSet.ofCharacter('9')));
    RegExp e3 = new RegExp1(CCLASS, l);
    m.insert("macro", e3);
    RegExp s = new RegExp1(STAR, e1);
    RegExp u = new RegExp1(MACROUSE, "macro");
    RegExp b = new RegExp2(BAR, e2, u);
    e1 = e1.normalise(m);
    e2 = e2.normalise(m);
    b = b.normalise(m);
    s = s.normalise(m);
    u = u.normalise(m);
    assertTrue(e1.isCharClass());
    assertTrue(e2.isCharClass());
    assertTrue(b.isCharClass());
    assertFalse(s.isCharClass());
    assertTrue(u.isCharClass());
  }
}
