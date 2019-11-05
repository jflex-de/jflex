/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import jflex.core.DFA;
import jflex.core.LexParse;
import jflex.core.Options;
import jflex.core.Out;

/** Factory class for Emitter */
public final class Emitters {

  private Emitters() {}

  /**
   * Create Emitter that writes to file
   *
   * @param inputFile input grammar.
   * @param parser a {@link LexParse}.
   * @param dfa a {@link DFA}.
   * @return {@link Emitter}.
   * @throws java.io.IOException if any.
   */
  public static Emitter createFileEmitter(File inputFile, LexParse parser, DFA dfa)
      throws IOException {

    String name = Emitter.getBaseName(parser.scanner.className()) + ".java";

    File outputFile = Emitter.normalize(name, inputFile);
    String outputFileName = outputFile.getAbsolutePath();

    Out.println("Writing code to \"" + outputFile + "\"");

    PrintWriter out =
        new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), Options.encoding)));

    return new Emitter(outputFileName, inputFile, parser, dfa, out);
  }

  /**
   * Create Emitter that writes to writer
   *
   * @param parser a {@link LexParse}.
   * @param dfa a {@link DFA}.
   * @param writer output file.
   * @return {@link Emitter}.
   */
  public static Emitter createPrintWriterEmitter(LexParse parser, DFA dfa, PrintWriter writer) {
    return new Emitter(null, new File(""), parser, dfa, writer);
  }
}
