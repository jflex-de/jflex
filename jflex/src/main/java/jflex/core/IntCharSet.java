/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PrimitiveIterator;
import jflex.chars.Interval;
import jflex.chars.Interval.IntervalIterator;
import jflex.core.unicode.UnicodeProperties;

/**
 * Mutable Char Set implemented with intervals.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.0-SNAPSHOT
 */
public final class IntCharSet implements Iterable<Integer> {

  private static final boolean DEBUG = false;

  /* invariant: all intervals are disjoint, ordered */
  private List<Interval> intervals = new ArrayList<>();

  /** for iterating over the char set */
  private int pos;

  /** Constructs an empty char set. */
  public IntCharSet() {}

  /** Creates a charset that contains only one interval. */
  public static IntCharSet of(Interval interval) {
    IntCharSet charset = new IntCharSet();
    charset.intervals.add(interval);
    if (DEBUG) assert charset.invariants();
    return charset;
  }

  /**
   * Creates a charset that contains the given intervals.
   *
   * <p>The intervals must be sorted and disjointed. Use {@link #add(Interval)} otherwise.
   */
  public static IntCharSet of(List<Interval> intervals) {
    IntCharSet charset = new IntCharSet();
    charset.intervals.addAll(intervals);
    return charset;
  }

  /**
   * Creates a charset that contains only one interval, given by its {@code start} and {@code end}
   * values.
   */
  public static IntCharSet ofCharacterRange(int start, int end) {
    return of(new Interval(start, end));
  }

  /** Creates a char set that contains only the given character. */
  public static IntCharSet ofCharacter(int singleChar) {
    return of(Interval.ofCharacter(singleChar));
  }

  /**
   * Creates the set of all characters.
   *
   * @return a new IntCharSet that contains all characters.
   */
  static IntCharSet allChars() {
    return IntCharSet.ofCharacterRange(0, CharClasses.maxChar);
  }

  /**
   * The set of new-line characters.
   *
   * @return a new IntCharSet that contains all characters that are considered a new-line char in
   *     Java.
   */
  public static IntCharSet nlChars() {
    IntCharSet set = new IntCharSet();
    set.intervals.add(new Interval('\n', '\r'));
    set.intervals.add(Interval.ofCharacter('\u0085'));
    set.intervals.add(new Interval('\u2028', '\u2029'));
    return set;
  }

  /**
   * Returns the index of the interval that contains the character {@code c}.
   *
   * <p>Binary search which interval contains the given character.
   *
   * @param c the character to search for
   * @return the index of the enclosing interval, or -1 if no such interval
   */
  private int indexOf(int c) {
    int start = 0;
    int end = intervals.size() - 1;

    while (start <= end) {
      int check = (start + end) / 2;
      Interval i = intervals.get(check);

      if (start == end) {
        return i.contains(c) ? start : -1;
      }

      if (c < i.start) {
        end = check - 1;
        continue;
      }

      if (c > i.end) {
        start = check + 1;
        continue;
      }

      if (DEBUG) assert intervals.get(check).contains(c);
      return check;
    }

    if (DEBUG) {
      for (Interval i : intervals) assert (!i.contains(c));
    }
    return -1;
  }

  /** Merges the given set into this one. */
  public void add(IntCharSet set) {
    for (Interval interval : set.intervals) {
      add(interval);
    }
  }

  /**
   * Adds a single interval to this IntCharSet.
   *
   * @param interval a {@link jflex.chars.Interval} object.
   */
  public void add(Interval interval) {
    if (DEBUG) assert interval.invariants();

    int size = intervals.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervals.get(i);

      if (elem.end + 1 < interval.start) {
        continue;
      }

      if (elem.contains(interval)) {
        if (DEBUG) assert invariants();
        return;
      }

      if (elem.start > interval.end + 1) {
        intervals.add(i, Interval.copyOf(interval));
        if (DEBUG) assert invariants();
        return;
      }

      if (interval.start < elem.start) {
        elem.start = interval.start;
      }

      if (interval.end <= elem.end) {
        if (DEBUG) assert invariants();
        return;
      }

      elem.end = interval.end;

      i++;
      // delete all x with x.contains( interval.end )
      while (i < size) {
        Interval x = intervals.get(i);
        if (x.start > elem.end + 1) {
          if (DEBUG) assert invariants();
          return;
        }

        if (x.end > elem.end) {
          elem.end = x.end;
        }
        intervals.remove(i);
        size--;
      }

      if (DEBUG) assert invariants();
      return;
    }

