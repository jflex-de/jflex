/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

import java.io.File;
import java.util.Objects;
import jflex.core.OptionUtils;
import jflex.exceptions.GeneratorException;
import jflex.generator.LexGenerator;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;

/**
 * Low priority thread for code generation (low priority that gui has time for screen updates)
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class GeneratorThread extends Thread {

  /** there must be at most one instance of this Thread running */
  private static volatile boolean running = false;

  /** input file setting from GUI */
  String inputFile;

  /** output directory */
  String outputDir;

  /** main UI component, likes to be notified when generator finishes */
  final MainFrame parent;

  /**
   * Create a new GeneratorThread, but do not run it yet.
   *
   * @param parent the frame, main UI component
   * @param inputFile input file from UI settings
   * @param outputDir output directory from UI settings
   */
  public GeneratorThread(MainFrame parent, String inputFile, String outputDir) {
    this.parent = parent;
    this.inputFile = inputFile;
    this.outputDir = outputDir;
  }

  /** Runs the generator thread. Only one instance of it can run at any time. */
  @Override
  public void run() {
    if (running) {
      Out.error(ErrorMessages.ALREADY_RUNNING);
      parent.generationFinished(false);
    } else {
      running = true;
      setPriority(MIN_PRIORITY);
      try {
        if (!Objects.equals(outputDir, "")) {
          OptionUtils.setDir(outputDir);
        }
        new LexGenerator(new File(inputFile)).generate();
        Out.statistics();
        parent.generationFinished(true);
      } catch (GeneratorException e) {
        Out.statistics();
        parent.generationFinished(false);
      } finally {
        running = false;
      }
    }
  }
}
