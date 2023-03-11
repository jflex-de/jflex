/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex;

import java.io.File;
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
import jflex.core.OptionUtils;
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
 * @version JFlex 1.9.1
 */
public class Main {

  /**
   * parseOptions.
   *
   * @param argv an array of {@link java.lang.String} objects.
   * @return a {@link java.util.List} object.
   * @throws SilentExit if any.
   */
  private static List<File> parseOptions(String[] argv) throws SilentExit {
    List<File> files = new ArrayList<>();

    for (int i = 0; i < argv.length; i++) {

      if (Objects.equals(argv[i], "-d")
          || Objects.equals(argv[i], "--outdir")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_DIRECTORY);
          throw new GeneratorException();
        }
        OptionUtils.setDir(argv[i]);
        continue;
      }

      if (Objects.equals(argv[i], "--skel")
          || Objects.equals(argv[i], "-skel")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_SKEL_FILE);
          throw new GeneratorException();
        }

        OptionUtils.setSkeleton(new File(argv[i]));
        continue;
      }

      if (Objects.equals(argv[i], "--encoding")) {
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_ENCODING);
          throw new GeneratorException();
        }

        OptionUtils.setEncoding(argv[i]);
        continue;
      }

      if (Objects.equals(argv[i], "-jlex")
          || Objects.equals(argv[i], "--jlex")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.jlex = true;
        continue;
      }

      if (Objects.equals(argv[i], "-v")
          || Objects.equals(argv[i], "--verbose")
          || Objects.equals(argv[i], "-verbose")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Options.verbose = true;
        Options.progress = true;
        Options.enable(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "-q")
          || Objects.equals(argv[i], "--quiet")
          || Objects.equals(argv[i], "-quiet")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Options.verbose = false;
        Options.progress = false;
        Options.suppress(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "--warn-all")) { // $NON-NLS-1$
        OptionUtils.enableAllWarnings();
        continue;
      }

      if (Objects.equals(argv[i], "--no-warn-all")) { // $NON-NLS-1$
        OptionUtils.suppressAllWarnings();
        continue;
      }

      if (Objects.equals(argv[i], "--warn-unused")) { // $NON-NLS-1$
        Options.enable(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (Objects.equals(argv[i], "--no-warn-unused")) { // $NON-NLS-1$
        Options.suppress(ErrorMessages.MACRO_UNUSED);
        continue;
      }

      if (argv[i].startsWith("--warn-")) { // $NON-NLS-1$
        OptionUtils.enableWarning(argv[i].substring(7));
        continue;
      }

      if (argv[i].startsWith("--no-warn-")) { // $NON-NLS-1$
        OptionUtils.suppressWarning(argv[i].substring(10));
        continue;
      }

      if (Objects.equals(argv[i], "--dump")
          || Objects.equals(argv[i], "-dump")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.dump = true;
        continue;
      }

      if (Objects.equals(argv[i], "--time")
          || Objects.equals(argv[i], "-time")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.time = true;
        continue;
      }

      if (Objects.equals(argv[i], "--version")
          || Objects.equals(argv[i], "-version")) { // $NON-NLS-1$ //$NON-NLS-2$
        Out.println(ErrorMessages.THIS_IS_JFLEX, Build.VERSION);
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--dot")
          || Objects.equals(argv[i], "-dot")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.dot = true;
        continue;
      }

      if (Objects.equals(argv[i], "--help")
          || Objects.equals(argv[i], "-h")
          || Objects.equals(argv[i], "/h")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        printUsage();
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--info")
          || Objects.equals(argv[i], "-info")) { // $NON-NLS-1$ //$NON-NLS-2$
        printSystemInfo();
        throw new SilentExit(0);
      }

      if (Objects.equals(argv[i], "--nomin")
          || Objects.equals(argv[i], "-nomin")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.no_minimize = true;
        continue;
      }

      if (Objects.equals(argv[i], "--pack")
          || Objects.equals(argv[i], "-pack")) { // $NON-NLS-1$ //$NON-NLS-2$
        /* no-op - pack is the only generation method */
        continue;
      }

      if (Objects.equals(argv[i], "--nobak")
          || Objects.equals(argv[i], "-nobak")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.no_backup = true;
        continue;
      }

      if (Objects.equals(argv[i], "--legacydot")
          || Objects.equals(argv[i], "-legacydot")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.legacy_dot = true;
        continue;
      }

      if (Objects.equals(argv[i], "--uniprops")
          || Objects.equals(argv[i], "-uniprops")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          Out.error(
              ErrorMessages.PROPS_ARG_REQUIRES_UNICODE_VERSION, UnicodeProperties.UNICODE_VERSIONS);
          throw new GeneratorException();
        }
        String unicodeVersion = argv[i];
        try {
          printUnicodePropertyValuesAndAliases(unicodeVersion);
        } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
          Out.error(
              ErrorMessages.UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE,
              UnicodeProperties.UNICODE_VERSIONS);
          throw new GeneratorException(e);
        }
        throw new SilentExit();
      }

      if (argv[i].startsWith("-")) { // $NON-NLS-1$
        Out.error(ErrorMessages.UNKNOWN_COMMANDLINE, argv[i]);
        printUsage();
        throw new SilentExit();
      }

      // if argv[i] is not an option, try to read it as file
      File f = new File(argv[i]);
      if (f.isFile() && f.canRead()) files.add(f);
      else {
        Out.error("Sorry, couldn't open \"" + f + "\""); // $NON-NLS-2$
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
   * @throws UnicodeProperties.UnsupportedUnicodeVersionException if unicodeVersion is not supported
   */
  private static void printUnicodePropertyValuesAndAliases(String unicodeVersion)
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
      Out.print(value);
      if (aliases.size() > 0) {
        for (String alias : aliases) {
          Out.print(", " + alias);
        }
      }
      Out.println("");
    }
  }

  /** Prints the cli usage on stdout. */
  private static void printUsage() {
    Out.println(""); // $NON-NLS-1$
    Out.println("Usage: jflex <options> <input-files>");
    Out.println("");
    Out.println("Where <options> can be one or more of");
    Out.println("-d <directory>       write generated file to <directory>");
    Out.println("--skel <file>        use external skeleton <file>");
    Out.println("--encoding <name>    use <name> as input/output encoding");
    Out.println("--pack               set default code generation method (default)");
    Out.println("--jlex               strict JLex compatibility");
    Out.println("--legacydot          dot (.) metachar matches [^\\n] instead of");
    Out.println("                       [^\\n\\r\\u000B\\u000C\\u0085\\u2028\\u2029]");
    Out.println("--nomin              skip minimization step");
    Out.println("--nobak              don't create backup files");
    Out.println("--dump               display transition tables");
    Out.println(
        "--dot                write graphviz .dot files for the generated automata (alpha)");
    Out.println("--verbose");
    Out.println("-v                   display generation progress messages (default)");
    Out.println("--quiet");
    Out.println("-q                   display errors only");
    Out.println("--warn-<warning>     switch on warning type <warning>, where <warning> is one of");
    Out.println("                       unused, never-match, empty-match, cupsym-after-cup,");
    Out.println("                       unicode-too-long, all.");
    Out.println("--no-warn-<warning>  suppress warnings of type <warning>");
    Out.println("--time               display generation time statistics");
    Out.println("--version            print the version number of this copy of jflex");
    Out.println("--info               print system + JDK information");
    Out.println("--uniprops <ver>     print all supported properties for Unicode version <ver>");
    Out.println("--help");
    Out.println("-h                   print this message");
    Out.println("");
    Out.println(ErrorMessages.THIS_IS_JFLEX, Build.VERSION);
    Out.println("Have a nice day!");
  }

  /**
   * generate.
   *
   * @param argv an array of {@link java.lang.String} objects.
   * @throws SilentExit if any.
   */
  public static void generate(String[] argv) throws SilentExit {
    List<File> files = parseOptions(argv);

    if (files.size() > 0) {
      for (File file : files) {
        new LexGenerator(file).generate();
      }
    } else {
      new MainFrame();
    }
  }

  /**
   * Starts the generation process with the files in {@code argv} or pops up a window to choose a
   * file, when {@code argv} doesn't have any file entries.
   *
   * @param argv the commandline.
   */
  public static void main(String[] argv) {
    OptionUtils.setDefaultOptions();
    try {
      generate(argv);
    } catch (GeneratorException e) {
      if (e.isUnExpected()) {
        Out.error(
            "Unexpected exception encountered. This indicates a bug in JFlex."
                + Out.NL
                + "Please consider filing an issue at http://github.com/jflex-de/jflex/issues/new"
                + Out.NL);
        Throwable cause = e.getCause();
        if (cause != null) {
          String msg = cause.getLocalizedMessage();
          if (msg != null) Out.error(msg);
          cause.printStackTrace();
        }
      } else {
        Out.statistics();
      }
      System.exit(1);
    } catch (SilentExit e) {
      System.exit(e.exitCode());
    }
  }

  // Only CLI, not meant for instantiation.
  private Main() {}

  /** Print system information (e.g. in case of unexpected exceptions) */
  public static void printSystemInfo() {
    Out.err("Java version:     " + System.getProperty("java.version"));
    Out.err("Runtime name:     " + System.getProperty("java.runtime.name"));
    Out.err("Vendor:           " + System.getProperty("java.vendor"));
    Out.err("VM version:       " + System.getProperty("java.vm.version"));
    Out.err("VM vendor:        " + System.getProperty("java.vm.vendor"));
    Out.err("VM name:          " + System.getProperty("java.vm.name"));
    Out.err("VM info:          " + System.getProperty("java.vm.info"));
    Out.err("OS name:          " + System.getProperty("os.name"));
    Out.err("OS arch:          " + System.getProperty("os.arch"));
    Out.err("OS version:       " + System.getProperty("os.version"));
    Out.err("Encoding:         " + System.getProperty("file.encoding"));
    Out.err("Unicode versions: " + UnicodeProperties.UNICODE_VERSIONS);
    Out.err("JFlex version:    " + Build.VERSION);
  }
}
