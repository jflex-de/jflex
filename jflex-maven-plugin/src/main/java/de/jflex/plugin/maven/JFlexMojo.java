/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Maven3 plugin                                                     *
 * Copyright (c) 2007-2015  Régis Décamps <decamps@users.sf.net>           *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package de.jflex.plugin.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import jflex.Main;
import jflex.Options;

/**
 * Generates lexical scanners from one or more <a href="http://jflex.de/">JFlex</a>
 * grammar files.
 * 
 * @goal generate
 * @phase generate-sources
 * @author Régis Décamps (decamps@users.sf.net)
 * 
 */
public class JFlexMojo extends AbstractMojo {
	/**
	 * Name of the directory where to look for jflex files by default.
	 */
	public static final String SRC_MAIN_JFLEX = "src/main/jflex";

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	// cannot use {@value SRC_MAIN_JFLEX} because Maven site goals.html
	// is kept raw.
	/**
	 * List of grammar definitions to run the JFlex parser generator on.
	 * Each path may either specify a single grammar file or a directory.
	 * Directories will be recursively scanned for files with one of the
	 * following extensions: ".jflex", ".flex", ".jlex" or ".lex".
	 * By default, all files in <code>src/main/jflex</code> will be
	 * processed.
	 * 
	 * @see #SRC_MAIN_JFLEX
	 * @parameter
	 */
	private File[] lexDefinitions;

	/**
	 * Name of the directory into which JFlex should generate the parser.
	 * 
	 * @parameter expression="${project.build.directory}/generated-sources/jflex"
	 */
	private File outputDirectory;

	/**
	 * The granularity in milliseconds of the last modification date for
	 * testing whether a source needs regeneration.
	 * 
	 * @parameter expression="${lastModGranularityMs}" default-value="0"
	 */
	private int staleMillis;

	/**
	 * Whether source code generation should be verbose.
	 * 
	 * @parameter default-value="false"
	 */
	private boolean verbose;
	
	/**
	 * Whether a warning will be logged when there are unused macros.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean unusedWarning;

	/**
	 * Whether to dump full debug information.
	 *
	 * @parameter default-value="false"
	 */
	private boolean dump;

	/**
	 * Whether to produce graphviz .dot files for the generated automata. This
	 * feature is EXPERIMENTAL.
	 * 
	 * @parameter default-value="false"
	 */
	private boolean dot;

	/**
	 * Use external skeleton file.
	 * 
	 * @parameter
	 */
	private File skeleton;

	/**
	 * Strict JLex compatibility.
	 * 
	 * @parameter default-value="false"
	 */
	private boolean jlex;

	/**
	 * The generation method to use for the scanner. The only valid value is
	 * <code>pack</code>.
	 * 
	 * @parameter default-value="pack"
	 */
	private String generationMethod = "pack"; // NOPMD

	/**
	 * A flag whether to perform the DFA minimization step during scanner
	 * generation.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean minimize = true; // NOPMD

	/**
	 * A flag whether to enable the generation of a backup copy if the generated
	 * source file already exists.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean backup = true; // NOPMD

  /**
   * If true, the dot (.) metachar matches [^\n]
   * instead of [^\n\r\u000B\u000C\u0085\u2028\u2029].
   *
   * @parameter default-value="false"
   */
  private boolean legacyDot = false; // NOPMD

  // TODO: In the JFlex version after 1.6, this parameter will cease to exist.
  /**
   * If true, the generated scanner will include a constructor taking
   * an InputStream.
   *
   * @parameter default-value="false"
   */
  private boolean inputStreamCtor = false; // NOPMD

