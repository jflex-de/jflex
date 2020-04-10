package jflex.ucd_generator.emitter.unicode_version;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import jflex.testing.diff.DiffOutputStream;
import jflex.ucd_generator.model.UnicodeData;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import jflex.ucd_generator.ucd.Version;
import org.junit.Test;

/** Golden test for {@link UnicodeVersionEmitter}. */
public class UnicodeVersionEmitterGoldenTest {
  @Test
  public void emitUnicode_0_1() throws Exception {
    Version version_0_1 = new Version(0, 1);
    File goldenFile =
        new File("javatests/jflex/ucd_generator/emitter/unicode_version/Unicode_0_1.java.golden");
    // in-memory output
    DiffOutputStream output =
        new DiffOutputStream(Files.newReader(goldenFile, StandardCharsets.UTF_8));

    // fake ucd version 0.1
    UcdVersion ucd0_1 =
        UcdVersion.builder()
            .setVersion(version_0_1)
            .putFile(UcdFileType.UnicodeData, new File("FakeUnicodeData.txt"))
            .build();

    UnicodeData unicodeData = new UnicodeData(version_0_1);
    unicodeData.maximumCodePoint(0x1234);
    unicodeData.addPropertyInterval("age=1.1", 0x0000, 0x01f5);
    unicodeData.addPropertyInterval("age=1.1", 0x01fa, 0x0217);
    unicodeData.addPropertyInterval("age=4.1", 0x0000, 0x0241);
    unicodeData.addPropertyInterval("age=4.1", 0x0250, 0x036f);
    unicodeData.addPropertyInterval("age=4.1", 0x0374, 0x0375);
    unicodeData.addPropertyInterval(
        "age=4.1",
        Character.toCodePoint('\ud800', '\udc0d'),
        Character.toCodePoint('\ud800', '\udc26'));
    unicodeData.addCaselessMatches('a', "41", "", "");
    unicodeData.addCaselessMatches('b', "42", "43", "44");
    UnicodeVersionEmitter emitter = new UnicodeVersionEmitter("org.example", ucd0_1, unicodeData);

    emitter.emitUnicodeVersion(output);
    assertThat(output.remainingContent()).isEmpty();
  }
}
