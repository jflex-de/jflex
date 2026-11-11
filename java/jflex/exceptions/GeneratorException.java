/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.exceptions;

import jflex.l10n.ErrorMessages;

/**
 * Thrown when code generation has to be aborted.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class GeneratorException extends RuntimeException {

  private static final long serialVersionUID = -9128247888544263982L;
  private boolean unexpected;
  private boolean errorMessage;

  /** Creates an exception for an error that has already been reported to the user. */
  public GeneratorException() {
    super();
    // we use GeneratorException() to abort control flow, i.e. these are
    // expected and we leave unexpected on false.
  }

  public GeneratorException(Throwable cause, boolean unexpected) {
    super("Generation aborted: " + cause, cause);
    this.unexpected = unexpected;
  }

  public GeneratorException(Throwable cause) {
    this(cause, false);
  }

  /**
   * Creates an exception for an error that has not been reported to the user yet.
   *
   * <p>For code that has no {@link jflex.logging.Out} to report to, e.g. {@link
   * jflex.option.Options}. The caller that has an {@code Out} reports the error with {@link
   * jflex.logging.Out#error(GeneratorException)}.
   *
   * @param message the code of the error message
   * @param args arguments of the error message, if any
   */
  public GeneratorException(ErrorMessages message, Object... args) {
    this(ErrorMessages.error(message, args));
  }

  /**
   * Creates an exception for an error that has not been reported to the user yet.
   *
   * @param message the error message to show the user
   * @see #GeneratorException(ErrorMessages, Object...)
   */
  public GeneratorException(String message) {
    super(message);
    this.errorMessage = true;
  }

  public boolean isUnExpected() {
    return unexpected;
  }

  /**
   * Whether {@link #getMessage()} is an error message for the user that has not been reported yet.
   *
   * @see #GeneratorException(ErrorMessages, Object...)
   */
  public boolean hasErrorMessage() {
    return errorMessage;
  }
}
