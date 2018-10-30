package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

public class EmitterTest {

  @Test
  public void emitUnicodeProperties() throws Exception {
    File goldenFile = new File("javatests/jflex/ucd_generator/UnicodeProperties.java.golden");

    // in-memory output
    DiffOutputStream output =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));

    // fake ucd version 1.2
    UcdVersion.Builder ucd1_2 =
        UcdVersion.builder().putFile(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    UcdVersion.Builder ucd2_3 =
        UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
    UcdVersion.Builder ucd2_4 =
        UcdVersion.builder().putFile(UcdFileType.Blocks, new File("FakeUnicodeData.txt"));
    UcdVersions versions =
        UcdVersions.of(
            "1.2.0", ucd1_2,
            "2.0.1", ucd2_3,
            "2.4.6", ucd2_4);
    Emitter emitter = new Emitter("org.example", versions);

    emitter.emitUnicodeProperties(output);
    assertThat(output.isCompleted()).isTrue();
  }
}
