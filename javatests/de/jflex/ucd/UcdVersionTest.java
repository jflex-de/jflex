/*
 * Copyright (C) 2020-2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.ucd;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.version.Version;
import java.io.File;
import org.junit.Test;

/** Test {@link UcdVersion}. */
public class UcdVersionTest {
  /**
   * Test that Unicode files are found on a recent zipped release. See
   * https://www.unicode.org/Public/10.0.0/ucd/UCD.zip
   */
  @Test
  public void findUcdFiles_unicode_10() throws Exception {
    // --version=10.0.0 external/emoji_5_emoji_data_txt/file/downloaded external/ucd_10/Blocks.txt
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(
            new Version("10.0.0"),
            ImmutableList.of(
                "external/emoji_5_emoji_data_txt/file/downloaded", "external/ucd_10_0/Blocks.txt"));

    assertThat(parsedVersion.files())
        .containsExactly(
            UcdFileType.Blocks, new File("external/ucd_10_0/Blocks.txt"),
            UcdFileType.Emoji, new File("external/emoji_5_emoji_data_txt/file/downloaded"));
  }

  /**
   * Test that Unicode files are found on an old directory release. See
   * https://www.unicode.org/Public/4.0-Update1/
   */
  @Test
  public void findUcdFiles_unicode_4_0_1() throws Exception {
    UcdVersion parsedVersion =
        UcdVersion.findUcdFiles(
            new Version("4.0.1"),
            ImmutableList.of("external/ucd_4_0_1_DerivedAge_4_0_1_txt/file/downloaded"));

    assertThat(parsedVersion.files())
        .containsExactly(
            UcdFileType.DerivedAge,
            new File("external/ucd_4_0_1_DerivedAge_4_0_1_txt/file/downloaded"));
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
                "external/ucd_3_1_1_PropList_3_1_1_txt/file/downloaded",
                "external/ucd_3_1_0_LineBreak_6_txt/file/downloaded",
                "external/ucd_derived_age/file/downloaded"));

    assertThat(parsedVersion.files().keySet())
        .containsExactly(UcdFileType.PropList, UcdFileType.LineBreak, UcdFileType.DerivedAge);
  }
}
