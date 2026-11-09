/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.ucd_test_generator.testage;

import static de.jflex.ucd_test_generator.base.AbstractGenerator.olderAges;

import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd_test_generator.base.UnicodeVersion;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Generates the age tests of a Unicode version: the scanner specs, their golden files and the test
 * class.
 *
 * <p>Arguments: the Unicode version, the output directory, and the UCD files of the version.
 */
public class AgeTestGenerator {

  private AgeTestGenerator() {}

  public static void main(String[] args) throws Exception {
    UnicodeVersion version = UnicodeVersion.create(args[0]);
    Path outDir = Paths.get(args[1]);
    UcdVersion ucd =
        UcdVersion.findUcdFiles(version.version(), Arrays.asList(args).subList(2, args.length));
    generate(version, ucd.getFile(UcdFileType.DerivedAge).toPath(), outDir);
  }

  public static void generate(UnicodeVersion unicodeVersion, Path derivedAgeTxt, Path outDir)
      throws IOException {
    List<Version> ages = olderAges(unicodeVersion.version());
    UnicodeAgeGoldens goldens =
        UnicodeAgeGoldens.create(
            unicodeVersion.version(),
            ages,
            UnicodeAgeTestGenerator.getDataset(unicodeVersion.version()),
            derivedAgeTxt);
    for (Version age : ages) {
      UnicodeAgeFlexGenerators.createForAge(unicodeVersion, age).generate(outDir);
      new UnicodeAgeGoldenGenerator(
              unicodeVersion,
              UnicodeAgeFlexGenerators.className(unicodeVersion, age.underscoreVersion()),
              goldens.age(age))
          .generate(outDir);
    }
    UnicodeAgeFlexGenerators.createForUnassignedAge(unicodeVersion).generate(outDir);
    new UnicodeAgeGoldenGenerator(
            unicodeVersion,
            UnicodeAgeFlexGenerators.className(unicodeVersion, "unassigned"),
            goldens.unassigned())
        .generate(outDir);
    new UnicodeAgeSubtractionFlexGenerator(unicodeVersion).generate(outDir);
    new UnicodeAgeGoldenGenerator(
            unicodeVersion,
            UnicodeAgeFlexGenerators.className(unicodeVersion, "subtraction"),
            goldens.subtraction())
        .generate(outDir);
    new UnicodeAgeTestGenerator(unicodeVersion, ages).generate(outDir);
  }
}
