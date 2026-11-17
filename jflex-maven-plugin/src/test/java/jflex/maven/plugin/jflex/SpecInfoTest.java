/*
 * JFlex Maven3 plugin
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.maven.plugin.jflex;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import org.junit.jupiter.api.Test;

public class SpecInfoTest {
  @Test
  public void testGetOutputFilename() {
    SpecInfo clazz = new SpecInfo("Bar", "org.foo");
    assertEquals(new File("org/foo/Bar.java"), new File(clazz.getOutputFilename()));
  }

  @Test
  public void testGetOutputFilename_defaultPackage() {
    SpecInfo clazz = new SpecInfo("Bar", null);
    assertEquals(new File("Bar.java"), new File(clazz.getOutputFilename()));
  }
}
