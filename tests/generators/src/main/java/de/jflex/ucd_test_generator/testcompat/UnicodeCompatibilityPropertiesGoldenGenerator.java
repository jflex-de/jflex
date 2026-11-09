/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testcompat;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd.Versions;
import de.jflex.ucd_generator.ucd.UnicodeData;
import de.jflex.ucd_test_generator.base.AbstractRangesGoldenGenerator;
import de.jflex.ucd_test_generator.base.UnicodeRangesGoldenTemplateVars;
import de.jflex.ucd_test_generator.base.UnicodeVersion;

public class UnicodeCompatibilityPropertiesGoldenGenerator extends AbstractRangesGoldenGenerator {

  private final UnicodeData unicodeData;
  private final String propName;

  protected UnicodeCompatibilityPropertiesGoldenGenerator(
      UnicodeVersion unicodeVersion, UnicodeData unicodeData, String propName) {
    super(unicodeVersion);
    this.unicodeData = unicodeData;
    this.propName = propName;
  }

  @Override
  protected UnicodeRangesGoldenTemplateVars createTemplateVars() {
    UnicodeRangesGoldenTemplateVars vars = new UnicodeRangesGoldenTemplateVars();
    vars.className =
        "UnicodeCompatibilityProperties_" + propName + "_" + unicodeVersion.underscoreVersion();
    vars.ranges = unicodeData.getPropertyValueIntervals(propName);
    if (unicodeVersion.version().equals(Versions.VERSION_1_1)) {
      vars.ranges = joinContiguousIntervals(vars.ranges);
    }
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeRangesGoldenTemplateVars vars) {
    return vars.className + ".output";
  }

  private static ImmutableList<CodepointRange> joinContiguousIntervals(
      ImmutableList<CodepointRange> ranges) {
    ImmutableList.Builder<CodepointRange> builder = new ImmutableList.Builder<>();
    CodepointRange prev = ranges.get(0);
    for (int i = 1; i < ranges.size(); i++) {
      CodepointRange r = ranges.get(i);
      if (prev.end() + 1 == r.start()) {
        prev = CodepointRange.create(prev.start(), r.end());
      } else {
        builder.add(prev);
        prev = r;
      }
    }
    builder.add(ranges.get(ranges.size() - 1));
    return builder.build();
  }
}
