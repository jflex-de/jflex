package jflex.ucd_generator.scanner.model;

import static jflex.ucd_generator.util.SurrogateUtils.isSurrogate;
import static jflex.ucd_generator.util.SurrogateUtils.removeSurrogates;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableSortedMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jflex.ucd_generator.ucd.CodepointRange;
import jflex.ucd_generator.ucd.CodepointRangeSet;
import jflex.ucd_generator.ucd.MutableCodepointRange;
import jflex.ucd_generator.util.PropertyNameNormalizer;

@AutoValue
public abstract class PropertyValueIntervals implements Iterable<String> {
  public abstract ImmutableSortedMap<String, CodepointRangeSet> propertyValueIntervals();

  static Builder builder() {
    return new AutoValue_PropertyValueIntervals.Builder();
  }

  Builder toBuilder() {
    Builder builder = builder();
    for (Map.Entry<String, CodepointRangeSet> entry : propertyValueIntervals().entrySet()) {
      builder.mPropertyValueIntervals.put(entry.getKey(), entry.getValue().toMutableList());
    }
    return builder;
  }

  @Override
  public Iterator<String> iterator() {
    return propertyValueIntervals().keySet().iterator();
  }

  @AutoValue.Builder
  abstract static class Builder {
    private final Map<String, List<MutableCodepointRange>> mPropertyValueIntervals =
        new HashMap<>();

    abstract PropertyValueIntervals internalBuild();

    PropertyValueIntervals build() {
      addInternalPropertyValueIntervals();
      return internalBuild();
    }

    abstract ImmutableSortedMap.Builder<String, CodepointRangeSet> propertyValueIntervalsBuilder();

    private void addInternalPropertyValueIntervals() {
      for (String propName : mPropertyValueIntervals.keySet()) {
        CodepointRangeSet rangeSet =
            CodepointRangeSet.builder().addAll(mPropertyValueIntervals.get(propName)).build();
        propertyValueIntervalsBuilder().put(propName, rangeSet);
      }
    }

    private void addCompatibilityProperties() {
      propertyValueIntervalsBuilder().put("blank", createBlankSet());
    }

    private CodepointRangeSet createBlankSet() {
      return CodepointRangeSet.builder()
          .addAll(getIntervals("Zs").orElse(mPropertyValueIntervals.get("whitespace")))
          .build();
    }

    private List<MutableCodepointRange> getOrCreateIntervals(String propName) {
      return mPropertyValueIntervals.computeIfAbsent(propName, k -> new ArrayList<>());
    }

    private Optional<List<MutableCodepointRange>> getIntervals(String propName) {
      return Optional.ofNullable(mPropertyValueIntervals.get(propName));
    }

    /**
     * Given a binary property name, and starting and ending code points, adds the interval to the
     * {@link #propertyValueIntervals} map.
     *
     * @param propName The property name, e.g. "Assigned".
     * @param startCodePoint The first code point in the interval.
     * @param endCodePoint The last code point in the interval.
     * @param propertyNameNormalizer
     */
    public Builder addPropertyInterval(
        String propName,
        int startCodePoint,
        int endCodePoint,
        PropertyNameNormalizer propertyNameNormalizer) {
      propName = propertyNameNormalizer.getCanonicalPropertyName(propName);
      if (isSurrogate(propName)) {
        // Skip surrogates
        return this;
      }
      List<CodepointRange> ranges = removeSurrogates(startCodePoint, endCodePoint);
      if (ranges.isEmpty()) {
        return this;
      }
      List<MutableCodepointRange> intervals = getOrCreateIntervals(propName);
      for (CodepointRange range : ranges) {
        // UnicodeData-1.1.5.txt does not list the end point for the Unified Han
        // range (starting point is listed as U+4E00).  This is U+9FFF according
        // to <http://unicode.org/Public/TEXT/OLDAPIX/CHANGES.TXT>:
        //
        //    U+4E00 ^ U+9FFF		20,992	I-ZONE Ideographs
        //
        // U+4E00 is listed in UnicodeData-1.1.5.txt as having the "Lo" property
        // value, as are the previous code points, so to include
        // [ U+4E00 - U+9FFF ], this interval should be extended to U+9FFF.
        // TODO
        //        if (range.end() == 0x4E00 && Objects.equalsmajorMinor, "1.1")) {
        //          range.end = 0x9FFF;
        //        }
        intervals.add(new MutableCodepointRange(startCodePoint, endCodePoint));
      }
      return this;
    }

    Builder addPropertyInterval(
        String propName,
        String propValue,
        int startCodePoint,
        int endCodePoint,
        PropertyNameNormalizer propertyNameNormalizer) {
      return addPropertyInterval(
          propName + "=" + propName, startCodePoint, endCodePoint, propertyNameNormalizer);
    }
  }
}
