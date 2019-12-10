/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import jflex.core.DFA;
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.core.NFA;
import jflex.core.OptionUtils;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.performance.Timer;
import jflex.scanner.ScannerException;

/**
 * This is the generator of JFlex, controlling the scanner generation process.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class LexGenerator {

  private LexGenerator() {}

  /**
   * Generates a scanner for the specified input file.
   *
   * @param inputFile a file containing a lexical specification to generate a scanner for.
   * @return the file name of the generated Java sources.
   */
  public static String generate(File inputFile) {
    if (Options.encoding == null) {
      OptionUtils.setDefaultOptions();
    }

    Out.resetCounters();

    Timer totalTime = new Timer();
    Timer time = new Timer();

    totalTime.start();

    try (Reader inputReader =
        new InputStreamReader(
            Files.newInputStream(Paths.get(inputFile.toString())), Options.encoding)) {
      Out.println(ErrorMessages.READING, inputFile.toString());
      LexScan scanner = new LexScan(inputReader);
      scanner.setFile(inputFile);
      LexParse parser = new LexParse(scanner);

      NFA nfa = (NFA) parser.parse().value;

      Out.checkErrors();

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.NFA_IS) + Out.NL + nfa + Out.NL);

      if (Options.dot) nfa.writeDot(Emitter.normalize("nfa.dot", null)); // $NON-NLS-1$

      Out.println(ErrorMessages.NFA_STATES, nfa.numStates());

      time.start();
      DFA dfa = nfa.getDFA();
      time.stop();
      Out.time(ErrorMessages.DFA_TOOK, time);

      dfa.checkActions(scanner, parser);

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.DFA_IS) + Out.NL + dfa + Out.NL);

      if (Options.dot) dfa.writeDot(Emitter.normalize("dfa-big.dot", null)); // $NON-NLS-1$

      Out.checkErrors();

      time.start();
      dfa.minimize();
      time.stop();

      Out.time(ErrorMessages.MIN_TOOK, time);

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.MIN_DFA_IS) + Out.NL + dfa);

      if (Options.dot) dfa.writeDot(Emitter.normalize("dfa-min.dot", null)); // $NON-NLS-1$

      time.start();

      Emitter emitter = Emitters.createFileEmitter(inputFile, parser, dfa);
      emitter.emit();

      time.stop();

      Out.time(ErrorMessages.WRITE_TOOK, time);

      totalTime.stop();

      Out.time(ErrorMessages.TOTAL_TIME, totalTime);
      return emitter.outputFileName;
    } catch (ScannerException e) {
      Out.error(e.file, e.message, e.line, e.column);
      throw new GeneratorException(e);
    } catch (MacroException e) {
      Out.error(e.getMessage());
      throw new GeneratorException(e);
    } catch (IOException e) {
      Out.error(ErrorMessages.IO_ERROR, e.toString());
      throw new GeneratorException(e);
    } catch (OutOfMemoryError e) {
      Out.error(ErrorMessages.OUT_OF_MEMORY);
      throw new GeneratorException();
    } catch (GeneratorException e) {
      throw e;
    } catch (Exception e) {
      throw new GeneratorException(e, true);
    }
  }
}
