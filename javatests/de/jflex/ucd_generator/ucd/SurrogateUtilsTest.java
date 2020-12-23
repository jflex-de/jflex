package de.jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.isSurrogate;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.removeSurrogates;

import com.google.common.collect.ImmutableList;
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

  @Test
  public void removeSurrogate_surrogates() {
    assertThat(removeSurrogates(0xd83f,0xdffe)).isEmpty();
  }

  @Test
  public void removeSurrogate_notSurrogates() {
    assertThat(removeSurrogates(0x0000,0x01f5))
        .containsAllIn(ImmutableList.of(CodepointRange.create(0x0000, 0x01f5)));
  }
}
