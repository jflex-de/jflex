/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2017  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import jflex.LexGenerator;
import jflex.Options;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Generates lexical scanners from one or more <a href="http://jflex.de/">JFlex</a> grammar files.
 *
 * @author Régis Décamps (decamps@users.sf.net)
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = false)
public class JFlexMojo extends AbstractMojo {
  /** Name of the directory where to look for jflex files by default. */
  private static final String SRC_MAIN_JFLEX = "src/main/jflex";

  @Parameter(property = "project", required = true, readonly = true)
  private MavenProject project;

  // cannot use {@value SRC_MAIN_JFLEX} because Maven site goals.html
  // is kept raw.
  /**
   * List of grammar definitions to run the JFlex parser generator on. Each path may either specify
   * a single grammar file or a directory. Directories will be recursively scanned for files with
   * one of the following extensions: ".jflex", ".flex", ".jlex" or ".lex". By default, all files in
   * {@code src/main/jflex} will be processed.
   *
   * @see #SRC_MAIN_JFLEX
   */
  @Parameter private File[] lexDefinitions;

  /** Name of the directory into which JFlex should generate the parser. */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/jflex")
  private File outputDirectory;

  /**
   * The granularity in milliseconds of the last modification date for testing whether a source
   * needs regeneration.
   */
  @Parameter(property = "lastModGranularityMs", defaultValue = "0")
  private int staleMillis;

  /** Whether source code generation should be verbose. */
  @Parameter(defaultValue = "false")
  private boolean verbose;

  /** Whether a warning will be logged when there are unused macros. */
  @Parameter(defaultValue = "true")
  private boolean unusedWarning;

  /** Whether to dump full debug information. */
  @Parameter(defaultValue = "false")
  private boolean dump;

  /**
   * Whether to produce graphviz .dot files for the generated automata. This feature is
   * EXPERIMENTAL.
   */
  @Parameter(defaultValue = "false")
  private boolean dot;

  /** Use external skeleton file. */
  @Parameter private File skeleton;

  /** Strict JLex compatibility. */
  @Parameter(defaultValue = "false")
  private boolean jlex;

  /** The generation method to use for the scanner. The only valid value is {@code pack}. */
  @Parameter(defaultValue = "pack")
  private String generationMethod = "pack"; // NOPMD

  /** A flag whether to perform the DFA minimization step during scanner generation. */
  @Parameter(defaultValue = "true")
  private boolean minimize = true; // NOPMD

  /**
   * A flag whether to enable the generation of a backup copy if the generated source file already
   * exists.
   */
  @Parameter(defaultValue = "true")
  private boolean backup = true; // NOPMD

  /**
   * If true, the dot (.) metachar matches [^\n] instead of [^\n\r\u000B\u000C\u0085\u2028\u2029].
   */
  @Parameter(defaultValue = "false")
  private boolean legacyDot = false; // NOPMD

  /**
   * The name of the character encoding for reading lexer specifications. Uses JVM default encoding
   * if unset.
   */
  @Parameter(defaultValue = "")
  private String encodingName = ""; // NOPMD

  /**
   * Generate java parsers from lexer definition files.
   *
   * <p>This methods is checks parameters, sets options and calls JFlex.Main.generate()
   */
  public void execute() throws MojoExecutionException, MojoFailureException {
    this.outputDirectory = getAbsolutePath(this.outputDirectory);

    // compiling the generated source in target/generated-sources/ is
    // the whole point of this plugin compared to running the ant plugin
    project.addCompileSourceRoot(outputDirectory.getPath());

    List<File> filesIt;
    if (lexDefinitions == null) {
      // use default lexfiles if none provided
      getLog().debug("Use lexer files found in (default) " + SRC_MAIN_JFLEX);
      filesIt = new ArrayList<>();
      File defaultDir = getAbsolutePath(new File(SRC_MAIN_JFLEX));
      if (defaultDir.isDirectory()) {
        filesIt.add(defaultDir);
      }
    } else {
      // use arguments provided in the plugin configuration
      filesIt = Arrays.asList(lexDefinitions);

      getLog()
          .debug(
              "Parsing "
                  + lexDefinitions.length
                  + " jflex files or directories given in configuration");
    }
    // process all lexDefinitions
    for (File lexDefinition : filesIt) {
      lexDefinition = getAbsolutePath(lexDefinition);
      parseLexDefinition(lexDefinition);
    }
  }

