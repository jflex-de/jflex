package jflex.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.Symbol;
import jflex.core.unicode.UnicodeProperties;
import jflex.performance.Timer;

public abstract class AbstractLexScan {

  int balance = 0;
  int commentbalance = 0;
  int action_line = 0;
  int bufferSize = 16384;

  File file;
  Stack<File> files = new Stack<File>();

  StringBuilder userCode = new StringBuilder();

  String classCode;
  String initCode;
  String initThrow;
  String eofCode;
  String eofThrow;
  String lexThrow;
  String eofVal;
  String scanErrorException;
  String cupSymbol = "sym";

  StringBuilder actionText = new StringBuilder();
  StringBuilder string = new StringBuilder();

  private UnicodeProperties unicodeProperties;

  boolean charCount;
  boolean lineCount;
  boolean columnCount;
  boolean cupCompatible;
  boolean cup2Compatible;
  boolean cupDebug;
  boolean isInteger;
  boolean isIntWrap;
  boolean isYYEOF;
  boolean notUnix;
  boolean isPublic;
  boolean isFinal;
  boolean isAbstract;
  boolean bolUsed;
  boolean standalone;
  boolean debugOption;
  boolean caseless;
  boolean inclusive_states;
  boolean eofclose;
  boolean isASCII;

  String isImplementing;
  String isExtending;
  String className = "Yylex";
  String functionName;
  String tokenType;
  String visibility = "public";

  List<String> ctorArgs = new ArrayList<String>();
  List<String> ctorTypes = new ArrayList<String>();

  LexicalStates states = new LexicalStates();

  List<Action> actions = new ArrayList<Action>();

  private int nextState;

  boolean macroDefinition;

  Timer t = new Timer();

  // CharClasses.init() is delayed until UnicodeProperties.init() has been called,
  // since the max char code won't be known until then.
  private CharClasses charClasses = new CharClasses();

  public CharClasses getCharClasses() {
    return charClasses;
  }

  public int currentLine() {
    return yyline;
  }

  public void setFile(File file) {
    this.file = file;
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }

  // updates line and column count to the beginning of the first
  // non whitespace character in yytext, but leaves yyline+yycolumn
  // untouched
  private Symbol symbol_countUpdate(int type, Object value) {
    int lc = yyline;
    int cc = yycolumn;
    String text = yytext();

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);

      if (c != '\n' && c != '\r' && c != ' ' && c != '\t') {
        return new Symbol(type, lc, cc, value);
      }

      if (c == '\n') {
        lc++;
        cc = 0;
      } else cc++;
    }

    return new Symbol(type, yyline, yycolumn, value);
  }

  private String makeMacroIdent() {
    String matched = yytext().trim();
    return matched.substring(1, matched.length() - 1).trim();
  }

  public static String conc(Object a, Object b) {
    if (a == null && b == null) {
      return null;
    }
    if (a == null) {
      return b.toString();
    }
    if (b == null) {
      return a.toString();
    }

    return a.toString() + b.toString();
  }

  public static String concExc(Object a, Object b) {
    if (a == null && b == null) {
      return null;
    }
    if (a == null) {
      return b.toString();
    }
    if (b == null) {
      return a.toString();
    }

    return a.toString() + ", " + b.toString();
  }

  public UnicodeProperties getUnicodeProperties() {
    return unicodeProperties;
  }

  private void populateDefaultVersionUnicodeProperties() {
    try {
      unicodeProperties = new UnicodeProperties();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new ScannerException(file, ErrorMessages.UNSUPPORTED_UNICODE_VERSION, yyline);
    }
    charClasses.init(Options.jlex ? 127 : unicodeProperties.getMaximumCodePoint(), this);
  }

  private void includeFile(String filePath) {
    File f = new File(file.getParentFile(), filePath);
    if (!f.canRead()) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, yyline);
    }
    // check for cycle
    if (files.search(f) > 0) {
      throw new ScannerException(file, ErrorMessages.FILE_CYCLE, yyline);
    }
    try {
      yypushStream(new FileReader(f));
      files.push(file);
      file = f;
      Out.println("Including \"" + file + "\"");
    } catch (FileNotFoundException e) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, yyline);
    }
  }
}
