package jflex;

import java.util.List;
import java.util.ArrayList;

public class NamedRangeSet {
  private List<NamedRange> ranges;
  
  public NamedRangeSet(List<NamedRange> ranges) {
    this.ranges = ranges;
  }
  
  public NamedRangeSet(NamedRange range) {
    ranges = new ArrayList<NamedRange>();
    ranges.add(range);
  }
  
  public NamedRangeSet() {
    ranges = new ArrayList<NamedRange>();
  }
  
  public List<NamedRange> getRanges() {
    return ranges;
  }

  /**
   * Assumptions: ranges are disjoint, non-contiguous, and ordered.
   * 
   * Names of ranges are not necessarily preserved.
   * 
   * @param other The ranges to add
   */
  public void add(NamedRangeSet other) {
    List<NamedRange> newRanges = new ArrayList<NamedRange>();
    int thisPos = 0;
    int otherPos = 0;
    while (thisPos < ranges.size() || otherPos < other.ranges.size()) {
      NamedRange toAdd;
      if (thisPos < ranges.size() 
          && (otherPos >= other.ranges.size()
              || ranges.get(thisPos).start <= other.ranges.get(otherPos).start )) {
        toAdd = ranges.get(thisPos++);
      } else {
        toAdd = other.ranges.get(otherPos++);
      }
      if (newRanges.isEmpty()
          || toAdd.start > newRanges.get(newRanges.size() - 1).end + 1) {
        newRanges.add(new NamedRange(toAdd.start, toAdd.end, toAdd.name));
      } else {
        newRanges.get(newRanges.size() - 1).end = toAdd.end;
      }
    }
    this.ranges = newRanges;
  }

  /**
   * Assumptions: ranges are disjoint, non-contiguous, and ordered.
   * 
   * Names of ranges are not necessarily preserved.
   * 
   * @param other The ranges to remove
   */
  public void sub(NamedRangeSet other) {
    List<NamedRange> newRanges = new ArrayList<NamedRange>();
    OUTER_LOOP: for (NamedRange range : ranges) {
      NamedRange thisRange = new NamedRange(range.start, range.end, range.name);
      for (NamedRange otherRange : other.ranges) {
        if (otherRange.start <= thisRange.start
            && otherRange.end >= thisRange.end) {
          // otherRange is a superset of thisRange
          continue OUTER_LOOP; // no need to check other 
        }
        if (otherRange.end < thisRange.start
            || otherRange.start > thisRange.end) {
          // thisRange and otherRange do not intersect
          // do nothing here
        } else if (otherRange.start <= thisRange.start) {
          // otherRange intersects with the beginning of thisRange
          thisRange.start = otherRange.end + 1;
        } else if (otherRange.end >= thisRange.end) {
          // otherRange intersects with the end of thisRange
          thisRange.end = otherRange.start - 1;
        } else {
          // otherRange is a strict subset of thisRange
          newRanges.add(new NamedRange
            (thisRange.start, otherRange.start - 1, thisRange.name));
          thisRange.start = otherRange.end + 1;
        }
      }
      newRanges.add(thisRange);
    }
    ranges = newRanges;
  }

  /**
   * Deep copy 
   * @return deep copy of this set of named ranges 
   */
  public NamedRangeSet copy() {
    NamedRangeSet copy = new NamedRangeSet();
    for (NamedRange range : ranges) {
      copy.ranges.add(new NamedRange(range.start, range.end, range.name));
    }
    return copy;
  }
}
