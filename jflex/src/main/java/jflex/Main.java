/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.9.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import java.io.File;
import java.util.List;
import jflex.exceptions.SilentExit;
import jflex.gui.MainFrame;

/** This is the main class for the GUI of JFlex. */
public class Main {
  private Main() {}

  public static void main(String[] args) {
    try {
      List<File> inputFiles = Cli.parseOptions(args);
      if (inputFiles.isEmpty()) {
        new MainFrame();
      } else {
        Cli.main(args);
      }
    } catch (SilentExit e) {
      System.exit(e.exitCode());
    }
  }
}
