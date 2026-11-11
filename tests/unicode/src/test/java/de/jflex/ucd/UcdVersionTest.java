/*
 * Copyright (C) 2020-2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.version.Version;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Test {@link UcdVersion}. */
public class UcdVersionTest {
  @TempDir File tempDir;

  /**
   * Test that Unicode files are found on a recent zipped release. See
   * https://www.unicode.org/Public/10.0.0/ucd/UCD.zip
   */
  @Test
  public void findUcdFiles_unicode_10() throws Exception {
    File emoji = UcdFiles.singleFile("emoji_5", "emoji-data.txt");
    File blocks = UcdFiles.zippedFile("ucd_10_0", "Blocks.txt");
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(
            new Version("10.0.0"), ImmutableList.of(emoji.getPath(), blocks.getPath()));

    assertThat(parsedVersion.files())
        .containsExactly(UcdFileType.Blocks, blocks, UcdFileType.Emoji, emoji);
  }

  /**
   * Test that Unicode files are found on an old directory release. See
   * https://www.unicode.org/Public/4.0-Update1/
   */
  @Test
  public void findUcdFiles_unicode_4_0_1() throws Exception {
    File derivedAge = UcdFiles.singleFile("ucd_4_0_1", "DerivedAge-4.0.1.txt");
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(new Version("4.0.1"), ImmutableList.of(derivedAge.getPath()));

    assertThat(parsedVersion.files()).containsExactly(UcdFileType.DerivedAge, derivedAge);
  }

  /**
   * Test that Unicode files are found for Unicode 3.1.1. See
   * https://www.unicode.org/Public/3.1-Update1
   *
   * <ul>
   *   <li>PropList from the patch 3.1.1
   *   <li>LineBreak from the parent 3.1.0 (with a strange name)
   *   <li>DerivedAge from UNIDATA
   * </ul>
   */
  @Test
  public void findUcdFiles_unicode_3_1_1() throws Exception {
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(
            new Version("3.1.1"),
            ImmutableList.of(
                UcdFiles.singleFile("ucd_3_1_1", "PropList-3.1.1.txt").getPath(),
                UcdFiles.singleFile("ucd_3_1_0", "LineBreak-6.txt").getPath(),
                UcdFiles.derivedAge().getPath()));

    assertThat(parsedVersion.files().keySet())
        .containsExactly(UcdFileType.PropList, UcdFileType.LineBreak, UcdFileType.DerivedAge);
  }

  /** Test that only the file name selects the type, and that the path is kept unchanged. */
  @Test
  public void findUcdFiles_plainDownloads() throws Exception {
    File derivedAge = newFile("ucd_derived_age/DerivedAge.txt");
    File emoji = newFile("Blocks/emoji-data.txt");
    File scripts = newFile("UnicodeData/Scripts-4.0.1.txt");
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(
            new Version("4.0.1"),
            ImmutableList.of(derivedAge.getPath(), emoji.getPath(), scripts.getPath()));

    assertThat(parsedVersion.files())
        .containsExactly(
            UcdFileType.DerivedAge, derivedAge,
            UcdFileType.Emoji, emoji,
            UcdFileType.Scripts, scripts);
  }

  private File newFile(String path) throws IOException {
    File file = new File(tempDir, path);
    Files.createDirectories(file.getParentFile().toPath());
    Files.createFile(file.toPath());
    return file;
  }
}
