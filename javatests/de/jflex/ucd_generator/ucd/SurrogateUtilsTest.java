package de.jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.isSurrogate;

import org.junit.Test;

/** Test for {@link SurrogateUtils}. */
public class SurrogateUtilsTest {
  @Test
  public void isSurrogate_not() {
    assertThat(isSurrogate("block=generalcategory")).isFalse();
  }

  @Test
  public void isSurrogate_sc() {
    assertThat(isSurrogate("cs")).isTrue();
    assertThat(isSurrogate("csxxx")).isFalse();
    assertThat(isSurrogate("xxxcs")).isFalse();
  }

  @Test
  public void isSurrogate_surrogate() {
    assertThat(isSurrogate("block=surrogate")).isTrue();
    assertThat(isSurrogate("block=hightprivateusesurrogates")).isTrue();
  }
}
