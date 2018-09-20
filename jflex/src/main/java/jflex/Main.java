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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import jflex.gui.MainFrame;
import jflex.unicode.UnicodeProperties;

/**
 * This is the main class of JFlex controlling the scanner generation process. It is responsible for
 * parsing the commandline, getting input files, starting up the GUI if necessary, etc.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class Main {

  /** JFlex version */
  public static final String version = "1.7.0"; // $NON-NLS-1$

  /**
   * Generates a scanner for the specified input file.
   *
   * @param inputFile a file containing a lexical specification to generate a scanner for.
   */
  public static void generate(File inputFile) {

    Out.resetCounters();

    Timer totalTime = new Timer();
    Timer time = new Timer();

    LexScan scanner = null;
    LexParse parser = null;
    Reader inputReader = null;

    totalTime.start();

    try {
      Out.println(ErrorMessages.READING, inputFile.toString());
      inputReader = new InputStreamReader(new FileInputStream(inputFile), Options.encoding);
      scanner = new LexScan(inputReader);
      scanner.setFile(inputFile);
      parser = new LexParse(scanner);
    } catch (FileNotFoundException e) {
      Out.error(ErrorMessages.CANNOT_OPEN, inputFile.toString());
      throw new GeneratorException();
    }

    try {
      NFA nfa = (NFA) parser.parse().value;

      Out.checkErrors();

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.NFA_IS) + Out.NL + nfa + Out.NL);

      if (Options.dot) nfa.writeDot(Emitter.normalize("nfa.dot", null)); // $NON-NLS-1$

      Out.println(ErrorMessages.NFA_STATES, nfa.numStates);

      time.start();
      DFA dfa = nfa.getDFA();
      time.stop();
      Out.time(ErrorMessages.DFA_TOOK, time);

      dfa.checkActions(scanner, parser);

      nfa = null;

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.DFA_IS) + Out.NL + dfa + Out.NL);

      if (Options.dot) dfa.writeDot(Emitter.normalize("dfa-big.dot", null)); // $NON-NLS-1$

      Out.checkErrors();

      time.start();
      dfa.minimize();
      time.stop();

      Out.time(ErrorMessages.MIN_TOOK, time);

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.MIN_DFA_IS) + Out.NL + dfa);

      if (Options.dot) dfa.writeDot(Emitter.normalize("dfa-min.dot", null)); // $NON-NLS-1$

      time.start();

      Emitter e = new Emitter(inputFile, parser, dfa);
      e.emit();

      time.stop();

      Out.time(ErrorMessages.WRITE_TOOK, time);

      totalTime.stop();

      Out.time(ErrorMessages.TOTAL_TIME, totalTime);
    } catch (ScannerException e) {
      Out.error(e.file, e.message, e.line, e.column);
      throw new GeneratorException();
    } catch (MacroException e) {
      Out.error(e.getMessage());
      throw new GeneratorException();
    } catch (IOException e) {
      Out.error(ErrorMessages.IO_ERROR, e.toString());
      throw new GeneratorException();
    } catch (OutOfMemoryError e) {
      Out.error(ErrorMessages.OUT_OF_MEMORY);
      throw new GeneratorException();
    } catch (GeneratorException e) {
      throw new GeneratorException();
    } catch (Exception e) {
      e.printStackTrace();
      throw new GeneratorException();
    }
  }

  /**
   * parseOptions.
   *
   * @param argv an array of {@link java.lang.String} objects.
   * @return a {@link java.util.List} object.
   * @throws jflex.SilentExit if any.
   */
  public static List<File> parseOptions(String argv[]) throws SilentExit {
    List<File> files = new ArrayList<>();

    for (int i = 0; i < argv.length; i++) {

      if (argv[i].equals("-d") || argv[i].equals("--outdir")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_DIRECTORY);
          throw new GeneratorException();
        }
        Options.setDir(argv[i]);
        continue;
      }

      if (argv[i].equals("--skel") || argv[i].equals("-skel")) { // $NON-NLS-1$ //$NON-NLS-2$
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_SKEL_FILE);
          throw new GeneratorException();
        }

        Options.setSkeleton(new File(argv[i]));
        continue;
      }

      if (argv[i].equals("--encoding")) {
        if (++i >= argv.length) {
          Out.error(ErrorMessages.NO_ENCODING);
          throw new GeneratorException();
        }

        Options.setEncoding(argv[i]);
        continue;
      }

      if (argv[i].equals("-jlex") || argv[i].equals("--jlex")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.jlex = true;
        continue;
      }

      if (argv[i].equals("-v")
          || argv[i].equals("--verbose")
          || argv[i].equals("-verbose")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Options.verbose = true;
        Options.progress = true;
        Options.unused_warning = true;
        continue;
      }

      if (argv[i].equals("-q")
          || argv[i].equals("--quiet")
          || argv[i].equals("-quiet")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        Options.verbose = false;
        Options.progress = false;
        Options.unused_warning = false;
        continue;
      }

      if (argv[i].equals("--warn-unused")) { // $NON-NLS-1$
        Options.unused_warning = true;
        continue;
      }

      if (argv[i].equals("--no-warn-unused")) { // $NON-NLS-1$
        Options.unused_warning = false;
        continue;
      }

      if (argv[i].equals("--dump") || argv[i].equals("-dump")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.dump = true;
        continue;
      }

      if (argv[i].equals("--time") || argv[i].equals("-time")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.time = true;
        continue;
      }

      if (argv[i].equals("--version") || argv[i].equals("-version")) { // $NON-NLS-1$ //$NON-NLS-2$
        Out.println(ErrorMessages.THIS_IS_JFLEX, version);
        throw new SilentExit(0);
      }

      if (argv[i].equals("--dot") || argv[i].equals("-dot")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.dot = true;
        continue;
      }

      if (argv[i].equals("--help")
          || argv[i].equals("-h")
          || argv[i].equals("/h")) { // $NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        printUsage();
        throw new SilentExit(0);
      }

      if (argv[i].equals("--info") || argv[i].equals("-info")) { // $NON-NLS-1$ //$NON-NLS-2$
        Out.printSystemInfo();
        throw new SilentExit(0);
      }

      if (argv[i].equals("--nomin") || argv[i].equals("-nomin")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.no_minimize = true;
        continue;
      }

      if (argv[i].equals("--pack") || argv[i].equals("-pack")) { // $NON-NLS-1$ //$NON-NLS-2$
        /* no-op - pack is the only generation method */
        continue;
      }

      if (argv[i].equals("--nobak") || argv[i].equals("-nobak")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.no_backup = true;
        continue;
      }

      if (argv[i].equals("--legacydot")
          || argv[i].equals("-legacydot")) { // $NON-NLS-1$ //$NON-NLS-2$
        Options.legacy_dot = true;
        continue;
      }

      if (argv[i].equals("--uniprops")
          || argv[i].equals("-uniprops")) { // $NON-NLS-1$ //$NON-NLS-2$
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
          throw new GeneratorException();
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
  public static void printUsage() {
    Out.println(""); // $NON-NLS-1$
    Out.println("Usage: jflex <options> <input-files>");
    Out.println("");
    Out.println("Where <options> can be one or more of");
    Out.println("-d <directory>     write generated file to <directory>");
    Out.println("--skel <file>      use external skeleton <file>");
    Out.println("--encoding <name>  use <name> as input/output encoding");
    Out.println("--pack             set default code generation method (default)");
    Out.println("--jlex             strict JLex compatibility");
    Out.println("--legacydot        dot (.) metachar matches [^\\n] instead of");
    Out.println("                   [^\\n\\r\\u000B\\u000C\\u0085\\u2028\\u2029]");
    Out.println("--nomin            skip minimization step");
    Out.println("--nobak            don't create backup files");
    Out.println("--dump             display transition tables");
    Out.println("--dot              write graphviz .dot files for the generated automata (alpha)");
    Out.println("--verbose");
    Out.println("-v                 display generation progress messages (default)");
    Out.println("--quiet");
    Out.println("-q                 display errors only");
    Out.println("--time             display generation time statistics");
    Out.println("--version          print the version number of this copy of jflex");
    Out.println("--info             print system + JDK information");
    Out.println("--uniprops <ver>   print all supported properties for Unicode version <ver>");
    Out.println("--help");
    Out.println("-h                 print this message");
    Out.println("");
    Out.println(ErrorMessages.THIS_IS_JFLEX, version);
    Out.println("Have a nice day!");
  }

  /**
   * generate.
   *
   * @param argv an array of {@link java.lang.String} objects.
   * @throws jflex.SilentExit if any.
   */
  public static void generate(String argv[]) throws SilentExit {
    List<File> files = parseOptions(argv);

    if (files.size() > 0) {
      for (File file : files) generate(file);
    } else {
      new MainFrame();
    }
  }

  /**
   * Starts the generation process with the files in <code>argv</code> or pops up a window to choose
   * a file, when <code>argv</code> doesn't have any file entries.
   *
   * @param argv the commandline.
   */
  public static void main(String argv[]) {
    try {
      generate(argv);
    } catch (GeneratorException e) {
      Out.statistics();
      System.exit(1);
    } catch (SilentExit e) {
      System.exit(e.exitCode());
    }
  }
}
