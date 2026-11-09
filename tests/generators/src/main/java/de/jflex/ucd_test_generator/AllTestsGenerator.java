/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_test_generator;

import de.jflex.ucd.UcdReleases;
import de.jflex.ucd.Versions;
import de.jflex.ucd_test_generator.base.AbstractGenerator;
import de.jflex.ucd_test_generator.testage.AgeTestGenerator;
import de.jflex.ucd_test_generator.testblock.BlocksTestGenerator;
import de.jflex.ucd_test_generator.testcaseless.CaselessTestGenerator;
import de.jflex.ucd_test_generator.testcompat.CompatPropertiesTestGenerator;
import de.jflex.ucd_test_generator.testderivedcoreprop.DerivedCorePropertiesTestGenerator;
import de.jflex.ucd_test_generator.testdigit.DigitTestGenerator;
import de.jflex.ucd_test_generator.testemoji.EmojiTestGenerator;
import de.jflex.version.Version;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Runs the test generators for all Unicode versions.
 *
 * <p>Arguments: the directory with the UCD downloads of {@code tests/unicode/pom.xml}, and the
 * output directory.
 */
public final class AllTestsGenerator {

  public static void main(String[] args) throws Exception {
    Path ucdDir = Paths.get(args[0]);
    String outDir = args[1];
    for (Version version : AbstractGenerator.KNOWN_VERSIONS) {
      List<String> argv = new ArrayList<>(Arrays.asList(version.toString(), outDir));
      argv.addAll(UcdReleases.ucdFiles(ucdDir, version));
      generate(version, argv.toArray(new String[0]));
    }
  }

  private static void generate(Version version, String[] args) throws Exception {
    CaselessTestGenerator.main(args);
    CompatPropertiesTestGenerator.main(args);
    DigitTestGenerator.main(args);
    if (isAtLeast(version, Versions.VERSION_2_0)) {
      AgeTestGenerator.main(args);
      BlocksTestGenerator.main(args);
    }
    if (isAtLeast(version, Versions.VERSION_3_1)) {
      DerivedCorePropertiesTestGenerator.main(args);
    }
    if (isAtLeast(version, Versions.VERSION_8_0)) {
      EmojiTestGenerator.main(args);
    }
  }

  private static boolean isAtLeast(Version version, Version minimum) {
    return Version.MAJOR_MINOR_COMPARATOR.compare(version, minimum) >= 0;
  }

  private AllTestsGenerator() {}
}
