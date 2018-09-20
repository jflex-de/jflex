/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Collects all global JFlex options. Can be set from command line parser, ant task, gui, etc.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class Options {

  /** If true, additional verbose debug information is produced. This is a compile time option. */
  public static final boolean DEBUG = false;

  /** output directory */
  private static File directory;
  /** strict JLex compatibility */
  public static boolean jlex;
  /** don't run minimization algorithm if this is true */
  public static boolean no_minimize;
  /** don't write backup files if this is true */
  public static boolean no_backup;
  /** If false, only error/warning output will be generated */
  public static boolean verbose;
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

  static {
    setDefaults();
  }

  /**
   * getDir.
   *
   * @return a {@link java.io.File} object.
   */
  public static File getDir() {
    return directory;
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

    directory = d;
  }

  /** Sets encoding for input files, and check availability of encoding on this JVM. */
  public static void setEncoding(String encodingName) {
    if (Charset.isSupported(encodingName)) {
      encoding = Charset.forName(encodingName);
    } else {
      Out.error(ErrorMessages.CHARSET_NOT_SUPPORTED, encodingName);
      throw new GeneratorException();
    }
  }

  /** Sets all options back to default values. */
  public static void setDefaults() {
    directory = null;
    jlex = false;
    no_minimize = false;
    no_backup = false;
    verbose = true;
    progress = true;
    unused_warning = true;
    time = false;
    dot = false;
    dump = false;
    legacy_dot = false;
    encoding = Charset.defaultCharset();
    Skeleton.readDefault();
  }

  /**
   * setSkeleton.
   *
   * @param skel a {@link java.io.File} object.
   */
  public static void setSkeleton(File skel) {
    Skeleton.readSkelFile(skel);
  }
}
