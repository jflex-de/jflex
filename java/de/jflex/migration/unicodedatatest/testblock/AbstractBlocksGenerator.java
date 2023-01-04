/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testblock;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.migration.unicodedatatest.base.UnicodeVersionTemplateVars;
import de.jflex.ucd.NamedCodepointRange;

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
