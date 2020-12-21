package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

/** Integration test for {@link UcdGenerator} */
public class UcdGeneratorIntegrationTest {

  @Test
  public void emitUnicodeVersionXY_4_1() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_4_1, outputDir);

    File f = new File(outputDir, "Unicode_4_1.java");
    assertThat(f.exists()).isTrue();
  }

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

  @Test
  public void emitUnicodeVersionXY_6_3() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_6_3, outputDir);

    File f = new File(outputDir, "Unicode_6_3.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_6_3.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }

  @Test
  public void emitUnicodeVersionXY_10_0() throws Exception {
    File outputDir = new File("/tmp");
    UcdGenerator.emitUnicodeVersionXY(TestedVersions.UCD_VERSION_10, outputDir);

    File f = new File(outputDir, "Unicode_10_0.java");
    assertThat(f.exists()).isTrue();

    File goldenFile = new File("javatests/jflex/ucd_generator/Unicode_10_0.java.golden");
    DiffOutputStream goldenOutputStream =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));
    Files.copy(f, goldenOutputStream);
  }
}
