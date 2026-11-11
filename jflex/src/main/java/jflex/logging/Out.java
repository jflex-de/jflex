/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.logging;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import jflex.base.Build;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.option.Options;
import jflex.performance.Timer;

/**
 * The console output of one generator run.
 *
 * <p>Reads the switches verbose, time and dump from {@link Options} to determine the verbosity of
 * JFlex output. There is no switch for suppressing error messages.
 *
 * <p>Writes to {@code System.out}, to an {@link OutputStream}, or to a TextArea in GUI mode.
 *
 * <p>Counts error and warning messages.
 *
 * <p>Concurrent runs use separate Out instances (and separate {@link Options}).
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public final class Out {

  /** Platform specific newline. */
  public static final String NL = System.getProperty("line.separator");

  /** count total warnings */
  private int warnings;

  /** count total errors */
  private int errors;

  /** output device */
  private final StdOutWriter out;

  /**
   * The options that output depends on: verbosity, time statistics, dump, suppressed warnings,
   * encoding. Read live, so later changes to the options apply.
   */
  private final Options options;

  /**
   * Creates output to {@code System.out}.
   *
   * @param options the options of the generator run
   */
  public Out(Options options) {
    this(options, System.out);
  }

  /**
   * Creates output to a stream.
   *
   * @param options the options of the generator run
   * @param stream where to write all output to
   */
  public Out(Options options, OutputStream stream) {
    this.options = options;
    this.out = new StdOutWriter(stream);
  }

  /**
   * Creates output in GUI mode.
   *
   * @param options the options of the generator run
   * @param text the message TextArea of the JFlex GUI
   */
  public Out(Options options, TextArea text) {
    this.options = options;
    this.out = new StdOutWriter();
    out.setGUIMode(text);
  }

  /**
   * Report time statistic data.
   *
   * @param message the message to be printed
   * @param time elapsed time
   */
  public void time(ErrorMessages message, Timer time) {
    if (options.time()) {
      String msg = ErrorMessages.get(message, time.toString());
      out.println(msg);
    }
  }

  /**
   * Report time statistic data.
   *
   * @param message the message to be printed
   */
  public void time(String message) {
    if (options.time()) {
      out.println(message);
    }
  }

  /**
   * Report generation progress.
   *
   * @param message the message to be printed
   */
  public void println(String message) {
    if (options.verbose()) {
      out.println(message);
    }
  }

  /**
   * Report generation progress.
   *
   * @param message the message to be printed
   * @param data data to be inserted into the message
   */
  public void println(ErrorMessages message, String data) {
    if (options.verbose()) {
      out.println(ErrorMessages.get(message, data));
    }
  }

  /**
   * Report generation progress.
   *
   * @param message the message to be printed
   * @param data data to be inserted into the message
   */
  public void println(ErrorMessages message, int data) {
    if (options.verbose()) {
      out.println(ErrorMessages.get(message, data));
    }
  }

  /**
   * Report generation progress.
   *
   * @param message the message to be printed
   */
  public void print(String message) {
    if (options.verbose()) {
      out.print(message);
    }
  }

  /**
   * Dump debug information to System.out
   *
   * <p>Use like this {@code if (Out.DEBUG) Out.debug(message)} to save performance during normal
   * operation (when DEBUG is turned off).
   *
   * @param message a {@link java.lang.String} object.
   */
  public static void debug(String message) {
    if (Build.DEBUG) {
      System.out.println(message);
    }
  }

  /**
   * Unconditionally print trace information to System.out.
   *
   * <p>For use under class-local compile-time {@code DEBUG} flags.
   *
   * @param message the message to print
   */
  public static void trace(String message) {
    System.out.println(message);
  }

  /**
   * All parts of JFlex, that want to provide dump information should use this method for their
   * output.
   *
   * @param message the message to be printed
   */
  public void dump(String message) {
    if (options.dump()) {
      out.println(message);
    }
  }

  /**
   * All parts of JFlex, that want to report error messages should use this method for their output.
   *
   * @param message the message to be printed
   */
  public void err(String message) {
    out.println(message);
  }

  /** throws a GeneratorException if there are any errors recorded */
  public void checkErrors() {
    if (errors > 0) {
      throw new GeneratorException();
    }
  }

  /** Returns the number of errors reported since the last {@link #resetCounters()}. */
  public int errors() {
    return errors;
  }

  /** Returns the number of warnings reported since the last {@link #resetCounters()}. */
  public int warnings() {
    return warnings;
  }

  /** print error and warning statistics */
  public void statistics() {
    StringBuilder line = new StringBuilder(errors + " error");
    if (errors != 1) line.append("s");

    line.append(", ").append(warnings).append(" warning");
    if (warnings != 1) line.append("s");

    line.append(".");
    err(line.toString());
  }

  /** reset error and warning counters */
  public void resetCounters() {
    errors = 0;
    warnings = 0;
  }

  /**
   * print a warning message without line information
   *
   * @param message code of the warning message
   * @see ErrorMessages
   */
  public void warning(ErrorMessages message) {
    warning(message, 0);
  }

  /**
   * Print a warning message with arguments without line information
   *
   * @param message code of the warning message
   * @param args arguments of the warning message
   * @see ErrorMessages
   */
  public void warning(ErrorMessages message, Object... args) {
    warning(message, 0, args);
  }

  /**
   * Print a warning with line information.
   *
   * @param message code of the warning message
   * @param line the line information
   * @see ErrorMessages
   */
  public void warning(ErrorMessages message, int line) {
    warning(message, line, (Object[]) null);
  }

  /**
   * Print a warning with line information and arguments.
   *
   * @param message code of the warning message
   * @param line the line information
   * @param args arguments to the warning message
   * @see ErrorMessages
   */
  public void warning(ErrorMessages message, int line, Object... args) {
    if (options.isSuppressed(message)) return;

    warnings++;

    String msg = NL + "Warning";
    if (line > 0) msg = msg + " in line " + (line + 1);

    if (args != null) {
      err(msg + ": " + ErrorMessages.get(message, args));
    } else {
      err(msg + ": " + ErrorMessages.get(message));
    }
  }

  /**
   * print warning message with location information
   *
   * @param file the file the warning is issued for
   * @param message the code of the message to print
   * @param line the line number of the position
   * @param column the column of the position
   */
  public void warning(File file, ErrorMessages message, int line, int column) {
    if (options.isSuppressed(message)) return;

    String msg = NL + "Warning";
    if (file != null) msg += " in file \"" + file + "\"";
    if (line >= 0) msg = msg + " (line " + (line + 1) + ")";

    try {
      err(msg + ": " + NL + ErrorMessages.get(message));
    } catch (ArrayIndexOutOfBoundsException e) {
      err(msg);
    }

    warnings++;

    if (line >= 0) {
      if (column >= 0) showPosition(file, line, column);
      else showPosition(file, line);
    }
  }

  /**
   * print error message (string)
   *
   * @param message the message to print
   */
  public void error(String message) {
    errors++;
    err(NL + message);
  }

  /**
   * Print the error message of an exception if it has one.
   *
   * @param e the exception to report
   * @see GeneratorException#GeneratorException(ErrorMessages, Object...)
   */
  public void error(GeneratorException e) {
    if (e.hasErrorMessage()) {
      error(e.getMessage());
    }
  }

  /**
   * print error message (code)
   *
   * @param message the code of the error message
   * @see ErrorMessages
   */
  public void error(ErrorMessages message) {
    errors++;
    err(NL + "Error: " + ErrorMessages.get(message));
  }

  /**
   * print error message with data
   *
   * @param data data to insert into the message
   * @param message the code of the error message
   * @see ErrorMessages
   */
  public void error(ErrorMessages message, String data) {
    errors++;
    err(NL + "Error: " + ErrorMessages.get(message, data));
  }

  /**
   * print error message with location information
   *
   * @param file the file the error occurred for
   * @param message the code of the error message to print
   * @param line the line number of error position
   * @param column the column of error position
   */
  public void error(File file, ErrorMessages message, int line, int column) {

    String msg = NL + "Error";
    if (file != null) msg += " in file \"" + file + "\"";
    if (line >= 0) msg = msg + " (line " + (line + 1) + ")";

    try {
      err(msg + ": " + NL + ErrorMessages.get(message));
    } catch (ArrayIndexOutOfBoundsException e) {
      err(msg);
    }

    errors++;

    if (line >= 0) {
      if (column >= 0) showPosition(file, line, column);
      else showPosition(file, line);
    }
  }

  /**
   * prints a line of a file with marked position.
   *
   * @param file the file of which to show the line
   * @param line the line to show
   * @param column the column in which to show the marker
   */
  public void showPosition(File file, int line, int column) {
    try {
      String ln = getLine(file, line);
      if (ln != null) {
        err(ln);

        if (column < 0) return;

        String t = "^";
        for (int i = 0; i < column; i++) t = " " + t;

        err(t);
      }
    } catch (IOException e) {
      /* silently ignore IO errors, don't show anything */
    }
  }

  /**
   * print a line of a file
   *
   * @param file the file to show
   * @param line the line number
   */
  public void showPosition(File file, int line) {
    try {
      String ln = getLine(file, line);
      if (ln != null) err(ln);
    } catch (IOException e) {
      /* silently ignore IO errors, don't show anything */
    }
  }

  /**
   * get one line from a file
   *
   * @param file the file to read
   * @param line the line number to get
   * @throws IOException if any error occurs
   */
  private String getLine(File file, int line) throws IOException {
    BufferedReader reader = Files.newBufferedReader(file.toPath(), options.encoding());

    String msg = "";

    for (int i = 0; i <= line; i++) msg = reader.readLine();

    reader.close();

    return msg;
  }
}
