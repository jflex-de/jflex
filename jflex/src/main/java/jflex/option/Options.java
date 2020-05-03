/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.option;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Collects all global JFlex options.
 *
 * <p>Can be set from command line parser, ant task, gui, etc.
 *
 * @see jflex.core.OptionUtils
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class Options {

  /** output directory */
  public static File directory;
  /**
   * The root source directory.
   *
   * <p>In a maven project, this is the directory that contains {@code src} and {@code target}.
   */
  private static File rootDirectory;
  /** strict JLex compatibility */
  public static boolean jlex;
  /** don't run minimization algorithm if this is true */
  public static boolean no_minimize;
  /** don't write backup files if this is true */
  public static boolean no_backup;
  /** If false, only error/warning output will be generated */
  public static boolean verbose = true;
  /** Whether to warn about unused macros. */
  public static boolean unused_warning;
  /** If true, progress dots will be printed */
  public static boolean progress;
  /** If true, jflex will print time statistics about the generation process */
  public static boolean time;
  /** If true, jflex will write graphviz .dot files for generated automata */
  public static boolean dot;
  /** If true, you will be flooded with information (e.g. dfa tables). */
  public static boolean dump;
  /**
   * If true, dot (.) metachar matches [^\n] instead of [^\r\n\u000B\u000C\u0085\u2028\u2029]|"\r\n"
   */
  public static boolean legacy_dot;
  /** The encoding to use for input and output files. */
  public static Charset encoding;

  /** Prevent instantiation of static-only calss */
  // (to be changed to instances in thread-safety refactor)
  private Options() {}

  public static File getDir() {
    return directory;
  }

  /**
   * Returns the root directory that contains source code. This is the java working (from system
   * property {@code user.dir}) by default.
   */
  public static File getRootDirectory() {
    return rootDirectory;
  }

  public static void setRootDirectory(File rootDir) {
    rootDirectory = rootDir;
  }

  public static void resetRootDirectory() {
    rootDirectory = new File("");
  }
}
