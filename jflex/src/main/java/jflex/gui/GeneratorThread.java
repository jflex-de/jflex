/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.1-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

import java.io.File;
import jflex.ErrorMessages;
import jflex.GeneratorException;
import jflex.LexGenerator;
import jflex.Options;
import jflex.Out;

/**
 * Low priority thread for code generation (low priority that gui has time for screen updates)
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.1-SNAPSHOT
 */
public class GeneratorThread extends Thread {

  /** there must be at most one instance of this Thread running */
  private static volatile boolean running = false;

  private final Options generatorOptions;

  private Out log;

  /** input file setting from GUI */
  String inputFile;

  /** main UI component, likes to be notified when generator finishes */
  MainFrame parent;

  /**
   * Create a new GeneratorThread, but do not run it yet.
   *
   * @param parent the frame, main UI component
   * @param inputFile input file from UI settings
   */
  public GeneratorThread(MainFrame parent, String inputFile, Options generatorOptions) {
    this.parent = parent;
    this.inputFile = inputFile;
    this.generatorOptions = generatorOptions;
  }

  /** Runs the generator thread. Only one instance of it can run at any time. */
  public void run() {
    if (running) {
      log.error(ErrorMessages.ALREADY_RUNNING);
      parent.generationFinished(false);
    } else {
      running = true;
      setPriority(MIN_PRIORITY);
      try {
        LexGenerator lexGenerator = new LexGenerator(generatorOptions);
        lexGenerator.generateFromFile(new File(inputFile));
        log.printStatistics();
        parent.generationFinished(true);
      } catch (GeneratorException e) {
        log.printStatistics();
        parent.generationFinished(false);
      } finally {
        running = false;
      }
    }
  }
}
