package de.jflex.ucd_generator.model;

import static com.google.common.truth.Truth.assertThat;

import de.jflex.ucd_generator.TestedVersions;
import de.jflex.ucd_generator.ucd.CodepointRange;
import org.junit.Test;

public class UnicodeDataTest {

  @Test
  public void addCompatibilityProperties() {
    UnicodeData unicodeData = new UnicodeData(TestedVersions.UCD_VERSION_6_3.version());
    unicodeData.addBinaryPropertyInterval("nd", '\u0030', '\u0039');
    unicodeData.addBinaryPropertyInterval("hexdigit", '\u0041', '\u0046');
    unicodeData.addBinaryPropertyInterval("zl", '\u2028', '\u2028');
    unicodeData.addBinaryPropertyInterval("zp", '\u2029', '\u2029');
    unicodeData.addBinaryPropertyInterval("whitespace", '\t', '\r');
    unicodeData.addBinaryPropertyInterval("whitespace", '\u2028', '\u2029');
    unicodeData.addCompatibilityProperties();
    assertThat(unicodeData.intervals().get("blank").ranges())
        .contains(CodepointRange.createPoint('\t'));
  }
}
