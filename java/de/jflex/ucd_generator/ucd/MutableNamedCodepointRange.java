package de.jflex.ucd_generator.ucd;

import java.util.Comparator;

public class MutableNamedCodepointRange {

  public static final Comparator<MutableNamedCodepointRange> START_COMPARATOR =
      (cp1, cp2) ->
          MutableCodepointRange.COMPARATOR_START_POINT.compare(
              cp1.codepointRange, cp2.codepointRange);

  public final String name;
  final MutableCodepointRange codepointRange;

  public MutableNamedCodepointRange(String name, MutableCodepointRange codepointRange) {
    this.name = name;
    this.codepointRange = codepointRange;
  }

  public static MutableNamedCodepointRange create(String name, int start, int end) {
    return new MutableNamedCodepointRange(name, MutableCodepointRange.create(start, end));
  }

  public int start() {
    return codepointRange.start;
  }

  public int end() {
    return codepointRange.end;
  }
}
