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
import java.util.List;
import java.util.Objects;
import jflex.chars.Interval;
import jflex.core.unicode.UnicodeProperties;

/**
 * Char Set implemented with intervals.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.0-SNAPSHOT
 */
public final class IntCharSet implements Comparable<IntCharSet> {

  private static final boolean DEBUG = false;

  /* invariant: all intervals are disjoint, ordered */
  private List<Interval> intervals = new ArrayList<>();

  /** for iterating over the char set */
  private int pos;

  /** Creates an empty char set. */
  public IntCharSet() {}

  /** Creates a char set that contains only the given character. */
  public IntCharSet(int c) {
    this(new Interval(c));
  }

  /** Creates a char set that contains only the given interval. */
  public IntCharSet(int start, int end) {
    this(new Interval(start, end));
  }

  /** Creates a charset that contains only one interval. */
  public IntCharSet(Interval interval) {
    intervals.add(interval);
  }

  /**
   * Constructor for IntCharSet.
   *
   * @param chars a {@link java.util.List} object.
   */
  public IntCharSet(List<Interval> chars) {
    int size = chars.size();
    intervals = new ArrayList<>(size);

    for (Interval interval : chars) add(interval);
  }

  /**
   * The set of all characters.
   *
   * @return a new IntCharSet that contains all characters.
   */
  public static IntCharSet allChars() {
    return new IntCharSet(0, CharClasses.maxChar);
  }

  /**
   * The set of new-line characters.
   *
   * @return a new IntCharSet that contains all characters that are considered a new-line char in
   *     Java.
   */
  public static IntCharSet nlChars() {
    IntCharSet set = new IntCharSet(new Interval('\n', '\r'));
    set.add(new Interval('\u0085', '\u0085'));
    set.add(new Interval('\u2028', '\u2029'));
    return set;
  }

  /**
   * returns the index of the interval that contains the character c, -1 if there is no such
   * interval
   *
   * <p>post: {@code -1 <= return < intervals.size() && (return > -1 -->
   * intervals[return].contains(c))}
   *
   * @param c the character
   * @return the index of the enclosing interval, -1 if no such interval
   */
  private int indexOf(int c) {
    int start = 0;
    int end = intervals.size() - 1;

    while (start <= end) {
      int check = (start + end) / 2;
      Interval i = intervals.get(check);

      if (start == end) return i.contains(c) ? start : -1;

      if (c < i.start) {
        end = check - 1;
        continue;
      }

      if (c > i.end) {
        start = check + 1;
        continue;
      }

      return check;
    }

    return -1;
  }

  /**
   * add.
   *
   * @param set a {@link IntCharSet} object.
   * @return a {@link IntCharSet} object.
   */
  public IntCharSet add(IntCharSet set) {
    for (Interval interval : set.intervals) add(interval);
    return this;
  }

  /**
   * add.
   *
   * @param interval a {@link jflex.chars.Interval} object.
   */
  public void add(Interval interval) {

    int size = intervals.size();

    for (int i = 0; i < size; i++) {
      Interval elem = intervals.get(i);

      if (elem.end + 1 < interval.start) continue;

      if (elem.contains(interval)) return;

      if (elem.start > interval.end + 1) {
        intervals.add(i, Interval.copyOf(interval));
        return;
      }

      if (interval.start < elem.start) elem.start = interval.start;

      if (interval.end <= elem.end) return;

      elem.end = interval.end;

      i++;
      // delete all x with x.contains( interval.end )
      while (i < size) {
        Interval x = intervals.get(i);
        if (x.start > elem.end + 1) return;

        if (x.end > elem.end) {
          elem.end = x.end;
        }
        intervals.remove(i);
        size--;
      }
      return;
    }

    intervals.add(Interval.copyOf(interval));
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

      // assert(elem.end+1 >= c && (elem.start > c || elem.end < c));

      if (elem.start > c + 1) {
        intervals.add(i, Interval.ofCharacter(c));
        return;
      }

      // assert(elem.end+1 >= c && elem.start <= c+1 && (elem.start > c || elem.end < c));

      if (c + 1 == elem.start) {
        elem.start = c;
        return;
      }

      // assert(elem.end+1 == c);
      elem.end = c;

      // merge with next interval if it contains c
      if (i + 1 >= size) return;
      Interval x = intervals.get(i + 1);
      if (x.start <= c + 1) {
        elem.end = x.end;
        intervals.remove(i + 1);
      }
      return;
    }

    // end reached but nothing found -> append at end
    intervals.add(new Interval(c));
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
   * Check wether this set contains a another set.
   *
   * @param other an IntCharSet.
   * @return true iff all characters of {@code other} are contained in this set.
   */
  public boolean contains(IntCharSet other) {
    // treat null as empty set
    if (other == null) return true;
    IntCharSet set = other.copy();
    IntCharSet inter = this.and(other);
    set.sub(inter);
    return !set.containsElements();
  }

  /**
   * {@inheritDoc}
   *
   * <p>o instanceof IntCharSet
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof IntCharSet)) {
      return false;
    }
    IntCharSet set = (IntCharSet) o;

    return Objects.equals(intervals, set.intervals);
  }

  /** {@inheritDoc} */
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
    }

    return result;
  }

  /* prec: this.contains(set), set != null */
  /**
   * Returns the relative complement of this set relative to the provided set.
   *
   * @param set a {@link IntCharSet} to substract from this set.
   */
  public void sub(IntCharSet set) {
    if (DEBUG) {
      Out.dump("complement");
      Out.dump("this  : " + this);
      Out.dump("other : " + set);
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
   * Returns the number of Intervals.
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
  public Interval getNext() {
    if (pos == intervals.size()) pos = 0;
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
  public IntCharSet getCaseless(UnicodeProperties unicodeProperties) {
    IntCharSet n = copy();

    for (Interval elem : intervals) {
      for (int c = elem.start; c <= elem.end; c++) {
        IntCharSet equivalenceClass = unicodeProperties.getCaselessMatches(c);
        if (null != equivalenceClass) n.add(equivalenceClass);
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

    for (Interval interval : intervals) result.append(interval);

    result.append(" }");

    return result.toString();
  }

  /**
   * Return a (deep) copy of this char set
   *
   * @return the copy
   */
  public IntCharSet copy() {
    IntCharSet result = new IntCharSet();
    for (Interval interval : intervals) {
      result.intervals.add(Interval.copyOf(interval));
    }
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
  @Override
  public int compareTo(IntCharSet o) {
    if (o == null) throw new NullPointerException();

    if (this.equals(o)) return 0;

    if (!this.containsElements()) return -1;
    if (!o.containsElements()) return 1;

    if (this.intervals.get(0).start < o.intervals.get(0).start) return -1;
    else return 1;
  }
}
