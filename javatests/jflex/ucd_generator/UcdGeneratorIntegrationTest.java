package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;
import org.junit.Test;

public class UcdGeneratorIntegrationTest {
  @Test
  public void emitUnicodeVersionXY_10_0() throws Exception {
    UcdVersion ucdVersion10 =
        UcdVersion.builder()
            .setVersion(new Version(10, 0))
            .putFile(UcdFileType.PropertyAliases, ucd10File("PropertyAliases.txt"))
            .putFile(UcdFileType.PropertyValueAliases, ucd10File("PropertyValueAliases.txt"))
            .putFile(UcdFileType.UnicodeData, ucd10File("UnicodeData.txt"))
            .putFile(UcdFileType.PropList, ucd10File("PropList.txt"))
            .putFile(UcdFileType.DerivedCoreProperties, ucd10File("DerivedCoreProperties.txt"))
            .putFile(UcdFileType.Scripts, ucd10File("Scripts.txt"))
            .putFile(UcdFileType.Blocks, ucd10File("Blocks.txt"))
            .build();
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(ucdVersion10, outputDir);

    File f = new File(outputDir, "Unicode_10_0.java");
    assertThat(f.exists()).isTrue();
  }

  private static File ucd10File(String name) {
    return new File("external/ucd_10/" + name);
  }
}
