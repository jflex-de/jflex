/*
 * Copyright 2020, Google, LLC.
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.large_input;

public enum State {
  BEFORE_2GB,
  AFTER_2GB,
  LINE_FEED,
  END_OF_FILE,
}
