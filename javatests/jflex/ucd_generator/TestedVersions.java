package jflex.ucd_generator;

import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;

/** Constant holder for {@link UcdVersion}s under test. */
public class TestedVersions {

  public static final UcdVersion UCD_VERSION_5_0 =
      UcdVersion.builder()
          .setVersion(new Version(5, 0, 0))
          .putFile(UcdFileType.PropertyAliases, ucdFile("ucd_5_0_0", "PropertyAliases.txt"))
          .putFile(
              UcdFileType.PropertyValueAliases, ucdFile("ucd_5_0_0", "PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucdFile("ucd_5_0_0", "UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucdFile("ucd_5_0_0", "PropList.txt"))
          .putFile(
              UcdFileType.DerivedCoreProperties, ucdFile("ucd_5_0_0", "DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucdFile("ucd_5_0_0", "Scripts.txt"))
          .putFile(UcdFileType.Blocks, ucdFile("ucd_5_0_0", "Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucdFile("ucd_5_0_0", "LineBreak.txt"))
          .putFile(
              UcdFileType.GraphemeBreakProperty,
              ucdAuxFile("ucd_5_0_0", "GraphemeBreakProperty.txt"))
          .putFile(
              UcdFileType.SentenceBreakProperty,
              ucdAuxFile("ucd_5_0_0", "SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucdAuxFile("ucd_5_0_0", "WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucdFile("ucd_5_0_0", "DerivedAge.txt"))
          .build();

  public static final UcdVersion UCD_VERSION_6_3 =
      UcdVersion.builder()
          .setVersion(new Version(6, 3, 0))
          .putFile(UcdFileType.PropertyAliases, ucdFile("ucd_6_3_0", "PropertyAliases.txt"))
          .putFile(
              UcdFileType.PropertyValueAliases, ucdFile("ucd_6_3_0", "PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucdFile("ucd_6_3_0", "UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucdFile("ucd_6_3_0", "PropList.txt"))
          .putFile(
              UcdFileType.DerivedCoreProperties, ucdFile("ucd_6_3_0", "DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucdFile("ucd_6_3_0", "Scripts.txt"))
          .putFile(UcdFileType.ScriptExtensions, ucdFile("ucd_6_3_0", "ScriptExtensions.txt"))
          .putFile(UcdFileType.Blocks, ucdFile("ucd_6_3_0", "Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucdFile("ucd_6_3_0", "LineBreak.txt"))
          .putFile(
              UcdFileType.GraphemeBreakProperty,
              ucdAuxFile("ucd_6_3_0", "GraphemeBreakProperty.txt"))
          .putFile(
              UcdFileType.SentenceBreakProperty,
              ucdAuxFile("ucd_6_3_0", "SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucdAuxFile("ucd_6_3_0", "WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucdFile("ucd_6_3_0", "DerivedAge.txt"))
          .build();
  public static final UcdVersion UCD_VERSION_10 =
      UcdVersion.builder()
          .setVersion(new Version(10, 0, 0))
          .putFile(UcdFileType.PropertyAliases, ucdFile("ucd_10", "PropertyAliases.txt"))
          .putFile(UcdFileType.PropertyValueAliases, ucdFile("ucd_10", "PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucdFile("ucd_10", "UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucdFile("ucd_10", "PropList.txt"))
          .putFile(
              UcdFileType.DerivedCoreProperties, ucdFile("ucd_10", "DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucdFile("ucd_10", "Scripts.txt"))
          .putFile(UcdFileType.ScriptExtensions, ucdFile("ucd_10", "ScriptExtensions.txt"))
          .putFile(UcdFileType.Blocks, ucdFile("ucd_10", "Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucdFile("ucd_10", "LineBreak.txt"))
          .putFile(
              UcdFileType.GraphemeBreakProperty, ucdAuxFile("ucd_10", "GraphemeBreakProperty.txt"))
          .putFile(
              UcdFileType.SentenceBreakProperty, ucdAuxFile("ucd_10", "SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucdAuxFile("ucd_10", "WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucdFile("ucd_10", "DerivedAge.txt"))
          .putFile(UcdFileType.Emoji, ucdFile("emoji_5_emoji_data_txt", "file/downloaded"))
          .build();

  private static File ucdAuxFile(String bazelVersionTarget, String name) {
    File auxDir = new File(ucdVersionDirectory(bazelVersionTarget), "auxiliary");
    return new File(auxDir, name);
  }

  private static File ucdFile(String bazelVersionTarget, String name) {
    return new File(ucdVersionDirectory(bazelVersionTarget), name);
  }

  private static File ucdVersionDirectory(String bazelVersionTarget) {
    return new File(new File("external"), bazelVersionTarget);
  }

  private TestedVersions() {}
}
