package de.jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.containsSurrogate;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.isSurrogateProperty;
import static de.jflex.ucd_generator.ucd.SurrogateUtils.removeSurrogates;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

/** Test for {@link SurrogateUtils}. */
public class SurrogateUtilsTest {
  @Test
  public void isSurrogate_not() {
    assertThat(isSurrogateProperty("block=generalcategory")).isFalse();
  }

  @Test
  public void isSurrogate_sc() {
    assertThat(isSurrogateProperty("cs")).isTrue();
    assertThat(isSurrogateProperty("csxxx")).isFalse();
    assertThat(isSurrogateProperty("xxxcs")).isFalse();
  }

  @Test
  public void isSurrogate_surrogate() {
    assertThat(isSurrogateProperty("block=surrogate")).isTrue();
    assertThat(isSurrogateProperty("block=hightprivateusesurrogates")).isTrue();
  }

  @Test
  public void removeSurrogate_surrogates() {
    assertThat(removeSurrogates(0xd83f, 0xdffe)).isEmpty();
  }

  @Test
  public void removeSurrogate_notSurrogates() {
    assertThat(removeSurrogates(0x0000, 0x01f5))
        .containsAllIn(ImmutableList.of(CodepointRange.create(0x0000, 0x01f5)));
  }

  @Test
  public void containsSurrogates_before() {
    assertThat(containsSurrogate(CodepointRange.create(0x0000, 0x01f5))).isFalse();
  }

  @Test
  public void containsSurrogates_within() {
    assertThat(containsSurrogate(CodepointRange.create(0xD801, 0xD802))).isTrue();
    assertThat(containsSurrogate(CodepointRange.create(0xd83f, 0xdffe))).isTrue();
  }

  @Test
  public void containsSurrogates_endOverlaps() {
    assertThat(containsSurrogate(CodepointRange.create(0x0000, 0xD802))).isTrue();
  }

  @Test
  public void containsSurrogates_after() {
    assertThat(containsSurrogate(CodepointRange.create(0xE0000, 0xE0001))).isFalse();
    assertThat(containsSurrogate(CodepointRange.create(0xE0000, 0xF8FF))).isFalse();
  }

  @Test
  public void containsSurrogates_larger() {
    assertThat(containsSurrogate(CodepointRange.create(0x20ad, 0xfffb))).isTrue();
  }
}
