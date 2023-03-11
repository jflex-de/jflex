/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.generator;

import java.io.File;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import jflex.base.Build;
import jflex.base.Pair;
import jflex.core.AbstractLexScan;
import jflex.core.Action;
import jflex.core.EOFActions;
import jflex.core.LexParse;
import jflex.core.LexScan;
import jflex.core.unicode.CMapBlock;
import jflex.core.unicode.CharClasses;
import jflex.dfa.DFA;
import jflex.exceptions.GeneratorException;
import jflex.io.FileUtils;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.skeleton.Skeleton;

/**
 * This class manages the actual code generation, putting the scanner together, filling in skeleton
 * sections etc.
 *
 * <p>Table compression, String packing etc. is also done here.
 *
 * @author Gerwin Klein
 * @version JFlex 1.9.1
 */
public final class Emitter {
  // bit masks for state attributes
  private static final int FINAL = 1;
  private static final int NOLOOK = 8;

  private final File inputFile;
  final String outputFileName;

  private final PrintWriter out;
  private final Skeleton skel;
  private final AbstractLexScan scanner;
  private final LexParse parser;
  private final DFA dfa;

  private boolean[] isTransition;

  // for row killing:
  private int[] rowMap;
  private boolean[] rowKilled;

  // for col killing:
  private int numCols;
  private int[] colMap;
  private boolean[] colKilled;

  /** maps actions to their switch label */
  private final Map<Action, Integer> actionTable = new LinkedHashMap<>();

  private final String visibility;
  private String eofCode;
  private String eofThrow;

  /**
   * Emits the java code.
   *
   * @param inputFile input grammar.
   * @param parser a {@link LexParse}.
   * @param dfa a {@link DFA}.
   * @param writer output file.
   */
  Emitter(String outputFileName, File inputFile, LexParse parser, DFA dfa, PrintWriter writer) {
    this.outputFileName = outputFileName;
    this.out = writer;
    this.parser = parser;
    this.scanner = parser.scanner;
    this.visibility = scanner.visibility();
    this.inputFile = inputFile;
    this.dfa = dfa;
    this.skel = new Skeleton(out);
  }

  /**
   * Computes base name of the class name. Needs to take into account generics.
   *
   * @param className Class name for which to construct the base name
   */
  static String getBaseName(String className) {
    int gen = className.indexOf('<');
    if (gen < 0) {
      return className;
    } else {
      return className.substring(0, gen);
    }
  }

  /**
   * Constructs a file in Options.getDir() or in the same directory as another file. Makes a backup
   * if the file already exists.
   *
   * @param name the name (without path) of the file
   * @param input fall back location if {@code path = null} (expected to be a file in the directory
   *     to write to)
   * @return The constructed File
   */
  public static File normalize(String name, File input) {
    File outputFile;

    if (Options.getDir() == null)
      if (input == null || input.getParent() == null) outputFile = new File(name);
      else outputFile = new File(input.getParent(), name);
    else outputFile = new File(Options.getDir(), name);

    if (outputFile.exists() && !Options.no_backup) {
      File backup = new File(outputFile.toString() + "~");

      if (backup.exists()) {
        //noinspection ResultOfMethodCallIgnored
        backup.delete();
      }

      if (outputFile.renameTo(backup))
        Out.println("Old file \"" + outputFile + "\" saved as \"" + backup + "\"");
      else Out.println("Couldn't save old file \"" + outputFile + "\", overwriting!");
    }

    return outputFile;
  }

  private void println() {
    out.println();
  }

  private void println(String line) {
    out.println(line);
  }

  private void println(int i) {
    out.println(i);
  }

  private void print(String line) {
    out.print(line);
  }

  private void print(int i) {
    out.print(i);
  }

  private void print(int i, @SuppressWarnings("SameParameterValue") int tab) {
    int exp;

    if (i < 0) exp = 1;
    else exp = 10;

    while (tab-- > 1) {
      if (Math.abs(i) < exp) print(" ");
      exp *= 10;
    }

    print(i);
  }

  private boolean hasGenLookAhead() {
    return dfa.lookaheadUsed();
  }

  private void emitLookBuffer() {
    if (!hasGenLookAhead()) return;

    println("  /** For the backwards DFA of general lookahead statements */");
    println(
        "  private boolean [] zzFin = new boolean [Math.min(ZZ_BUFFERSIZE, zzMaxBufferLen())+1];");
    println();
  }

  private void emitScanError() {
    print("  private static void zzScanError(int errorCode)");

    if (scanner.scanErrorException() != null) print(" throws " + scanner.scanErrorException());

    println(" {");

    skel.emitNext();

    if (scanner.scanErrorException() == null) println("    throw new Error(message);");
    else println("    throw new " + scanner.scanErrorException() + "(message);");

    skel.emitNext();

    print("  " + visibility + " void yypushback(int number) ");

    if (scanner.scanErrorException() == null) println(" {");
    else println(" throws " + scanner.scanErrorException + " {");
  }

