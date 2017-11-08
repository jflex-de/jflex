package de.jflex.plugin.cup;

import com.google.common.annotations.VisibleForTesting;
import java_cup.Main;

/** Wrapper around the dirty CUP API. */
class CliCupInvoker {
  /**
   * Invokes CUP.
   *
   * @param javaPackage Specify that the parser and sym classes are to be placed in the named
   *     package. By default, no package specification is put in the generated code (hence the
   *     classes default to the special "unnamed" package).
   * @param parserClassName Output parser and action code into a file (and class) with the given
   *     name instead of the default of "parser".
   * @param symClassName Output the symbol constant code into a class with the given name instead of
   *     the default of "sym".
   * @param symbolInterface Output the symbol constant code as an interface rather than as a class.
   * @param cupFileName input specification.
   */
  void invoke(
      String javaPackage,
      String parserClassName,
      String symClassName,
      boolean symbolInterface,
      String cupFileName)
      throws Exception {
    // Seriously? cup doesn't have a better API than calling main like on cli!
    String[] args =
        buildArgv(javaPackage, parserClassName, symClassName, symbolInterface, cupFileName);
    Main.main(args);
  }

  @VisibleForTesting
  static String[] buildArgv(
      String javaPackage,
      String parserClassName,
      String symClassName,
      boolean symbolInterface,
      String cupFileName) {
    return new String[] {
      "-package",
      javaPackage,
      "-parser",
      parserClassName,
      "-symbols",
      symClassName,
      symbolInterface ? "-interface" : "",
      // inputFile
      cupFileName
    };
  }
}
