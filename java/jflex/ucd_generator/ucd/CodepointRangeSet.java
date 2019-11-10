package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableRangeSet;
import java.util.Collection;

@AutoValue
public abstract class CodepointRangeSet {

  public abstract ImmutableRangeSet<Integer> rangeSet();

  public static Builder builder() {
    return new AutoValue_CodepointRangeSet.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    abstract ImmutableRangeSet.Builder<Integer> rangeSetBuilder();

    public Builder addAll(Collection<CodepointRange> codepointRanges) {
      for (CodepointRange range : codepointRanges) {
        rangeSetBuilder().add(range.toRange());
      }
      return this;
    }

    public abstract CodepointRangeSet build();
  }
}
