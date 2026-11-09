/*
 * Copyright (C) 2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_generator.TestedVersions;
import de.jflex.version.Version;
import java.io.IOException;
import org.junit.Before;
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

  @Test
  public void scanScriptExtensions() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("hiragana"))
        .isNotEmpty(); // from scanScripts
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("katakana")).isNotEmpty();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scx=hira")).isEmpty();
    // 3034 has Script=Common
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana")).isFalse();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana")).isFalse();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "common")).isTrue();

    ucdScanner.scanScriptExtensions();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    // Script_Extensions does not change Script
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana")).isFalse();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana")).isFalse();
    // 3031..3035    ; Hira Kana # Lm   [5] VERTICAL KANA REPEAT MARK..
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=hira")).isTrue();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=kana")).isTrue();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=common")).isFalse();
    // Code points without extension get their Script as Script_Extensions
    assertThat(ucdScanner.unicodeData.codePointInProperty('a', "scx=latin")).isTrue();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scx=hira"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("Script_Extensions=Hiragana"));
  }

  @Test
  public void scanBlocks() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
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
    // 2E00..2E7F; Supplemental Punctuation
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("block=supplementalpunctuation"))
        .containsExactly(CodepointRange.create(0x2E00, 0x2E7F));
    // blk; Sup_Punctuation                  ; Supplemental_Punctuation
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("blk=Sup_Punctuation"))
        .containsExactly(CodepointRange.create(0x2E00, 0x2E7F));
  }

  @Test
  public void scanLineBreak() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=breakbefore"));
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("lb=BB"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("Line_Break=Break_Before"));
  }

  @Test
  public void scanGraphemeBreakProperty() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    ucdScanner.scanScriptExtensions();
    ucdScanner.scanBlocks();
    ucdScanner.scanLineBreak();
    assertThat(
            ucdScanner.unicodeData.getPropertyValueIntervals(
                "graphemeclusterbreak=regionalindicator"))
        .isEmpty();

    ucdScanner.scanGraphemeBreakProperty();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    // 1F1E6..1F1FF  ; Regional_Indicator # So  [26] REGIONAL INDICATOR SYMBOL LETTER A..Z
    assertThat(
            ucdScanner.unicodeData.getPropertyValueIntervals(
                "graphemeclusterbreak=regionalindicator"))
        .containsExactly(CodepointRange.create(0x1F1E6, 0x1F1FF));
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("gcb=ri"))
        .containsExactly(CodepointRange.create(0x1F1E6, 0x1F1FF));
  }

  @Test
  public void scanSentenceBreakProperty() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("sb=cl"));
  }

  @Test
  public void scanWordBreakProperty() throws IOException {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
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
    // 0022          ; Double_Quote # Po       QUOTATION MARK
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"))
        .containsExactly(CodepointRange.createPoint('"'));
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wb=dq"))
        .containsExactly(CodepointRange.createPoint('"'));
  }

  @Test
  public void scanDerivedAge() throws IOException {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.cloneScriptsToScriptExtensions();
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

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3")).isEmpty();
    ucdScanner.scanDerivedAge();

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "4.0"))
        .containsExactly("4.0", "v40");
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3")).isNotEmpty();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3"))
        .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("age=v63"));
    // Age includes all code points of the previous versions
    assertThat(ucdScanner.unicodeData.codePointInProperty('a', "age=1.1")).isTrue();
    assertThat(ucdScanner.unicodeData.codePointInProperty('a', "age=6.3")).isTrue();
    // 061C          ; 6.3 #       ARABIC LETTER MARK
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x061C, "age=6.2")).isFalse();
    assertThat(ucdScanner.unicodeData.codePointInProperty(0x061C, "age=6.3")).isTrue();
    // Future versions are not known
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=9.0")).isEmpty();
  }
}
