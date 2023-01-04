/*
 * Copyright (C) 2019-2020 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.ucd;

import com.google.auto.value.AutoValue;
import java.util.Comparator;

@AutoValue
public abstract class NamedCodepointRange<T> {

  public static final Comparator<NamedCodepointRange> START_COMPARATOR =
      (o1, o2) -> CodepointRange.COMPARATOR.compare(o1.range(), o2.range());

  private static final String HEX_FORMAT = "%04X";

  public abstract T name();

  public abstract CodepointRange range();

  public static <T> NamedCodepointRange<T> create(T name, CodepointRange range) {
    return new AutoValue_NamedCodepointRange<T>(name, range);
  }

  public static <T> NamedCodepointRange<T> create(T name, int start, int end) {
    return create(name, CodepointRange.create(start, end));
  }

  @Override
  public final String toString() {
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
