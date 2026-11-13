/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.core.NFA;
import jflex.core.unicode.CharClasses;
import jflex.dfa.DFA;
import jflex.dfa.DfaFactory;
import jflex.exceptions.GeneratorException;
import jflex.exceptions.MacroException;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.performance.Timer;
import jflex.scanner.ScannerException;
import jflex.skeleton.Skeleton;

/**
 * This is the generator of JFlex, controlling the scanner generation process.
 *
 * @author Gerwin Klein
 * @author Régis Décamps
 * @version JFlex 1.10.0-SNAPSHOT
 */
public class LexGenerator {

  private final File inputFile;
  private final Options options;
  private final Out out;
  private DFA dfa;
  private CharClasses charClasses;

  private final Timer totalTime = new Timer();

  /** Creates a generator for {@code inputFile} with the default options. */
  public LexGenerator(File inputFile) {
    this(inputFile, new Options());
  }

  /**
   * Creates a generator for {@code inputFile} that prints to {@code System.out}.
   *
   * @param inputFile the spec to generate a scanner from
   * @param options the options for this run (not shared with concurrent runs)
   */
  public LexGenerator(File inputFile, Options options) {
    this(inputFile, options, new Out(options));
  }

  /**
   * Creates a generator for {@code inputFile}.
   *
   * @param inputFile the spec to generate a scanner from
   * @param options the options for this run
   * @param out where this run prints its messages
   */
  public LexGenerator(File inputFile, Options options, Out out) {
    this.inputFile = inputFile;
    this.options = options;
    this.out = out;
  }

  /**
   * Generates a scanner for the specified input file.
   *
   * <p>Settings in the spec ({@code %suppress}, {@code %no-warn}, {@code %warn}, {@code
   * %apiprivate}) only apply to this spec.
   *
   * @return the file name of the generated Java sources.
   * @throws GeneratorException if generation fails; the error has been reported to the output
   */
  public String generate() {
    Set<ErrorMessages> suppressedWarnings = options.suppressedWarnings();
    Skeleton skeleton = options.skeleton();
    try {
      return generateScanner();
    } finally {
      options.setSuppressedWarnings(suppressedWarnings);
      options.setSkeleton(skeleton);
    }
  }

  private String generateScanner() {

    out.resetCounters();

    Timer time = new Timer();

    totalTime.start();

    try (Reader inputReader =
        new InputStreamReader(
            Files.newInputStream(Paths.get(inputFile.toString())), options.encoding())) {
      out.println(ErrorMessages.READING, inputFile.toString());
      LexScan scanner = new LexScan(inputReader);
      scanner.setOptions(options);
      scanner.setOut(out);
      scanner.setFile(inputFile);
      LexParse parser = new LexParse(scanner);

      NFA nfa = (NFA) parser.parse().value;
      charClasses = parser.getCharClasses();

      out.checkErrors();

      if (options.dump()) out.dump(ErrorMessages.get(ErrorMessages.NFA_IS) + Out.NL + nfa + Out.NL);

      if (options.dot())
        nfa.writeDot(Emitter.normalize("nfa.dot", null, options, out)); // $NON-NLS-1$

      out.println(ErrorMessages.NFA_STATES, nfa.numStates());

      time.start();
      dfa = DfaFactory.createFromNfa(nfa, options, out);
      time.stop();
      out.time(ErrorMessages.DFA_TOOK, time);

      dfa.checkActions(scanner, parser);

      if (options.dump()) out.dump(ErrorMessages.get(ErrorMessages.DFA_IS) + Out.NL + dfa + Out.NL);

      if (options.dot())
        dfa.writeDot(Emitter.normalize("dfa-big.dot", null, options, out)); // $NON-NLS-1$

      out.checkErrors();

      time.start();
      int numStatesBefore = dfa.numStates();
      if (options.noMinimize()) {
        out.println("minimization skipped.");
      } else {
        dfa.minimize();
      }
      out.println(
          String.format(
              "%d states before minimization, %d states in minimized DFA",
              numStatesBefore, dfa.numStates()));
      time.stop();

      out.time(ErrorMessages.MIN_TOOK, time);

      if (options.dump()) out.dump(ErrorMessages.get(ErrorMessages.MIN_DFA_IS) + Out.NL + dfa);

      if (options.dot())
        dfa.writeDot(Emitter.normalize("dfa-min.dot", null, options, out)); // $NON-NLS-1$

      time.start();

      Emitter emitter = Emitters.createFileEmitter(inputFile, parser, dfa);
      emitter.emit();

      time.stop();

      out.time(ErrorMessages.WRITE_TOOK, time);

      totalTime.stop();

      out.time(ErrorMessages.TOTAL_TIME, totalTime);
      return emitter.outputFileName;
    } catch (ScannerException e) {
      out.error(e.file, e.message, e.line, e.column);
      throw new GeneratorException(e);
    } catch (MacroException e) {
      out.error(e.getMessage());
      throw new GeneratorException(e);
    } catch (IOException e) {
      out.error(ErrorMessages.IO_ERROR, e.toString());
      throw new GeneratorException(e);
    } catch (OutOfMemoryError e) {
      out.error(ErrorMessages.OUT_OF_MEMORY);
      throw new GeneratorException(e);
    } catch (GeneratorException e) {
      // report any outstanding errors thrown by code that has no Out, and rethrow.
      out.error(e);
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

  /**
   * Returns the canonical minimized DFA of the generated scanner. See {@link DFA#canonical()}. The
   * inputs are the numbers of the {@link #charClasses()}.
   *
   * @return a copy of the DFA
   */
  public DFA minimizedDfa() {
    checkNotNull(dfa, "DFA does not exist. Call generate() first.");
    checkState(dfa.isMinimized(), "DFA is not minimized. Call minimize() first.");
    return dfa.canonical();
  }

  /**
   * Returns the character classes of the generated scanner: the partition of the input characters
   * into sets that the scanner does not distinguish, in canonical order.
   *
   * @return a copy of the partition
   */
  public CharClasses charClasses() {
    checkNotNull(charClasses, "Character classes do not exist. Call generate() first.");
    return CharClasses.copyOf(charClasses);
  }

  /** Returns the number of character classes of the generated scanner. */
  public int charClassesCount() {
    checkNotNull(charClasses, "Character classes do not exist. Call generate() first.");
    return charClasses.getNumClasses();
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
