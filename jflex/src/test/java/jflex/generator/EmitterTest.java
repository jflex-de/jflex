/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import jflex.option.Options;
import org.junit.Test;

/**
 * Some unit tests for the jflex Emitter class
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class EmitterTest {
  @Test
  public void testSourceFileString() {
    Options.resetRootDirectory();
    String bad = "something/or_other\\filename\\nFILE_NAMES_MUST_BE_ESCAPED\\u000A.flex";
    if (File.separatorChar == '\\') {
      assertThat(Emitter.sourceFileString(new File(bad)))
          .isEqualTo("something/or_other/filename/nFILE_NAMES_MUST_BE_ESCAPED/u000A.flex");
    } else {
      assertThat(Emitter.sourceFileString(new File(bad)))
          .isEqualTo("something/or_other\\\\filename\\\\nFILE_NAMES_MUST_BE_ESCAPED\\\\u000A.flex");
    }
  }
}
