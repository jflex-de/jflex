/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 * This is the main class of JFlex controlling the scanner generation process. It is responsible for
 * parsing the commandline, getting input files, starting up the GUI if necessary, etc.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0-SNAPSHOT
 */
public class Main {

  /** JFlex version */
  public static final String version = "1.7.0-SNAPSHOT"; // $NON-NLS-1$

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
    FileReader inputReader = null;

    totalTime.start();

    try {
      Out.println(ErrorMessages.READING, inputFile.toString());
      inputReader = new FileReader(inputFile);
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

  private static List<File> handleArgs(CommandLine args) throws SilentExit {
    if (args.hasOption("outdir")) {
      Options.setDir(new File(args.getOptionValue("d")));
    }
    if (args.hasOption("skel")) {
      Options.setSkeleton(new File(args.getOptionValue("skel")));
    }
    if (args.hasOption("jflex")) {
      Options.jlex = true;
    }
    if (args.hasOption("verbose")) {
      Options.verbose = true;
      Options.progress = true;
      Options.unused_warning = true;
    }
    if (args.hasOption("quiet")) {
      Options.verbose = false;
      Options.progress = false;
      Options.unused_warning = false;
    }
    if (args.hasOption("warn-unused")) {
      Options.unused_warning = true;
    }
    if (args.hasOption("no-warn-unused")) {
      Options.unused_warning = false;
    }
    if (args.hasOption("dump")) {
      Options.dump = true;
    }

    if (args.hasOption("time")) {
      Options.time = true;
    }
    if (args.hasOption("version")) {
      Out.println(ErrorMessages.THIS_IS_JFLEX, version);
      throw new SilentExit(0);
    }
    if (args.hasOption("dot")) {
      Options.dot = true;
    }
    if (args.hasOption("info")) {
      Out.printSystemInfo();
      throw new SilentExit(0);
    }
    if (args.hasOption("nomin")) {
      Options.no_minimize = true;
    }
    if (args.hasOption("nobak")) {
      Options.no_backup = true;
    }
    if (args.hasOption("legacy-dot")) {
      Options.legacy_dot = true;
    }
    if (args.hasOption("uniprops")) {
      String unicodeVersion = args.getOptionValue("uniprops");
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
    List<File> files = new ArrayList<>(args.getArgs().length);
    for (String filename : args.getArgs()) {
      File f = new File(filename);
      if (f.isFile() && f.canRead()) {
        files.add(f);
      } else {
        Out.error("Sorry, couldn't open \"" + f + "\""); // $NON-NLS-2$
        throw new GeneratorException();
      }
    }

    return ImmutableList.copyOf(files);
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

  private static void printUsage(org.apache.commons.cli.Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("jflex [options] [input files…]", options);
  }

  private static org.apache.commons.cli.Options createCliOptions() {
    return new org.apache.commons.cli.Options()
        .addOption("d", "outdir", true, "write generated file to <directory>")
        .addOption("skel", "skeleton", true, "use external skeleton <file>")
        .addOption("jflex", "jlex", false, "strict JLex compatibility")
        .addOption(
            "legacydot",
            "legacy-dot",
            false,
            "dot (.) metachar matches [^\\n] instead of [^\\n\\r\\u000B\\u000C\\u0085\\u2028\\u2029]")
        .addOption("nomin", "no-min", false, "skip minimization step")
        .addOption("nobak", "no-backup", false, "don't create backup files")
        .addOption("dump", "dump", false, "display transition tables")
        .addOption(
            "dot", "dot", false, "write graphviz .dot files for the generated automata (alpha)")
        .addOption("v", "verbose", false, "display generation progress messages (default)")
        .addOption("q", "quiet", false, "display errors only")
        .addOption("", "no-warn-unused", false, "TODO")
        .addOption("", "warn-unused", false, "TODO")
        .addOption("time", "time", false, "display generation time statistics")
        .addOption("version", "version", false, "print the version number of this copy of jflex")
        .addOption("info", "info", false, "print system + JDK information")
        .addOption(
            "uniprops",
            "unicode-properties",
            true,
            "print all supported properties for Unicode version <ver>")
        .addOption("h", "help", false, "print the help message");
  }

  /**
   * Starts the generation process with the files in <code>argv</code> or pops up a window to choose
   * a file, when <code>argv</code> doesn't have any file entries.
   *
   * @param argv the commandline argument values.
   */
  public static void main(String argv[]) throws Exception {
    try {
      execute(argv);
    } catch (SilentExit e) {
      if (e.getCause() != null) {
        System.err.println(e.getCause().getLocalizedMessage());
      }
      System.exit(e.exitCode());
    }
  }

  private static void execute(String[] argv) throws SilentExit, IOException {
    org.apache.commons.cli.Options options = createCliOptions();
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine args = parser.parse(options, argv);
      if (args.hasOption("help")) {
        printUsage(options);
        throw new SilentExit();
      }
      if (args.hasOption("info")) {
        Out.printSystemInfo();
        // TODO(regisd) I see no reason to exit, but this is legacy behaviour.
        throw new SilentExit();
      }
      if (args.hasOption("info")) {
        // TODO(regisd) I see no reason to exit, but this is legacy behaviour.
        throw new SilentExit(ErrorMessages.get(ErrorMessages.THIS_IS_JFLEX, version));
      }
      List<File> inputs = handleArgs(args);
      if (inputs.isEmpty()) {
        // No file was provided. Start GUI.
        // TODO(regisd): new MainFrame(generatorOptions.buildUpon());
      } else {
        for (File file : inputs) {
          generate(file);
        }
      }
    } catch (ParseException e) {
      printUsage(options);
      throw new SilentExit(e);
    }
  }
}
