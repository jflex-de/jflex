/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.base;

/**
 * Simple pair of integers.
 *
 * <p>Used in NFA to represent a partial NFA by its start and end state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
public final class IntPair {

  public final int start;
  public final int end;

  public IntPair(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public int hashCode() {
    return end + (start << 8);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof IntPair) {
      IntPair p = (IntPair) o;
      return start == p.start && end == p.end;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + start + "," + end + ")";
  }
}
