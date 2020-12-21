package jflex.ucd_generator;

import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;
import jflex.ucd_generator.ucd.Versions;

/** Constant holder for {@link UcdVersion}s under test. */
public class TestedVersions {

  public static final UcdVersion UCD_VERSION_4_1 = newUcdVersion("4.1.0", "ucd_4_1_0").build();
  public static final UcdVersion UCD_VERSION_5_0 = newUcdVersion("5.0.0", "ucd_5_0_0").build();
  public static final UcdVersion UCD_VERSION_5_1 = newUcdVersion("5.1.0", "ucd_5_1_0").build();
  public static final UcdVersion UCD_VERSION_5_2 = newUcdVersion("5.2.0", "ucd_5_2_0").build();
  public static final UcdVersion UCD_VERSION_6_0 = newUcdVersion("6.0.0", "ucd_6_0_0").build();
  public static final UcdVersion UCD_VERSION_6_1 = newUcdVersion("6.1.0", "ucd_6_1_0").build();
  public static final UcdVersion UCD_VERSION_6_2 = newUcdVersion("6.2.0", "ucd_6_2_0").build();
  public static final UcdVersion UCD_VERSION_6_3 = newUcdVersion("6.3.0", "ucd_6_3_0").build();
  public static final UcdVersion UCD_VERSION_7_0 = newUcdVersion("7.0", "ucd_7").build();
  public static final UcdVersion UCD_VERSION_8_0 =
      newUcdVersion("8.0", "ucd_8").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_2")).build();
  public static final UcdVersion UCD_VERSION_9_0 =
      newUcdVersion("9.0", "ucd_9").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_4")).build();
  public static final UcdVersion UCD_VERSION_10_0 =
      newUcdVersion("10.0", "ucd_10").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_5")).build();
  public static final UcdVersion UCD_VERSION_11_0 =
      newUcdVersion("11.0", "ucd_11").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_11")).build();
  public static final UcdVersion UCD_VERSION_12_0 =
      newUcdVersion("12.0", "ucd_12").putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_12")).build();
  public static final UcdVersion UCD_VERSION_12_1 =
      newUcdVersion("12.1", "ucd_12_1")
          .putFile(UcdFileType.Emoji, ucdEmojiFile("emoji_12_1"))
          .build();

  private static UcdVersion.Builder newUcdVersion(String versionName, String bazelTarget) {
    UcdVersion.Builder version =
        UcdVersion.builder(versionName)
            .putFile(UcdFileType.PropertyAliases, ucdFile(bazelTarget, UcdFileType.PropertyAliases))
            .putFile(
                UcdFileType.PropertyValueAliases, ucdFile(bazelTarget, UcdFileType.PropertyValueAliases))
            .putFile(UcdFileType.UnicodeData, ucdFile(bazelTarget, UcdFileType.UnicodeData))
            .putFile(UcdFileType.PropList, ucdFile(bazelTarget, UcdFileType.PropList))
            .putFile(
                UcdFileType.DerivedCoreProperties,
                ucdFile(bazelTarget, UcdFileType.DerivedCoreProperties))
            .putFile(UcdFileType.Scripts, ucdFile(bazelTarget, UcdFileType.Scripts))
            .putFile(UcdFileType.Blocks, ucdFile(bazelTarget, UcdFileType.Blocks))
            .putFile(UcdFileType.LineBreak, ucdFile(bazelTarget, UcdFileType.LineBreak))
            .putFile(
                UcdFileType.GraphemeBreakProperty,
                ucdAuxFile(bazelTarget, UcdFileType.GraphemeBreakProperty))
            .putFile(
                UcdFileType.SentenceBreakProperty,
                ucdAuxFile(bazelTarget, UcdFileType.SentenceBreakProperty))
            .putFile(
                UcdFileType.WordBreakProperty, ucdAuxFile(bazelTarget, UcdFileType.WordBreakProperty))
            .putFile(UcdFileType.DerivedAge, ucdFile(bazelTarget, UcdFileType.DerivedAge));
    if (Version.MAJOR_MINOR_COMPARATOR.compare(version.version(), Versions.VERSION_6_0) >= 0) {
      version.putFile(UcdFileType.ScriptExtensions, ucdFile(bazelTarget, UcdFileType.ScriptExtensions));
    }
    return version;
  }

  private static File ucdAuxFile(String bazelVersionTarget, UcdFileType type) {
    File auxDir = new File(ucdVersionDirectory(bazelVersionTarget), "auxiliary");
    String name = type + ".txt";
    return new File(auxDir, name);
  }

  private static File ucdEmojiFile(String bazelVersionTarget) {
    return ucdFile(bazelVersionTarget + "_emoji_data_txt", "file/downloaded");
  }

  private static File ucdFile(String bazelVersionTarget, UcdFileType type) {
    String name = type + ".txt";
    return ucdFile(bazelVersionTarget, name);
  }

  private static File ucdFile(String bazelVersionTarget, String name) {
    return new File(ucdVersionDirectory(bazelVersionTarget), name);
  }

  private static File ucdVersionDirectory(String bazelVersionTarget) {
    return new File(new File("external"), bazelVersionTarget);
  }

  private TestedVersions() {}
}
