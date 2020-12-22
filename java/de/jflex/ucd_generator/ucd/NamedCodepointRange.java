package de.jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class NamedCodepointRange {

  public static final Comparator<NamedCodepointRange> START_COMPARATOR =
      (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());

  public abstract String name();

  public abstract CodepointRange range();

  static NamedCodepointRange create(String name, CodepointRange range) {
    return new AutoValue_NamedCodepointRange(name, range);
  }

  public static NamedCodepointRange create(String name, int start, int end) {
    return create(name, CodepointRange.create(start, end));
  }

  public int start() {
    return range().start();
  }

  public int end() {
    return range().end();
  }
}
