/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testing.testsuite.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jflex.l10n.ErrorMessages;

/**
 * A warning that JFlex is expected to report. For instance
 *
 * <pre>{@code
 * warnings = {@Warn(ErrorMessages.MACRO_UNUSED), @Warn(value = ErrorMessages.NEVER_MATCH, line = 17)}
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Warn {

  /** The code of the warning message. */
  ErrorMessages value();

  /**
   * The name of the file that the warning is reported for, without directories. For instance {@code
   * "rules.inc.jflex"}. Empty for leaving out the check.
   */
  String file() default "";

  /**
   * The line that the warning is reported for, as printed by JFlex (starting from 1). 0 for leaving
   * out the check.
   */
  int line() default 0;

  /** The column that the warning is reported for, starting from 1. 0 for leaving out the check. */
  int column() default 0;
}
