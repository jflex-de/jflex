package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Ignore;
import org.junit.Test;

/** Integration test for {@link UcdGenerator} */
public class UcdGeneratorIntegrationTest {

  @Ignore
  // Content differs on line 218:
  // : expected:<    "c[c]",> but was:<    "c[asefolding]",>
  // Expected :    "c[c]",
  // Actual   :    "c[asefolding]",
  @Test
  public void emitUnicodeVersionXY_5_0() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_5_0, outputDir);

    File f = new File(outputDir, "Unicode_5_0.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_5_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }
}
