/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import jflex.core.unicode.IntCharSet;
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
}
