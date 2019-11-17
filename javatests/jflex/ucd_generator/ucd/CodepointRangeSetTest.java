package jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class CodepointRangeSetTest {
  @Test
  public void add_disjoined() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(new MutableCodepointRange(42, 43))
            .add(new MutableCodepointRange(100, 200))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.create(42, 43), CodepointRange.create(100, 200))
        .inOrder();
  }

  @Test
  public void add_contiguous() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(new MutableCodepointRange(42, 43))
            .add(new MutableCodepointRange(44, 100))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create(42, 100)).inOrder();
  }
}