  private void emitMain(String functionName) {
    if (!(scanner.standalone() || scanner.debugOption() || scanner.cupDebug())) return;

    if (scanner.cupDebug()) {
      println("  /**");
      println("   * Converts an int token code into the name of the");
      println("   * token by reflection on the cup symbol class/interface " + scanner.cupSymbol());
      println("   */");
      println("  private static String getTokenName(int token) {");
      println("    try {");
      println(
          "      java.lang.reflect.Field [] classFields = "
              + scanner.cupSymbol()
              + ".class.getFields();");
      println("      for (int i = 0; i < classFields.length; i++) {");
      println("        if (classFields[i].getInt(null) == token) {");
      println("          return classFields[i].getName();");
      println("        }");
      println("      }");
      println("    } catch (Exception e) {");
      println("      e.printStackTrace(System.err);");
      println("    }");
      println("");
      println("    return \"UNKNOWN TOKEN\";");
      println("  }");
      println("");
      println("  /**");
      println("   * Same as " + functionName + " but also prints the token to standard out");
      println("   * for debugging.");
      println("   */");

      if (scanner.cupCompatible() || scanner.cup2Compatible()) {
        // cup interface forces public method
        print("  public ");
      } else {
        print("  " + visibility + " ");
      }
      if (scanner.tokenType() == null) {
        if (scanner.isInteger()) print("int");
        else if (scanner.isIntWrap()) print("Integer");
        else print("Yytoken");
      } else print(scanner.tokenType());

      print(" debug_");

      print(functionName);

      print("() throws java.io.IOException");

      for (String thrown : scanner.lexThrow()) {
        print("\n    , ");
        print(thrown);
      }

      if (scanner.scanErrorException() != null) {
        print(", ");
        print(scanner.scanErrorException());
      }

      println(" {");

      println("    " + scanner.tokenType() + " s = " + functionName + "();");
      print("    System.out.println( ");
      if (scanner.lineCount()) print("\"line:\" + (yyline+1) + ");
      if (scanner.columnCount()) print("\" col:\" + (yycolumn+1) + ");
      if (scanner.charCount()) print("\" char:\" + yychar + ");
      println("\" --\"+ yytext() + \"--\" + getTokenName(s.sym) + \"--\");");
      println("    return s;");
      println("  }");
      println("");
    }

    if (scanner.standalone()) {
      println("  /**");
      println("   * Runs the scanner on input files.");
      println("   *");
      println("   * This is a standalone scanner, it will print any unmatched");
      println("   * text to System.out unchanged.");
      println("   *");
      println("   * @param argv   the command line, contains the filenames to run");
      println("   *               the scanner on.");
      println("   */");
    } else {
      println("  /**");
      println("   * Runs the scanner on input files.");
      println("   *");
      println("   * This main method is the debugging routine for the scanner.");
      println("   * It prints debugging information about each returned token to");
      println("   * System.out until the end of file is reached, or an error occured.");
      println("   *");
      println("   * @param argv   the command line, contains the filenames to run");
      println("   *               the scanner on.");
      println("   */");
    }

    String className = getBaseName(scanner.className());

    println("  public static void main(String[] argv) {");
    println("    if (argv.length == 0) {");
    println(
        "      System.out.println(\"Usage : java "
            + className
            + " [ --encoding <name> ] <inputfile(s)>\");");
    println("    }");
    println("    else {");
    println("      int firstFilePos = 0;");
    println("      String encodingName = \"UTF-8\";");
    println("      if (argv[0].equals(\"--encoding\")) {");
    println("        firstFilePos = 2;");
    println("        encodingName = argv[1];");
    println("        try {");
    println("          // Side-effect: is encodingName valid?");
    println("          java.nio.charset.Charset.forName(encodingName);");
    println("        } catch (Exception e) {");
    println("          System.out.println(\"Invalid encoding '\" + encodingName + \"'\");");
    println("          return;");
    println("        }");
    println("      }");
    println("      for (int i = firstFilePos; i < argv.length; i++) {");
    println("        " + className + " scanner = null;");
    println("        java.io.FileInputStream stream = null;");
    println("        java.io.Reader reader = null;");
    println("        try {");
    println("          stream = new java.io.FileInputStream(argv[i]);");
    println("          reader = new java.io.InputStreamReader(stream, encodingName);");
    println("          scanner = new " + className + "(reader);");
    if (scanner.standalone()) {
      println("          while ( !scanner.zzAtEOF ) scanner." + functionName + "();");
    } else if (scanner.cupDebug()) {
      println("          while ( !scanner.zzAtEOF ) scanner.debug_" + functionName + "();");
    } else {
      println("          do {");
      println("            System.out.println(scanner." + functionName + "());");
      println("          } while (!scanner.zzAtEOF);");
      println("");
    }

    println("        }");
    println("        catch (java.io.FileNotFoundException e) {");
    println("          System.out.println(\"File not found : \\\"\"+argv[i]+\"\\\"\");");
    println("        }");
    println("        catch (java.io.IOException e) {");
    println("          System.out.println(\"IO error scanning file \\\"\"+argv[i]+\"\\\"\");");
    println("          System.out.println(e);");
    println("        }");
    println("        catch (Exception e) {");
    println("          System.out.println(\"Unexpected exception:\");");
    println("          e.printStackTrace();");
    println("        }");
    println("        finally {");
    println("          if (reader != null) {");
    println("            try {");
    println("              reader.close();");
    println("            }");
    println("            catch (java.io.IOException e) {");
    println("              System.out.println(\"IO error closing file \\\"\"+argv[i]+\"\\\"\");");
    println("              System.out.println(e);");
    println("            }");
    println("          }");
    println("          if (stream != null) {");
    println("            try {");
    println("              stream.close();");
    println("            }");
    println("            catch (java.io.IOException e) {");
    println("              System.out.println(\"IO error closing file \\\"\"+argv[i]+\"\\\"\");");
    println("              System.out.println(e);");
    println("            }");
    println("          }");
    println("        }");
    println("      }");
    println("    }");
    println("  }");
    println("");
  }

