/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import com.google.auto.value.AutoValue;

/**
 * Simple pair of integers.
 *
 * <p>Used in NFA to represent a partial NFA by its start and end state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
@AutoValue
abstract class IntPair {

  abstract int start();

  abstract int end();

  public int xxx;

  static IntPair create(int start, int end) {
    return new AutoValue_IntPair(start, end);
  }
}
