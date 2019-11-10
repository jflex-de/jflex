package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import jflex.ucd_generator.ucd.CodepointRange.Builder;

@AutoValue
public abstract class CodepointRangeSet {

  public abstract ImmutableList<CodepointRange> ranges();

  public static Builder builder() {
    return new AutoValue_CodepointRangeSet.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private ArrayList<CodepointRange.Builder> mRanges = new ArrayList<>();

    abstract ImmutableList.Builder<CodepointRange> rangesBuilder();

    public Builder addAll(List<CodepointRange.Builder> ranges) {
      for (CodepointRange.Builder range : ranges) {
        add(range);
      }
      return this;
    }

    public Builder add(CodepointRange.Builder range) {
      if (!mRanges.isEmpty()) {
        int lastIndex = mRanges.size() - 1;
        CodepointRange.Builder last = mRanges.get(lastIndex);
        if (last.end + 1 == range.start) {
          last.end = range.end;
          return this;
        }
      }
      mRanges.add(range);
      return this;
    }

    protected abstract CodepointRangeSet internalBuild();

    public CodepointRangeSet build() {
      rangesBuilder().addAll(mRanges.stream().map(CodepointRange.Builder::build).iterator());
      return internalBuild();
    }
  }
}
