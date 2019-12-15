package jflex.ucd_generator;

import static com.google.common.truth.Truth.assertThat;

import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.Version;
import org.junit.Before;
import org.junit.Test;

public class UcdScannerTest {

  private UcdScanner ucdScanner;

  @Before
  public void ucdScanner() {
    ucdScanner = new UcdScanner(TestedVersions.UCD_VERSION_10);
    assertThat(ucdScanner.ucdVersion().version()).isEqualTo(new Version(10, 0));
  }

  @Test
  public void scanPropertyAliases_getCanonicalPropertyName() throws Exception {
    ucdScanner.scanPropertyAliases();
    assertThat(ucdScanner.unicodeData.getCanonicalPropertyName("ccc"))
        .isEqualTo("canonicalcombiningclass");
    assertThat(ucdScanner.unicodeData.getPropertyValueAliases("age", "v90")).containsExactly("v90");
  }

  @Test
  public void scanPropertyValueAliases_getPropertyValueAliases() throws Exception {
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
}
