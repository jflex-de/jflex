package jflex;

import java.io.File;

public class Options {

  /** If true, additional verbose debug information is produced. This is a compile time option. */
  static final boolean DEBUG = false;

  private final boolean backup;
  private final boolean legacyDot;
  private final boolean dump;
  private final boolean generateDotFile;
  private final boolean strictJlex;
  private final boolean minimize;
  private final File outputDirectory;
  private final boolean showProgress;
  private final File skeleton;
  private final boolean timing;
  private final boolean unusedWarnings;
  private final boolean verbose;

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

  /** The directory to write generated code into. */
  // TODO(regisd): Clarify whether this includes java package path or not.
  public File outputDirectory() {
    return outputDirectory;
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

  /** Whether to be verbose. If false, only error/warning output will be generated. */
  public boolean verbose() {
    return verbose;
  }

  public static Builder builder() {
    return new Options.Builder()
        // Set default values
        .setBackup(true)
        .setDump(false)
        .setGenerateDotFile(false)
        .setLegacyDot(false)
        .setMinimize(true)
        .setShowProgress(true)
        .setStrictJlex(false)
        .setTiming(false)
        .setUnusedWarnings(true)
        .setVerbose(true);
  }

  public static Options defaultOptions() {
    return builder().build();
  }

  private Options(
      boolean backup,
      boolean legacyDot,
      boolean dump,
      boolean generateDotFile,
      boolean strictJlex,
      boolean minimize,
      File outputDirectory,
      boolean showProgress,
      File skeleton,
      boolean timing,
      boolean unusedWarnings,
      boolean verbose) {
    this.backup = backup;
    this.legacyDot = legacyDot;
    this.dump = dump;
    this.generateDotFile = generateDotFile;
    this.strictJlex = strictJlex;
    this.minimize = minimize;
    this.outputDirectory = outputDirectory;
    this.showProgress = showProgress;
    this.skeleton = skeleton;
    this.timing = timing;
    this.unusedWarnings = unusedWarnings;
    this.verbose = verbose;
  }

  @Override
  public String toString() {
    return "Options{"
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
        + "outputDirectory="
        + outputDirectory
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
      return (this.backup == that.backup())
          && (this.legacyDot == that.legacyDot())
          && (this.dump == that.dump())
          && (this.generateDotFile == that.generateDotFile())
          && (this.strictJlex == that.strictJlex())
          && (this.minimize == that.minimize())
          && (this.outputDirectory.equals(that.outputDirectory()))
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
    h ^= this.outputDirectory.hashCode();
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
    return h;
  }

  public Options.Builder buildUpon() {
    return new Options.Builder(this);
  }

  public static final class Builder {

    private Boolean backup;
    private Boolean legacyDot;
    private Boolean dump;
    private Boolean generateDotFile;
    private Boolean strictJlex;
    private Boolean minimize;
    private File outputDirectory;
    private Boolean showProgress;
    private File skeleton;
    private Boolean timing;
    private Boolean unusedWarnings;
    private Boolean verbose;

    Builder() {}

    private Builder(Options source) {
      this.backup = source.backup();
      this.legacyDot = source.legacyDot();
      this.dump = source.dump();
      this.generateDotFile = source.generateDotFile();
      this.strictJlex = source.strictJlex();
      this.minimize = source.minimize();
      this.outputDirectory = source.outputDirectory();
      this.showProgress = source.showProgress();
      this.skeleton = source.skeleton();
      this.timing = source.timing();
      this.unusedWarnings = source.unusedWarnings();
      this.verbose = source.verbose();
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

    public Options.Builder setOutputDirectory(File outputDirectory) {
      if (outputDirectory == null) {
        throw new NullPointerException("Null outputDirectory");
      }
      this.outputDirectory = outputDirectory;
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

    public Options.Builder setVerbose(boolean verbose) {
      this.verbose = verbose;
      return this;
    }

    public Options build() {
      String missing = "";
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
      if (this.verbose == null) {
        missing += " verbose";
      }
      if (!missing.isEmpty()) {
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new Options(
          this.backup,
          this.legacyDot,
          this.dump,
          this.generateDotFile,
          this.strictJlex,
          this.minimize,
          this.outputDirectory,
          this.showProgress,
          this.skeleton,
          this.timing,
          this.unusedWarnings,
          this.verbose);
    }
  }
}
