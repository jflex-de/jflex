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
import java.io.IOException;
import java.lang.reflect.Field;
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
import org.apache.commons.cli.Options;
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

  private static void printUnicodeProperties(String unicodeVersion, Out out) {
    try {
      printUnicodePropertyValuesAndAliases(unicodeVersion, out);
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new GeneratorException(ErrorMessages.UNSUPPORTED_UNICODE_VERSION_SUPPORTED_ARE, UnicodeProperties.UNICODE_VERSIONS);
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

  private static void printUsage(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("jflex [options] [input files…]", options);
  }

  private static Options createCliOptions() {
    return new Options()
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
    Options options = createCliOptions();
    CommandLineParser parser = new DefaultParser();
    try {
      CommandLine args = parser.parse(options, argv);
      if (args.hasOption("help")) {
        printUsage(options);
        throw new SilentExit();
      }
      if (args.hasOption("info")) {
        // TODO(regisd): log.printSystemInfo();
        // TODO(regisd) I see no reason to exit, but this is legacy behaviour.
        throw new SilentExit();
      }
      if (args.hasOption("info")) {
        // TODO(regisd) I see no reason to exit, but this is legacy behaviour.
        throw new SilentExit(ErrorMessages.get(ErrorMessages.THIS_IS_JFLEX, version));
      }
      GeneratorOptions generatorOptions = createGeneratorOptions(args);
      Out out = new Out(generatorOptions);
      if (args.hasOption("uniprops")) {
        printUnicodeProperties(args.getOptionValue("uniprops"), out);
        throw new SilentExit();
      }
      String[] inputs = args.getArgs();
      System.out.println("JFlex options: " + generatorOptions);
      if (inputs.length == 0) {
        // No file was provided. Start GUI.
        // TODO(regisd): new MainFrame(generatorOptions.buildUpon());
      } else {
        for (String fileName : inputs) {
          LexGenerator lexGenerator = new LexGenerator(generatorOptions, out);
          lexGenerator.generateFromFile(new File(fileName));
        }
      }
    } catch (ParseException e) {
      printUsage(options);
      throw new SilentExit(e);
    }
  }

  private static GeneratorOptions createGeneratorOptions(CommandLine args) {
    GeneratorOptions.Builder opts = GeneratorOptions.newBuilder();
    if (args.hasOption("dot")) {
      opts.setGenerateDotFile(true);
    }
    if (args.hasOption("dot")) {
      opts.setLegacyDot(true);
    }
    if (args.hasOption("nobak")) {
      opts.setBackup(false);
    }
    if (args.hasOption("outdir")) {
      opts.setOutputDirectory(new File(args.getOptionValue("d")));
    }
    if (args.hasOption("skel")) {
      opts.setSkeleton(new File(args.getOptionValue("skel")));
    }
    if (args.hasOption("time")) {
      opts.setTiming(true);
    }
    if (args.hasOption("verbose")) {
      opts.setVerbose(true);
      opts.setShowProgress(true);
      opts.setUnusedWarnings(true);
    }
    if (args.hasOption("quiet")) {
      opts.setVerbose(false);
      opts.setShowProgress(false);
      opts.setUnusedWarnings(false);
    }
    if (args.hasOption("warn-unused")) {
      opts.setUnusedWarnings(true);
    }
    if (args.hasOption("no-min")) {
      opts.setMinimize(false);
    }
    if (args.hasOption("no-warn-unused")) {
      opts.setUnusedWarnings(false);
    }
    return opts.build();
  }
}
