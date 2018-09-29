/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Collects all global JFlex options. Can be set from command line parser, ant task, gui, etc.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.7.1-SNAPSHOT
 */
public class Options {

  /** If true, additional verbose debug information is produced. This is a compile time option. */
  static final boolean DEBUG = false;

  private final File rootDirectory;
  private final File outputDirectory;
  private final boolean backup;
  private final boolean legacyDot;
  private final boolean dump;
  private final boolean generateDotFile;
  private final boolean strictJlex;
  private final boolean minimize;
  private final boolean showProgress;
  private final File skeleton;
  private final boolean timing;
  private final boolean unusedWarnings;
  private final Charset encoding;
  private final boolean verbose;

  /**
   * Returns the root source directory.
   *
   * <p>In a maven project, this is the directory that contains {@code src} and {@code target}.
   */
  public File rootDirectory() {
    return rootDirectory;
  }

  /** Returns the directory to write generated code into. */
  // TODO(regisd): Clarify whether this includes java package path or not.
  public File outputDirectory() {
    return outputDirectory;
  }

  /** Whether to back-up modified files. */
  public boolean backup() {
    return backup;
  }

  /**
   * If true, dot ({@code .}) metachar matches "{@code [^\n]}" instead of "{@code
   * [^\r\n\u000B\u000C\u0085\u2028\u2029]|"\r\n}".
   */
  public boolean legacyDot() {
    return legacyDot;
  }

  /** Whether to dump generation data. You will be flooded with information (e.g. dfa tables). */
  public boolean dump() {
    return dump;
  }

  /** Whether jflex will write graphviz .dot files for generated automata (alpha feature). */
  public boolean generateDotFile() {
    return generateDotFile;
  }

  /** Whether to use strict JLex compatibility. */
  public boolean strictJlex() {
    return strictJlex;
  }

  /** Whether to minimize the DFA. */
  public boolean minimize() {
    return minimize;
  }

  /** Whether to show progress (e.g. printing dots). */
  public boolean showProgress() {
    return showProgress;
  }

  /** Skeleton file. JFlex comes with a skeleton, hence this should usually be left absent. */
  public File skeleton() {
    return skeleton;
  }

  /** Whether JFlex will record time printStatistics about the generation process */
  public boolean timing() {
    return timing;
  }

  /** Whether to warn about unused macros. */
  public boolean unusedWarnings() {
    return unusedWarnings;
  }

  /** The encoding to use for input files. */
  public Charset encoding() {
    return encoding;
  }

  /** Whether to be verbose. If false, only error/warning output will be generated. */
  public boolean verbose() {
    return verbose;
  }

  public static Builder builder() {
    return new Options.Builder()
        // Set default values
        .setRootDirectory(new File("")) // relative to where java is executed from
        .setBackup(true)
        .setDump(false)
        .setGenerateDotFile(false)
        .setLegacyDot(false)
        .setMinimize(true)
        .setShowProgress(true)
        .setStrictJlex(false)
        .setTiming(false)
        .setEncoding(Charset.defaultCharset())
        .setUnusedWarnings(true)
        .setVerbose(true);
  }

  public static Options defaultOptions() {
    return builder().build();
  }

  private Options(
      File rootDirectory,
      File outputDirectory,
      boolean backup,
      boolean legacyDot,
      boolean dump,
      boolean generateDotFile,
      boolean strictJlex,
      boolean minimize,
      boolean showProgress,
      File skeleton,
      boolean timing,
      boolean unusedWarnings,
      Charset encoding,
      boolean verbose) {
    this.backup = backup;
    this.legacyDot = legacyDot;
    this.dump = dump;
    this.generateDotFile = generateDotFile;
    this.strictJlex = strictJlex;
    this.minimize = minimize;
    this.rootDirectory = rootDirectory;
    this.outputDirectory = outputDirectory;
    this.showProgress = showProgress;
    this.skeleton = skeleton;
    this.timing = timing;
    this.unusedWarnings = unusedWarnings;
    this.encoding = encoding;
    this.verbose = verbose;
  }

  @Override
  public String toString() {
    return "Options{"
        + "rootDirectory="
        + rootDirectory
        + ", "
        + "outputDirectory="
        + outputDirectory
        + ", "
        + "backup="
        + backup
        + ", "
        + "legacyDot="
        + legacyDot
        + ", "
        + "dump="
        + dump
        + ", "
        + "generateDotFile="
        + generateDotFile
        + ", "
        + "strictJlex="
        + strictJlex
        + ", "
        + "minimize="
        + minimize
        + ", "
        + "showProgress="
        + showProgress
        + ", "
        + "skeleton="
        + skeleton
        + ", "
        + "timing="
        + timing
        + ", "
        + "unusedWarnings="
        + unusedWarnings
        + ", "
        + "verbose="
        + verbose
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Options) {
      Options that = (Options) o;
      return (this.rootDirectory.equals(that.rootDirectory()))
          && (this.outputDirectory.equals(that.outputDirectory()))
          && (this.backup == that.backup())
          && (this.legacyDot == that.legacyDot())
          && (this.dump == that.dump())
          && (this.generateDotFile == that.generateDotFile())
          && (this.strictJlex == that.strictJlex())
          && (this.minimize == that.minimize())
          && (this.showProgress == that.showProgress())
          && (this.skeleton.equals(that.skeleton()))
          && (this.timing == that.timing())
          && (this.unusedWarnings == that.unusedWarnings())
          && (this.verbose == that.verbose());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= this.rootDirectory.hashCode();
    h *= 1000003;
    h ^= this.outputDirectory.hashCode();
    h *= 1000003;
    h ^= this.backup ? 1231 : 1237;
    h *= 1000003;
    h ^= this.legacyDot ? 1231 : 1237;
    h *= 1000003;
    h ^= this.dump ? 1231 : 1237;
    h *= 1000003;
    h ^= this.generateDotFile ? 1231 : 1237;
    h *= 1000003;
    h ^= this.strictJlex ? 1231 : 1237;
    h *= 1000003;
    h ^= this.minimize ? 1231 : 1237;
    h *= 1000003;
    h ^= this.showProgress ? 1231 : 1237;
    h *= 1000003;
    h ^= this.skeleton.hashCode();
    h *= 1000003;
    h ^= this.timing ? 1231 : 1237;
    h *= 1000003;
    h ^= this.unusedWarnings ? 1231 : 1237;
    h *= 1000003;
    h ^= this.verbose ? 1231 : 1237;
    h *= 1000003;
    h ^= this.encoding.hashCode();
    return h;
  }

