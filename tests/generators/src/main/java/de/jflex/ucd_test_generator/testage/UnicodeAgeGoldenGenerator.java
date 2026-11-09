/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator.testage;

import de.jflex.ucd.NamedCodepointRange;
import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersionTemplateVars;
import java.util.List;

/**
 * Generates the golden file of an age scanner, with one {@code XXXX..YYYY; label} line per block.
 */
class UnicodeAgeGoldenGenerator extends AbstractGenerator<UnicodeVersionTemplateVars> {

  private final String scannerName;
  private final List<NamedCodepointRange<String>> blocks;

  UnicodeAgeGoldenGenerator(
      UnicodeVersion unicodeVersion, String scannerName, List<NamedCodepointRange<String>> blocks) {
    super("UnicodeAgeGolden", unicodeVersion);
    this.scannerName = scannerName;
    this.blocks = blocks;
  }

  @Override
  protected UnicodeVersionTemplateVars createTemplateVars() {
    UnicodeVersionTemplateVars vars = new UnicodeVersionTemplateVars();
    vars.className = scannerName;
    return vars;
  }

  @Override
  protected String render(UnicodeVersionTemplateVars vars) {
    StringBuilder sb = new StringBuilder();
    for (NamedCodepointRange<String> block : blocks) {
      sb.append(block).append('\n');
    }
    return sb.toString();
  }

  @Override
  protected String getOuputFileName(UnicodeVersionTemplateVars vars) {
    return vars.className + ".output";
  }
}
