/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

/**
 * An interval of characters with basic operations.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public final class Interval {

  /* start and end of the interval */
  public int start, end;

  /**
   * Construct a new interval from <code>start</code> to <code>end</code>.
   *
   * @param start first character the interval should contain
   * @param end last character the interval should contain
   */
  public Interval(int start, int end) {
    this.start = start;
    this.end = end;
  }

  /**
   * Copy constructor.
   *
   * @param other a {@link jflex.Interval} object.
   */
  public Interval(Interval other) {
    this.start = other.start;
    this.end = other.end;
  }

  /**
   * Return <code>true</code> iff <code>point</code> is contained in this interval.
   *
   * @param point the character to check
   * @return whether the codepoint is contained in the interval.
   */
  public boolean contains(int point) {
    return start <= point && end >= point;
  }

  /**
   * Return <code>true</code> iff this interval completely contains the other one.
   *
   * @param other the other interval
   * @return whether this interval completely contains the other one.
   */
  public boolean contains(Interval other) {
    return this.start <= other.start && this.end >= other.end;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Return <code>true</code> if <code>o</code> is an interval with the same borders.
   */
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Interval)) return false;

    Interval other = (Interval) o;
    return other.start == this.start && other.end == this.end;
  }

  /**
   * Set a new last character
   *
   * @param end the new last character of this interval
   */
  public void setEnd(int end) {
    this.end = end;
  }

  /**
   * Set a new first character
   *
   * @param start the new first character of this interval
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * Check whether a character is printable.
   *
   * @param c the character to check
   */
  private static boolean isPrintable(int c) {
    // fixme: should make unicode test here
    return c > 31 && c < 127;
  }

  /**
   * Get a String representation of this interval.
   *
   * @return a string <code>"[start-end]"</code> or <code>"[start]"</code> (if there is only one
   *     character in the interval) where <code>start</code> and <code>end</code> are either a
   *     number (the character code) or something of the from <code>'a'</code>.
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
