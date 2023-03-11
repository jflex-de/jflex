/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * PackEmitterTest
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class PackEmitterTest {

  private static final String NL = "\n";
  private PackEmitter p;

  @Before
  public void setUp() {
    p =
        new PackEmitter("Bla") {
          @Override
          public void emitUnpack() {}
        };
  }

  @Test
  public void testInit() {
    p.emitInit();
    assertThat(p.toString())
        .isEqualTo(
            "  private static final int [] ZZ_BLA = zzUnpackBla();"
                + NL
                + NL
                + "  private static final String ZZ_BLA_PACKED_0 ="
                + NL
                + "    \"");
  }

  @Test
  public void testEmitUCplain() {
    p.emitUC(8);
    p.emitUC(0xFF00);

    assertThat(p.toString()).isEqualTo("\\10\\uff00");
  }

  @Test
  public void testLineBreak() {
    for (int i = 0; i < 36; i++) {
      p.breaks();
      p.emitUC(i);
    }
    assertThat(p.toString())
        .isEqualTo(
            "\\0\\1\\2\\3\\4\\5\\6\\7\\10\\11\\12\\13\\14\\15\\16\\17\"+"
                + NL
                + "    \"\\20\\21\\22\\23\\24\\25\\26\\27\\30\\31\\32\\33\\34\\35\\36\\37\"+"
                + NL
                + "    \"\\40\\41\\42\\43");
  }
}
