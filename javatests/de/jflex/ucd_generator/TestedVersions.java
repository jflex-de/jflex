/*
 * Copyright (C) 2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator;

import de.jflex.ucd.UcdFileType;
import de.jflex.ucd.UcdFiles;
import de.jflex.ucd.UcdVersion;
import de.jflex.ucd.Versions;
import de.jflex.version.Version;
import java.io.File;

/** Constant holder for {@link UcdVersion}s under test. */
public class TestedVersions {

  public static final UcdVersion UCD_VERSION_1_1 =
      UcdVersion.builder("1.1.5")
          .putFile(
              UcdFileType.UnicodeData, UcdFiles.singleFile("ucd_1_1_5", "UnicodeData-1.1.5.txt"))
          .putFile(UcdFileType.DerivedAge, UcdFiles.derivedAge())
          .build();
  public static final UcdVersion UCD_VERSION_2_0 =
      UcdVersion.builder("2.0.14")
          .putFile(UcdFileType.Blocks, UcdFiles.singleFile("ucd_2_0_14", "Blocks-1.txt"))
          .putFile(
              UcdFileType.UnicodeData, UcdFiles.singleFile("ucd_2_0_14", "UnicodeData-2.0.14.txt"))
          .putFile(UcdFileType.PropList, UcdFiles.singleFile("ucd_2_0_14", "PropList-2.0.14.txt"))
          .putFile(UcdFileType.DerivedAge, UcdFiles.derivedAge())
          .build();
  public static final UcdVersion UCD_VERSION_2_1 =
      UcdVersion.builder("2.1.9")
          .putFile(UcdFileType.Blocks, UcdFiles.singleFile("ucd_2_1_9", "Blocks-2.txt"))
          .putFile(
              UcdFileType.UnicodeData, UcdFiles.singleFile("ucd_2_1_9", "UnicodeData-2.1.9.txt"))
          .putFile(UcdFileType.PropList, UcdFiles.singleFile("ucd_2_1_9", "PropList-2.1.9.txt"))
          .putFile(UcdFileType.DerivedAge, UcdFiles.derivedAge())
          .build();
  public static final UcdVersion UCD_VERSION_3_0 =
      UcdVersion.builder("3.0.1")
          .putFile(UcdFileType.Blocks, UcdFiles.singleFile("ucd_3_0_0", "Blocks-3.txt"))
          .putFile(UcdFileType.LineBreak, UcdFiles.singleFile("ucd_3_0_0", "LineBreak-5.txt"))
          .putFile(
              UcdFileType.UnicodeData, UcdFiles.singleFile("ucd_3_0_1", "UnicodeData-3.0.1.txt"))
          .putFile(UcdFileType.PropList, UcdFiles.singleFile("ucd_3_0_1", "PropList-3.0.1.txt"))
          .putFile(UcdFileType.DerivedAge, UcdFiles.derivedAge())
          .build();
  public static final UcdVersion UCD_VERSION_3_1 =
      UcdVersion.builder("3.1.1")
          .putFile(UcdFileType.Blocks, UcdFiles.singleFile("ucd_3_1_0", "Blocks-4.txt"))
          .putFile(UcdFileType.LineBreak, UcdFiles.singleFile("ucd_3_1_0", "LineBreak-6.txt"))
          .putFile(
              UcdFileType.UnicodeData, UcdFiles.singleFile("ucd_3_1_0", "UnicodeData-3.1.0.txt"))
          .putFile(UcdFileType.PropList, UcdFiles.singleFile("ucd_3_1_1", "PropList-3.1.1.txt"))
          .putFile(
              UcdFileType.DerivedCoreProperties,
              UcdFiles.singleFile("ucd_3_1_0", "DerivedCoreProperties-3.1.0.txt"))
          .putFile(UcdFileType.Scripts, UcdFiles.singleFile("ucd_3_1_0", "Scripts-3.1.0.txt"))
          .putFile(UcdFileType.DerivedAge, UcdFiles.derivedAge())
          .build();
  public static final UcdVersion UCD_VERSION_3_2 = newUcdVersion("3.2.0", "ucd_3_2_0").build();
  public static final UcdVersion UCD_VERSION_4_0 = newUcdVersion("4.0.1", "ucd_4_0_1").build();
  public static final UcdVersion UCD_VERSION_4_1 = newUcdVersion("4.1.0", "ucd_4_1_0").build();
  public static final UcdVersion UCD_VERSION_5_0 = newUcdVersion("5.0.0", "ucd_5_0").build();
  public static final UcdVersion UCD_VERSION_5_1 = newUcdVersion("5.1.0", "ucd_5_1").build();
  public static final UcdVersion UCD_VERSION_5_2 = newUcdVersion("5.2.0", "ucd_5_2").build();
  public static final UcdVersion UCD_VERSION_6_0 = newUcdVersion("6.0.0", "ucd_6_0").build();
  public static final UcdVersion UCD_VERSION_6_1 = newUcdVersion("6.1.0", "ucd_6_1").build();
  public static final UcdVersion UCD_VERSION_6_2 = newUcdVersion("6.2.0", "ucd_6_2").build();
  public static final UcdVersion UCD_VERSION_6_3 = newUcdVersion("6.3.0", "ucd_6_3").build();
  public static final UcdVersion UCD_VERSION_7_0 = newUcdVersion("7.0", "ucd_7_0").build();
  public static final UcdVersion UCD_VERSION_8_0 =
      newUcdVersion("8.0", "ucd_8_0").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_2")).build();
  public static final UcdVersion UCD_VERSION_9_0 =
      newUcdVersion("9.0", "ucd_9_0").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_4")).build();
  public static final UcdVersion UCD_VERSION_10_0 =
      newUcdVersion("10.0", "ucd_10_0").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_5")).build();
  public static final UcdVersion UCD_VERSION_11_0 =
      newUcdVersion("11.0", "ucd_11_0")
          .putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_11_0"))
          .build();
  public static final UcdVersion UCD_VERSION_12_0 =
      newUcdVersion("12.0", "ucd_12_0")
          .putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_12_0"))
          .build();
  public static final UcdVersion UCD_VERSION_12_1 =
      newUcdVersion("12.1", "ucd_12_1")
          .putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_12_1"))
          .build();

