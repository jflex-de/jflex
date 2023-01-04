/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.migration.unicodedatatest.testage;

import static de.jflex.migration.unicodedatatest.base.AbstractGenerator.olderAges;

import com.google.common.collect.ImmutableList;
import de.jflex.migration.unicodedatatest.base.UnicodeVersion;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.velocity.runtime.parser.ParseException;

class AgeTestGenerator {

  private AgeTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    generate(version, outDir);
  }

  public static void generate(UnicodeVersion unicodeVersion, Path outDir)
      throws IOException, ParseException {
    ImmutableList<Version> ages = olderAges(unicodeVersion.version());
    for (Version age : ages) {
      UnicodeAgeFlexGenerators.createForAge(unicodeVersion, age).generate(outDir);
    }
    UnicodeAgeFlexGenerators.createForUnassignedAge(unicodeVersion).generate(outDir);
    new UnicodeAgeSubtractionFlexGenerator(unicodeVersion).generate(outDir);
    new UnicodeAgeTestGenerator(unicodeVersion, ages).generate(outDir);
  }
}
