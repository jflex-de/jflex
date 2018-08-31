/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jflex.unicode.UnicodeProperties;

/**
 * This is the main class of JFlex controlling the scanner generation process. It is responsible for
 * parsing the commandline, getting input files, starting up the GUI if necessary, etc.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0-SNAPSHOT
 */
public class Main {

  /**
   * parseOptions.
   *
   * @param argv an array of cli argument values.
   * @param options A {@link GeneratorOptions} builder.
   * @return a {@link java.util.List} object.
   * @throws SilentExit if any.
   */
  private static List<File> parseOptions(String argv[], GeneratorOptions.Builder options)
      throws SilentExit {
    List<File> files = new ArrayList<>();

    for (int i = 0; i < argv.length; i++) {

      if (argv[i].equals("-d") || argv[i].equals("--outdir")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          throw new GeneratorException(ErrorMessages.NO_DIRECTORY);
        }
        options.setOutputDirectory(new File(argv[i]));
        continue;
      }

      if (argv[i].equals("--skel") || argv[i].equals("-skel")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          throw new GeneratorException(ErrorMessages.NO_SKEL_FILE);
        }

        options.setSkeleton(new File(argv[i]));
        continue;
      }

      if (argv[i].equals("-jlex") || argv[i].equals("--jlex")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setStrictJlex(true);
        continue;
      }

      if (argv[i].equals("-v")
          || argv[i].equals("--verbose")
          || argv[i].equals("-verbose")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        options.setVerbose(true);
        options.setShowProgress(true);
        options.setUnusedWarnings(true);
        continue;
      }

      if (argv[i].equals("-q")
          || argv[i].equals("--quiet")
          || argv[i].equals("-quiet")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        options.setVerbose(false);
        options.setShowProgress(false);
        options.setUnusedWarnings(false);
        continue;
      }

      if (argv[i].equals("--warn-unused") || argv[i].equals("--no-warn-unused")) { // $NON-NLS-1$
        options.setUnusedWarnings(true);
        continue;
      }

      if (argv[i].equals("--dump") || argv[i].equals("-dump")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setDump(true);
        continue;
      }

      if (argv[i].equals("--time") || argv[i].equals("-time")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setTiming(true);
        continue;
      }

      if (argv[i].equals("--version") || argv[i].equals("-version")) { // $NON-NLS-1$ //$NON-NLS-2$
        System.out.println(ErrorMessages.get(ErrorMessages.THIS_IS_JFLEX, version));
        throw new SilentExit(0);
      }

      if (argv[i].equals("--dot") || argv[i].equals("-dot")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setGenerateDotFile(true);
        continue;
      }

      if (argv[i].equals("--help")
          || argv[i].equals("-h")
          || argv[i].equals("/h")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        printUsage();
        throw new SilentExit(0);
      }

      if (argv[i].equals("--info") || argv[i].equals("-info")) { // $NON-NLS-1$ //$NON-NLS-2$
        // TODO(regisd) Out.printSystemInfo();
        throw new SilentExit(0);
      }

      if (argv[i].equals("--nomin") || argv[i].equals("-nomin")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setMinimize(false);
        continue;
      }

      if (argv[i].equals("--pack") || argv[i].equals("-pack")) { // $NON-NLS-1$ //$NON-NLS-2$
        /* no-op - pack is the only generation method */
        continue;
      }

      if (argv[i].equals("--nobak") || argv[i].equals("-nobak")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setBackup(false);
        continue;
      }

      if (argv[i].equals("--legacydot")
          || argv[i].equals("-legacydot")) { // $NON-NLS-1$ //$NON-NLS-2$
        options.setLegacyDot(true);
        continue;
      }

