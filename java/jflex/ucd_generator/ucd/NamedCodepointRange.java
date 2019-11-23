package jflex.ucd_generator.ucd;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class NamedCodepointRange {

  abstract String name();

  abstract CodepointRange range();

  static NamedCodepointRange create(String name, CodepointRange range) {
    return new AutoValue_NamedCodepointRange(name, range);
  }
}
