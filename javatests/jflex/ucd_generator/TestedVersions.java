package jflex.ucd_generator;

import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;

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
          .build();

  private static File ucd10File(String name) {
    return new File("external/ucd_10/" + name);
  }

  private TestedVersions() {}
}
