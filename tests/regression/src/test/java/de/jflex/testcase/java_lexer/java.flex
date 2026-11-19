/*
 * Copyright 1998-2018, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: GPL-2.0-only
 */

/*
 * Cut-down Java 1.2 lexer: an integration test on a scanner of the size of a programming
 * language. No Unicode escapes outside of literals.
 */

package de.jflex.testcase.java_lexer;

import static de.jflex.testcase.java_lexer.Token.*;

%%

%public
%class JavaLexer
%type Token

%unicode

%line
%column
%char

%{
  private final StringBuilder string = new StringBuilder();

  private Object value;

  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }

  /** Column of the current match, counted from 0. */
  public int column() {
    return yycolumn;
  }

  /** Offset of the current match in the input, in chars. */
  public long offset() {
    return yychar;
  }

  /** Value of the last token if it was a literal or an identifier, otherwise null. */
  public Object value() {
    return value;
  }

  private Token symbol(Token type) {
    return symbol(type, null);
  }

  private Token symbol(Token type, Object value) {
    this.value = value;
    return type;
  }

  /**
   * assumes correct representation of a long value for
   * specified radix in scanner buffer from <code>start</code>
   * to <code>end</code>
   */
  private long parseLong(int start, int end, int radix) {
    long result = 0;
    long digit;

    for (int i = start; i < end; i++) {
      digit  = Character.digit(yycharat(i),radix);
      result*= radix;
      result+= digit;
    }

    return result;
  }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

WhiteSpace = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} |
          {DocumentationComment}

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/*" "*"+ [^/*] ~"*/"

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

/* integer literals */
DecIntegerLiteral = (0 | [1-9][0-9]*)
DecLongLiteral    = {DecIntegerLiteral} [lL]

HexIntegerLiteral = 0 [xX] 0* {HexDigit} {1,8}
HexLongLiteral    = 0 [xX] 0* {HexDigit} {1,16} [lL]
HexDigit          = [0-9a-fA-F]

OctIntegerLiteral = 0+ [1-3]? {OctDigit} {1,15}
OctLongLiteral    = 0+ 1? {OctDigit} {1,21} [lL]
OctDigit          = [0-7]

/* floating point literals */
FloatLiteral  = ({FLit1}|{FLit2}|{FLit3}) {Exponent}? [fF]
DoubleLiteral = ({FLit1}|{FLit2}|{FLit3}) {Exponent}?

FLit1    = [0-9]+ \. [0-9]*
FLit2    = \. [0-9]+
FLit3    = [0-9]+
Exponent = [eE] [+-]? [0-9]+