  private void emitNoMatch() {
    println("            zzScanError(ZZ_NO_MATCH);");
  }

  private void emitNextInput() {
    println("          if (zzCurrentPosL < zzEndReadL) {");
    println("            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);");
    println("            zzCurrentPosL += Character.charCount(zzInput);");
    println("          }");
    println("          else if (zzAtEOF) {");
    println("            zzInput = YYEOF;");
    println("            break zzForAction;");
    println("          }");
    println("          else {");
    println("            // store back cached positions");
    println("            zzCurrentPos  = zzCurrentPosL;");
    println("            zzMarkedPos   = zzMarkedPosL;");
    println("            boolean eof = zzRefill();");
    println("            // get translated positions and possibly new buffer");
    println("            zzCurrentPosL  = zzCurrentPos;");
    println("            zzMarkedPosL   = zzMarkedPos;");
    println("            zzBufferL      = zzBuffer;");
    println("            zzEndReadL     = zzEndRead;");
    println("            if (eof) {");
    println("              zzInput = YYEOF;");
    println("              break zzForAction;");
    println("            }");
    println("            else {");
    println("              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);");
    println("              zzCurrentPosL += Character.charCount(zzInput);");
    println("            }");
    println("          }");
  }

  public static String sourceFileString(File file) {
    String path = FileUtils.getRelativePath(Options.getRootDirectory(), file);
    if (File.separatorChar == '\\') {
      path = FileUtils.slashify(path);
    }
    // Character '\' can be use for Unicode representation, e.g. \\u000A is new line
    return path.replace("\\", "\\\\");
  }

  private void emitHeader() {
    println("// DO NOT EDIT");
    println("// Generated by JFlex " + Build.VERSION + " http://jflex.de/");
    println("// source: " + sourceFileString(inputFile));
    println("");
  }

  private void emitUserCode() {
    println(scanner.userCode());

    if (scanner.cup2Compatible()) {
      println();
      println("/* CUP2 imports */");
      println("import edu.tum.cup2.scanner.*;");
      println("import edu.tum.cup2.grammar.*;");
      println();
    }
  }

  private void emitClassName() {
    if (!scanner.noSuppressWarnings()) {
      // TODO(#222) Actually fix the fall-through violations
      println("@SuppressWarnings(\"fallthrough\")");
    }
    if (scanner.isPublic()) print("public ");

    if (scanner.isAbstract()) print("abstract ");

    if (scanner.isFinal()) print("final ");

    print("class ");
    print(scanner.className());

    if (scanner.isExtending() != null) {
      print(" extends ");
      print(scanner.isExtending());
    }

    if (scanner.isImplementing() != null) {
      print(" implements ");
      print(scanner.isImplementing());
    }

    println(" {");
  }

  private void emitLexicalStates() {
    for (String name : scanner.stateNames()) {
      int num = scanner.getStateNumber(name);

      println("  " + visibility + " static final int " + name + " = " + 2 * num + ";");
    }

    // can't quite get rid of the indirection, even for non-bol lex states:
    // their DFA states might be the same, but their EOF actions might be different
    // (see bug #1540228)
    println("");
    println("  /**");
    println("   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l");
    println("   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l");
    println("   *                  at the beginning of a line");
    println("   * l is of the form l = 2*k, k a non negative integer");
    println("   */");
    println("  private static final int ZZ_LEXSTATE[] = {");

    int i, j = 0;
    print("    ");

    for (i = 0; i < 2 * dfa.numLexStates() - 1; i++) {
      print(dfa.entryState(i), 2);

      print(", ");

      if (++j >= 16) {
        println();
        print("    ");
        j = 0;
      }
    }

    println(dfa.entryState(i));
    println("  };");
  }

  private void emitDynamicInit() {
    int count = 0;
    int value = dfa.table(0, 0);

    println("  /**");
    println("   * The transition table of the DFA");
    println("   */");

    // allow values from -1 (translate +1), get emitter capacity based on number of states
    CountEmitter e = CountEmitter.emitter(dfa.numStates(), +1, "trans");
    e.emitInit();

    for (int i = 0; i < dfa.numStates(); i++) {
      if (!rowKilled[i]) {
        for (int c = 0; c < dfa.numInput(); c++) {
          if (!colKilled[c]) {
            if (dfa.table(i, c) == value) {
              count++;
            } else {
              e.emit(count, value);

              count = 1;
              value = dfa.table(i, c);
            }
          }
        }
      }
    }

    e.emit(count, value);
    e.emitUnpack();

    println(e.toString());
  }

  private void emitCharMapArrayUnPacked() {

    CharClasses cl = parser.getCharClasses();

    println("");
    println("  /**");
    println("   * Translates characters to character classes");
    println("   */");
    println("  private static final char [] ZZ_CMAP = {");

    int n = 0; // numbers of entries in current line
    print("    ");

    int max = cl.getMaxCharCode();

    // not very efficient, but good enough for <= 255 characters
    for (char c = 0; c <= max; c++) {
      print(colMap[cl.getClassCode(c)], 2);

      if (c < max) {
        print(", ");
        if (++n >= 16) {
          println();
          print("    ");
          n = 0;
        }
      }
    }

    println();
    println("  };");
    println();
  }