  /**
   * Generates java code of a parser from a lexer file.
   *
   * <p>If the {@code lexDefinition} is a directory, process all lexer files contained within.
   *
   * @param lexDefinition Lexer definiton file or directory to process.
   * @throws MojoFailureException if the file is not found.
   * @throws MojoExecutionException if file could not be parsed
   */
  @SuppressWarnings("unchecked")
  private void parseLexDefinition(File lexDefinition)
      throws MojoFailureException, MojoExecutionException {
    assert lexDefinition.isAbsolute() : lexDefinition;

    if (lexDefinition.isDirectory()) {
      // recursively process files contained within
      getLog().debug("Processing lexer files found in " + lexDefinition);
      FluentIterable<File> files =
          Files.fileTreeTraverser()
              .preOrderTraversal(lexDefinition)
              .filter(new ExtensionPredicate("jflex", "jlex", "lex", "flex"));
      for (File lexFile : files) {
        parseLexFile(lexFile);
      }
    } else {
      parseLexFile(lexDefinition);
    }
  }

  private void parseLexFile(File lexFile) throws MojoFailureException, MojoExecutionException {
    assert lexFile.isAbsolute() : lexFile;

    getLog().debug("Generating Java code from " + lexFile.getName());
    ClassInfo classInfo = findClassInfo(lexFile);

    checkParameters(lexFile);

    // set destination directory
    File generatedFile = new File(outputDirectory, classInfo.getOutputFilename());

    // generate only if needs to
    if (lexFile.lastModified() - generatedFile.lastModified() <= this.staleMillis) {
      getLog().info("  " + generatedFile.getName() + " is up to date.");
      getLog().debug("StaleMillis = " + staleMillis + "ms");
      return;
    }

    // set options. Very strange that JFlex expects this in a static way.
    Options.setDefaults();
    Options.setDir(generatedFile.getParentFile());
    Options.setRootDirectory(project.getBasedir());
    Options.dump = dump;
    Options.verbose = verbose;
    Options.unused_warning = unusedWarning;
    Options.dot = dot;
    Options.legacy_dot = legacyDot;
    if (skeleton != null) {
      Options.setSkeleton(skeleton);
    }
    Options.jlex = jlex;

    Options.no_minimize = !minimize; // NOPMD
    Options.no_backup = !backup; // NOPMD
    if (!Objects.equals("pack", generationMethod)) {
      throw new MojoExecutionException("Illegal generation method: " + generationMethod);
    }

    if (!isNullOrEmpty(encodingName)) {
      try {
        Options.setEncoding(encodingName);
      } catch (Exception e) {
        throw new MojoExecutionException(e.getMessage());
      }
    }

    try {
      LexGenerator.generate(lexFile);
      getLog().info("  generated " + generatedFile);
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  private ClassInfo findClassInfo(File lexFile) throws MojoFailureException {
    try {
      return LexSimpleAnalyzer.guessPackageAndClass(lexFile);
    } catch (FileNotFoundException e) {
      throw new MojoFailureException(e.getMessage(), e);
    } catch (IOException e) {
      return new ClassInfo(LexSimpleAnalyzer.DEFAULT_NAME, /*packageName=*/ "");
    }
  }

  /**
   * Check parameter lexFile.
   *
   * <p>Must not be {@code null} and file must exist.
   *
   * @param lexFile input file to check.
   * @throws MojoExecutionException in case of error
   */
  private void checkParameters(File lexFile) throws MojoExecutionException {
    if (lexFile == null) {
      throw new MojoExecutionException(
          "<lexDefinition> is empty. Please define input file with <lexDefinition>input.jflex</lexDefinition>");
    }
    if (!lexFile.isFile()) {
      throw new MojoExecutionException("Input file does not exist: " + lexFile);
    }
  }

  /**
   * Converts the specified path argument into an absolute path. If the path is relative like
   * "src/main/jflex", it is resolved against the base directory of the project (in constrast,
   * File.getAbsoluteFile() would resolve against the current directory which may be different,
   * especially during a reactor build).
   *
   * @param path The path argument to convert, may be {@code null}.
   * @return The absolute path corresponding to the input argument.
   */
  private File getAbsolutePath(File path) {
    if (path == null || path.isAbsolute()) {
      return path;
    }
    return new File(this.project.getBasedir().getAbsolutePath(), path.getPath());
  }

  static class ExtensionPredicate implements Predicate<File> {
    final ImmutableSet<String> extensions;

    ExtensionPredicate(ImmutableSet<String> extensions) {
      this.extensions = extensions;
    }

    ExtensionPredicate(String... extensions) {
      this(ImmutableSet.copyOf(extensions));
    }

    @Override
    public boolean apply(File file) {
      return extensions.contains(Files.getFileExtension(file.getName()));
    }
  }
}
