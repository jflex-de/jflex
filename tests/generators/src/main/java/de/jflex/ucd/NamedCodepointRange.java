/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import java.util.Comparator;
import java.util.Objects;

public final class NamedCodepointRange<T> {

  public static final Comparator<NamedCodepointRange> START_COMPARATOR =
      (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());

  private static final String HEX_FORMAT = "%04X";

  private final T name;
  private final CodepointRange range;

  private NamedCodepointRange(T name, CodepointRange range) {
    this.name = Objects.requireNonNull(name, "name");
    this.range = Objects.requireNonNull(range, "range");
  }

  public T name() {
    return name;
  }

  public CodepointRange range() {
    return range;
  }

  public static <T> NamedCodepointRange<T> create(T name, CodepointRange range) {
    return new NamedCodepointRange<T>(name, range);
  }

  public static <T> NamedCodepointRange<T> create(T name, int start, int end) {
    return create(name, CodepointRange.create(start, end));
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof NamedCodepointRange)) {
      return false;
    }
    NamedCodepointRange<?> other = (NamedCodepointRange<?>) o;
    return name.equals(other.name) && range.equals(other.range);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, range);
  }

  @Override
  public String toString() {
    return String.format("%04X..%04X; %s", range().start(), range().end(), name());
  }

  public int start() {
    return range().start();
  }

  public int end() {
    return range().end();
  }

  public String hexStart() {
    return String.format(HEX_FORMAT, range().start());
  }

  public String hexEnd() {
    return String.format(HEX_FORMAT, range().end());
  }

  public boolean isSurrogate() {
    return SurrogateUtils.containsSurrogate(range());
  }
}
