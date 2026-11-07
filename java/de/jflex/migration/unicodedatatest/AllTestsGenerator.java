/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest;

import de.jflex.migration.unicodedatatest.base.AbstractGenerator;
import de.jflex.migration.unicodedatatest.testage.AgeTestGenerator;
import de.jflex.migration.unicodedatatest.testblock.BlocksTestGenerator;
import de.jflex.migration.unicodedatatest.testcaseless.CaselessTestGenerator;
import de.jflex.migration.unicodedatatest.testcompat.CompatPropertiesTestGenerator;
import de.jflex.migration.unicodedatatest.testderivedcoreprop.DerivedCorePropertiesTestGenerator;
import de.jflex.migration.unicodedatatest.testdigit.DigitTestGenerator;
import de.jflex.migration.unicodedatatest.testemoji.EmojiTestGenerator;
import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.Versions;
import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Runs the test generators for all Unicode versions.
 *
 * <p>Arguments: the directory with the UCD downloads of {@code javatests/pom.xml}, and the output
 * directory.
 */
public final class AllTestsGenerator {

  /** The files that {@link de.jflex.ucd.UcdVersion} takes from a release. */
  private static final Pattern UCD_FILE =
      Pattern.compile(
          Arrays.stream(UcdFileType.values())
              .map(UcdFileType::name)
              .collect(Collectors.joining("|", "(", "|emoji-data)(-[0-9.]+)?\\.txt")));

  public static void main(String[] args) throws Exception {
    Path ucdDir = Paths.get(args[0]);
    String outDir = args[1];
    for (Version version : AbstractGenerator.KNOWN_VERSIONS) {
      List<String> argv = new ArrayList<>(Arrays.asList(version.toString(), outDir));
      for (String release : releases(version)) {
        argv.addAll(ucdFiles(ucdDir.resolve(release)));
      }
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

  private static List<String> ucdFiles(Path release) throws IOException {
    try (Stream<Path> files = Files.walk(release)) {
      return files
          .filter(file -> UCD_FILE.matcher(file.getFileName().toString()).matches())
          .map(Path::toString)
          .collect(Collectors.toList());
    }
  }

  /** Returns the directories in the UCD download directory that make up a Unicode version. */
  private static List<String> releases(Version version) {
    switch (version.toString()) {
      case "1.1":
        return Arrays.asList("ucd_1_1_5");
      case "2.0":
        return Arrays.asList("ucd_2_0_14", "ucd_derived_age");
      case "2.1":
        return Arrays.asList("ucd_2_1_9", "ucd_derived_age");
      case "3.0":
        return Arrays.asList("ucd_3_0_0", "ucd_3_0_1", "ucd_derived_age");
      case "3.1":
        return Arrays.asList("ucd_3_1_0", "ucd_3_1_1", "ucd_derived_age");
      case "3.2":
        return Arrays.asList("ucd_3_2_0");
      case "4.0":
        return Arrays.asList("ucd_4_0_1");
      case "4.1":
        return Arrays.asList("ucd_4_1_0");
      case "8.0":
        return Arrays.asList("ucd_8_0", "emoji_2");
      case "9.0":
        return Arrays.asList("ucd_9_0", "emoji_4");
      case "10.0":
        return Arrays.asList("ucd_10_0", "emoji_5");
      case "11.0":
        return Arrays.asList("ucd_11_0", "emoji_11_0");
      case "12.0":
        return Arrays.asList("ucd_12_0", "emoji_12_0");
      case "12.1":
        return Arrays.asList("ucd_12_1", "emoji_12_1");
      default:
        return Arrays.asList("ucd_" + version.underscoreVersion());
    }
  }

  private AllTestsGenerator() {}
}
