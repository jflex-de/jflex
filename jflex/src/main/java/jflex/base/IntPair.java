/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.base;

/**
 * Simple pair of integers.
 *
 * <p>Used in NFA to represent a partial NFA by its start and end state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public final class IntPair {

  private final int start;
  private final int end;

  private IntPair(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public static IntPair create(int start, int end) {
    return new IntPair(start, end);
  }

  public int start() {
    return start;
  }

  public int end() {
    return end;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof IntPair)) {
      return false;
    }
    IntPair that = (IntPair) o;
    return start == that.start && end == that.end;
  }

  @Override
  public int hashCode() {
    return 31 * start + end;
  }

  @Override
  public String toString() {
    return "IntPair{start=" + start + ", end=" + end + "}";
  }
}