  /**
   * Performs an in-place update to map the colMap translation over the char classes in the
   * second-level cmap table.
   */
  private void mapColMap(int[] blocks) {
    for (int i = 0; i < blocks.length; i++) {
      blocks[i] = colMap[blocks[i]];
    }
  }

  /**
   * Emits two-level character translation tables. The translation is from raw input codepoint to
   * the column in the generated DFA table.
   *
   * <p>For maxCharCode < 256, a single-level unpacked array is used instead.
   */
  private void emitCharMapTables() {
    CharClasses cl = parser.getCharClasses();

    if (cl.getMaxCharCode() < 256) {
      emitCharMapArrayUnPacked();
    } else {
      Pair<int[], int[]> tables = cl.getTables();
      mapColMap(tables.snd);

      println("");
      println("  /**");
      println("   * Top-level table for translating characters to character classes");
      println("   */");
      CountEmitter e = new CountEmitter("cmap_top");
      e.emitInit();
      e.emitCountValueString(tables.fst);
      e.emitUnpack();
      println(e.toString());

      println("");
      println("  /**");
      println("   * Second-level tables for translating characters to character classes");
      println("   */");
      e = new CountEmitter("cmap_blocks");
      e.emitInit();
      e.emitCountValueString(tables.snd);
      e.emitUnpack();
      println(e.toString());
    }
  }

  private void emitRowMapArray() {
    println("");
    println("  /**");
    println("   * Translates a state to a row index in the transition table");
    println("   */");

    HiLowEmitter e = new HiLowEmitter("RowMap");
    e.emitInit();
    for (int i = 0; i < dfa.numStates(); i++) {
      e.emit(rowMap[i] * numCols);
    }
    e.emitUnpack();
    println(e.toString());
  }

  private void emitAttributes() {
    // TODO(lsf): refactor to use CountEmitter.emitCountValueString
    println("  /**");
    println("   * ZZ_ATTRIBUTE[aState] contains the attributes of state {@code aState}");
    println("   */");

    CountEmitter e = new CountEmitter("Attribute");
    e.emitInit();

    int count = 1;
    int value = 0;
    if (dfa.isFinal(0)) value = FINAL;
    if (!isTransition[0]) value |= NOLOOK;

    for (int i = 1; i < dfa.numStates(); i++) {
      int attribute = 0;
      if (dfa.isFinal(i)) attribute = FINAL;
      if (!isTransition[i]) attribute |= NOLOOK;

      if (value == attribute) {
        count++;
      } else {
        e.emit(count, value);
        count = 1;
        value = attribute;
      }
    }

    e.emit(count, value);
    e.emitUnpack();

    println(e.toString());
  }

  private void emitClassCode() {
    if (scanner.classCode() != null) {
      println("  /* user code: */");
      println(scanner.classCode());
    }

    if (scanner.cup2Compatible()) {
      // convenience methods for CUP2
      println();
      println("  /* CUP2 code: */");
      println("  private <T> ScannerToken<T> token(Terminal terminal, T value) {");
      println("    return new ScannerToken<T>(terminal, value, yyline, yycolumn);");
      println("  }");
      println();
      println("  private ScannerToken<Object> token(Terminal terminal) {");
      println("    return new ScannerToken<Object>(terminal, yyline, yycolumn);");
      println("  }");
      println();
    }
  }

  private void emitConstructorDecl() {
    emitConstructorDecl(true);

    if ((scanner.standalone() || scanner.debugOption()) && scanner.ctorArgsCount() > 0) {
      Out.warning(ErrorMessages.CTOR_DEBUG);
      println();
      emitConstructorDecl(false);
    }
  }

  private void emitConstructorDecl(boolean printCtorArgs) {
    println("  /**");
    println("   * Creates a new scanner");
    println("   *");
    println("   * @param   in  the java.io.Reader to read input from.");
    println("   */");

    String warn =
        "// WARNING: this is a default constructor for "
            + "debug/standalone only. Has no custom parameters or init code.";

    if (!printCtorArgs) println(warn);

    print("  ");

    if (scanner.isPublic()) print("public ");
    print(getBaseName(scanner.className()));
    print("(java.io.Reader in");
    if (printCtorArgs) emitCtorArgs();
    print(")");

    if (scanner.initThrow() != null && printCtorArgs) {
      print(" throws ");
      print(scanner.initThrow());
    }

    println(" {");

    if (scanner.initCode() != null && printCtorArgs) {
      print("  ");
      print(scanner.initCode());
    }

    println("    this.zzReader = in;");

    println("  }");
    println();
  }

  private void emitCtorArgs() {
    for (int i = 0; i < scanner.ctorArgsCount(); i++) {
      print(", " + scanner.ctorType(i));
      print(" " + scanner.ctorArg(i));
    }
  }

  private void emitDoEOF() {
    if (eofCode == null) return;

    println("  /**");
    println("   * Contains user EOF-code, which will be executed exactly once,");
    println("   * when the end of file is reached");
    println("   */");

    print("  private void zzDoEOF()");

    if (eofThrow != null) {
      print(" throws ");
      print(eofThrow);
    }

    println(" {");

    println("    if (!zzEOFDone) {");
    println("      zzEOFDone = true;");
    println("    ");
    print(/*    */ eofCode);
    println("    }");
    println("  }");
    println("");
    println("");
  }

