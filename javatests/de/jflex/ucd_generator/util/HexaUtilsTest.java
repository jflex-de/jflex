package jflex.ucd_generator.util;

import static com.google.common.truth.Truth.assertThat;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import org.junit.Test;

/** Test for {@link HexaUtils}. */
public class HexaUtilsTest {
  @Test
  public void intFromHexa_int() {
    assertThat(HexaUtils.intFromHexa("FF")).isEqualTo(255);
  }

  @Test
  public void intFromHexa_empty() {
    assertThat(HexaUtils.intFromHexa("")).isNull();
  }

  @Test
  public void intFromHexa_other() {
    assertThrows(IllegalArgumentException.class, () -> HexaUtils.intFromHexa("wtf"));
  }
}
