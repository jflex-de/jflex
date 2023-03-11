/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.base;

import java.util.Objects;

/**
 * Generic immutable pair.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public class Pair<A, B> {
  public final A fst;
  public final B snd;

  public Pair(A fst, B snd) {
    this.fst = fst;
    this.snd = snd;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fst, snd);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Pair<?, ?>
        && Objects.equals(fst, ((Pair<?, ?>) other).fst)
        && Objects.equals(snd, ((Pair<?, ?>) other).snd);
  }

  @Override
  public String toString() {
    return "(" + fst + ", " + snd + ")";
  }
}
