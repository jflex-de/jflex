/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import jflex.core.unicode.IntCharSet;
import org.junit.Test;

/**
 * Unit tests for {@link jflex.core.RegExp}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class RegExpTest implements sym {

  @Test
  public void revString() {
    assertThat(RegExp.revString("blah")).isEqualTo("halb");
  }

  @Test
  public void testCharClass() {
    Macros m = new Macros();
    RegExp e1 = new RegExp1(PRIMCLASS, IntCharSet.ofCharacterRange('a', 'z'));
    RegExp e2 = new RegExp1(CHAR, 'Z');
    ArrayList<RegExp> l = new ArrayList<>();
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
    assertThat(e1.isCharClass()).isTrue();
    assertThat(e2.isCharClass()).isTrue();
    assertThat(b.isCharClass()).isTrue();
    assertThat(s.isCharClass()).isFalse();
    assertThat(u.isCharClass()).isTrue();
  }
}