    intervals.add(Interval.copyOf(interval));
    if (DEBUG) assert invariants();
  }

  /**
   * Adds a single character.
   *
   * @param c Character to add.
   */
  public void add(int c) {
    int size = intervals.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervals.get(i);
      if (elem.end + 1 < c) continue;

      if (elem.contains(c)) return; // already there, nothing to do

      if (DEBUG) assert (elem.end + 1 >= c && (elem.start > c || elem.end < c));

      if (elem.start > c + 1) {
        intervals.add(i, Interval.ofCharacter(c));
        if (DEBUG) assert invariants();
        return;
      }

      if (DEBUG)
        assert (elem.end + 1 >= c && elem.start <= c + 1 && (elem.start > c || elem.end < c));

      if (c + 1 == elem.start) {
        elem.start = c;
        if (DEBUG) assert invariants();
        return;
      }

      if (DEBUG) assert (elem.end + 1 == c);
      elem.end = c;

      // merge with next interval if it contains c
      if (i + 1 >= size) {
        if (DEBUG) assert invariants();
        return;
      }
      Interval x = intervals.get(i + 1);
      if (x.start <= c + 1) {
        elem.end = x.end;
        intervals.remove(i + 1);
      }

      if (DEBUG) assert invariants();
      return;
    }

    // end reached but nothing found -> append at end
    intervals.add(Interval.ofCharacter(c));
    if (DEBUG) assert invariants();
  }

  /**
   * Returns whether this set contains a given character.
   *
   * @param singleChar a single character (int).
   * @return true iff singleChar is contained in the set.
   */
  public boolean contains(int singleChar) {
    return indexOf(singleChar) >= 0;
  }

  /**
   * Check whether this set contains a another set.
   *
   * @param other an IntCharSet.
   * @return true iff all characters of {@code other} are contained in this set.
   */
  public boolean contains(IntCharSet other) {
    // treat null as empty set
    if (other == null) {
      return true;
    }
    IntCharSet set = IntCharSet.copyOf(other);
    IntCharSet inter = this.and(other);
    set.sub(inter);
    return !set.containsElements();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IntCharSet)) {
      return false;
    }
    IntCharSet set = (IntCharSet) o;

    return Objects.equals(intervals, set.intervals);
  }

  @Override
  public int hashCode() {
    int h = 1;
    for (Interval interval : intervals) {
      h *= 1000003;
      h ^= interval.hashCode();
    }
    return h;
  }

  /**
   * Intersects two sets.
   *
   * @param set a {@link IntCharSet} object.
   * @return the {@link IntCharSet} common to the two sets.
   */
  public IntCharSet and(IntCharSet set) {
    if (DEBUG) {
      Out.dump("intersection");
      Out.dump("this  : " + this);
      Out.dump("other : " + set);
    }

    IntCharSet result = new IntCharSet();

    int i = 0; // index in this.intervals
    int j = 0; // index in set.intervals

    int size = intervals.size();
    int setSize = set.intervals.size();

    while (i < size && j < setSize) {
      Interval x = this.intervals.get(i);
      Interval y = set.intervals.get(j);

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      result.intervals.add(new Interval(max(x.start, y.start), min(x.end, y.end)));

      if (x.end >= y.end) j++;
      if (y.end >= x.end) i++;
    }

    if (DEBUG) {
      Out.dump("result: " + result);
      assert result.invariants();
    }

    return result;
  }

  /**
   * Returns the relative complement of this set relative to the provided set.
   *
   * <p>Assumes that {@code set} is non-null, and contained in this IntCharSet.
   *
   * @param set a {@link IntCharSet} to substract from this set.
   */
  public void sub(IntCharSet set) {
    if (DEBUG) {
      Out.dump("complement");
      Out.dump("this  : " + this);
      Out.dump("other : " + set);
      assert invariants();
      assert set != null && set.invariants();
      assert isSubSet(set, this);
    }

    int i = 0; // index in this.intervals
    int j = 0; // index in set.intervals

    int setSize = set.intervals.size();

    while (i < intervals.size() && j < setSize) {
      Interval x = this.intervals.get(i);
      Interval y = set.intervals.get(j);

      if (DEBUG) {
        Out.dump("this      : " + this);
        Out.dump("this  [" + i + "] : " + x);
        Out.dump("other [" + j + "] : " + y);
      }

      if (x.end < y.start) {
        i++;
        continue;
      }

      if (y.end < x.start) {
        j++;
        continue;
      }

      // x.end >= y.start && y.end >= x.start ->
      // x.end <= y.end && x.start >= y.start (prec)

      if (x.start == y.start && x.end == y.end) {
        intervals.remove(i);
        j++;
        continue;
      }

      // x.end <= y.end && x.start >= y.start &&
      // (x.end < y.end || x.start > y.start) ->
      // x.start < x.end

      if (x.start == y.start) {
        x.start = y.end + 1;
        j++;
        continue;
      }

      if (x.end == y.end) {
        x.end = y.start - 1;
        i++;
        j++;
        continue;
      }

      intervals.add(i, new Interval(x.start, y.start - 1));
      x.start = y.end + 1;

      i++;
      j++;
    }

    if (DEBUG) {
      Out.dump("result: " + this);
      assert invariants();
    }
  }

  /**
   * Returns whether the set contains elements.
   *
   * @return Whether the set is non-empty.
   */
  public boolean containsElements() {
    return intervals.size() > 0;
  }

  /**
   * Returns the number of intervals.
   *
   * @return number of intervals.
   */
  public int numIntervals() {
    return intervals.size();
  }

  /**
   * Returns the intervals.
   *
   * @return a {@link java.util.List} object.
   */
  public List<Interval> getIntervals() {
    return intervals;
  }

  // beware: depends on caller protocol, single user only
  /**
   * Returns the next interval.
   *
   * @return the next {@link jflex.chars.Interval}.
   */
  public Interval getNext() { // TODO: remove, use an Iterator instead
    if (DEBUG) assert containsElements();
    if (pos == intervals.size()) {
      pos = 0;
    }
    return intervals.get(pos++);
  }

  /**
   * Create a caseless version of this charset.
   *
   * <p>The caseless version contains all characters of this char set, and additionally all
   * lower/upper/title case variants of the characters in this set.
   *
   * @param unicodeProperties The Unicode Properties to use when generating caseless equivalence
   *     classes.
   * @return a caseless copy of this set
   */
  IntCharSet getCaseless(UnicodeProperties unicodeProperties) {
    IntCharSet n = copyOf(this);

    for (Interval elem : intervals) {
      for (int c = elem.start; c <= elem.end; c++) {
        IntCharSet equivalenceClass = unicodeProperties.getCaselessMatches(c);
        if (null != equivalenceClass) {
          n.add(equivalenceClass);
        }
      }
    }

    return n;
  }

  /**
   * Make a string representation of this char set.
   *
   * @return a string representing this char set.
   */
  public String toString() {
    StringBuilder result = new StringBuilder("{ ");

    for (Interval interval : intervals) {
      result.append(interval);
    }

    result.append(" }");

    return result.toString();
  }

  /**
   * Creates a IntCharSet from an existing IntCharSet.
   *
   * @return a (deep) copy of the char set.
   */
  public static IntCharSet copyOf(IntCharSet intCharSet) {
    IntCharSet result = new IntCharSet();
    for (Interval interval : intCharSet.intervals) {
      result.intervals.add(Interval.copyOf(interval));
    }
    if (DEBUG) assert result.invariants();
    return result;
  }

  /**
   * Compare this IntCharSet to another IntCharSet.
   *
   * <p>Assumption: the IntCharSets are disjoint, e.g. members of a partition.
   *
   * <p>This method does *not* implement subset order, but instead compares the smallest elements of
   * the two sets, with the empty set smaller than any other set. This is to make the order total
   * for partitions as in {@link CharClasses}. It is unlikely to otherwise be a useful order, and it
   * does probably not implement the contract for {@link Comparable#compareTo} correctly if the sets
   * have the same smallest element but are not equal.
   *
   * @param o the IntCharSet to compare to
   * @return 0 if the parameter is equal, -1 if its smallest element (if any) is larger than the
   *     smallest element of this set, and +1 if it is larger.
   */
  public int compareTo(IntCharSet o) {
    if (o == null) {
      throw new NullPointerException();
    }

    if (this.equals(o)) {
      return 0;
    }

    if (DEBUG) {
      assert !this.and(o).containsElements();
    }

    if (!this.containsElements()) {
      return -1;
    }
    if (!o.containsElements()) {
      return 1;
    }

    if (this.intervals.get(0).start < o.intervals.get(0).start) {
      return -1;
    } else {
      return 1;
    }
  }

  /**
   * Checks the invariants of this object.
   *
   * @return true when the invariants of this objects hold.
   */
  public boolean invariants() {
    for (Interval i : intervals) if (!i.invariants()) return false;

    for (int j = 0; j < intervals.size() - 1; j++) {
      // disjoint and ordered
      if (!(intervals.get(j).end < intervals.get(j + 1).start)) return false;
    }

    // if there are elements, pos must point to an interval
    return !containsElements() || pos < intervals.size();
  }

  /**
   * Very slow but elementary method to determine whether s1 is a subset of s2. For assertions in
   * debugging/testing only.
   *
   * @param s1 the first IntCharSet
   * @param s2 the second IntCharSet
   * @return true iff s1 is a subset of s2
   */
  public static boolean isSubSet(IntCharSet s1, IntCharSet s2) {
    for (int i : s1) if (!s2.contains(i)) return false;
    return true;
  }

  @Override
  public IntCharSetIterator iterator() {
    return new IntCharSetIterator();
  }

  /** Iterator for enumerating the elements of this IntCharSet */
  public class IntCharSetIterator implements PrimitiveIterator.OfInt {
    /** Iterator over the Interval list */
    private Iterator<Interval> intervalsIterator;
    /** Iterator within the current Interval */
    private IntervalIterator current;

    /** New iterator for this IntCharSet */
    private IntCharSetIterator() {
      intervalsIterator = intervals.iterator();
      if (intervalsIterator.hasNext()) current = intervalsIterator.next().iterator();
    }

    @Override
    public boolean hasNext() {
      return current != null && (current.hasNext() || intervalsIterator.hasNext());
    }

    @Override
    public int nextInt() {
      if (!current.hasNext()) current = intervalsIterator.next().iterator();
      return current.nextInt();
    }
  }
}