  /**
	 * Generate java parsers from lexer definition files.
	 *
	 * This methods is checks parameters, sets options and calls
	 * JFlex.Main.generate()
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
			filesIt = new ArrayList<File>();
			File defaultDir = getAbsolutePath(new File(SRC_MAIN_JFLEX));
			if (defaultDir.isDirectory()) {
				filesIt.add(defaultDir);
			}
		}
		else {
			// use arguments provided in the plugin configuration
			filesIt = Arrays.asList(lexDefinitions);

			getLog().debug("Parsing " + lexDefinitions.length
					+ " jflex files or directories given in configuration");
		}
		// process all lexDefinitions
		for (File lexDefinition : filesIt) {
			lexDefinition = getAbsolutePath(lexDefinition);
			parseLexDefinition(lexDefinition);
		}
	}

	/**
	 * Generate java code of a parser from a lexer file.
	 * 
	 * If the {@code lexDefinition} is a directory, process all lexer files
	 * contained within.
	 * 
	 * @param lexDefinition
	 *            Lexer definiton file or directory to process.
	 * @throws MojoFailureException
	 *             if the file is not found.
	 * @throws MojoExecutionException
	 */
	@SuppressWarnings("unchecked")
	private void parseLexDefinition(File lexDefinition)
			throws MojoFailureException, MojoExecutionException {
		assert lexDefinition.isAbsolute() : lexDefinition;

		if (lexDefinition.isDirectory()) {
			// recursively process files contained within
			String[] extensions = { "jflex", "jlex", "lex", "flex" };
			getLog().debug("Processing lexer files found in "
					+ lexDefinition);
			Iterator<File> fileIterator = FileUtils.iterateFiles(lexDefinition,
					extensions, true);
			while (fileIterator.hasNext()) {
				File lexFile = fileIterator.next();
				parseLexFile(lexFile);
			}
		} else {
			parseLexFile(lexDefinition);
		}
	}

	private void parseLexFile(File lexFile) throws MojoFailureException,
			MojoExecutionException {
		assert lexFile.isAbsolute() : lexFile;

		getLog().debug("Generating Java code from " + lexFile.getName());
		ClassInfo classInfo;
		try {
			classInfo = LexSimpleAnalyzer.guessPackageAndClass(lexFile);
		} catch (FileNotFoundException e) {
			throw new MojoFailureException(e.getMessage(), e);
		} catch (IOException e) {
			classInfo = new ClassInfo();
			classInfo.className = LexSimpleAnalyzer.DEFAULT_NAME;
			classInfo.packageName = null; // NOPMD
		}

		checkParameters(lexFile);

		/* set destination directory */
		File generatedFile = new File(outputDirectory,
				classInfo.getOutputFilename());

		/* Generate only if needs to */
		if (lexFile.lastModified() - generatedFile.lastModified() <= this.staleMillis) {
			getLog().info("  " + generatedFile.getName() + " is up to date.");
			getLog().debug("StaleMillis = "+staleMillis+"ms");
			return;
		}

		/*
		 * set options. Very strange that JFlex expects this in a static way.
		 */
		Options.setDefaults();
		Options.setDir(generatedFile.getParentFile());
		Options.dump = dump;
		Options.verbose = verbose;
		Options.unused_warning = unusedWarning;
		Options.dot = dot;
    Options.legacy_dot = legacyDot;
    Options.emitInputStreamCtor = inputStreamCtor;
		if (skeleton != null) {
			Options.setSkeleton(skeleton);
		}
		Options.jlex = jlex;

		Options.no_minimize = !minimize; // NOPMD
		Options.no_backup = !backup;     // NOPMD
		if ("pack".equals(generationMethod)) {
			/* no-op - there is only one generation method */
		} else {
			throw new MojoExecutionException("Illegal generation method: "
					+ generationMethod);
		}

		try {
			Main.generate(lexFile);
			getLog().info("  generated " + generatedFile);
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	/**
	 * Check parameter lexFile.
	 * 
	 * Must not be null and file must exist.
	 * 
	 * @param lexFile
	 *            input file to check.
	 * @throws MojoExecutionException
	 *             in case of error
	 */
	private void checkParameters(File lexFile) throws MojoExecutionException {
		if (lexFile == null) {
			throw new MojoExecutionException(
					"<lexDefinition> is empty. Please define input file with <lexDefinition>input.jflex</lexDefinition>");
		}
		assert lexFile.isAbsolute() : lexFile;
		if (!lexFile.isFile()) {
			throw new MojoExecutionException("Input file does not exist: "
					+ lexFile);
		}
	}

	/**
	 * Converts the specified path argument into an absolute path. If the path
	 * is relative like "src/main/jflex", it is resolved against the base
	 * directory of the project (in constrast, File.getAbsoluteFile() would
	 * resolve against the current directory which may be different, especially
	 * during a reactor build).
	 * 
	 * @param path
	 *            The path argument to convert, may be {@code null}.
	 * @return The absolute path corresponding to the input argument.
	 */
	protected File getAbsolutePath(File path) {
		if (path == null || path.isAbsolute()) {
			return path;
		}
		return new File(this.project.getBasedir().getAbsolutePath(), path.getPath());
	}
}
