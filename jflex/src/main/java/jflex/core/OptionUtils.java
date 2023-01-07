/*
 * Copyright (C) 2019, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java.io.File;
import java.nio.charset.Charset;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.skeleton.Skeleton;

public class OptionUtils {
  private OptionUtils() {}

  /** Sets encoding for input files, and check availability of encoding on this JVM. */
  public static void setEncoding(String encodingName) {
    if (Charset.isSupported(encodingName)) {
      Options.encoding = Charset.forName(encodingName);
    } else {
      Out.error(ErrorMessages.CHARSET_NOT_SUPPORTED, encodingName);
      throw new GeneratorException();
    }
  }

  /** Sets all options back to default values. */
  public static void setDefaultOptions() {
    Options.directory = null;
    // System.getProperty("user.dir"), the directory where java was run from.
    Options.resetRootDirectory();
    Options.jlex = false;
    Options.no_minimize = false;
    Options.no_backup = false;
    Options.verbose = true;
    Options.progress = true;
    Options.time = false;
    Options.dot = false;
    Options.dump = false;
    Options.legacy_dot = false;
    Options.encoding = Charset.defaultCharset();
    Skeleton.readDefault();
  }

  /**
   * Warn on unused macros or not.
   *
   * @param unusedWarning whether unused macros should be warned about.
   */
  public static void set_unused_warning(boolean unusedWarning) {
    if (unusedWarning) {
      Options.enable(ErrorMessages.MACRO_UNUSED);
    } else {
      Options.suppress(ErrorMessages.MACRO_UNUSED);
    }
  }

  public static void setSkeleton(File skel) {
    Skeleton.readSkelFile(skel);
  }

  /**
   * Set output directory
   *
   * @param d the directory to write output files to
   */
  public static void setDir(File d) {
    if (d.isFile()) {
      Out.error("Error: \"" + d + "\" is not a directory.");
      throw new GeneratorException();
    }

    if (!d.isDirectory() && !d.mkdirs()) {
      Out.error("Error: couldn't create directory \"" + d + "\"");
      throw new GeneratorException();
    }

    Options.directory = d;
  }

  /**
   * Set output directory
   *
   * @param dirName the name of the directory to write output files to
   */
  public static void setDir(String dirName) {
    setDir(new File(dirName));
  }

  /**
   * Enable a warning type.
   *
   * @param warning the warning to enable, must match one of the {@link ErrorMessages} enum values.
   * @throws GeneratorException if the warning is not known or not configurable.
   * @see ErrorMessages
   */
  public static void enableWarning(String warning) {
    try {
      ErrorMessages msg = ErrorMessages.valueOf(warning.toUpperCase().replace('-', '_'));
      Options.enable(msg);
    } catch (IllegalArgumentException e) {
      Out.error(ErrorMessages.UNKNOWN_WARNING, warning);
      throw new GeneratorException(e);
    }
  }

  /**
   * Suppress a warning type.
   *
   * @param warning the warning to suppress, must match one of the {@link ErrorMessages} enum
   *     values.
   * @throws GeneratorException if the warning is not known or not configurable.
   * @see ErrorMessages
   */
  public static void suppressWarning(String warning) {
    try {
      ErrorMessages msg = ErrorMessages.valueOf(warning.toUpperCase().replace('-', '_'));
      Options.suppress(msg);
    } catch (IllegalArgumentException e) {
      Out.error(ErrorMessages.UNKNOWN_WARNING, warning);
      throw new GeneratorException(e);
    }
  }

  /**
   * Enable all warnings.
   *
   * @see ErrorMessages
   */
  public static void enableAllWarnings() {
    for (ErrorMessages msg : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(msg)) {
        Options.enable(msg);
      }
    }
  }

  /**
   * Suppress all warnings.
   *
   * @see ErrorMessages
   */
  public static void suppressAllWarnings() {
    for (ErrorMessages msg : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(msg)) {
        Options.suppress(msg);
      }
    }
  }
}