  private void emitLexFunctHeader(String functionName) {

    if (scanner.cupCompatible() || scanner.cup2Compatible()) {
      print("  @Override");
      // force public, because we have to implement cup/cup2 interface
      print("  public ");
    } else {
      print("  " + visibility + " ");
    }

    if (scanner.tokenType() == null) {
      if (scanner.isInteger()) print("int");
      else if (scanner.isIntWrap()) print("Integer");
      else print("Yytoken");
    } else print(scanner.tokenType());

    print(" ");

    print(functionName);

    print("() throws java.io.IOException");

    for (String thrown : scanner.lexThrow()) {
      print("\n    , ");
      print(thrown);
    }

    if (scanner.scanErrorException() != null) {
      print(",\n     ");
      print(scanner.scanErrorException());
    }

    println("\n  {");

    skel.emitNext();

    println("    int [] zzTransL = ZZ_TRANS;");
    println("    int [] zzRowMapL = ZZ_ROWMAP;");
    println("    int [] zzAttrL = ZZ_ATTRIBUTE;");

    skel.emitNext();

    if (scanner.charCount()) {
      println("      yychar+= zzMarkedPosL-zzStartRead;");
      println("");
    }

    if (scanner.lineCount() || scanner.columnCount()) {
      println("      boolean zzR = false;");
      println("      int zzCh;");
      println("      int zzCharCount;");
      println("      for (zzCurrentPosL = zzStartRead  ;");
      println("           zzCurrentPosL < zzMarkedPosL ;");
      println("           zzCurrentPosL += zzCharCount ) {");
      println("        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);");
      println("        zzCharCount = Character.charCount(zzCh);");
      println("        switch (zzCh) {");
      println("        case '\\u000B':  // fall through");
      println("        case '\\u000C':  // fall through");
      println("        case '\\u0085':  // fall through");
      println("        case '\\u2028':  // fall through");
      println("        case '\\u2029':");
      if (scanner.lineCount()) println("          yyline++;");
      if (scanner.columnCount()) println("          yycolumn = 0;");
      println("          zzR = false;");
      println("          break;");
      println("        case '\\r':");
      if (scanner.lineCount()) println("          yyline++;");
      if (scanner.columnCount()) println("          yycolumn = 0;");
      println("          zzR = true;");
      println("          break;");
      println("        case '\\n':");
      println("          if (zzR)");
      println("            zzR = false;");
      println("          else {");
      if (scanner.lineCount()) println("            yyline++;");
      if (scanner.columnCount()) println("            yycolumn = 0;");
      println("          }");
      println("          break;");
      println("        default:");
      println("          zzR = false;");
      if (scanner.columnCount()) println("          yycolumn += zzCharCount;");
      println("        }");
      println("      }");
      println();

      if (scanner.lineCount()) {
        println("      if (zzR) {");
        println("        // peek one character ahead if it is");
        println("        // (if we have counted one line too much)");
        println("        boolean zzPeek;");
        println("        if (zzMarkedPosL < zzEndReadL)");
        println("          zzPeek = zzBufferL[zzMarkedPosL] == '\\n';");
        println("        else if (zzAtEOF)");
        println("          zzPeek = false;");
        println("        else {");
        println("          boolean eof = zzRefill();");
        println("          zzEndReadL = zzEndRead;");
        println("          zzMarkedPosL = zzMarkedPos;");
        println("          zzBufferL = zzBuffer;");
        println("          if (eof)");
        println("            zzPeek = false;");
        println("          else");
        println("            zzPeek = zzBufferL[zzMarkedPosL] == '\\n';");
        println("        }");
        println("        if (zzPeek) yyline--;");
        println("      }");
      }
    }

    if (scanner.bolUsed()) {
      // zzMarkedPos > zzStartRead <=> last match was not empty
      // if match was empty, last value of zzAtBOL can be used
      // zzStartRead is always >= 0
      println("      if (zzMarkedPosL > zzStartRead) {");
      println("        switch (zzBufferL[zzMarkedPosL-1]) {");
      println("        case '\\n':");
      println("        case '\\u000B':  // fall through");
      println("        case '\\u000C':  // fall through");
      println("        case '\\u0085':  // fall through");
      println("        case '\\u2028':  // fall through");
      println("        case '\\u2029':  // fall through");
      println("          zzAtBOL = true;");
      println("          break;");
      println("        case '\\r': ");
      println("          if (zzMarkedPosL < zzEndReadL)");
      println("            zzAtBOL = zzBufferL[zzMarkedPosL] != '\\n';");
      println("          else if (zzAtEOF)");
      println("            zzAtBOL = false;");
      println("          else {");
      println("            boolean eof = zzRefill();");
      println("            zzMarkedPosL = zzMarkedPos;");
      println("            zzEndReadL = zzEndRead;");
      println("            zzBufferL = zzBuffer;");
      println("            if (eof) ");
      println("              zzAtBOL = false;");
      println("            else ");
      println("              zzAtBOL = zzBufferL[zzMarkedPosL] != '\\n';");
      println("          }");
      println("          break;");
      println("        default:");
      println("          zzAtBOL = false;");
      println("        }");
      println("      }");
    }

    skel.emitNext();

    if (scanner.bolUsed()) {
      println("      if (zzAtBOL)");
      println("        zzState = ZZ_LEXSTATE[zzLexicalState+1];");
      println("      else");
      println("        zzState = ZZ_LEXSTATE[zzLexicalState];");
      println();
    } else {
      println("      zzState = ZZ_LEXSTATE[zzLexicalState];");
      println();
    }

    println("      // set up zzAction for empty match case:");
    println("      int zzAttributes = zzAttrL[zzState];");
    println("      if ( (zzAttributes & 1) == 1 ) {");
    println("        zzAction = zzState;");
    println("      }");
    println();

    skel.emitNext();
  }

