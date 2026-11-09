/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import de.jflex.version.Version;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Unicode versions that JFlex supports, and the UCD downloads of {@code javatests/pom.xml} that
 * make up each of them.
 *
 * <p>A version often needs more than one download. Update releases only have the files that
 * changed, the releases before 3.2 have no {@code DerivedAge.txt}, and the emoji data of 8.0 and
 * later is published separately.
 */
public final class UcdReleases {

  /** The files that {@link UcdVersion} takes from a release. */
  private static final Pattern UCD_FILE =
      Pattern.compile(
          Arrays.stream(UcdFileType.values())
              .map(UcdFileType::name)
              .collect(Collectors.joining("|", "(", "|emoji-data)(-[0-9.]+)?\\.txt")));

  /** Maps each version, with its update number, to its download directories. */
  private static final TreeMap<Version, List<String>> RELEASES =
      new TreeMap<>(Version.MAJOR_MINOR_COMPARATOR);

  static {
    add("1.1.5", "ucd_1_1_5");
    add("2.0.14", "ucd_2_0_14", "ucd_derived_age");
    add("2.1.9", "ucd_2_1_9", "ucd_derived_age");
    add("3.0.1", "ucd_3_0_0", "ucd_3_0_1", "ucd_derived_age");
    add("3.1.1", "ucd_3_1_0", "ucd_3_1_1", "ucd_derived_age");
    add("3.2.0", "ucd_3_2_0");
    add("4.0.1", "ucd_4_0_1");
    add("4.1.0", "ucd_4_1_0");
    add("5.0.0", "ucd_5_0");
    add("5.1.0", "ucd_5_1");
    add("5.2.0", "ucd_5_2");
    add("6.0.0", "ucd_6_0");
    add("6.1.0", "ucd_6_1");
    add("6.2.0", "ucd_6_2");
    add("6.3.0", "ucd_6_3");
    add("7.0.0", "ucd_7_0");
    add("8.0.0", "ucd_8_0", "emoji_2");
    add("9.0.0", "ucd_9_0", "emoji_4");
    add("10.0.0", "ucd_10_0", "emoji_5");
    add("11.0.0", "ucd_11_0", "emoji_11_0");
    add("12.0.0", "ucd_12_0", "emoji_12_0");
    add("12.1.0", "ucd_12_1", "emoji_12_1");
  }

  private static void add(String version, String... releases) {
    RELEASES.put(new Version(version), Arrays.asList(releases));
  }

  /** Returns the supported versions with their update numbers, oldest first. */
  public static List<Version> versions() {
    return new ArrayList<>(RELEASES.keySet());
  }

  /**
   * Returns the paths of the UCD files of a version.
   *
   * @param ucdDir the directory with the UCD downloads
   * @param version the version; only its major and minor numbers count
   */
  public static List<String> ucdFiles(Path ucdDir, Version version) throws IOException {
    List<String> releases =
        Objects.requireNonNull(RELEASES.get(version), "Unknown Unicode version " + version);
    List<String> files = new ArrayList<>();
    for (String release : releases) {
      files.addAll(ucdFilesIn(ucdDir.resolve(release)));
    }
    return files;
  }

  /**
   * Returns a file of a version by its path inside the release, e.g. {@code
   * auxiliary/LineBreakTest.txt}, or null if no release of the version has it.
   *
   * @param ucdDir the directory with the UCD downloads
   * @param version the version; only its major and minor numbers count
   */
  public static Path findFile(Path ucdDir, Version version, String relativePath) {
    List<String> releases =
        Objects.requireNonNull(RELEASES.get(version), "Unknown Unicode version " + version);
    for (String release : releases) {
      Path file = ucdDir.resolve(release).resolve(relativePath);
      if (Files.isRegularFile(file)) {
        return file;
      }
    }
    return null;
  }

  private static List<String> ucdFilesIn(Path release) throws IOException {
    try (Stream<Path> files = Files.walk(release)) {
      return files
          .filter(file -> UCD_FILE.matcher(file.getFileName().toString()).matches())
          .map(Path::toString)
          .collect(Collectors.toList());
    }
  }

  private UcdReleases() {}
}
