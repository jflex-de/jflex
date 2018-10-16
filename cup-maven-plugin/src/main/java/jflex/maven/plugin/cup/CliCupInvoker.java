package jflex.maven.plugin.cup;

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.io.File;
import java.util.ArrayList;
import java_cup.Main;
import org.apache.maven.plugin.logging.Log;

/** Wrapper around the dirty CUP API. */
class CliCupInvoker {

  private final Log log;

  CliCupInvoker(Log log) {
    this.log = log;
  };

  /**
   * Invokes CUP.
   *
   * @param javaPackage Specify that the parser and sym classes are to be placed in the named
   *     package. By default, no package specification is put in the generated code (hence the
   *     classes default to the special "unnamed" package).
   * @param outputDirectory Directory in which to output the generated Java Parser.
   * @param parserClassName Output parser and action code into a file (and class) with the given
   *     name instead of the default of "parser".
   * @param symClassName Output the symbol constant code into a class with the given name instead of
   *     the default of "sym".
   * @param symbolInterface Output the symbol constant code as an interface rather than as a class.
   * @param cupFileName input specification.
   */
  void invoke(
      String javaPackage,
      File outputDirectory,
      String parserClassName,
      String symClassName,
      boolean symbolInterface,
      String cupFileName)
      throws Exception {
    // CUP can only be invoked with main() method with CLI args.
    String[] args =
        buildArgv(
            javaPackage,
            outputDirectory,
            parserClassName,
            symClassName,
            symbolInterface,
            cupFileName);
    log.debug("cup " + Joiner.on(' ').join(args));
    Main.main(args);
  }

  @VisibleForTesting
  static String[] buildArgv(
      String javaPackage,
      File outputDirectory,
      String parserClassName,
      String symClassName,
      boolean symbolInterface,
      String cupFileName) {
    // It's a builder but it's easier to read the IllegalArgumentException if each arg is added on
    // its own line.
    ArgBuilder args = new ArgBuilder();
    if (!Strings.isNullOrEmpty(javaPackage)) {
      args.addOption("package", javaPackage);
    }
    args.addOption("destdir", outputDirectory.getAbsolutePath());
    args.addOption("parser", parserClassName);
    args.addOption("symbols", symClassName);
    if (symbolInterface) {
      args.addOption("interface");
    }
    // inputFile
    args.addArg(cupFileName);
    return args.buildArray();
  }

  private static class ArgBuilder {

    private final ArrayList<String> args = new ArrayList<>();

    ArgBuilder addArg(String argument) {
      args.add(argument);
      return this;
    }

    ArgBuilder addOption(String optionName) {
      checkArgument(!Strings.isNullOrEmpty(optionName), "Option name cannot be empty");
      args.add("-" + optionName);
      return this;
    }

    ArgBuilder addOption(String optionName, String optionValue) {
      checkArgument(!Strings.isNullOrEmpty(optionName), "Option name cannot be empty");
      checkArgument(
          !Strings.isNullOrEmpty(optionValue),
          String.format("Value for option -%s should not be empty", optionName));
      args.add("-" + optionName);
      args.add(optionValue);
      return this;
    }

    String[] buildArray() {
      return args.toArray(new String[args.size()]);
    }
  }
}