  public Options.Builder buildUpon() {
    return new Options.Builder(this);
  }

  public static final class Builder {

    private File rootDirectory;
    private File outputDirectory;
    private Boolean backup;
    private Boolean legacyDot;
    private Boolean dump;
    private Boolean generateDotFile;
    private Boolean strictJlex;
    private Boolean minimize;
    private Boolean showProgress;
    private File skeleton;
    private Boolean timing;
    private Boolean unusedWarnings;
    private Charset encoding;
    private Boolean verbose;

    Builder() {}

    private Builder(Options source) {
      this.rootDirectory = source.rootDirectory();
      this.outputDirectory = source.outputDirectory();
      this.backup = source.backup();
      this.legacyDot = source.legacyDot();
      this.dump = source.dump();
      this.generateDotFile = source.generateDotFile();
      this.strictJlex = source.strictJlex();
      this.minimize = source.minimize();
      this.showProgress = source.showProgress();
      this.skeleton = source.skeleton();
      this.timing = source.timing();
      this.unusedWarnings = source.unusedWarnings();
      this.verbose = source.verbose();
    }

    public Options.Builder setRootDirectory(File rootDirectory) {
      if (rootDirectory == null) {
        throw new NullPointerException("Null rootDirectory");
      }
      this.rootDirectory = rootDirectory;
      return this;
    }

    public Options.Builder setOutputDirectory(File outputDirectory) {
      if (outputDirectory == null) {
        throw new NullPointerException("Null outputDirectory");
      }
      this.outputDirectory = outputDirectory;
      return this;
    }

    public Options.Builder setBackup(boolean backup) {
      this.backup = backup;
      return this;
    }

    public Options.Builder setLegacyDot(boolean legacyDot) {
      this.legacyDot = legacyDot;
      return this;
    }

    public Options.Builder setDump(boolean dump) {
      this.dump = dump;
      return this;
    }

    public Options.Builder setGenerateDotFile(boolean generateDotFile) {
      this.generateDotFile = generateDotFile;
      return this;
    }

    public Options.Builder setStrictJlex(boolean strictJlex) {
      this.strictJlex = strictJlex;
      return this;
    }

    public Options.Builder setMinimize(boolean minimize) {
      this.minimize = minimize;
      return this;
    }

    public Options.Builder setShowProgress(boolean showProgress) {
      this.showProgress = showProgress;
      return this;
    }

    public Options.Builder setSkeleton(File skeleton) {
      if (skeleton == null) {
        throw new NullPointerException("Null skeleton");
      }
      this.skeleton = skeleton;
      return this;
    }

    public Options.Builder setTiming(boolean timing) {
      this.timing = timing;
      return this;
    }

    public Options.Builder setUnusedWarnings(boolean unusedWarnings) {
      this.unusedWarnings = unusedWarnings;
      return this;
    }

    public Options.Builder setEncoding(Charset encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder setEncoding(String encodingName) throws UnsupportedCharsetException {
      return setEncoding(Charset.forName(encodingName));
    }

    public Options.Builder setVerbose(boolean verbose) {
      this.verbose = verbose;
      return this;
    }

    public Options build() {
      String missing = "";
      if (this.rootDirectory == null) {
        missing += " rootDirectory";
      }
      if (this.outputDirectory == null) {
        missing += " outputDirectory";
      }
      if (this.backup == null) {
        missing += " backup";
      }
      if (this.legacyDot == null) {
        missing += " legacyDot";
      }
      if (this.dump == null) {
        missing += " dump";
      }
      if (this.generateDotFile == null) {
        missing += " generateDotFile";
      }
      if (this.strictJlex == null) {
        missing += " strictJlex";
      }
      if (this.minimize == null) {
        missing += " minimize";
      }
      if (this.showProgress == null) {
        missing += " showProgress";
      }
      if (this.timing == null) {
        missing += " timing";
      }
      if (this.unusedWarnings == null) {
        missing += " unusedWarnings";
      }
      if (this.encoding == null) {
        missing += " encoding";
      }
      if (this.verbose == null) {
        missing += " verbose";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new Options(
          this.rootDirectory,
          this.outputDirectory,
          this.backup,
          this.legacyDot,
          this.dump,
          this.generateDotFile,
          this.strictJlex,
          this.minimize,
          this.showProgress,
          this.skeleton,
          this.timing,
          this.unusedWarnings,
          this.encoding,
          this.verbose);
    }
  }
}
