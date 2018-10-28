package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
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
    ImmutableMap<UcdFileType, File> ucd1 =
        ImmutableMap.of(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    ImmutableMap<UcdFileType, File> ucd2 =
        ImmutableMap.of(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    UcdVersions versions =
        UcdVersions.of(
            "1.2", ucd1,
            "2.3.4", ucd2);
    Emitter emitter = new Emitter("org.example", versions);

    emitter.emitUnicodeProperties(output);
    assertThat(output.isCompleted()).isTrue();
  }
}
