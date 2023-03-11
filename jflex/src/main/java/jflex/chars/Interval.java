/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.chars;

import java.util.PrimitiveIterator;

/**
 * A mutable interval of characters with basic operations.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.9.1
 */
public final class Interval implements Iterable<Integer> {

  /** Start of the interval. */
  public int start;
  /** End of the interval. */
  public int end;

  /**
   * Constructs a new interval from {@code start} to {@code end}, including both end points.
   *
   * @param start first codepoint the interval contains
   * @param end last codepoint the interval contains
   */
  public Interval(int start, int end) {
    this.start = Math.min(start, end);
    this.end = Math.max(start, end);
    assert invariants();
  }

  /**
   * Returns {@code true} iff {@code point} is contained in this interval.
   *
   * @param point the character codepoint to check
   * @return whether the code point is contained in the interval.
   */
  public boolean contains(int point) {
    return start <= point && end >= point;
  }

  /**
   * Return {@code true} iff this interval completely contains the other one.
   *
   * @param other the other interval
   * @return whether this interval completely contains the other one.
   */
  public boolean contains(Interval other) {
    return this.start <= other.start && this.end >= other.end;
  }

  /** Returns {@code true} if {@code o} is an interval with the same borders. */
  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Interval)) return false;

    Interval other = (Interval) o;
    return other.start == this.start && other.end == this.end;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= start;
    h *= 1000003;
    h ^= end;
    return h;
  }

  /**
   * Returns whether a character is printable.
   *
   * @param c the codepoint to check
   */
  private static boolean isPrintable(int c) {
    // fixme: should make unicode test here
    return 31 < c && c < 127;
  }

  /**
   * Returns a String representation of this interval.
   *
   * @return a string "{@code [start-end]}" or "{@code [start]}" (if there is only one character in
   *     the interval) where {@code start} and {@code end} are either a number (the character code)
   *     or something of the from {@code 'a'}.
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder("[");

    if (isPrintable(start)) result.append("'").append((char) start).append("'");
    else result.append(start);

    if (start != end) {
      result.append("-");

      if (isPrintable(end)) result.append("'").append((char) end).append("'");
      else result.append(end);
    }

    result.append("]");
    return result.toString();
  }

  /**
   * Creates an interval of a single character.
   *
   * @param c The unique codepoint contained in this interval.
   * @return A single-character interval.
   */
  public static Interval ofCharacter(int c) {
    return new Interval(c, c);
  }

  /**
   * Creates a copy of the interval.
   *
   * @return the copy of the given interval.
   */
  public static Interval copyOf(Interval interval) {
    return new Interval(interval.start, interval.end);
  }

  /**
   * Computes the size of this interval.
   *
   * @return how many characters this interval spans
   */
  public int size() {
    return end - start + 1;
  }

  /**
   * Checks the invariants of this object.
   *
   * @return true when the invariants of this objects hold.
   */
  public boolean invariants() {
    return start <= end;
  }

  @Override
  public IntervalIterator iterator() {
    return new IntervalIterator();
  }

  /** Iterator for enumerating the elements of this Interval */
  public class IntervalIterator implements PrimitiveIterator.OfInt {
    /** The current iterator position */
    private int pos;

    /** New iterator that starts at the beginning of the */
    private IntervalIterator() {
      pos = start;
    }

    @Override
    public boolean hasNext() {
      return pos < end;
    }

    @Override
    public int nextInt() {
      return pos++;
    }
  }
}