  private void emitCMapAccess() {
    println("  /**");
    println("   * Translates raw input code points to DFA table row");
    println("   */");
    println("  private static int zzCMap(int input) {");
    if (parser.getCharClasses().getMaxCharCode() <= 0xFF) {
      println("    return ZZ_CMAP[input];");
    } else {
      println("    int offset = input & " + (CMapBlock.BLOCK_SIZE - 1) + ";");
      println(
          "    return offset == input"
              + " ?"
              + " ZZ_CMAP_BLOCKS[offset]"
              + " :"
              + " ZZ_CMAP_BLOCKS[ZZ_CMAP_TOP[input >> "
              + CMapBlock.BLOCK_BITS
              + "] | offset];");
    }
    println("  }");
  }

  private void emitGetRowMapNext() {
    println("          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMap(zzInput) ];");
    println("          if (zzNext == " + DFA.NO_TARGET + ") break zzForAction;");
    println("          zzState = zzNext;");
    println();

    println("          zzAttributes = zzAttrL[zzState];");

    println("          if ( (zzAttributes & " + FINAL + ") == " + FINAL + " ) {");

    skel.emitNext();

    println(
        "            if ( (zzAttributes & " + NOLOOK + ") == " + NOLOOK + " ) break zzForAction;");

    skel.emitNext();
  }

