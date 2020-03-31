package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.Version;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** Integration test for the {@link UcdScanner}. */
public class UcdScannerIntegrationTest {

  /**
   * Unicode 5.0 property value: {me}
   *
   * <pre>{@code "\u0488\u0489" + "\u06de\u06de" + "\u20dd\u20e0" + "\u20e2\u20e4"} </pre>
   */
  private static final ImmutableList<CodepointRange> INTERVALS_FOR_GENERALCATEGORY_ME =
      ImmutableList.of(
          CodepointRange.create(0x0488, 0x489),
          CodepointRange.createPoint(0x06de),
          CodepointRange.create(0x20dd, 0x20e0),
          CodepointRange.create(0x20e2, 0x20e4));

  private UcdScanner ucdScanner;

  @Before
  public void ucdScanner() {
    ucdScanner = new UcdScanner(TestedVersions.UCD_VERSION_5_0);
    assertThat(ucdScanner.ucdVersion().version()).isEqualTo(new Version(5, 0, 0));
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
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
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

  @Ignore // TODO
  @Test
  public void scanScripts() throws Exception {
    ucdScanner.scanPropertyAliases();
    ucdScanner.scanPropertyValueAliases();
    ucdScanner.scanUnicodeData();
    ucdScanner.scanPropList();
    ucdScanner.scanDerivedCoreProperties();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam")).isEmpty();

    ucdScanner.scanScripts();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam")).isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlam"))
    //     .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("script=adlm"));
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
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hiragana"))
        .isEmpty();

    ucdScanner.scanScriptExtensions();
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("me"))
        .containsExactlyElementsIn(INTERVALS_FOR_GENERALCATEGORY_ME);
    assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira"))
        .isNotEmpty();
    // TODO(regisd) It seems I'm missing some property value aliases
    // assertThat(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hira"))
    //
    // .isEqualTo(ucdScanner.unicodeData.getPropertyValueIntervals("scriptextensions=hiragana"));
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
