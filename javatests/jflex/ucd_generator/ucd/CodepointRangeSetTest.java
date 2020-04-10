package jflex.ucd_generator.ucd;

import static com.google.common.truth.Truth.assertThat;

import jflex.testing.assertion.MoreAsserts;
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
  public void add_contiguous_inOrder() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(44, 100))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create(42, 100)).inOrder();
  }

  @Test
  public void add_contiguous_inOrder2() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create('\000', '\u001f'))
            .add(CodepointRange.create('\u0020', '\u007e'))
            .add(CodepointRange.create('\u007f', '\u009f'))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.create('\000', '\u009f'))
        .inOrder();
  }

  @Test
  public void add_contiguous_outOfOrder() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.createPoint(1))
            .add(CodepointRange.create(44, 100))
            .add(CodepointRange.create(1000, 2000))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(
            CodepointRange.createPoint(1),
            CodepointRange.create(42, 100),
            CodepointRange.create(1000, 2000))
        .inOrder();
  }

  @Test
  public void add_contiguous_outOfOrder2() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create('\000', '\u001f'))
            .add(CodepointRange.create('\u007f', '\u009f'))
            .add(CodepointRange.create('\u0020', '\u007e'))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(CodepointRange.create('\000', '\u009f'))
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
            .add(CodepointRange.create(100, 101))
            .substract(CodepointRange.create(1, 42))
            .build();
    assertThat(rangeSet.ranges()).containsExactly(CodepointRange.create(100, 101));
  }

  @Test
  public void substract_endsEqual() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create('\t', '\r'))
            .add(CodepointRange.create('\u2028', '\u2029'))
            .substract(CodepointRange.create('\t' + 1, '\r'))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(
            CodepointRange.createPoint('\t'), CodepointRange.create('\u2028', '\u2029'));
  }

  @Test
  public void substract_lastPoint() {
    CodepointRangeSet rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create('\u1ff6', '\u1fff'))
            .add(CodepointRange.create('\u2029', '\u202A'))
            .substract(CodepointRange.createPoint('\u1fff'))
            .build();
    assertThat(rangeSet.ranges())
        .containsExactly(
            CodepointRange.create('\u1ff6', '\u1ffe'), CodepointRange.create('\u2029', '\u202A'));
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
    CodepointRangeSet.Builder rangeSet =
        CodepointRangeSet.builder()
            .add(CodepointRange.create(42, 43))
            .add(CodepointRange.create(50, 99))
            .substract(CodepointRange.create(40, 100));
    MoreAsserts.assertThrows(IllegalStateException.class, rangeSet::build);
  }

  @Test
  public void intersection() {
    CodepointRangeSet.Builder rangeSetBuilder =
        CodepointRangeSet.builder()
            .add(CodepointRange.createPoint('\u0020'))
            .add(CodepointRange.createPoint('\u0085'))
            .add(CodepointRange.create('\u2000', '\u200a'));
    assertThat(rangeSetBuilder.intersection(CodepointRange.createPoint('\u0085')))
        .containsExactly(MutableCodepointRange.createPoint('\u0085'));
  }
}
