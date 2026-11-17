/*
 * Copyright 2018, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.cup;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import org.junit.jupiter.api.Test;

/** Test for {@link jflex.maven.plugin.cup.CliCupInvoker}. */
public class CliCupInvokerTest {

  @Test
  public void buildArgv() throws Exception {
    assertArrayEquals(
        new String[] {
          "-package",
          "foo.bar",
          "-destdir",
          "/outputDirectory",
          "-parser",
          "Parser",
          "-symbols",
          "Symbols",
          "-interface",
          "/a/b/c/def.cup"
        },
        CliCupInvoker.buildArgv(
            "foo.bar",
            new File("/outputDirectory"),
            "Parser",
            "Symbols", /* interface */
            true,
            "/a/b/c/def.cup"));
  }
}
