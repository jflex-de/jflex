/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jlex;

/** The rules of {@code caseless.flex}. */
public enum State {
  A,
  WORD,
  HELLO,
  LATIN1,
  CYRILLIC,
  NOT_DELTA,
  NEW_LINE,
  OTHER,
  END_OF_FILE,
}
