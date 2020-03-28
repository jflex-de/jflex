package jflex.ucd_generator;

import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;

/** Constant holder for {@link UcdVersion}s under test. */
public class TestedVersions {

  public static final UcdVersion UCD_VERSION_10 =
      UcdVersion.builder()
          .setVersion(new Version(10, 0))
          .putFile(UcdFileType.PropertyAliases, ucd10File("PropertyAliases.txt"))
          .putFile(UcdFileType.PropertyValueAliases, ucd10File("PropertyValueAliases.txt"))
          .putFile(UcdFileType.UnicodeData, ucd10File("UnicodeData.txt"))
          .putFile(UcdFileType.PropList, ucd10File("PropList.txt"))
          .putFile(UcdFileType.DerivedCoreProperties, ucd10File("DerivedCoreProperties.txt"))
          .putFile(UcdFileType.Scripts, ucd10File("Scripts.txt"))
          .putFile(UcdFileType.ScriptExtensions, ucd10File("ScriptExtensions.txt"))
          .putFile(UcdFileType.Blocks, ucd10File("Blocks.txt"))
          .putFile(UcdFileType.LineBreak, ucd10File("LineBreak.txt"))
          .putFile(UcdFileType.GraphemeBreakProperty, ucd10AuxFile("GraphemeBreakProperty.txt"))
          .putFile(UcdFileType.SentenceBreakProperty, ucd10AuxFile("SentenceBreakProperty.txt"))
          .putFile(UcdFileType.WordBreakProperty, ucd10AuxFile("WordBreakProperty.txt"))
          .putFile(UcdFileType.DerivedAge, ucd10File("DerivedAge.txt"))
          .build();

  private static File ucd10AuxFile(String name) {
    return ucdAuxFile("ucd_10", name);
  }

  private static File ucd10File(String name) {
    return ucdFile("ucd_10", name);
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