  private static UcdVersion.Builder newUcdVersion(String versionName, String release) {
    Version version = new Version(versionName);
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, Versions.VERSION_4_1) < 0) {
      return newUcdVersionIndividualFiles(version, release);
    } else {
      return newUcdVersionZipped(version, release);
    }
  }

  private static UcdVersion.Builder newUcdVersionZipped(Version version, String release) {
    UcdVersion.Builder ucdVersion =
        UcdVersion.builder(version)
            .putFile(UcdFileType.PropertyAliases, ucdFile(release, UcdFileType.PropertyAliases))
            .putFile(
                UcdFileType.PropertyValueAliases,
                ucdFile(release, UcdFileType.PropertyValueAliases))
            .putFile(UcdFileType.UnicodeData, ucdFile(release, UcdFileType.UnicodeData))
            .putFile(UcdFileType.PropList, ucdFile(release, UcdFileType.PropList))
            .putFile(
                UcdFileType.DerivedCoreProperties,
                ucdFile(release, UcdFileType.DerivedCoreProperties))
            .putFile(UcdFileType.Scripts, ucdFile(release, UcdFileType.Scripts))
            .putFile(UcdFileType.Blocks, ucdFile(release, UcdFileType.Blocks))
            .putFile(UcdFileType.LineBreak, ucdFile(release, UcdFileType.LineBreak))
            .putFile(
                UcdFileType.GraphemeBreakProperty,
                ucdAuxFile(release, UcdFileType.GraphemeBreakProperty))
            .putFile(
                UcdFileType.SentenceBreakProperty,
                ucdAuxFile(release, UcdFileType.SentenceBreakProperty))
            .putFile(
                UcdFileType.WordBreakProperty, ucdAuxFile(release, UcdFileType.WordBreakProperty))
            .putFile(UcdFileType.DerivedAge, ucdFile(release, UcdFileType.DerivedAge));
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, Versions.VERSION_6_0) >= 0) {
      ucdVersion.putFile(
          UcdFileType.ScriptExtensions, ucdFile(release, UcdFileType.ScriptExtensions));
    }
    return ucdVersion;
  }

  /**
   * Used for releases which were not zipped. Modern Unicode releases go through {@link
   * #newUcdVersionZipped}.
   */
  private static UcdVersion.Builder newUcdVersionIndividualFiles(Version version, String release) {
    String suffix = "-" + version + ".txt";
    UcdVersion.Builder ucdVersion =
        UcdVersion.builder(version)
            .putFile(UcdFileType.Blocks, UcdFiles.singleFile(release, "Blocks" + suffix))
            .putFile(UcdFileType.DerivedAge, UcdFiles.singleFile(release, "DerivedAge" + suffix))
            .putFile(
                UcdFileType.DerivedCoreProperties,
                UcdFiles.singleFile(release, "DerivedCoreProperties" + suffix))
            .putFile(UcdFileType.LineBreak, UcdFiles.singleFile(release, "LineBreak" + suffix))
            .putFile(UcdFileType.PropList, UcdFiles.singleFile(release, "PropList" + suffix))
            .putFile(UcdFileType.Scripts, UcdFiles.singleFile(release, "Scripts" + suffix))
            .putFile(UcdFileType.UnicodeData, UcdFiles.singleFile(release, "UnicodeData" + suffix));
    // Aliases didn't exist before Unicode 3.2
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version, Versions.VERSION_3_2) >= 0) {
      ucdVersion
          .putFile(
              UcdFileType.PropertyAliases, UcdFiles.singleFile(release, "PropertyAliases" + suffix))
          .putFile(
              UcdFileType.PropertyValueAliases,
              UcdFiles.singleFile(release, "PropertyValueAliases" + suffix));
    }
    return ucdVersion;
  }

  private static File ucdAuxFile(String release, UcdFileType type) {
    return UcdFiles.zippedFile(release, "auxiliary/" + type + ".txt");
  }

  private static File ucdEmojiFile(String release) {
    return UcdFiles.singleFile(release, "emoji-data.txt");
  }

  private static File ucdFile(String release, UcdFileType type) {
    return UcdFiles.zippedFile(release, type + ".txt");
  }

  private TestedVersions() {}
}