  /**
   * Escapes all " ' \ tabs and newlines
   *
   * @param s The string to escape
   * @return The escaped string
   */
  private static String escapify(String s) {
    StringBuilder result = new StringBuilder(s.length() * 2);

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\'':
          result.append("\\\'");
          break;
        case '\"':
          result.append("\\\"");
          break;
        case '\\':
          result.append("\\\\");
          break;
        case '\t':
          result.append("\\t");
          break;
        case '\r':
          if (i + 1 == s.length() || s.charAt(i + 1) != '\n') result.append("\"+ZZ_NL+\"");
          break;
        case '\n':
          result.append("\"+ZZ_NL+\"");
          break;
        default:
          result.append(c);
      }
    }

    return result.toString();
  }

  /** emitActionTable. */
  private void emitActionTable() {
    int lastAction = 1;
    int count = 0;
    int value = 0;

    println("  /**");
    println("   * Translates DFA states to action switch labels.");
    println("   */");
    CountEmitter e = new CountEmitter("Action");
    e.emitInit();

    for (int i = 0; i < dfa.numStates(); i++) {
      int newVal = 0;
      if (dfa.isFinal(i)) {
        Action action = dfa.action(i);
        if (action.isEmittable()) {
          Integer stored = actionTable.get(action);
          if (stored == null) {
            stored = lastAction++;
            actionTable.put(action, stored);
          }
          newVal = stored;
        }
      }

      if (value == newVal) {
        count++;
      } else {
        if (count > 0) e.emit(count, value);
        count = 1;
        value = newVal;
      }
    }

    if (count > 0) e.emit(count, value);

    e.emitUnpack();
    println(e.toString());
  }

  private void emitTokenSizeLimit(String limit) {
    println();
    println(
        "  /** Returns the maximum size of the scanner buffer, which limits the size of tokens."
            + " */");
    println("  private int zzMaxBufferLen() {");
    if (limit == null) {
      println("    return Integer.MAX_VALUE;");
    } else {
      println("    return " + limit + ";");
    }
    println("  }");
    println();
    println("  /**  Whether the scanner buffer can grow to accommodate a larger token. */");
    println("  private boolean zzCanGrow() {");
    if (limit == null) {
      println("    return true;");
    } else {
      println("    return zzBuffer.length < " + limit + ";");
    }
    println("  }");
    println();
  }

  private void emitActions() {
    println("        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {");

    int i = actionTable.size() + 1;

    for (Map.Entry<Action, Integer> entry : actionTable.entrySet()) {
      Action action = entry.getKey();
      int label = entry.getValue();

      println("          case " + label + ":");

      if (action.lookAhead() == Action.Kind.FIXED_BASE) {
        println("            // lookahead expression with fixed base length");
        println("            zzMarkedPos = Character.offsetByCodePoints");
        println(
            "                (zzBufferL, zzStartRead, zzEndRead - zzStartRead, zzStartRead, "
                + action.getLookLength()
                + ");");
      }

      if (action.lookAhead() == Action.Kind.FIXED_LOOK
          || action.lookAhead() == Action.Kind.FINITE_CHOICE) {
        println("            // lookahead expression with fixed lookahead length");
        println("            zzMarkedPos = Character.offsetByCodePoints");
        println(
            "                (zzBufferL, zzStartRead, zzEndRead - zzStartRead, zzMarkedPos, -"
                + action.getLookLength()
                + ");");
      }

      if (action.lookAhead() == Action.Kind.GENERAL_LOOK) {
        println("            // general lookahead, find correct zzMarkedPos");
        println("            { int zzFState = " + dfa.entryState(action.getEntryState()) + ";");
        println("              int zzFPos = zzStartRead;");
        println("              if (zzFin.length <= zzBufferL.length) {");
        println("                zzFin = new boolean[zzBufferL.length+1];");
        println("              }");
        println("              boolean zzFinL[] = zzFin;");
        println("              while (zzFState != -1 && zzFPos < zzMarkedPos) {");
        println("                zzFinL[zzFPos] = ((zzAttrL[zzFState] & 1) == 1);");
        println("                zzInput = Character.codePointAt(zzBufferL, zzFPos, zzMarkedPos);");
        println("                zzFPos += Character.charCount(zzInput);");
        println("                zzFState = zzTransL[ zzRowMapL[zzFState] + zzCMap(zzInput) ];");
        println("              }");
        println("              if (zzFState != -1) {");
        println("                zzFinL[zzFPos++] = ((zzAttrL[zzFState] & 1) == 1);");
        println("              }");
        println("              while (zzFPos <= zzMarkedPos) {");
        println("                zzFinL[zzFPos++] = false;");
        println("              }");
        println();
        println("              zzFState = " + dfa.entryState(action.getEntryState() + 1) + ";");
        println("              zzFPos = zzMarkedPos;");
        println("              while (!zzFinL[zzFPos] || (zzAttrL[zzFState] & 1) != 1) {");
        println(
            "                zzInput = Character.codePointBefore(zzBufferL, zzFPos, zzStartRead);");
        println("                zzFPos -= Character.charCount(zzInput);");
        println("                zzFState = zzTransL[ zzRowMapL[zzFState] + zzCMap(zzInput) ];");
        println("              };");
        println("              zzMarkedPos = zzFPos;");
        println("            }");
      }

      if (scanner.debugOption()) {
        print("            System.out.println(");
        if (scanner.lineCount()) print("\"line: \"+(yyline+1)+\" \"+");
        if (scanner.columnCount()) print("\"col: \"+(yycolumn+1)+\" \"+");
        if (scanner.charCount()) print("\"char: \"+yychar+\" \"+");
        println("\"match: --\"+zzToPrintable(yytext())+\"--\");");
        print("            System.out.println(\"action [" + action.priority + "] { ");
        print(escapify(action.content));
        println(" }\");");
      }

      println("            { " + action.content);
      println("            }");
      println("          // fall through");
      println("          case " + (i++) + ": break;");
    }
  }

  private void emitEOFVal() {
    EOFActions eofActions = parser.getEOFActions();

    if (eofCode != null) println("            zzDoEOF();");

    if (eofActions.numActions() > 0) {
      println("            switch (zzLexicalState) {");

      // pick a start value for break case labels.
      // must be larger than any value of a lex state:
      int last = dfa.numStates();

      for (String name : scanner.stateNames()) {
        int num = scanner.getStateNumber(name);
        Action action = eofActions.getAction(num);

        if (action != null) {
          println("            case " + name + ": {");
          if (scanner.debugOption()) {
            print("              System.out.println(");
            if (scanner.lineCount()) print("\"line: \"+(yyline+1)+\" \"+");
            if (scanner.columnCount()) print("\"col: \"+(yycolumn+1)+\" \"+");
            if (scanner.charCount()) print("\"char: \"+yychar+\" \"+");
            println("\"match: <<EOF>>\");");
            print("              System.out.println(\"action [" + action.priority + "] { ");
            print(escapify(action.content));
            println(" }\");");
          }
          println("              " + action.content);
          println("            }  // fall though");
          println("            case " + (++last) + ": break;");
        }
      }

      println("            default:");
    }

    Action defaultAction = eofActions.getDefault();

    if (defaultAction != null) {
      println("              {");
      if (scanner.debugOption()) {
        print("                System.out.println(");
        if (scanner.lineCount()) print("\"line: \"+(yyline+1)+\" \"+");
        if (scanner.columnCount()) print("\"col: \"+(yycolumn+1)+\" \"+");
        if (scanner.charCount()) print("\"char: \"+yychar+\" \"+");
        println("\"match: <<EOF>>\");");
        print("                System.out.println(\"action [" + defaultAction.priority + "] { ");
        print(escapify(defaultAction.content));
        println(" }\");");
      }
      println("                " + defaultAction.content);
      println("              }");
    } else if (scanner.eofVal() != null) println("          { " + scanner.eofVal() + " }");
    else if (scanner.isInteger()) {
      if (scanner.tokenType() != null) {
        Out.error(ErrorMessages.INT_AND_TYPE);
        throw new GeneratorException();
      }
      println("        return YYEOF;");
    } else println("        return null;");

    if (eofActions.numActions() > 0) println("        }");
  }

  private void findActionStates() {
    isTransition = new boolean[dfa.numStates()];

    for (int i = 0; i < dfa.numStates(); i++) {
      char j = 0;
      while (!isTransition[i] && j < dfa.numInput())
        isTransition[i] = dfa.table(i, j++) != DFA.NO_TARGET;
    }
  }

  private void reduceColumns() {
    colMap = new int[dfa.numInput()];
    colKilled = new boolean[dfa.numInput()];

    int i, j, k;
    int translate = 0;
    boolean equal;

    numCols = dfa.numInput();

    for (i = 0; i < dfa.numInput(); i++) {

      colMap[i] = i - translate;

      for (j = 0; j < i; j++) {

        // test for equality:
        k = -1;
        equal = true;
        while (equal && ++k < dfa.numStates()) equal = dfa.table(k, i) == dfa.table(k, j);

        if (equal) {
          translate++;
          colMap[i] = colMap[j];
          colKilled[i] = true;
          numCols--;
          break;
        } // if
      } // for j
    } // for i
  }

  private void reduceRows() {
    rowMap = new int[dfa.numStates()];
    rowKilled = new boolean[dfa.numStates()];

    int i, j, k;
    int translate = 0;
    boolean equal;

    // i is the state to add to the new table
    for (i = 0; i < dfa.numStates(); i++) {

      rowMap[i] = i - translate;

      // check if state i can be removed (i.e. already
      // exists in entries 0..i-1)
      for (j = 0; j < i; j++) {

        // test for equality:
        k = -1;
        equal = true;
        while (equal && ++k < dfa.numInput()) equal = dfa.table(i, k) == dfa.table(j, k);

        if (equal) {
          translate++;
          rowMap[i] = rowMap[j];
          rowKilled[i] = true;
          break;
        } // if
      } // for j
    } // for i
  }

  /** Set up EOF code section according to scanner.eofcode */
  private void setupEOFCode() {
    if (scanner.eofclose()) {
      eofCode = LexScan.conc(scanner.eofCode(), "  yyclose();");
      eofThrow = LexScan.concExc(scanner.eofThrow(), "java.io.IOException");
    } else {
      eofCode = scanner.eofCode();
      eofThrow = scanner.eofThrow();
    }
  }

  /**
   * Emit {@code yychar}, {@code yycolumn}, {@code zzAtBOL}, {@code zzEOFDone} with warning
   * suppression when needed.
   */
  private void emitVarDefs() {
    // We can't leave out these declarations completely, even if unused, because
    // the reset functions in the skeleton refer to them. They are written to,
    // but not read. Only other option would be to pull these out of the skeleton
    // as well.

    println("  /** Number of newlines encountered up to the start of the matched text. */");
    if (!scanner.lineCount()) {
      println("  @SuppressWarnings(\"unused\")");
    }
    println("  private int yyline;");
    println();
    println(
        "  /** Number of characters from the last newline up to the start of the matched text. */");
    if (!scanner.columnCount()) {
      println("  @SuppressWarnings(\"unused\")");
    }
    println("  private int yycolumn;");
    println();
    println("  /** Number of characters up to the start of the matched text. */");
    if (!scanner.charCount()) {
      println("  @SuppressWarnings(\"unused\")");
    }
    println("  private long yychar;");
    println();
    println("  /** Whether the scanner is currently at the beginning of a line. */");
    if (!scanner.bolUsed()) {
      println("  @SuppressWarnings(\"unused\")");
    }
    println("  private boolean zzAtBOL = true;");
    println();
    println("  /** Whether the user-EOF-code has already been executed. */");
    if (eofCode == null) {
      println("  @SuppressWarnings(\"unused\")");
    }
    println("  private boolean zzEOFDone;");
    println();
  }

  /** Main Emitter method. */
  public void emit() {
    String functionName = (scanner.functionName() != null) ? scanner.functionName() : "yylex";

    setupEOFCode();

    reduceColumns();
    findActionStates();

    emitHeader();
    emitUserCode();
    emitClassName();

    skel.emitNext();

    println("  private static final int ZZ_BUFFERSIZE = " + scanner.bufferSize() + ";");

    if (scanner.debugOption()) {
      println("  private static final String ZZ_NL = System.getProperty(\"line.separator\");");
    }

    skel.emitNext();

    emitLexicalStates();

    emitCharMapTables();

    emitActionTable();

    reduceRows();

    emitRowMapArray();

    emitDynamicInit();

    skel.emitNext();

    emitAttributes();

    skel.emitNext();

    emitLookBuffer();

    emitVarDefs();

    emitClassCode();

    skel.emitNext();

    emitConstructorDecl();

    if (scanner.debugOption()) {
      println("");
      println("  private static String zzToPrintable(String str) {");
      println("    StringBuilder builder = new StringBuilder();");
      println("    for (int n = 0 ; n < str.length() ; ) {");
      println("      int ch = str.codePointAt(n);");
      println("      int charCount = Character.charCount(ch);");
      println("      n += charCount;");
      println("      if (ch > 31 && ch < 127) {");
      println("        builder.append((char)ch);");
      println("      } else if (charCount == 1) {");
      println("        builder.append(String.format(\"\\\\u%04X\", ch));");
      println("      } else {");
      println("        builder.append(String.format(\"\\\\U%06X\", ch));");
      println("      }");
      println("    }");
      println("    return builder.toString();");
      println("  }");
    }

    emitTokenSizeLimit(scanner.getTokenSizeLimit());

    emitCMapAccess();

    skel.emitNext();

    emitScanError();

    skel.emitNext();

    emitDoEOF();

    skel.emitNext();

    emitLexFunctHeader(functionName);

    emitNextInput();

    emitGetRowMapNext();

    skel.emitNext();

    emitEOFVal();

    skel.emitNext();

    emitActions();

    skel.emitNext();

    emitNoMatch();

    skel.emitNext();

    emitMain(functionName);

    skel.emitNext();

    out.close();
  }
}
