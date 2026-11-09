/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_input;

/** Thrown when the lexer's {@code zzchar} is negative, which should never happen. */
public class NegativeYyCharException extends IllegalStateException {
  NegativeYyCharException(long yychar) {
    super("The scanner has a negative number of read characters: " + yychar);
  }
}
