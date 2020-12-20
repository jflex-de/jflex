package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import jflex.ucd_generator.ucd.UcdFileType;
import jflex.ucd_generator.ucd.UcdVersion;
import org.junit.Test;

/** Test {@link Main}. */
public class MainTest {
  @Test
  public void findUcdFiles() throws FileNotFoundException {
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
}
