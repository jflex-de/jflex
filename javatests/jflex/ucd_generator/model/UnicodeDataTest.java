package jflex.ucd_generator.model;

import static com.google.common.truth.Truth.assertThat;

import jflex.ucd_generator.TestedVersions;
import jflex.ucd_generator.ucd.CodepointRange;
import org.junit.Test;

public class UnicodeDataTest {

  @Test
  public void addCompatibilityProperties() {
    UnicodeData unicodeData = new UnicodeData(TestedVersions.UCD_VERSION_6_0.version());
    unicodeData.addPropertyInterval("nd", '\u0030', '\u0039');
    unicodeData.addPropertyInterval("hexdigit", '\u0041', '\u0046');
    unicodeData.addPropertyInterval("zl", '\u2028', '\u2028');
    unicodeData.addPropertyInterval("zp", '\u2029', '\u2029');
    unicodeData.addPropertyInterval("whitespace", '\t', '\r');
    unicodeData.addPropertyInterval("whitespace", '\u2028', '\u2029');
    unicodeData.addCompatibilityProperties();
    assertThat(unicodeData.intervals().get("blank").ranges())
        .contains(CodepointRange.createPoint('\t'));
  }
}
