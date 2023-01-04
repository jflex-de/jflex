/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testage;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.base.Pair;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.version.Version;
import java.util.stream.Stream;

class UnicodeAgeSubtractionFlexGenerator
    extends AbstractGenerator<UnicodeAgeSubtractionTemplateVars> {

  protected UnicodeAgeSubtractionFlexGenerator(UnicodeVersion unicodeVersion) {
    super("UnicodeAgeSubtraction.flex", unicodeVersion);
  }

  @Override
  protected UnicodeAgeSubtractionTemplateVars createTemplateVars() {
    UnicodeAgeSubtractionTemplateVars vars = new UnicodeAgeSubtractionTemplateVars();
    vars.className =
        String.format(
            "UnicodeAge_%s_age_subtraction", unicodeVersion.version().underscoreVersion());
    ImmutableList<Version> ages = olderAges(unicodeVersion.version());
    Stream<Pair<Version>> agePairs =
        Streams.zip(ages.stream(), ages.stream().skip(1), Pair::create);
    vars.ages = agePairs.collect(ImmutableList.toImmutableList());
    return vars;
  }

  @Override
  protected String getOuputFileName(UnicodeAgeSubtractionTemplateVars vars) {
    return vars.className + ".flex";
  }
}
