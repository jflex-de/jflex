/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.testemoji;

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
          "external/ucd_10_0/Blocks.txt",
          "external/ucd_10_0/UnicodeData.txt",
          "external/ucd_10_0/PropList.txt",
          "external/emoji_5_emoji_data_txt/file/downloaded",
        });
  }
}
