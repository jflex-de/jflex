/*
 * JFlex Maven3 plugin
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>
 * SPDX-License-Identifier: BSD-3-Clause
 */
package jflex.maven.plugin.jflex;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import org.junit.Test;

public class SpecInfoTest {
  @Test
  public void testGetOutputFilename() {
    SpecInfo clazz = new SpecInfo("Bar", "org.foo");
    assertThat(new File(clazz.getOutputFilename())).isEqualTo(new File("org/foo/Bar.java"));
  }

  @Test
  public void testGetOutputFilename_defaultPackage() {
    SpecInfo clazz = new SpecInfo("Bar", null);
    assertThat(new File(clazz.getOutputFilename())).isEqualTo(new File("Bar.java"));
  }
}
