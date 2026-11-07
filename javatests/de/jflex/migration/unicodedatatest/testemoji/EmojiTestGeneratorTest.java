/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testemoji;

import de.jflex.ucd.UcdFiles;
import org.junit.Test;

/** Minimal integration test for {@link EmojiTestGenerator}. */
public class EmojiTestGeneratorTest {
  /** Tests that the generation doesn't throw exception. */
  @Test
  public void generate_ucd10() throws Exception {
    EmojiTestGenerator.main(
        new String[] {
          "10.0",
          "/tmp",
          UcdFiles.zippedFile("ucd_10_0", "Blocks.txt").getPath(),
          UcdFiles.zippedFile("ucd_10_0", "UnicodeData.txt").getPath(),
          UcdFiles.zippedFile("ucd_10_0", "PropList.txt").getPath(),
          UcdFiles.singleFile("emoji_5", "emoji-data.txt").getPath(),
        });
  }
}
