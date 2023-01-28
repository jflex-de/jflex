/*
 * Copyright (C) 2022, Gerwin Klein, Régis Décamps
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java_cup.runtime.Symbol;
import jflex.core.unicode.CharClasses;
import jflex.core.unicode.ILexScan;
import jflex.core.unicode.IntCharSet;
import jflex.core.unicode.UnicodeProperties;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.scanner.LexicalStates;
import jflex.scanner.ScannerException;

public abstract class AbstractLexScan implements ILexScan {

  public enum CharSetSize {
    SEVEN_BIT,
    EIGHT_BIT,
    UNICODE
  };

  int bufferSize = 16384;

  File file;

  private final Deque<File> files = new ArrayDeque<>();

  StringBuilder userCode = new StringBuilder();

  String classCode;
  String initCode;
  String initThrow;
  String eofCode;
  String eofThrow;
  List<String> lexThrow = new ArrayList<>();
  String eofVal;
  public String scanErrorException;
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
  boolean noSuppressWarnings;

  String isImplementing;
  String isExtending;
  String className = "Yylex";
  String functionName;
  String tokenType;
  String visibility = "public";
  String tokenSizeLimit;

  List<String> ctorArgs = new ArrayList<>();
  List<String> ctorTypes = new ArrayList<>();

  LexicalStates states = new LexicalStates();

  List<Action> actions = new ArrayList<>();

  CharClasses charClasses;

  @Override
  public UnicodeProperties getUnicodeProperties() {
    return unicodeProperties;
  }

  public int getMaximumCodePoint() {
    if (unicodeProperties == null) {
      populateDefaultVersionUnicodeProperties();
    }
    return unicodeProperties.getMaximumCodePoint();
  }

  public IntCharSet getIntCharSet(String propertyValue) {
    if (unicodeProperties == null) {
      populateDefaultVersionUnicodeProperties();
    }
    return unicodeProperties.getIntCharSet(propertyValue);
  }

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

  @SuppressWarnings("UnusedException")
  void populateDefaultVersionUnicodeProperties() {
    try {
      unicodeProperties = new UnicodeProperties();
    } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
      throw new ScannerException(file, ErrorMessages.UNSUPPORTED_UNICODE_VERSION, lexLine());
    }
  }

  public void initCharClasses(CharSetSize size) {
    initCharClasses(size, null);
  }

  @SuppressWarnings("UnusedException") // Used in generated LexScan
  public void initCharClasses(CharSetSize size, String version) {
    if (charClasses != null) {
      throw new ScannerException(file, ErrorMessages.DOUBLE_CHARSET, lexLine());
    }
    if (version == null || version.length() == 0) {
      populateDefaultVersionUnicodeProperties();
    } else {
      try {
        unicodeProperties = new UnicodeProperties(version);
      } catch (UnicodeProperties.UnsupportedUnicodeVersionException e) {
        throw new ScannerException(file, ErrorMessages.UNSUPPORTED_UNICODE_VERSION, lexLine());
      }
    }
    switch (size) {
      case SEVEN_BIT:
        charClasses = new CharClasses(127, this);
        break;
      case EIGHT_BIT:
        charClasses = new CharClasses(255, this);
        break;
      case UNICODE:
        charClasses = new CharClasses(unicodeProperties.getMaximumCodePoint(), this);
        break;
    }
  }

  // Used in generated LexScan
  // ScannerException is descriptive enough
  @SuppressWarnings({"unused", "UnusedException"})
  void includeFile(String filePath) {
    File f = new File(file.getParentFile(), filePath);
    if (!f.canRead()) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, lexLine());
    }
    // check for cycle
    if (files.contains(f)) {
      throw new ScannerException(file, ErrorMessages.FILE_CYCLE, lexLine());
    }
    try {
      lexPushStream(f);
      files.push(file);
      file = f;
      Out.println("Including \"" + file + "\"");
    } catch (IOException e) {
      throw new ScannerException(file, ErrorMessages.NOT_READABLE, lexLine());
    }
  }

  @SuppressWarnings("unused") // Used in generated LexScan
  File popFile() {
    return files.pop();
  }

  public Iterable<Action> actions() {
    return actions;
  }

  public File file() {
    return file;
  }

  public String classCode() {
    return classCode;
  }

  public String initCode() {
    return initCode;
  }

  public String initThrow() {
    return initThrow;
  }

  public String eofCode() {
    return eofCode;
  }

  public String eofThrow() {
    return eofThrow;
  }

  // TODO(regisd) Return ImmutableList instead
  public List<String> lexThrow() {
    return lexThrow;
  }

  public String eofVal() {
    return eofVal;
  }

  public String scanErrorException() {
    return scanErrorException;
  }

  public String userCode() {
    return userCode.toString();
  }

  public String cupSymbol() {
    return cupSymbol;
  }

  public boolean charCount() {
    return charCount;
  }

  public boolean lineCount() {
    return lineCount;
  }

  public boolean columnCount() {
    return columnCount;
  }

  public boolean cupCompatible() {
    return cupCompatible;
  }

  public boolean cup2Compatible() {
    return cup2Compatible;
  }

  public boolean cupDebug() {
    return cupDebug;
  }

  public boolean isInteger() {
    return isInteger;
  }

  public boolean isIntWrap() {
    return isIntWrap;
  }

  public boolean isPublic() {
    return isPublic;
  }

  public boolean isFinal() {
    return isFinal;
  }

  public boolean isAbstract() {
    return isAbstract;
  }

  public boolean bolUsed() {
    return bolUsed;
  }

  public boolean standalone() {
    return standalone;
  }

  public boolean debugOption() {
    return debugOption;
  }

  public boolean eofclose() {
    return eofclose;
  }

  public String isImplementing() {
    return isImplementing;
  }

  public String isExtending() {
    return isExtending;
  }

  public String className() {
    return className;
  }

  public String functionName() {
    return functionName;
  }

  public String tokenType() {
    return tokenType;
  }

  public String visibility() {
    return visibility;
  }

  public Set<String> stateNames() {
    return states.names();
  }

  public int getStateNumber(String name) {
    return states.getNumber(name);
  }

  public int ctorArgsCount() {
    return ctorArgs.size();
  }

  public String ctorType(int i) {
    return ctorTypes.get(i);
  }

  public String ctorArg(int i) {
    return ctorArgs.get(i);
  }

  public int bufferSize() {
    return bufferSize;
  }

  public boolean noSuppressWarnings() {
    return noSuppressWarnings;
  }

  public String getTokenSizeLimit() {
    return tokenSizeLimit;
  }

  /**
   * Returns the current line number.
   *
   * @deprecated Use {@link #lexLine} directly.
   */
  @Deprecated
  public int currentLine() {
    return lexLine();
  }

  /**
   * @deprecated Use {@link #columnCount}
   */
  @SuppressWarnings("unused") // Used by generated LexScan
  @Deprecated
  public boolean isColumnCount() {
    return columnCount;
  }

  /**
   * Warn if the matched length of a Unicode escape sequence is longer than expected. Push back the
   * extra characters to be matched again.
   *
   * @param len expected Unicode escape sequence length
   */
  public void maybeWarnUnicodeMatch(int len) {
    // 2 for "\"" followed by "u" or "U" at start of match
    len += 2;
    if (lexLength() > len) {
      Out.warning(file, ErrorMessages.UNICODE_TOO_LONG, lexLine(), lexColumn() + len);
      lexPushback(lexLength() - len);
    }
  }

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract int lexLine();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract int lexColumn();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract int lexLength();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract String lexText();

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract void lexPushback(int n);

  @SuppressWarnings("WeakerAccess") // Implemented by generated LexScan
  protected abstract void lexPushStream(File f) throws IOException;
}
