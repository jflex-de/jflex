/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testblock;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;

abstract class AbstractBlocksGenerator<T extends UnicodeVersionTemplateVars, U>
    extends AbstractGenerator<T> {

  protected final ImmutableList<NamedCodepointRange<U>> blocks;

  public AbstractBlocksGenerator(
      String templateName,
      UnicodeVersion unicodeVersion,
      ImmutableList<NamedCodepointRange<U>> blocks) {
    super(templateName, unicodeVersion);
    this.blocks = ImmutableList.copyOf(blocks);
  }
}
