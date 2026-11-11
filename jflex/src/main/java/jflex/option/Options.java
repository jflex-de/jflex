/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.option;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import jflex.exceptions.GeneratorException;
import jflex.l10n.ErrorMessages;
import jflex.skeleton.Skeleton;

/**
 * The options of a JFlex generator run.
 *
 * <p>A new instance has the default options. They are set from the command line parser, ant task,
 * Maven plugin, GUI, etc. Each concurrent run should use a separate instance.
 *
 * <p>Setters that validate their argument throw a {@link GeneratorException} with an error message.
 * The caller needs to reports the message with {@link jflex.logging.Out#error(GeneratorException)}.
 *
 * @author Gerwin Klein
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class Options {

  /** Warnings that should not be printed. */
  private final Set<ErrorMessages> suppressedWarnings = new HashSet<>();

  /** output directory, or null for the directory of the spec */
  private File directory;

  /**
   * The root source directory.
   *
   * <p>In a maven project, this is the directory that contains {@code src} and {@code target}.
   */
  private File rootDirectory;

  /** strict JLex compatibility */
  private boolean jlex;

  /** don't run minimization algorithm if this is true */
  private boolean noMinimize;

  /** don't write backup files if this is true */
  private boolean noBackup;

  /** If false, only error/warning output will be generated */
  private boolean verbose;

  /** If true, progress dots will be printed */
  private boolean progress;

  /** If true, jflex will print time statistics about the generation process */
  private boolean time;

  /** If true, jflex will write graphviz .dot files for generated automata */
  private boolean dot;

  /** If true, you will be flooded with information (e.g. dfa tables). */
  private boolean dump;

  /**
   * If true, dot (.) metachar matches [^\n] instead of [^\r\n\u000B\u000C\u0085\u2028\u2029]|"\r\n"
   */
  private boolean legacyDot;

  /** The encoding to use for input and output files. */
  private Charset encoding;

  /** The skeleton of the generated scanner. */
  private Skeleton skeleton;

  /** Creates default options. */
  public Options() {
    reset();
  }

  /** Sets all options back to their default values. */
  public void reset() {
    directory = null;
    rootDirectory = new File(""); // resolves to System.getProperty("user.dir")
    jlex = false;
    noMinimize = false;
    noBackup = false;
    verbose = true;
    progress = true;
    time = false;
    dot = false;
    dump = false;
    legacyDot = false;
    encoding = Charset.defaultCharset();
    skeleton = Skeleton.DEFAULT;
    enableAllWarnings();
  }

  /** Returns a copy of these options. Later changes do not affect the copy and vice versa. */
  public Options copy() {
    Options copy = new Options();
    copy.directory = directory;
    copy.rootDirectory = rootDirectory;
    copy.jlex = jlex;
    copy.noMinimize = noMinimize;
    copy.noBackup = noBackup;
    copy.verbose = verbose;
    copy.progress = progress;
    copy.time = time;
    copy.dot = dot;
    copy.dump = dump;
    copy.legacyDot = legacyDot;
    copy.encoding = encoding;
    copy.skeleton = skeleton;
    copy.suppressedWarnings.addAll(suppressedWarnings);
    return copy;
  }

  /** Returns the output directory, or null for the directory of the spec. */
  public File directory() {
    return directory;
  }

  /**
   * Sets the output directory and creates it if necessary.
   *
   * @param d the directory to write output files to
   * @throws GeneratorException if {@code d} is a file or cannot be created
   */
  public void setDir(File d) {
    if (d.isFile()) {
      throw new GeneratorException("Error: \"" + d + "\" is not a directory.");
    }

    if (!d.isDirectory() && !d.mkdirs()) {
      throw new GeneratorException("Error: couldn't create directory \"" + d + "\"");
    }

    directory = d;
  }

  /**
   * Sets the output directory and creates it if necessary.
   *
   * @param dirName the name of the directory to write output files to
   */
  public void setDir(String dirName) {
    setDir(new File(dirName));
  }

  /**
   * Returns the root directory that contains source code. This is the java working directory (from
   * system property {@code user.dir}) by default.
   */
  public File rootDirectory() {
    return rootDirectory;
  }

  /**
   * Set the root source directory.
   *
   * @param rootDir the root source directory.
   */
  public void setRootDirectory(File rootDir) {
    rootDirectory = rootDir;
  }

  /** Strict JLex compatibility. */
  public boolean jlex() {
    return jlex;
  }

  /** Sets strict JLex compatibility (command line option {@code --jlex}). */
  public void setJLex(boolean jlex) {
    this.jlex = jlex;
  }

  /** Whether to skip the minimization algorithm. */
  public boolean noMinimize() {
    return noMinimize;
  }

  /** Sets whether to skip the DFA minimization algorithm (command line option {@code --nomin}). */
  public void setNoMinimize(boolean noMinimize) {
    this.noMinimize = noMinimize;
  }

  /** Whether to skip writing backup files. */
  public boolean noBackup() {
    return noBackup;
  }

  /** Sets whether to skip writing backup files (command line option {@code --nobak}). */
  public void setNoBackup(boolean noBackup) {
    this.noBackup = noBackup;
  }

  /** If false, only error/warning output will be generated. */
  public boolean verbose() {
    return verbose;
  }

  /**
   * Sets verbose output. If false, only errors and warnings are printed (command line option {@code
   * --quiet}).
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /** Whether to print progress dots. */
  public boolean progress() {
    return progress;
  }

  /** Sets whether to print progress dots during generation. */
  public void setProgress(boolean progress) {
    this.progress = progress;
  }

  /** Whether to print time statistics about the generation process. */
  public boolean time() {
    return time;
  }

  /**
   * Sets whether to print time statistics about the generation process (command line option {@code
   * --time}).
   */
  public void setTime(boolean time) {
    this.time = time;
  }

  /** Whether to write graphviz .dot files for the generated automata. */
  public boolean dot() {
    return dot;
  }

  /**
   * Sets whether to write graphviz .dot files for the generated automata (command line option
   * {@code --dot}).
   */
  public void setDot(boolean dot) {
    this.dot = dot;
  }

  /** Whether to dump the generated automata and tables. */
  public boolean dump() {
    return dump;
  }

  /**
   * Sets whether to dump the generated automata and tables (command line option {@code --dump}).
   */
  public void setDump(boolean dump) {
    this.dump = dump;
  }

  /**
   * If true, dot (.) metachar matches [^\n] instead of [^\r\n\u000B\u000C\u0085\u2028\u2029]|"\r\n"
   */
  public boolean legacyDot() {
    return legacyDot;
  }

  /**
   * Sets whether the dot (.) metachar matches [^\n] only, as in versions before 1.7 (command line
   * option {@code --legacydot}).
   */
  public void setLegacyDot(boolean legacyDot) {
    this.legacyDot = legacyDot;
  }

  /** The encoding for input and output files. */
  public Charset encoding() {
    return encoding;
  }

  /**
   * Sets the encoding for input and output files.
   *
   * @param encoding the charset to use, e.g. from {@link Charset#forName(String)}
   */
  public void setEncoding(Charset encoding) {
    this.encoding = encoding;
  }

  /**
   * Sets the encoding for input and output files, and checks availability of the encoding on this
   * JVM.
   *
   * @param encodingName the name of the encoding, e.g. "utf-8"
   * @throws GeneratorException if the encoding is not supported
   */
  public void setEncoding(String encodingName) {
    if (Charset.isSupported(encodingName)) {
      encoding = Charset.forName(encodingName);
    } else {
      throw new GeneratorException(ErrorMessages.CHARSET_NOT_SUPPORTED, encodingName);
    }
  }

  /** The skeleton of the generated scanner. */
  public Skeleton skeleton() {
    return skeleton;
  }

  /**
   * Sets the skeleton of the generated scanner.
   *
   * @param skeleton the skeleton to use, e.g. {@link Skeleton#DEFAULT}
   */
  public void setSkeleton(Skeleton skeleton) {
    this.skeleton = skeleton;
  }

  /**
   * Reads an external skeleton file.
   *
   * @param skeletonFile the file to read (must be != null and readable)
   * @throws GeneratorException if the file cannot be read or is not a skeleton
   */
  public void setSkeleton(File skeletonFile) {
    skeleton = Skeleton.readSkelFile(skeletonFile);
  }

  /**
   * Returns true if the given warning message is suppressed (should not be printed and counted).
   *
   * @param msg the error/warning message to check
   * @return true iff the warning is suppressed
   */
  public boolean isSuppressed(ErrorMessages msg) {
    return suppressedWarnings.contains(msg);
  }

  /**
   * Returns the currently suppressed warnings.
   *
   * @return a copy of the set of suppressed warnings, for {@link #setSuppressedWarnings(Set)}
   */
  public Set<ErrorMessages> suppressedWarnings() {
    return new HashSet<>(suppressedWarnings);
  }

  /**
   * Suppresses exactly the given warnings and enables all others.
   *
   * @param warnings the warnings to suppress, e.g. from {@link #suppressedWarnings()}
   * @throws IllegalArgumentException if one of the warnings is not configurable
   */
  public void setSuppressedWarnings(Set<ErrorMessages> warnings) {
    suppressedWarnings.clear();
    for (ErrorMessages msg : warnings) {
      suppress(msg);
    }
  }

  /**
   * Configure the given warning message to be suppressed.
   *
   * @param msg the warning message to suppress.
   */
  public void suppress(ErrorMessages msg) {
    if (ErrorMessages.isConfigurableWarning(msg)) {
      suppressedWarnings.add(msg);
    } else {
      throw new IllegalArgumentException("Cannot suppress non-configurable warning: " + msg);
    }
  }

  /**
   * Configure the given warning message to be enabled.
   *
   * @param msg the warning message to enable.
   */
  public void enable(ErrorMessages msg) {
    if (ErrorMessages.isConfigurableWarning(msg)) {
      suppressedWarnings.remove(msg);
    } else {
      throw new IllegalArgumentException("Cannot enable non-configurable warning: " + msg);
    }
  }

  /**
   * Warn on unused macros or not.
   *
   * @param unusedWarning whether unused macros should be warned about.
   */
  public void setUnusedWarning(boolean unusedWarning) {
    if (unusedWarning) {
      enable(ErrorMessages.MACRO_UNUSED);
    } else {
      suppress(ErrorMessages.MACRO_UNUSED);
    }
  }

  /**
   * Enable a warning type.
   *
   * @param warning the warning to enable, must match one of the {@link ErrorMessages} enum values.
   * @throws GeneratorException if the warning is not known or not configurable.
   * @see ErrorMessages
   */
  public void enableWarning(String warning) {
    try {
      ErrorMessages msg = ErrorMessages.valueOf(warning.toUpperCase().replace('-', '_'));
      enable(msg);
    } catch (IllegalArgumentException e) {
      throw new GeneratorException(ErrorMessages.UNKNOWN_WARNING, warning);
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
  public void suppressWarning(String warning) {
    try {
      ErrorMessages msg = ErrorMessages.valueOf(warning.toUpperCase().replace('-', '_'));
      suppress(msg);
    } catch (IllegalArgumentException e) {
      throw new GeneratorException(ErrorMessages.UNKNOWN_WARNING, warning);
    }
  }

  /**
   * Enable all warnings.
   *
   * @see ErrorMessages
   */
  public void enableAllWarnings() {
    for (ErrorMessages msg : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(msg)) {
        enable(msg);
      }
    }
  }

  /**
   * Suppress all warnings.
   *
   * @see ErrorMessages
   */
  public void suppressAllWarnings() {
    for (ErrorMessages msg : ErrorMessages.values()) {
      if (ErrorMessages.isConfigurableWarning(msg)) {
        suppress(msg);
      }
    }
  }
}
