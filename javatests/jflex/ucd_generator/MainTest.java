package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.File;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import org.junit.Test;

/** Test {@link Main}. */
public class MainTest {
  /**
   * Test that Unicode files are found on a recent zipped release. See
   * https://www.unicode.org/Public/10.0.0/ucd/UCD.zip
   */
  @Test
  public void findUcdFiles_unicode10() throws Exception {
    // --version=10.0.0 external/emoji_5_emoji_data_txt/file/downloaded external/ucd_10/Blocks.txt
    UcdVersion parsedVersion =
        Main.findUcdFiles(
            "10.0.0",
            ImmutableList.of(
                "external/emoji_5_emoji_data_txt/file/downloaded", "external/ucd_10/Blocks.txt"));

    assertThat(parsedVersion.files())
        .containsExactly(
            UcdFileType.Blocks, new File("external/ucd_10/Blocks.txt"),
            UcdFileType.Emoji, new File("external/emoji_5_emoji_data_txt/file/downloaded"));
  }

  /**
   * Test that Unicode files are found on an old directory release. See
   * https://www.unicode.org/Public/4.0-Update1/
   */
  @Test
  public void findUcdFiles_unicode4_0_1() throws Exception {
    UcdVersion parsedVersion =
        Main.findUcdFiles(
            "4.0.1", ImmutableList.of("external/ucd_4_0_1_DerivedAge_4_0_1_txt/file/downloaded"));

    assertThat(parsedVersion.files())
        .containsExactly(
            UcdFileType.DerivedAge,
            new File("external/ucd_4_0_1_DerivedAge_4_0_1_txt/file/downloaded"));
  }
}
