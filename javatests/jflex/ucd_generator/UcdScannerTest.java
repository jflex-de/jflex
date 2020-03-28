package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.Version;
import org.junit.Before;
import org.junit.Test;

/** Integration test for the {@link UcdScanner}. */
public class UcdScannerTest {

  private UcdScanner ucdScanner;

  @Before
  public void ucdScanner() {
    ucdScanner = new UcdScanner(TestedVersions.UCD_VERSION_10);
    assertThat(ucdScanner.ucdVersion().version()).isEqualTo(new Version(10, 0));
  }

  @Test
  public void scanPropertyAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertThat(ucdScanner.unicodeData.getCanonicalPropertyName("ccc"))
        .isEqualTo("canonicalcombiningclass");
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "v90")).containsExactly("v90");
  }

  @Test
  public void scanPropertyValueAliases() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "v90")).containsExactly("v90");

    ucdScanner.scanPropertyValueAliases();
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "v90"))
        .containsExactly("v90", "9.0");
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("wordbreak", "midnum"))
        .containsExactly("midnum", "mn");
  }

  @Test
  public void scanUnicodeData() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("assigned")).isEmpty();

    ucdScanner.scanUnicodeData();
    assertThat(ucdScanner.unicodeData.maximumCodePoint()).isEqualTo(1114111);
    assertThat(ucdScanner.unicodeData.maxCaselessMatchPartitionSize()).isEqualTo(4);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("generalcategory=cc"))
        .containsExactly(CodepointRange.create(0, 31), CodepointRange.create(127, 159));
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("assigned")).hasSize(649);
  }

  @Test
  public void scanPropList() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("otheruppercase")).isEmpty();

    ucdScanner.scanPropList();
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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam")).isEmpty();

    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam")).isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlm"));
  }

  @Test
  public void scanScripExtensions() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hiragana"))
        .isEmpty();

    ucdScanner.scanScriptExtensions();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira"))
    //
    // .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hiragana"));
  }

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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("block=supplementalpunctuation"))
        .isNotEmpty();
  }

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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb")).isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=bb"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("linebreak=breakbefore"));
  }

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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=ebasegaz"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("graphemeclusterbreak=ebasegaz"))
    // .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("gcb=ebg"));
  }

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

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=close"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("sentencebreak=cl"));
  }

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

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("wordbreak=doublequote"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("wb=dq"));

  }

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

    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("age=9.0")).isNotEmpty();
  }
}
