/*
 * Copyright (C) 2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd_generator.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.jflex.ucd.CodepointRange;
import de.jflex.ucd_generator.TestedVersions;
import de.jflex.version.Version;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Integration test for the {@link UcdScanner} on Unicode 6. */
public class UcdScannerIntegrationTest {

  /**
   * Unicode 6.0 property value: {me}
   *
   * <pre>{@code
   * "\u0488\u0489" + "\u20dd\u20e0" + "\u20e2\u20e4" + "\ua670\ua672"
   * }</pre>
   */
  private static final List<CodepointRange> INTERVALS_FOR_GENERALCATEGORY_ME =
      Arrays.asList(
          CodepointRange.create('\u0488', '\u0489'),
          CodepointRange.create('\u20dd', '\u20e0'),
          CodepointRange.create('\u20e2', '\u20e4'),
          CodepointRange.create('\ua670', '\ua672'));

  private UcdScanner ucdScanner;

  @BeforeEach
  public void ucdScanner() {
    ucdScanner = new UcdScanner(TestedVersions.UCD_VERSION_6_3);
    assertEquals(new Version(6, 3, 0), ucdScanner.ucdVersion().version());
  }

  @Test
  public void scanPropertyAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertEquals("canonicalcombiningclass", ucdScanner.unicodeData.getCanonicalPropertyName("ccc"));
    assertEquals(
        new HashSet<>(Arrays.asList("sc", "script")),
        ucdScanner.unicodeData.getPropertyAliases("script"));
    assertEquals(
        new HashSet<>(Arrays.asList("bc", "bidiclass")),
        ucdScanner.unicodeData.getPropertyAliases("Bidi_Class"));
  }

  @Test
  public void scanPropertyValueAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertEquals(
        new HashSet<>(Arrays.asList("at")),
        ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "at"));
    assertEquals(
        new HashSet<>(Arrays.asList("aterm")),
        ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "aterm"));

    ucdScanner.scanPropertyValueAliases();
    assertEquals(
        new HashSet<>(Arrays.asList("at", "aterm")),
        ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "at"));
    assertEquals(
        new HashSet<>(Arrays.asList("at", "aterm")),
        ucdScanner.unicodeData.getPropertyValueAliases("sentencebreak", "aterm"));
  }

  @Test
  public void scanUnicodeData() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    // generalcategory=me
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("me").isEmpty());

    ucdScanner.scanUnicodeData();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertEquals(1114111, ucdScanner.unicodeData.maximumCodePoint());
    assertEquals(4, ucdScanner.unicodeData.maxCaselessMatchPartitionSize());
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("gc"),
        ucdScanner.unicodeData.getPropertyValueIntervals("generalcategory"));
  }

  @Test
  public void scanPropList() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("otheruppercase").isEmpty());

    ucdScanner.scanPropList();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertTrue(
        ucdScanner
            .unicodeData
            .getPropertyValueIntervals("otheruppercase")
            .contains(CodepointRange.create(8544, 8559)));
  }

  @Test
  public void scanDerivedCoreProperties() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("idcontinue").isEmpty());

    ucdScanner.scanDerivedCoreProperties();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertTrue(
        ucdScanner
            .unicodeData
            .getPropertyValueIntervals("idcontinue")
            .contains(CodepointRange.create(48, 57)));
  }

  @Test
  public void scanScripts() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("arabic").isEmpty());

    ucdScanner.scanScripts();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertFalse(ucdScanner.unicodeData.getPropertyValueIntervals("arabic").isEmpty());
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
    assertFalse(
        ucdScanner.unicodeData.getPropertyValueIntervals("hiragana").isEmpty()); // from scanScripts
    assertFalse(ucdScanner.unicodeData.getPropertyValueIntervals("katakana").isEmpty());
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("scx=hira").isEmpty());
    // 3034 has Script=Common
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana"));
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana"));
    assertTrue(ucdScanner.unicodeData.codePointInProperty(0x3034, "common"));

    ucdScanner.scanScriptExtensions();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    // Script_Extensions does not change Script
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x3034, "hiragana"));
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x3034, "katakana"));
    // 3031..3035    ; Hira Kana # Lm   [5] VERTICAL KANA REPEAT MARK..
    assertTrue(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=hira"));
    assertTrue(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=kana"));
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x3034, "scx=common"));
    // Code points without extension get their Script as Script_Extensions
    assertTrue(ucdScanner.unicodeData.codePointInProperty('a', "scx=latin"));
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("Script_Extensions=Hiragana"),
        ucdScanner.unicodeData.getPropertyValueIntervals("scx=hira"));
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
    assertTrue(
        ucdScanner
            .unicodeData
            .getPropertyValueIntervals("block=supplementalpunctuation")
            .isEmpty());

    ucdScanner.scanBlocks();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertFalse(
        ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira").isEmpty());
    // 2E00..2E7F; Supplemental Punctuation
    assertEquals(
        Arrays.asList(CodepointRange.create(0x2E00, 0x2E7F)),
        ucdScanner.unicodeData.getPropertyValueIntervals("block=supplementalpunctuation"));
    // blk; Sup_Punctuation                  ; Supplemental_Punctuation
    assertEquals(
        Arrays.asList(CodepointRange.create(0x2E00, 0x2E7F)),
        ucdScanner.unicodeData.getPropertyValueIntervals("blk=Sup_Punctuation"));
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
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb").isEmpty());

    ucdScanner.scanLineBreak();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertFalse(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb").isEmpty());
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=breakbefore"),
        ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb"));
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("Line_Break=Break_Before"),
        ucdScanner.unicodeData.getPropertyValueIntervals("lb=BB"));
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
    assertTrue(
        ucdScanner
            .unicodeData
            .getPropertyValueIntervals("graphemeclusterbreak=regionalindicator")
            .isEmpty());

    ucdScanner.scanGraphemeBreakProperty();
    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    // 1F1E6..1F1FF  ; Regional_Indicator # So  [26] REGIONAL INDICATOR SYMBOL LETTER A..Z
    assertEquals(
        Arrays.asList(CodepointRange.create(0x1F1E6, 0x1F1FF)),
        ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=regionalindicator"));
    assertEquals(
        Arrays.asList(CodepointRange.create(0x1F1E6, 0x1F1FF)),
        ucdScanner.unicodeData.getPropertyValueIntervals("gcb=ri"));
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

    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close").isEmpty());
    ucdScanner.scanSentenceBreakProperty();

    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertFalse(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close").isEmpty());
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("sb=cl"),
        ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"));
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

    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote").isEmpty());
    ucdScanner.scanWordBreakProperty();

    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    // 0022          ; Double_Quote # Po       QUOTATION MARK
    assertEquals(
        Arrays.asList(CodepointRange.createPoint('"')),
        ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"));
    assertEquals(
        Arrays.asList(CodepointRange.createPoint('"')),
        ucdScanner.unicodeData.getPropertyValueIntervals("wb=dq"));
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

    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3").isEmpty());
    ucdScanner.scanDerivedAge();

    assertEquals(
        INTERVALS_FOR_GENERALCATEGORY_ME, ucdScanner.unicodeData.getPropertyValueIntervals("me"));
    assertEquals(
        new HashSet<>(Arrays.asList("4.0", "v40")),
        ucdScanner.unicodeData.getPropertyValueAliases("age", "4.0"));
    assertFalse(ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3").isEmpty());
    assertEquals(
        ucdScanner.unicodeData.getPropertyValueIntervals("age=v63"),
        ucdScanner.unicodeData.getPropertyValueIntervals("age=6.3"));
    // Age includes all code points of the previous versions
    assertTrue(ucdScanner.unicodeData.codePointInProperty('a', "age=1.1"));
    assertTrue(ucdScanner.unicodeData.codePointInProperty('a', "age=6.3"));
    // 061C          ; 6.3 #       ARABIC LETTER MARK
    assertFalse(ucdScanner.unicodeData.codePointInProperty(0x061C, "age=6.2"));
    assertTrue(ucdScanner.unicodeData.codePointInProperty(0x061C, "age=6.3"));
    // Future versions are not known
    assertTrue(ucdScanner.unicodeData.getPropertyValueIntervals("age=9.0").isEmpty());
  }
}