      if (argv[i].equals("--uniprops")
          || argv[i].equals("-uniprops")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          throw new GeneratorException(
              ErrorMessages.PROPS_ARG_REQUIRES_UNICODE_VERSION, UnicodeProperties.UNICODE_VERSIONS);
        }
        String unicodeVersion = argv[i];
        printUnicodeProperties(unicodeVersion, new Out(options.build()));
        throw new SilentExit(); // TODO(regisd): Shouldn't it be exit 0?
      }

      if (argv[i].startsWith("-")) { // $NON-NLS-1$
        printUsage();
        throw new SilentExit(ErrorMessages.UNKNOWN_COMMANDLINE, argv[i]);
      }

      files.add(new File(argv[i]));
    }

    return files;
  }

  /** JFlex version */
  public static final String version = "1.7.0-SNAPSHOT"; // $NON-NLS-1$

  private static void printUnicodeProperties(String unicodeVersion, Out out) {
    try {
      printUnicodePropertyValuesAndAliases(unicodeVersion, out);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new GeneratorException(
          ErrorMessages.UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE,
          UnicodeProperties.UNICODE_VERSIONS);
    }
  }

  /**
   * Prints one Unicode property value per line, along with its aliases, if any, for the given
   * unicodeVersion.
   *
   * @param unicodeVersion The Unicode version to print property values and aliases for
   * @throws UnicodeProperties.UnsupportedUnicodeVersionException if unicodeVersion is not supported
   */
  private static void printUnicodePropertyValuesAndAliases(String unicodeVersion, Out log)
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
      throw new UnicodeProperties.UnsupportedUnicodeVersionException();
    }
    SortedMap<String, SortedSet<String>> propertyValuesToAliases = new TreeMap<>();
    for (String value : propertyValues) {
      propertyValuesToAliases.put(value, new TreeSet<String>());
    }
    for (int i = 0; i < propertyValueAliases.length; i += 2) {
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
      log.print(value);
      if (aliases.size() > 0) {
        for (String alias : aliases) {
          log.print(", " + alias);
        }
      }
      log.println("");
    }
  }

  /** Prints the cli usage on stdout. */
  public static void printUsage() {
    System.out.println(""); // $NON-NLS-1$
    System.out.println("Usage: jflex <options> <input-files>");
    System.out.println("");
    System.out.println("Where <options> can be one or more of");
    System.out.println("-d <directory>    write generated file to <directory>");
    System.out.println("--skel <file>     use external skeleton <file>");
    System.out.println("--pack            set default code generation method (default)");
    System.out.println("--jlex            strict JLex compatibility");
    System.out.println("--legacydot       dot (.) metachar matches [^\\n] instead of");
    System.out.println("                  [^\\n\\r\\u000B\\u000C\\u0085\\u2028\\u2029]");
    System.out.println("--nomin           skip minimization step");
    System.out.println("--nobak           don't create backup files");
    System.out.println("--dump            display transition tables");
    System.out.println(
        "--dot             write graphviz .dot files for the generated automata (alpha)");
    System.out.println("--verbose");
    System.out.println("-v                display generation progress messages (default)");
    System.out.println("--quiet");
    System.out.println("-q                display errors only");
    System.out.println("--time            display generation time printStatistics");
    System.out.println("--version         print the version number of this copy of jflex");
    System.out.println("--info            print system + JDK information");
    System.out.println(
        "--uniprops <ver>  print all supported properties for Unicode version <ver>");
    System.out.println("--help");
    System.out.println("-h                print this message");
    System.out.println("");
    System.out.println(ErrorMessages.get(ErrorMessages.THIS_IS_JFLEX, version));
    System.out.println("Have a nice day!");
  }

  private static void generate(String[] argv) throws SilentExit, GeneratorException {
    GeneratorOptions.Builder opts = GeneratorOptions.newBuilder();
    List<File> files = parseOptions(argv, opts);
    GeneratorOptions generatorOptions = opts.build();

    System.out.println("JFlex options: " + generatorOptions);

    if (files.isEmpty()) {
      // No file was provided. Start GUI.
      // TODO(regisd): new MainFrame(generatorOptions.buildUpon());
    } else {
      for (File file : files) {
        LexGenerator lexGenerator = new LexGenerator(generatorOptions);
        try {
          lexGenerator.generateFromFile(file);
        } catch (GeneratorException e) {
          lexGenerator.printStatistics();
          throw e;
        }
      }
    }
  }

  /**
   * Starts the generation process with the files in <code>argv</code> or pops up a window to choose
   * a file, when <code>argv</code> doesn't have any file entries.
   *
   * @param argv the commandline argument values.
   */
  public static void main(String argv[]) {
    try {
      generate(argv);
    } catch (SilentExit e) {
      if (e.getCause() != null) {
        System.err.println(e.getCause().getLocalizedMessage());
      }
      System.exit(e.exitCode());
    } catch (GeneratorException e) {
      // statistics printed on Out.
      // Even if we don't print the fulls tack, an error message can always help
      System.err.println(e.getMessage());
      System.exit(1);
    }
  }
}
