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
@AutoValue
public abstract class IntPair {

  public abstract int start();

  public abstract int end();

  public static IntPair create(int start, int end) {
    return new AutoValue_IntPair(start, end);
  }
}
