package jflex.ucd_generator.scanner;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import jflex.ucd_generator.scanner.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdVersion;
import org.junit.Test;

/** Test for {@link UnicodeDataScanner}, generated by jflex. */
public class UnicodeDataScannerTest {

  @Test
  public void scanUnicode10() throws Exception {
    File file = new File(new File("external/ucd_10"), "UnicodeData.txt");
    if (!file.exists()) {
      throw new FileNotFoundException("Missing test data (Unicode 10): " + file.getAbsolutePath());
    }
    UcdVersion ucdVersion = UcdVersion.builder().setVersion("10.0").build();
    UnicodeData ucdDataBuilder = UnicodeData(ucdVersion.version());
    UnicodeDataScanner scanner =
        new UnicodeDataScanner(Files.newReader(file, Charsets.UTF_8), ucdVersion, ucdDataBuilder);
    scanner.scan();
    UnicodeData unicodeData = ucdDataBuilder.build();
    assertThat(unicodeData.maximumCodePoint()).isEqualTo(0x10ffff);
    assertThat(unicodeData.caselessMatchPartitions()).isNotEmpty();
    assertThat(unicodeData.maxCaselessMatchPartitionSize()).isEqualTo(4);
  }
}
