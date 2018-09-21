/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Anttask                                                           *
 * Copyright (C) 2001       Rafal Mantiuk <Rafal.Mantiuk@bellstream.pl>    *
 * Copyright (C) 2003       changes by Gerwin Klein <lsf@jflex.de>         *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.anttask;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jflex.Main;
import jflex.Options;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * JFlex ant task.
 *
 * @author Rafal Mantiuk
 * @version JFlex 1.7.0
 */
public class JFlexTask extends Task {
  private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+(\\S+)\\s*;");
  private static final Pattern CLASS_PATTERN = Pattern.compile("%class\\s+(\\S+)");

  private File inputFile;

  // found out by looking into .flex file
  private String className = null;
  private String packageName = null;

  /** for javac-like dest dir behaviour */
  private File destinationDir;

  /** the actual output directory (outputDir = destinationDir + package)) */
  private File outputDir = null;

  /** Constructor for JFlexTask. */
  public JFlexTask() {
    // ant default is different from the rest of JFlex
    setVerbose(false);
    setUnusedWarning(true);
    Options.progress = false;
  }

  /**
   * Executes the ant task.
   *
   * @throws org.apache.tools.ant.BuildException if any.
   */
  public void execute() throws BuildException {
    try {
      if (inputFile == null)
        throw new BuildException("Input file needed. Use <jflex file=\"your_scanner.flex\"/>");

      if (!inputFile.canRead()) throw new BuildException("Cannot read input file " + inputFile);

      try {
        findPackageAndClass();
        normalizeOutdir();
        File destFile = new File(outputDir, className + ".java");

        if (inputFile.lastModified() > destFile.lastModified()) {
          Main.generate(inputFile);
          if (!Options.verbose) System.out.println("Generated: " + destFile.getName());
        }
      } catch (IOException e1) {
        throw new BuildException("IOException: " + e1.toString());
      }
    } catch (jflex.GeneratorException e) {
      throw new BuildException("JFlex: generation failed!");
    }
  }

  /**
   * Peek into .flex file to get package and class name
   *
   * @throws java.io.IOException if there is a problem reading the .flex file
   */
  public void findPackageAndClass() throws IOException {
    // find name of the package and class in jflex source file
    packageName = null;
    className = null;

    try (LineNumberReader reader = new LineNumberReader(new FileReader(inputFile))) {
      while (className == null || packageName == null) {
        String line = reader.readLine();
        if (line == null) {
          break;
        }

        if (packageName == null) {
          Matcher matcher = PACKAGE_PATTERN.matcher(line);
          if (matcher.find()) {
            packageName = matcher.group(1);
          }
        }

        if (className == null) {
          Matcher matcher = CLASS_PATTERN.matcher(line);
          if (matcher.find()) {
            className = matcher.group(1);
          }
        }
      }

      // package name may be null, but class name not
      if (className == null) {
        className = "Yylex";
      }
    }
  }

  /**
   * Sets the actual output directory if not already set.
   *
   * <p>Uses javac logic to determine output dir = dest dir + package name If not destdir has been
   * set, output dir = parent of input file
   *
   * <p>Assumes that package name is already set.
   */
  public void normalizeOutdir() {
    if (outputDir != null) return;

    // find out what the destination directory is. Append packageName to dest
    // dir.
    File destDir;

    // this is not the default the jflex logic, but javac-like
    if (destinationDir != null) {
      if (packageName == null) {
        destDir = destinationDir;
      } else {
        String path = packageName.replace('.', File.separatorChar);
        destDir = new File(destinationDir, path);
      }
    } else { // save parser to the same dir as .flex
      destDir = new File(inputFile.getParent());
    }

    setOutdir(destDir);
  }

  /**
   * getPackage.
   *
   * @return package name of input file
   * @see #findPackageAndClass()
   */
  public String getPackage() {
    return packageName;
  }

  /**
   * Getter for the field <code>className</code>.
   *
   * @return class name of input file
   * @see #findPackageAndClass()
   */
  public String getClassName() {
    return className;
  }

  /**
   * setDestdir.
   *
   * @param destinationDir a {@link java.io.File} object.
   */
  public void setDestdir(File destinationDir) {
    this.destinationDir = destinationDir;
  }

  /**
   * setOutdir.
   *
   * @param outDir a {@link java.io.File} object.
   */
  public void setOutdir(File outDir) {
    this.outputDir = outDir;
    Options.setDir(outputDir);
  }

  /**
   * setFile.
   *
   * @param file a {@link java.io.File} object.
   */
  public void setFile(File file) {
    this.inputFile = file;
  }

  /**
   * setGenerateDot.
   *
   * @param genDot a boolean.
   */
  public void setGenerateDot(boolean genDot) {
    setDot(genDot);
  }

  /**
   * setTimeStatistics.
   *
   * @param displayTime a boolean.
   */
  public void setTimeStatistics(boolean displayTime) {
    Options.time = displayTime;
  }

  /**
   * setTime.
   *
   * @param displayTime a boolean.
   */
  public void setTime(boolean displayTime) {
    setTimeStatistics(displayTime);
  }

  /**
   * setVerbose.
   *
   * @param verbose a boolean.
   */
  public void setVerbose(boolean verbose) {
    Options.verbose = verbose;
    Options.unused_warning = verbose;
  }

  /**
   * setUnusedWarning.
   *
   * @param warn a boolean.
   */
  public void setUnusedWarning(boolean warn) {
    Options.unused_warning = warn;
  }

  /**
   * setSkeleton.
   *
   * @param skeleton a {@link java.io.File} object.
   */
  public void setSkeleton(File skeleton) {
    Options.setSkeleton(skeleton);
  }

  /**
   * setSkel.
   *
   * @param skeleton a {@link java.io.File} object.
   */
  public void setSkel(File skeleton) {
    setSkeleton(skeleton);
  }

  /**
   * setSkipMinimization.
   *
   * @param skipMin a boolean.
   */
  public void setSkipMinimization(boolean skipMin) {
    setNomin(skipMin);
  }

  /**
   * setNomin.
   *
   * @param b a boolean.
   */
  public void setNomin(boolean b) {
    Options.no_minimize = b;
  }

  /**
   * setNobak.
   *
   * @param b a boolean.
   */
  public void setNobak(boolean b) {
    Options.no_backup = b;
  }

  /**
   * setPack.
   *
   * @param b a boolean.
   */
  public void setPack(boolean b) {
    /* no-op - this is the only available generation method */
  }

  /**
   * setDot.
   *
   * @param b a boolean.
   */
  public void setDot(boolean b) {
    Options.dot = b;
  }

  /**
   * setDump.
   *
   * @param b a boolean.
   */
  public void setDump(boolean b) {
    Options.dump = b;
  }

  /**
   * setJLex.
   *
   * @param b a boolean.
   */
  public void setJLex(boolean b) {
    Options.jlex = b;
  }

  /**
   * setLegacyDot.
   *
   * @param b a boolean.
   */
  public void setLegacyDot(boolean b) {
    Options.legacy_dot = b;
  }

  /**
   * Set the input encoding. If unset will use the JVM default.
   *
   * @param encodingName the name of the encoding to set (e.g. "utf-8").
   */
  public void setEncoding(String encodingName) {
    Options.setEncoding(encodingName);
  }
}
