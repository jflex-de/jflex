/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.base;

import com.google.auto.value.AutoValue;

/**
 * Simple pair of integers.
 *
 * <p>Used in NFA to represent a partial NFA by its start and end state.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
@AutoValue
public abstract class IntPair {

  public abstract int start();

  public abstract int end();

  public static IntPair create(int start, int end) {
    return new AutoValue_IntPair(start, end);
  }
}
