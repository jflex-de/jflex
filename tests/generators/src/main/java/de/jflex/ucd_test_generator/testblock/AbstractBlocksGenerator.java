/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testblock;

import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class AbstractBlocksGenerator<T extends UnicodeVersionTemplateVars, U>
    extends AbstractGenerator<T> {

  protected final List<NamedCodepointRange<U>> blocks;

  public AbstractBlocksGenerator(
      String templateName, UnicodeVersion unicodeVersion, List<NamedCodepointRange<U>> blocks) {
    super(templateName, unicodeVersion);
    this.blocks = Collections.unmodifiableList(new ArrayList<>(blocks));
  }
}
