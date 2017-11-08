package de.jflex.plugin.cup;

import java_cup.Main;

/** Wrapper around the dirty CUP API. */
public class CliCupInvoker implements CupInvoker {

  @Override
  public void invoke(
      String javaPackage,
      String parserClassName,
      String symClassName,
      boolean symbolInterface,
      String cupFileName)
      throws Exception {
    // Seriously? cup doesn't have a better API than calling main like on cli!
    String[] args = {
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
    Main.main(args);
  }
}
