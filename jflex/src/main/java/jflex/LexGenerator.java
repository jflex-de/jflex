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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is the main class of JFlex controlling the scanner generation process. It is responsible for
 * parsing the commandline, getting input files, starting up the GUI if necessary, etc.
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0-SNAPSHOT
 */
public class LexGenerator {

  private final Options options;
  private final Out log;

  public LexGenerator(Options options) {
    this.options = options;
    this.log = new Out(this.options);
  }

  /**
   * Generates a scanner for the specified input file.
   *
   * @param inputFile a file containing a lexical specification to generateFromFile a scanner for.
   */
  public void generateFromFile(File inputFile) throws GeneratorException {
    // Verify that the file exists
    if (!inputFile.isFile() || !inputFile.canRead()) {
      throw new GeneratorException(
          new IOException("Sorry, couldn't open: \"" + inputFile.getAbsolutePath() + "\""));
    }

    log.resetCounters();

    Timer totalTime = new Timer();
    Timer time = new Timer();

    LexScan scanner;
    LexParse parser;
    FileReader inputReader = null;

    totalTime.start();

    try {
      log.println(ErrorMessages.READING, inputFile.toString());
      inputReader = new FileReader(inputFile);
      scanner =
          new LexScan(inputReader)
              .withLexicanSpecification(inputFile)
              .withOptions(options);
      parser = new LexParse(scanner, options, log);
    } catch (FileNotFoundException e) {
      throw new GeneratorException(e, ErrorMessages.CANNOT_OPEN, inputFile);
    }

    try {
      NFA nfa = (NFA) parser.parse().value;

      log.checkErrors();

      if (options.dump())
        log.dump(ErrorMessages.get(ErrorMessages.NFA_IS) + Out.NL + nfa + Out.NL);

      if (options.generateDotFile()) {
        nfa.writeDot(
            Emitter.normalize(
                options.outputDirectory(),
                "nfa.dot",
                null,
                options.backup())); // $NON-NLS-1$
      }
      log.println(ErrorMessages.NFA_STATES, nfa.numStates);

      time.start();
      DFA dfa = nfa.createDFA();
      time.stop();
      log.time(ErrorMessages.DFA_TOOK, time);

      dfa.checkActions(scanner, parser);

      nfa = null;

      if (options.dump())
        log.dump(ErrorMessages.get(ErrorMessages.DFA_IS) + Out.NL + dfa + Out.NL);

      if (options.generateDotFile()) {
        dfa.writeDot(
            Emitter.normalize(
                options.outputDirectory(),
                "dfa-big.dot",
                null,
                options.backup())); // $NON-NLS-1$
      }
      log.checkErrors();

      time.start();
      dfa.minimize();
      time.stop();

      log.time(ErrorMessages.MIN_TOOK, time);

      if (options.dump())
        log.dump(ErrorMessages.get(ErrorMessages.MIN_DFA_IS) + Out.NL + dfa);

      if (options.generateDotFile())
        dfa.writeDot(
            Emitter.normalize(
                options.outputDirectory(),
                "dfa-min.dot",
                null,
                options.backup())); // $NON-NLS-1$

      time.start();

      Emitter e = new Emitter(inputFile, parser, dfa, options);
      e.emit(log);

      time.stop();

      log.time(ErrorMessages.WRITE_TOOK, time);

      totalTime.stop();

      log.time(ErrorMessages.TOTAL_TIME, totalTime);
    } catch (ScannerException e) {
      log.error(e.file, e.message, e.line, e.column);
      throw new GeneratorException(e);
    } catch (OutOfMemoryError e) {
      throw new GeneratorException(ErrorMessages.OUT_OF_MEMORY);
    } catch (Exception e) {
      throw new GeneratorException(e);
    }
  }

  public void printStatistics() {
    log.printStatistics();
  }
}
