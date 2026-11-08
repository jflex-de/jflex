/*
 * Copyright (C) 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import static org.junit.Assert.assertFalse;

import jflex.l10n.ErrorMessages;
import jflex.option.Options;
import org.junit.After;
import org.junit.Test;

/** Tests for {@link OptionUtils}. */
public class OptionUtilsTest {

  @After
  public void resetOptions() {
    OptionUtils.setDefaultOptions();
  }

  @Test
  public void setDefaultOptions_enablesSuppressedWarnings() {
    OptionUtils.suppressAllWarnings();

    OptionUtils.setDefaultOptions();

    for (ErrorMessages msg : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(msg)) {
        assertFalse(msg + " suppressed after setDefaultOptions()", Options.isSuppressed(msg));
      }
    }
  }

  @Test
  public void setDefaultOptions_enablesSingleSuppressedWarning() {
    OptionUtils.suppressWarning("never-match");

    OptionUtils.setDefaultOptions();

    assertFalse(
        "NEVER_MATCH suppressed after setDefaultOptions()",
        Options.isSuppressed(ErrorMessages.NEVER_MATCH));
  }
}
