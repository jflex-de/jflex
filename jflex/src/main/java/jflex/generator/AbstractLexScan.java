package jflex.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java_cup.runtime.Symbol;
import jflex.core.unicode.UnicodeProperties;
import jflex.l10n.ErrorMessages;

public abstract class AbstractLexScan {

  int bufferSize = 16384;

  File file;
  private Stack<File> files = new Stack<>();

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

  StringBuilder string = new StringBuilder();

  @SuppressWarnings("WeakerAccess") // used in generated LexScan
  UnicodeProperties unicodeProperties;

  boolean charCount;
  boolean lineCount;
  boolean columnCount;
  boolean cupCompatible;
  boolean cup2Compatible;
  boolean cupDebug;
  boolean isInteger;
  boolean isIntWrap;
  boolean isPublic;
  boolean isFinal;
  boolean isAbstract;
  boolean bolUsed;
  boolean standalone;
  boolean debugOption;
  boolean eofclose;

  String isImplementing;
  String isExtending;
  String className = "Yylex";
  String functionName;
  String tokenType;
  String visibility = "public";

  List<String> ctorArgs = new ArrayList<>();
  List<String> ctorTypes = new ArrayList<>();

  LexicalStates states = new LexicalStates();

  List<Action> actions = new ArrayList<>();

  // CharClasses.init() is delayed until UnicodeProperties.init() has been called,
  // since the max char code won't be known until then.
  private CharClasses charClasses = new CharClasses();

  // TODO(regisd) Return an immutable representation of char classes
  @SuppressWarnings("unused") // Used in generated LexParse
  public CharClasses getCharClasses() {
    return charClasses;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  Symbol symbol(int type, Object value) {
    return new Symbol(type, lexLine(), lexColumn(), value);
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  Symbol symbol(int type) {
    return new Symbol(type, lexLine(), lexColumn());
  }

  /**
   * Updates line and column count to the beginning of the first non whitespace character in yytext,
   * but leaves yyline()+lexColumn() untouched.
   */
  @SuppressWarnings("unused") // Used in generated LexScan
  Symbol symbol_countUpdate(int type, Object value) {
    int lc = lexLine();
    int cc = lexColumn();
    String text = lexText();

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);

      if (c != '\n' && c != '\r' && c != ' ' && c != '\t') {
        return new Symbol(type, lc, cc, value);
      }

      if (c == '\n') {
        lc++;
        cc = 0;
      } else {
        cc++;
      }
    }

    return new Symbol(type, lexLine(), lexColumn(), value);
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  String makeMacroIdent() {
    String matched = lexText().trim();
    return matched.substring(1, matched.length() - 1).trim();
  }

  @SuppressWarnings("SameParameterValue") // Generated LexScan uses different parameters
  static String conc(Object a, Object b) {
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

  static String concExc(Object a, Object b) {
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

  UnicodeProperties getUnicodeProperties() {
    return unicodeProperties;
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  void populateDefaultVersionUnicodeProperties() {
    try {
      unicodeProperties = new UnicodeProperties();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new ScannerException(file, ErrorMessages.UNSUPPORTED_UNICODE_VERSION, lexLine());
    }
    initCharClasses();
  }

  @SuppressWarnings("WeakerAccess") // Used in generated LexScan
  void initCharClasses() {
    charClasses.init(Options.jlex ? 127 : unicodeProperties.getMaximumCodePoint(), this);
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  void includeFile(String filePath) {
    File f = new File(file.getParentFile(), filePath);
    if (!f.canRead()) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, lexLine());
    }
    // check for cycle
    if (files.search(f) > 0) {
      throw new ScannerException(file, ErrorMessages.FILE_CYCLE, lexLine());
    }
    try {
      lexPushStream(f);
      files.push(file);
      file = f;
      Out.println("Including \"" + file + "\"");
    } catch (FileNotFoundException e) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, lexLine());
    }
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  File popFile() {
    return files.pop();
  }

  /**
   * Returns the current line number.
   *
   * @deprecated Use {link #lexLine} directly.
   */
  @Deprecated
  public int currentLine() {
    return lexLine();
  }

  public String className() {
    return className;
  }

  @SuppressWarnings("unused") // Used by generated LexScan
  public boolean isColumnCount() {
    return columnCount;
  }

  public String visibility() {
    return visibility;
  }

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract int lexLine();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract int lexColumn();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract String lexText();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract void lexPushStream(File f) throws FileNotFoundException;
}
