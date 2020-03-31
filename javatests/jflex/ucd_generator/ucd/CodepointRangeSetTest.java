package jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

/** Test for {@link CodepointRangeSet}. */
public class CodepointRangeSetTest {
  @Test
  public void add_disjoined() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(100, 200))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.create(42, 43), CodepointRange.create(100, 200))
        .inOrder();
  }

  @Test
  public void add_contiguous() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(44, 100))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create(42, 100)).inOrder();
  }

  @Test
  public void add_contiguous_outOfOrder() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.createPoint(1))
            .add(CodepointRange.create(44, 100))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.createPoint(1), CodepointRange.create(42, 100))
        .inOrder();
  }

  @Test
  public void substract_disjoinedDoesNothing() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(100, 200))
            .substract(CodepointRange.create(10, 20))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.create(42, 43), CodepointRange.create(100, 200));
  }

  @Test
  public void substract_withinOne_splitIt() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(50, 99))
            .substract(CodepointRange.create(79, 80))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(
            CodepointRange.create(42, 43),
            CodepointRange.create(50, 78),
            CodepointRange.create(81, 99));
  }

  @Test
  public void substract_overlapping() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(1, 5))
            .add(CodepointRange.create(10, 50))
            .add(CodepointRange.create(60, 65))
            .add(CodepointRange.create(70, 99))
            .substract(CodepointRange.create(43, 78))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(
            CodepointRange.create(1, 5),
            CodepointRange.create(10, 42),
            CodepointRange.create(79, 99));
  }

  @Test
  public void substract_self() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(1, 42))
            .substract(CodepointRange.create(1, 42))
            .build();
    assertThat(rangeSet.ranges()).isEmpty();
  }

  @Test
  public void substract_endsEqual() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(917536, 1114111))
            .substract(CodepointRange.create(1114110, 1114111))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create(917536, 1114109));
  }

  @Test
  public void substract_startsEqual() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create('\ud800', '\ufa2d'))
            .substract(CodepointRange.create('\ud800', '\udfff'))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create('\ue000', '\ufa2d'));
  }

  @Test
  public void substract_removeAll() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(50, 99))
            .substract(CodepointRange.create(40, 100))
            .build();
    assertThat(rangeSet.ranges()).isEmpty();
  }
}
