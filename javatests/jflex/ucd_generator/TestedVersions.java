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
          .putFile(UcdFileType.PropertyAliases, ucd5File("PropertyAliases.txt"))
          .putFile(UcdFileType.PropertyValueAliases, ucd5File("PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucd5File("UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucd5File("PropList.txt"))
          .putFile(UcdFileType.DerivedCoreProperties, ucd5File("DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucd5File("Scripts.txt"))
          .putFile(UcdFileType.Blocks, ucd5File("Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucd5File("LineBreak.txt"))
          .putFile(UcdFileType.GraphemeBreakProperty, ucd5AuxFile("GraphemeBreakProperty.txt"))
          .putFile(UcdFileType.SentenceBreakProperty, ucd5AuxFile("SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucd5AuxFile("WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucd5File("DerivedAge.txt"))
          .build();

  public static final UcdVersion UCD_VERSION_6_3 =
      UcdVersion.builder()
          .setVersion(new Version(6, 3, 0))
          .putFile(UcdFileType.PropertyAliases, ucd6File("PropertyAliases.txt"))
          .putFile(UcdFileType.PropertyValueAliases, ucd6File("PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucd6File("UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucd6File("PropList.txt"))
          .putFile(UcdFileType.DerivedCoreProperties, ucd6File("DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucd6File("Scripts.txt"))
          .putFile(UcdFileType.ScriptExtensions, ucd6File("ScriptExtensions.txt"))
          .putFile(UcdFileType.Blocks, ucd6File("Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucd6File("LineBreak.txt"))
          .putFile(UcdFileType.GraphemeBreakProperty, ucd6AuxFile("GraphemeBreakProperty.txt"))
          .putFile(UcdFileType.SentenceBreakProperty, ucd6AuxFile("SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucd6AuxFile("WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucd6File("DerivedAge.txt"))
          .build();;

  private static File ucd5AuxFile(String name) {
    return ucdAuxFile("ucd_5_0_0", name);
  }

  private static File ucd5File(String name) {
    return ucdFile("ucd_5_0_0", name);
  }

  private static File ucd6AuxFile(String name) {
    return ucdAuxFile("ucd_6_3_0", name);
  }

  private static File ucd6File(String name) {
    return ucdFile("ucd_6_3_0", name);
  }

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
