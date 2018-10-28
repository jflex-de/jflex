package jflex.ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileReader;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

public class EmitterTest {

  @Test
  public void emitUnicodeProperties() throws Exception {
    File goldenFile = new File("jflex/src/main/java/jflex/core/unicode/UnicodeProperties.java");

    // in-memory output
    // ByteArrayOutputStream output = new ByteArrayOutputStream();
    DiffOutputStream output = new DiffOutputStream(new FileReader(goldenFile));

    // fake ucd version 1.2
    ImmutableMap<UcdFileType, File> ucd1 =
        ImmutableMap.of(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"));
    ImmutableMap<String, ImmutableMap<UcdFileType, File>> versions = ImmutableMap.of("1.2", ucd1);
    Emitter emitter = new Emitter(Package.getPackage("org.example"), versions);

    emitter.emitUnicodeProperties(output);

    // TODO(regisd) Integration test for what we have in codebase
    //    String expected = Files.asCharSource(goldenFile, Charsets.UTF_8).read();
    // assertThat(output.toString()).isEqualTo(expected);
  }
}
