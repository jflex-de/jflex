/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.scanner;

import java.io.File;
import jflex.l10n.ErrorMessages;

/**
 * This Exception could be thrown while scanning the specification (e.g. unmatched input)
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class ScannerException extends RuntimeException {

  /** */
  private static final long serialVersionUID = -6119623765759220207L;

  public int line;
  public int column;
  public ErrorMessages.ErrorMessage message;
  public File file;

  private ScannerException(
      File file, String text, ErrorMessages.ErrorMessage message, int line, int column) {
    super(text);
    this.file = file;
    this.message = message;
    this.line = line;
    this.column = column;
  }

  /**
   * Creates a new ScannerException with a message only.
   *
   * @param message the code for the error description presented to the user.
   */
  public ScannerException(ErrorMessages.ErrorMessage message) {
    this(null, ErrorMessages.get(message), message, -1, -1);
  }

  /**
   * Creates a new ScannerException for a file with a message only.
   *
   * @param file the file in which the error occurred
   * @param message the code for the error description presented to the user.
   */
  public ScannerException(File file, ErrorMessages.ErrorMessage message) {
    this(file, ErrorMessages.get(message), message, -1, -1);
  }

  /**
   * Creates a new ScannerException with a message and line number.
   *
   * @param message the code for the error description presented to the user.
   * @param line the number of the line in the specification that contains the error
   */
  public ScannerException(ErrorMessages.ErrorMessage message, int line) {
    this(null, ErrorMessages.get(message), message, line, -1);
  }

  /**
   * Creates a new ScannerException for a file with a message and line number.
   *
   * @param message the code for the error description presented to the user.
   * @param line the number of the line in the specification that contains the error
   * @param file a {@link java.io.File} object.
   */
  public ScannerException(File file, ErrorMessages.ErrorMessage message, int line) {
    this(file, ErrorMessages.get(message), message, line, -1);
  }

  /**
   * Creates a new ScannerException with a message, line number and column.
   *
   * @param message the code for the error description presented to the user.
   * @param line the number of the line in the specification that contains the error
   * @param column the column where the error starts
   * @param file a {@link java.io.File} object.
   */
  public ScannerException(File file, ErrorMessages.ErrorMessage message, int line, int column) {
    this(file, ErrorMessages.get(message), message, line, column);
  }
}
