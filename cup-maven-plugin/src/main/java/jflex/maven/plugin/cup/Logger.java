/*
 * Copyright 2018, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.maven.plugin.cup;

import org.apache.maven.plugin.logging.Log;

/** Wrapper around Mojo logger that supports String format. */
public class Logger {

  private final Log log;

  public Logger(Log log) {
    this.log = log;
  }

  public void d(String format, Object... args) {
    this.log.debug(String.format(format, args));
  }

  public void i(String format, Object... args) {
    this.log.info(String.format(format, args));
  }
}
