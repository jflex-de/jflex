package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class CodepointRangeSet {

  public abstract ImmutableList<CodepointRange> ranges();

  public static Builder builder() {
    return new AutoValue_CodepointRangeSet.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    private ArrayList<MutableCodepointRange> mRanges = new ArrayList<>();

    abstract ImmutableList.Builder<CodepointRange> rangesBuilder();

    public Builder addAll(List<MutableCodepointRange> ranges) {
      for (MutableCodepointRange range : ranges) {
        add(range);
      }
      return this;
    }

    public Builder add(MutableCodepointRange range) {
      if (!mRanges.isEmpty()) {
        int lastIndex = mRanges.size() - 1;
        MutableCodepointRange last = mRanges.get(lastIndex);
        if (last.end + 1 == range.start) {
          last.end = range.end;
          return this;
        }
      }
      mRanges.add(range);
      return this;
    }

    abstract CodepointRangeSet internalBuild();

    public CodepointRangeSet build() {
      rangesBuilder().addAll(mRanges.stream().map(CodepointRange::of).iterator());
      return internalBuild();
    }
  }
}
