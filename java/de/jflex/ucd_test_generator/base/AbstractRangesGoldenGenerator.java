/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.base;

import de.jflex.ucd.CodepointRange;

/** Generates a golden file with one {@code XXXX..YYYY} line per range in {@code vars.ranges}. */
public abstract class AbstractRangesGoldenGenerator
    extends AbstractGenerator<UnicodeRangesGoldenTemplateVars> {

  protected AbstractRangesGoldenGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeRangesGolden", unicodeVersion);
  }

  @Override
  protected String render(UnicodeRangesGoldenTemplateVars vars) {
    StringBuilder sb = new StringBuilder();
    for (CodepointRange range : vars.ranges) {
      sb.append(String.format("%04X..%04X\n", range.start(), range.end()));
    }
    return sb.toString();
  }
}
