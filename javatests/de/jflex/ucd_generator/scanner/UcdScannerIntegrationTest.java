/*
 * Copyright (C) 2020 Google, LLC.
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
package de.jflex.ucd_generator.scanner;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd_generator.TestedVersions;
import de.jflex.ucd_generator.ucd.CodepointRange;
import de.jflex.version.Version;
import java.io.IOException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Integration test for the {@link UcdScanner} on Unicode 6. */
public class UcdScannerIntegrationTest {

  /**
   * Unicode 6.0 property value: {me}
   *
   * <pre>{@code
   * "\u0488\u0489" + "\u20dd\u20e0" + "\u20e2\u20e4" + "\ua670\ua672"
   * }</pre>
   */
  private static final ImmutableList<CodepointRange> INTERVALS_FOR_GENERALCATEGORY_ME =
      ImmutableList.of(
          CodepointRange.create('\u0488', '\u0489'),
          CodepointRange.create('\u20dd', '\u20e0'),
          CodepointRange.create('\u20e2', '\u20e4'),
          CodepointRange.create('\ua670', '\ua672'));

  private UcdScanner ucdScanner;

  @Before
  public void ucdScanner() {
    ucdScanner = new UcdScanner(TestedVersions.UCD_VERSION_6_3);
    assertThat(ucdScanner.ucdVersion().version()).isEqualTo(new Version(6, 3, 0));
  }

  @Test
  public void scanPropertyAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertThat(ucdScanner.unicodeData.getCanonicalPropertyName("ccc"))
        .isEqualTo("canonicalcombiningclass");
    assertThat(ucdScanner.unicodeData.getPropertyAliases("script")).containsExactly("sc", "script");
    assertThat(ucdScanner.unicodeData.getPropertyAliases("Bidi_Class"))
        .containsExactly("bc", "bidiclass");
  }

  @Test
  public void scanPropertyValueAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "at"))
        .containsExactly("at");
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "aterm"))
        .containsExactly("aterm");

    ucdScanner.scanPropertyValueAliases();
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "at"))
        .containsExactly("at", "aterm");
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "aterm"))
        .containsExactly("at", "aterm");
  }

  @Test
  public void scanUnicodeData() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    // generalcategory=me
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me")).isEmpty();

    ucdScanner.scanUnicodeData();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME)
        .inOrder();
    assertThat(ucdScanner.unicodeData.maximumCodePoint()).isEqualTo(1114111);
    assertThat(ucdScanner.unicodeData.maxCaselessMatchPartitionSize()).isEqualTo(4);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("generalcategory"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("gc"));
  }

  @Test
  public void scanPropList() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("otheruppercase")).isEmpty();

    ucdScanner.scanPropList();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("otheruppercase"))
        .contains(CodepointRange.create(8544, 8559));
  }

  @Test
  public void scanDerivedCoreProperties() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("idcontinue")).isEmpty();

    ucdScanner.scanDerivedCoreProperties();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("idcontinue"))
        .contains(CodepointRange.create(48, 57));
  }

  @Test
  public void scanScripts() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("arabic")).isEmpty();

    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("arabic")).isNotEmpty();
  }

  @Ignore // TODO
  @Test
  public void scanScripExtensions() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("hiragana"))
        .isNotEmpty(); // from scanScripts
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("katakana")).isNotEmpty();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana")).isFalse();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana")).isFalse();

    ucdScanner.scanScriptExtensions();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    // 3031..3035    ; Hira Kana # Lm   [5] VERTICAL KANA REPEAT MARK..
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana")).isTrue();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana")).isTrue();
  }

  @Ignore // TODO
  @Test
  public void scanBlocks() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("block=supplementalpunctuation"))
        .isEmpty();

    ucdScanner.scanBlocks();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira"))
        .isNotEmpty();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("block=supplementalpunctuation"))
        .isNotEmpty();
  }

  @Ignore // TODO
  @Test
  public void scanLineBreak() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb")).isEmpty();

    ucdScanner.scanLineBreak();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb")).isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=breakbefore"));
  }

  @Ignore // TODO
  @Test
  public void scanGraphemeBreakProperty() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    ucdScanner.scanLineBreak();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=ebasegaz"))
        .isEmpty();

    ucdScanner.scanGraphemeBreakProperty();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=ebasegaz"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=ebasegaz"))
    // .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("gcb=ebg"));
  }

  @Ignore // TODO
  @Test
  public void scanSentenceBreakProperty() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    ucdScanner.scanLineBreak();
    ucdScanner.scanGraphemeBreakProperty();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close")).isEmpty();
    ucdScanner.scanSentenceBreakProperty();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=cl"));
  }

  @Ignore // TODO
  @Test
  public void scanWordBreakProperty() throws IOException {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    ucdScanner.scanLineBreak();
    ucdScanner.scanGraphemeBreakProperty();
    ucdScanner.scanSentenceBreakProperty();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote")).isEmpty();
    ucdScanner.scanWordBreakProperty();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("wb=dq"));

  }

  @Ignore // TODO
  @Test
  public void scanDerivedAge() throws IOException {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    ucdScanner.scanLineBreak();
    ucdScanner.scanGraphemeBreakProperty();
    ucdScanner.scanSentenceBreakProperty();
    ucdScanner.scanWordBreakProperty();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=9.0")).isEmpty();
    ucdScanner.scanDerivedAge();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "4.0"))
        .containsExactly("4.0", "v40");
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=9.0")).isNotEmpty();
  }
}
