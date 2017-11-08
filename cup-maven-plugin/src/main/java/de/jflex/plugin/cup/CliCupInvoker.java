package de.jflex.plugin.cup;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.io.File;
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
    // Seriously? cup doesn't have a better API than calling main like on cli!
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
    return new String[] {
      "-package",
      Preconditions.checkNotNull(javaPackage),
      "-destdir",
      Preconditions.checkNotNull(outputDirectory.getAbsolutePath()),
      "-parser",
      Preconditions.checkNotNull(parserClassName),
      "-symbols",
      Preconditions.checkNotNull(symClassName),
      symbolInterface ? "-interface" : "",
      // inputFile
      cupFileName
    };
  }
}
