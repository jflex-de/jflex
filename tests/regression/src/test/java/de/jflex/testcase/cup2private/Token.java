/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.cup2private;

import edu.tum.cup2.grammar.Terminal;

public enum Token implements Terminal {
  WORD,
  OTHER,
  EOF,
}
