/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jflex.base.Build;
import jflex.core.unicode.UnicodeProperties;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.SilentExit;
import jflex.generator.LexGenerator;
import jflex.gui.MainFrame;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;

/**
 * This is the command-line interface.
 *
 * <p>It is responsible for parsing the commandline, getting input files, starting up the GUI if
 * necessary, etc. and invokes {@link LexGenerator} accordingly.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class Main {

  /**
   * parseOptions.
   *
   * @param argv an array of {@link java.lang.String} objects.
   * @param options the options to set from the command line.
   * @param out where to print messages.
   * @return a {@link java.util.List} of input files.
   * @throws SilentExit if any.
   * @throws GeneratorException on invalid options. Contains the error message if the error has not
   *     been reported yet
   */
  private static List<File> parseOptions(String[] argv, Options options, Out out)
      throws SilentExit {
    List<File> files = new ArrayList<>();

    for (int i = 0; i < argv.length; i++) {

      if (Objects.equals(argv[i], "-d")
          || Objects.equals(argv[i], "--outdir")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          out.error(ErrorMessages.NO_DIRECTORY);
          throw new GeneratorException();
        }
        options.setDir(argv[i]);
        continue;
      }

      if (Objects.equals(argv[i], "--skel")
          || Objects.equals(argv[i], "-skel")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          out.error(ErrorMessages.NO_SKEL_FILE);
          throw new GeneratorException();
        }

        out.println(ErrorMessages.READING_SKEL, argv[i]);
        options.setSkeleton(new File(argv[i]));
        continue;
      }

      if (Objects.equals(argv[i], "--encoding")) {
        if (++i >= argv.length) {
          out.error(ErrorMessages.NO_ENCODING);
          throw new GeneratorException();
        }

        options.setEncoding(argv[i]);
        continue;
      }

      if (Objects.equals(argv[i], "-jlex")
          || Objects.equals(argv[i], "--jlex")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setJLex(true);
        continue;
      }

      if (Objects.equals(argv[i], "-v")
          || Objects.equals(argv[i], "--verbose")
          || Objects.equals(argv[i], "-verbose")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        options.setVerbose(true);
        options.setProgress(true);
        options.enable(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "-q")
          || Objects.equals(argv[i], "--quiet")
          || Objects.equals(argv[i], "-quiet")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        options.setVerbose(false);
        options.setProgress(false);
        options.suppress(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "--warn-all")) { // $NON-NLS-1$
        options.enableAllWarnings();
        continue;
      }

      if (Objects.equals(argv[i], "--no-warn-all")) { // $NON-NLS-1$
        options.suppressAllWarnings();
        continue;
      }

      if (Objects.equals(argv[i], "--warn-unused")) { // $NON-NLS-1$
        options.enable(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "--no-warn-unused")) { // $NON-NLS-1$
        options.suppress(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (argv[i].startsWith("--warn-")) { // $NON-NLS-1$
        options.enableWarning(argv[i].substring(7));
        continue;
      }

      if (argv[i].startsWith("--no-warn-")) { // $NON-NLS-1$
        options.suppressWarning(argv[i].substring(10));
        continue;
      }

      if (Objects.equals(argv[i], "--dump")
          || Objects.equals(argv[i], "-dump")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setDump(true);
        continue;
      }

      if (Objects.equals(argv[i], "--time")
          || Objects.equals(argv[i], "-time")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setTime(true);
        continue;
      }

      if (Objects.equals(argv[i], "--version")
          || Objects.equals(argv[i], "-version")) { // $NON-NLS-1$ //$NON-NLS-2$
        out.println(ErrorMessages.THIS_IS_JFLEX, Build.VERSION);
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--dot")
          || Objects.equals(argv[i], "-dot")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setDot(true);
        continue;
      }

      if (Objects.equals(argv[i], "--help")
          || Objects.equals(argv[i], "-h")
          || Objects.equals(argv[i], "/h")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        printUsage(out);
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--info")
          || Objects.equals(argv[i], "-info")) { // $NON-NLS-1$ //$NON-NLS-2$
        printSystemInfo(out);
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--nomin")
          || Objects.equals(argv[i], "-nomin")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setNoMinimize(true);
        continue;
      }

      if (Objects.equals(argv[i], "--pack")
          || Objects.equals(argv[i], "-pack")) { // $NON-NLS-1$ //$NON-NLS-2$
        /* no-op - pack is the only generation method */
        continue;
      }

      if (Objects.equals(argv[i], "--nobak")
          || Objects.equals(argv[i], "-nobak")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setNoBackup(true);
        continue;
      }

      if (Objects.equals(argv[i], "--legacydot")
          || Objects.equals(argv[i], "-legacydot")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setLegacyDot(true);
        continue;
      }

      if (Objects.equals(argv[i], "--uniprops")
          || Objects.equals(argv[i], "-uniprops")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          out.error(
              ErrorMessages.PROPS_ARG_REQUIRES_UNICODE_VERSION, UnicodeProperties.UNICODE_VERSIONS);
          throw new GeneratorException();
        }
        String unicodeVersion = argv[i];
        try {
          printUnicodePropertyValuesAndAliases(unicodeVersion, out);
        } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
          out.error(
              ErrorMessages.UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE,
              UnicodeProperties.UNICODE_VERSIONS);
          throw new GeneratorException(e);
        }
        throw new SilentExit();
      }

      if (argv[i].startsWith("-")) { // $NON-NLS-1$
        out.error(ErrorMessages.UNKNOWN_COMMANDLINE, argv[i]);
        printUsage(out);
        throw new SilentExit();
      }

      // if argv[i] is not an option, try to read it as file
      File f = new File(argv[i]);
      if (f.isFile() && f.canRead()) files.add(f);
      else {
        out.error("Sorry, couldn't open \"" + f + "\""); // $NON-NLS-2$
        throw new GeneratorException();
      }
    }

    return files;
  }

  /**
   * Prints one Unicode property value per line, along with its aliases, if any, for the given
   * unicodeVersion.
   *
   * @param unicodeVersion The Unicode version to print property values and aliases for
   * @param out where to print to
   * @throws UnicodeProperties.UnsupportedUnicodeVersionException if unicodeVersion is not supported
   */
  private static void printUnicodePropertyValuesAndAliases(String unicodeVersion, Out out)
      throws UnicodeProperties.UnsupportedUnicodeVersionException {
    Pattern versionPattern = Pattern.compile("(\\d+)(?:\\.(\\d+))?(?:\\.\\d+)?");
    Matcher matcher = versionPattern.matcher(unicodeVersion);
    if (!matcher.matches()) {
      throw new UnicodeProperties.UnsupportedUnicodeVersionException();
    }
    String underscoreVersion =
        matcher.group(1) + (null == matcher.group(2) ? "_0" : "_" + matcher.group(2));

    String[] propertyValues;
    String[] propertyValueAliases;
    try {
      Class<?> clazz = Class.forName("jflex.unicode.data.Unicode_" + underscoreVersion);
      Field field = clazz.getField("propertyValues");
      propertyValues = (String[]) field.get(null);
      field = clazz.getField("propertyValueAliases");
      propertyValueAliases = (String[]) field.get(null);
    } catch (Exception e) {
      throw new UnicodeProperties.UnsupportedUnicodeVersionException(e);
    }
    SortedMap<String, SortedSet<String>> propertyValuesToAliases = new TreeMap<>();
    for (String value : propertyValues) {
      propertyValuesToAliases.put(value, new TreeSet<String>());
    }
    for (int i = 0; i < propertyValueAliases.length - 1; i += 2) {
      String alias = propertyValueAliases[i];
      String value = propertyValueAliases[i + 1];
      SortedSet<String> aliases = propertyValuesToAliases.get(value);
      if (null == aliases) {
        aliases = new TreeSet<>();
        propertyValuesToAliases.put(value, aliases);
      }
      aliases.add(alias);
    }
    for (Map.Entry<String, SortedSet<String>> entry : propertyValuesToAliases.entrySet()) {
      String value = entry.getKey();
      SortedSet<String> aliases = entry.getValue();
      out.print(value);
      if (aliases.size() > 0) {
        for (String alias : aliases) {
          out.print(", " + alias);
        }
      }
      out.println("");
    }
  }

  /** Prints the cli usage. */
  private static void printUsage(Out out) {
    out.println(""); // $NON-NLS-1$
    out.println("Usage: jflex <options> <input-files>");
    out.println("");
    out.println("Where <options> can be one or more of");
    out.println("-d <directory>       write generated file to <directory>");
    out.println("--skel <file>        use external skeleton <file>");
    out.println("--encoding <name>    use <name> as input/output encoding");
    out.println("--pack               set default code generation method (default)");
    out.println("--jlex               strict JLex compatibility");
    out.println("--legacydot          dot (.) metachar matches [^\\n] instead of");
    out.println("                       [^\\n\\r\\u000B\\u000C\\u0085\\u2028\\u2029]");
    out.println("--nomin              skip minimization step");
    out.println("--nobak              don't create backup files");
    out.println("--dump               display transition tables");
    out.println(
        "--dot                write graphviz .dot files for the generated automata (alpha)");
    out.println("--verbose");
    out.println("-v                   display generation progress messages (default)");
    out.println("--quiet");
    out.println("-q                   display errors only");
    out.println("--warn-<warning>     switch on warning type <warning>, where <warning> is one of");
    out.println("                       unused, never-match, empty-match, cupsym-after-cup,");
    out.println("                       unicode-too-long, all.");
    out.println("--no-warn-<warning>  suppress warnings of type <warning>");
    out.println("--time               display generation time statistics");
    out.println("--version            print the version number of this copy of jflex");
    out.println("--info               print system + JDK information");
    out.println("--uniprops <ver>     print all supported properties for Unicode version <ver>");
    out.println("--help");
    out.println("-h                   print this message");
    out.println("");
    out.println(ErrorMessages.THIS_IS_JFLEX, Build.VERSION);
    out.println("Have a nice day!");
  }

  /**
   * Runs the generator with command line arguments, printing to {@code System.out}.
   *
   * @param argv the command line.
   * @throws SilentExit if any.
   * @throws GeneratorException if the options are invalid or generation fails. Error content will
   *     already have been reported in outut.
   */
  public static void generate(String[] argv) throws SilentExit {
    generate(argv, System.out);
  }

  /**
   * Runs the generator with command line arguments.
   *
   * @param argv the command line.
   * @param log where to print all messages.
   * @throws SilentExit if any.
   * @throws GeneratorException if the options are invalid or generation fails. Error content will
   *     already have been reported in outut.
   */
  public static void generate(String[] argv, OutputStream log) throws SilentExit {
    Options options = new Options();
    generate(argv, options, new Out(options, log));
  }

  private static void generate(String[] argv, Options options, Out out) throws SilentExit {
    List<File> files;
    try {
      files = parseOptions(argv, options, out);
    } catch (GeneratorException e) {
      // errors thrown by Options have not been reported yet
      out.error(e);
      throw e;
    }

    if (files.size() > 0) {
      for (File file : files) {
        new LexGenerator(file, options, out).generate();
      }
    } else {
      new MainFrame(options);
    }
  }

  /**
   * Starts the generation process with the files in {@code argv} or pops up a window to choose a
   * file, when {@code argv} doesn't have any file entries.
   *
   * @param argv the commandline.
   */
  public static void main(String[] argv) {
    Options options = new Options();
    Out out = new Out(options);
    try {
      generate(argv, options, out);
    } catch (GeneratorException e) {
      if (e.isUnExpected()) {
        out.error(
            "Unexpected exception encountered. This indicates a bug in JFlex."
                + Out.NL
                + "Please consider filing an issue at http://github.com/jflex-de/jflex/issues/new"
                + Out.NL);
        Throwable cause = e.getCause();
        if (cause != null) {
          String msg = cause.getLocalizedMessage();
          if (msg != null) out.error(msg);
          cause.printStackTrace();
        }
      } else {
        out.statistics();
      }
      System.exit(1);
    } catch (SilentExit e) {
      System.exit(e.exitCode());
    }
  }

  // Only CLI, not meant for instantiation.
  private Main() {}

  /**
   * Print system information (e.g. in case of unexpected exceptions)
   *
   * @param out where to print to
   */
  public static void printSystemInfo(Out out) {
    out.err("Java version:     " + System.getProperty("java.version"));
    out.err("Runtime name:     " + System.getProperty("java.runtime.name"));
    out.err("Vendor:           " + System.getProperty("java.vendor"));
    out.err("VM version:       " + System.getProperty("java.vm.version"));
    out.err("VM vendor:        " + System.getProperty("java.vm.vendor"));
    out.err("VM name:          " + System.getProperty("java.vm.name"));
    out.err("VM info:          " + System.getProperty("java.vm.info"));
    out.err("OS name:          " + System.getProperty("os.name"));
    out.err("OS arch:          " + System.getProperty("os.arch"));
    out.err("OS version:       " + System.getProperty("os.version"));
    out.err("Encoding:         " + System.getProperty("file.encoding"));
    out.err("Unicode versions: " + UnicodeProperties.UNICODE_VERSIONS);
    out.err("JFlex version:    " + Build.VERSION);
  }
}