/* string and character literals */
StringCharacter = [^\r\n\"\\]
SingleCharacter = [^\r\n\'\\]

%state STRING, CHARLITERAL

%%

<YYINITIAL> {

  /* keywords */
  "abstract"                     { return symbol(ABSTRACT); }
  "boolean"                      { return symbol(BOOLEAN); }
  "break"                        { return symbol(BREAK); }
  "byte"                         { return symbol(BYTE); }
  "case"                         { return symbol(CASE); }
  "catch"                        { return symbol(CATCH); }
  "char"                         { return symbol(CHAR); }
  "class"                        { return symbol(CLASS); }
  "const"                        { return symbol(CONST); }
  "continue"                     { return symbol(CONTINUE); }
  "do"                           { return symbol(DO); }
  "double"                       { return symbol(DOUBLE); }
  "else"                         { return symbol(ELSE); }
  "extends"                      { return symbol(EXTENDS); }
  "final"                        { return symbol(FINAL); }
  "finally"                      { return symbol(FINALLY); }
  "float"                        { return symbol(FLOAT); }
  "for"                          { return symbol(FOR); }
  "default"                      { return symbol(DEFAULT); }
  "implements"                   { return symbol(IMPLEMENTS); }
  "import"                       { return symbol(IMPORT); }
  "instanceof"                   { return symbol(INSTANCEOF); }
  "int"                          { return symbol(INT); }
  "interface"                    { return symbol(INTERFACE); }
  "long"                         { return symbol(LONG); }
  "native"                       { return symbol(NATIVE); }
  "new"                          { return symbol(NEW); }
  "goto"                         { return symbol(GOTO); }
  "if"                           { return symbol(IF); }
  "public"                       { return symbol(PUBLIC); }
  "short"                        { return symbol(SHORT); }
  "super"                        { return symbol(SUPER); }
  "switch"                       { return symbol(SWITCH); }
  "synchronized"                 { return symbol(SYNCHRONIZED); }
  "package"                      { return symbol(PACKAGE); }
  "private"                      { return symbol(PRIVATE); }
  "protected"                    { return symbol(PROTECTED); }
  "transient"                    { return symbol(TRANSIENT); }
  "return"                       { return symbol(RETURN); }
  "void"                         { return symbol(VOID); }
  "static"                       { return symbol(STATIC); }
  "while"                        { return symbol(WHILE); }
  "this"                         { return symbol(THIS); }
  "throw"                        { return symbol(THROW); }
  "throws"                       { return symbol(THROWS); }
  "try"                          { return symbol(TRY); }
  "volatile"                     { return symbol(VOLATILE); }
  "strictfp"                     { return symbol(STRICTFP); }

  /* boolean literals */
  "true"                         { return symbol(BOOLEAN_LITERAL, Boolean.valueOf(true)); }
  "false"                        { return symbol(BOOLEAN_LITERAL, Boolean.valueOf(false)); }

  /* null literal */
  "null"                         { return symbol(NULL_LITERAL); }


  /* separators */
  "("                            { return symbol(LPAREN); }
  ")"                            { return symbol(RPAREN); }
  "{"                            { return symbol(LBRACE); }
  "}"                            { return symbol(RBRACE); }
  "["                            { return symbol(LBRACK); }
  "]"                            { return symbol(RBRACK); }
  ";"                            { return symbol(SEMICOLON); }
  ","                            { return symbol(COMMA); }
  "."                            { return symbol(DOT); }

  /* operators */
  "="                            { return symbol(EQ); }
  ">"                            { return symbol(GT); }
  "<"                            { return symbol(LT); }
  "!"                            { return symbol(NOT); }
  "~"                            { return symbol(COMP); }
  "?"                            { return symbol(QUESTION); }
  ":"                            { return symbol(COLON); }
  "=="                           { return symbol(EQEQ); }
  "<="                           { return symbol(LTEQ); }
  ">="                           { return symbol(GTEQ); }
  "!="                           { return symbol(NOTEQ); }
  "&&"                           { return symbol(ANDAND); }
  "||"                           { return symbol(OROR); }
  "++"                           { return symbol(PLUSPLUS); }
  "--"                           { return symbol(MINUSMINUS); }
  "+"                            { return symbol(PLUS); }
  "-"                            { return symbol(MINUS); }
  "*"                            { return symbol(MULT); }
  "/"                            { return symbol(DIV); }
  "&"                            { return symbol(AND); }
  "|"                            { return symbol(OR); }
  "^"                            { return symbol(XOR); }
  "%"                            { return symbol(MOD); }
  "<<"                           { return symbol(LSHIFT); }
  ">>"                           { return symbol(RSHIFT); }
  ">>>"                          { return symbol(URSHIFT); }
  "+="                           { return symbol(PLUSEQ); }
  "-="                           { return symbol(MINUSEQ); }
  "*="                           { return symbol(MULTEQ); }
  "/="                           { return symbol(DIVEQ); }
  "&="                           { return symbol(ANDEQ); }
  "|="                           { return symbol(OREQ); }
  "^="                           { return symbol(XOREQ); }
  "%="                           { return symbol(MODEQ); }
  "<<="                          { return symbol(LSHIFTEQ); }
  ">>="                          { return symbol(RSHIFTEQ); }
  ">>>="                         { return symbol(URSHIFTEQ); }

  /* string literal */
  \"                             { yybegin(STRING); string.setLength(0); }

  /* character literal */
  \'                             { yybegin(CHARLITERAL); }

  /* numeric literals */
  "-2147483648"                  { return symbol(INTEGER_LITERAL, Integer.valueOf(Integer.MIN_VALUE)); }

  {DecIntegerLiteral}            { return symbol(INTEGER_LITERAL, Integer.valueOf(yytext())); }
  {DecLongLiteral}               { return symbol(INTEGER_LITERAL, Long.valueOf(yytext().substring(0,yylength()-1))); }

  {HexIntegerLiteral}            { return symbol(INTEGER_LITERAL, Integer.valueOf((int) parseLong(2, yylength(), 16))); }
  {HexLongLiteral}               { return symbol(INTEGER_LITERAL, Long.valueOf(parseLong(2, yylength()-1, 16))); }

  {OctIntegerLiteral}            { return symbol(INTEGER_LITERAL, Integer.valueOf((int) parseLong(0, yylength(), 8))); }
  {OctLongLiteral}               { return symbol(INTEGER_LITERAL, Long.valueOf(parseLong(0, yylength()-1, 8))); }

  {FloatLiteral}                 { return symbol(FLOATING_POINT_LITERAL, Float.valueOf(yytext().substring(0,yylength()-1))); }
  {DoubleLiteral}                { return symbol(FLOATING_POINT_LITERAL, Double.valueOf(yytext())); }
  {DoubleLiteral}[dD]            { return symbol(FLOATING_POINT_LITERAL, Double.valueOf(yytext().substring(0,yylength()-1))); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }

  /* identifiers */
  {Identifier}                   { return symbol(IDENTIFIER, yytext()); }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return symbol(STRING_LITERAL, string.toString()); }

  {StringCharacter}+             { string.append( yytext() ); }

  /* escape sequences */
  "\\b"                          { string.append( '\b' ); }
  "\\t"                          { string.append( '\t' ); }
  "\\n"                          { string.append( '\n' ); }
  "\\f"                          { string.append( '\f' ); }
  "\\r"                          { string.append( '\r' ); }
  "\\u"[0-9a-fA-F]{4}            { string.append( yytext() ); /* fudge unicode escaped for here */ }
  "\\\""                         { string.append( '\"' ); }
  "\\'"                          { string.append( '\'' ); }
  "\\\\"                         { string.append( '\\' ); }
  \\[0-3]?{OctDigit}?{OctDigit}  { char val = (char) Integer.parseInt(yytext().substring(1),8);
                                   string.append( val ); }

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated string at end of line"); }
}

<CHARLITERAL> {
  {SingleCharacter}\'            { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf(yytext().charAt(0))); }

  /* escape sequences */
  "\\b"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\b'));}
  "\\t"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\t'));}
  "\\n"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\n'));}
  "\\f"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\f'));}
  "\\r"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\r'));}
  "\\u"[0-9a-fA-F]{4}\'          { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('u')); /* fudge unicode escaped for here */ }
  "\\\""\'                       { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\"'));}
  "\\'"\'                        { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\''));}
  "\\\\"\'                       { yybegin(YYINITIAL); return symbol(CHARACTER_LITERAL, Character.valueOf('\\')); }
  \\[0-3]?{OctDigit}?{OctDigit}\' { yybegin(YYINITIAL);
                                    int val = Integer.parseInt(yytext().substring(1,yylength()-1),8);
                                  return symbol(CHARACTER_LITERAL, Character.valueOf((char)val)); }

  /* error cases */
  \\.                            { throw new RuntimeException("Illegal escape sequence \""+yytext()+"\""); }
  {LineTerminator}               { throw new RuntimeException("Unterminated character literal at end of line"); }
}

/* error fallback */
.|\n                             { throw new RuntimeException("Illegal character \""+yytext()+
                                                              "\" at line "+yyline+", column "+yycolumn); }
<<EOF>>                          { return symbol(EOF); }
