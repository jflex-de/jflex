package jflex;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import java.io.File;

@AutoValue
public abstract class GeneratorOptions {
  /** If true, additional verbose debug information is produced. This is a compile time option. */
  static final boolean DEBUG = false;

  /** Whether to back-up modified files. */
  public abstract boolean backup();
  /**
   * If true, dot ({@code .}) metachar matches "{@code [^\n]}" instead of "{@code
   * [^\r\n\u000B\u000C\u0085\u2028\u2029]|"\r\n}".
   */
  public abstract boolean legacyDot();

  /** Whether to dump generation data. You will be flooded with information (e.g. dfa tables). */
  public abstract boolean dump();
  /** Whether jflex will write graphviz .dot files for generated automata (alpha feature). */
  public abstract boolean generateDotFile();
  /** Whether to use strict JLex compatibility. */
  public abstract boolean strictJlex();
  /** Whether to minimize the DFA. */
  public abstract boolean minimize();
  /** The directory to write generated code into. */
  // TODO(regisd): Clarify whether this includes java package path or not.
  public abstract Optional<File> outputDirectory();
  /** Whether to show progress (e.g. printing dots). */
  public abstract boolean showProgress();
  /** Skeleton file. JFlex comes with a skeleton, hence this should usually be left absent. */
  public abstract Optional<File> skeleton();
  /** Whether JFlex will record time printStatistics about the generation process */
  public abstract boolean timing();
  /** Whether to warn about unused macros. */
  public abstract boolean unusedWarnings();
  /** Whether to be verbose. If false, only error/warning output will be generated. */
  public abstract boolean verbose();

  public static GeneratorOptions.Builder builder() {
    return new AutoValue_GeneratorOptions.Builder()
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

  public abstract Builder buildUpon();

  public static GeneratorOptions defaultOptions() {
    return builder().build();
  }

  @AutoValue.Builder
  public abstract static class Builder {

    public abstract GeneratorOptions build();

    public abstract Builder setBackup(boolean backup);

    public abstract Builder setDump(boolean shouldDump);

    public abstract Builder setGenerateDotFile(boolean generateDotFile);

    public abstract Builder setLegacyDot(boolean legacyDot);

    public abstract Builder setMinimize(boolean enableMinimizationStep);

    public abstract Builder setOutputDirectory(File outputDirectory);

    public abstract Builder setShowProgress(boolean showProgress);

    public abstract Builder setStrictJlex(boolean strict);

    public abstract Builder setSkeleton(File skeleton);

    public abstract Builder setTiming(boolean profilePerformance);

    public abstract Builder setUnusedWarnings(boolean showUnusedWarnings);

    public abstract Builder setVerbose(boolean isVerbose);
  }
}
