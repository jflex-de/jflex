/*
 * Copyright (C) 2021 Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.unicodedatatest.base;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pair<T> {
  public abstract T first();

  public abstract T second();

  public static <T> Pair create(T first, T second) {
    return new AutoValue_Pair(first, second);
  }
}
