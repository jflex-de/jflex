/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import org.junit.Test;

public class ClassInfoTest {
  @Test
  public void testGetOutputFilename() {
    ClassInfo clazz = new ClassInfo();
    clazz.className = "Bar";
    clazz.packageName = "org.foo";
    assertThat(new File(clazz.getOutputFilename())).isEqualTo(new File("org/foo/Bar.java"));
  }
}
