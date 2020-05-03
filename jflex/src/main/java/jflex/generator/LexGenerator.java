/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
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
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.core.NFA;
import jflex.core.OptionUtils;
import jflex.dfa.DFA;
import jflex.dfa.DfaFactory;
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
 * @version JFlex 1.8.2
 */
public class LexGenerator {

  private final File inputFile;
  private DFA dfa;

  private final Timer totalTime = new Timer();

  public LexGenerator(File inputFile) {
    this.inputFile = inputFile;
    if (Options.encoding == null) {
      OptionUtils.setDefaultOptions();
    }
  }

  /**
   * Generates a scanner for the specified input file.
   *
   * @return the file name of the generated Java sources.
   */
  public String generate() {

    Out.resetCounters();

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
      dfa = DfaFactory.createFromNfa(nfa);
      time.stop();
      Out.time(ErrorMessages.DFA_TOOK, time);

      dfa.checkActions(scanner, parser);

      if (Options.dump) Out.dump(ErrorMessages.get(ErrorMessages.DFA_IS) + Out.NL + dfa + Out.NL);

      if (Options.dot) dfa.writeDot(Emitter.normalize("dfa-big.dot", null)); // $NON-NLS-1$

      Out.checkErrors();

      time.start();
      int numStatesBefore = dfa.numStates();
      dfa.minimize();
      Out.println(
          String.format(
              "%d states before minimization, %d states in minimized DFA",
              numStatesBefore, dfa.numStates()));
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
      throw new GeneratorException(e);
    } catch (GeneratorException e) {
      throw e;
    } catch (Exception e) {
      throw new GeneratorException(e, true);
    }
  }

  public int minimizedDfaStatesCount() {
    checkNotNull(dfa, "DFA doesn't exist. Call generate() first.");
    checkState(dfa.isMinimized(), "DFA is not minimized. Call minimize() first.");
    return dfa.numStates();
  }

  private static Object checkNotNull(Object object, String msg) {
    if (object == null) {
      throw new NullPointerException(msg);
    }
    return object;
  }

  private static void checkState(boolean state, String msg) {
    if (!state) {
      throw new IllegalStateException(msg);
    }
  }
}
