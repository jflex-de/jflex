/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.9.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.List;
import jflex.exceptions.SilentExit;
import jflex.gui.MainFrame;

/**
 * This is the main class for the GUI of JFlex.
 */
public class Gui {
private Gui()  {}

  public static void main(String[] args) {
    try {
      List<File> inputFiles = Main.parseOptions(args);
      Preconditions.checkArgument(inputFiles.isEmpty());
    } catch (SilentExit e) {
      System.exit(e.exitCode());
    }
    new MainFrame();
  }
}
