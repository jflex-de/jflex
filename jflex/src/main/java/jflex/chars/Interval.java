/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.chars;

/**
 * An interval of characters with basic operations.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.0-SNAPSHOT
 */
public final class Interval {

  /** Start of the interval. */
  public int start;
  /** End of the interval. */
  public int end;

  /**
   * Construct a new interval from {@code start</code> to <code>end}.
   *
   * @param start first character the interval should contain
   * @param end last character the interval should contain
   */
  public Interval(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public Interval(Interval other) {
    this.start = other.start;
    this.end = other.end;
  }

  /**
   * Returns {@code true} iff {@code point} is contained in this interval.
   *
   * @param point the character to check
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
   * Check whether a character is printable.
   *
   * @param c the character to check
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
   * Make a copy of this interval.
   *
   * @return the copy
   */
  public Interval copy() {
    return new Interval(start, end);
  }
}
