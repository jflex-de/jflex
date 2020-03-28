package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import java.io.File;
import org.junit.Test;

/** Integration test for {@link UcdGenerator} */
public class UcdGeneratorIntegrationTest {

  @Test
  public void emitUnicodeVersionXY_10_0() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_10, outputDir);

    File f = new File(outputDir, "Unicode_10_0.java");
    assertThat(f.exists()).isTrue();
  }
}
