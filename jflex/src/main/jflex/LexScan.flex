/*
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jflex.core;

import java_cup.runtime.Symbol;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import jflex.core.unicode.IntCharSet;
import jflex.l10n.ErrorMessages;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.performance.Timer;
import jflex.scanner.ScannerException;
import jflex.skeleton.Skeleton;

%%

%final
%public
%class LexScan
%extends AbstractLexScan
%implements java_cup.runtime.Scanner
%function next_token

%type Symbol
%unicode

%column
%line

%eofclose

%state COMMENT, STATELIST, MACROS, REGEXPSTART
%state REGEXP, JAVA_CODE, STATES, STRING_CONTENT
%state CHARCLASS, COPY, REPEATEXP, EATWSPNL
%state CTOR_ARG, REGEXP_CODEPOINT_SEQUENCE
%state STRING_CODEPOINT_SEQUENCE, CHARCLASS_CODEPOINT

%cupdebug

%{

  int balance = 0;
  int commentbalance = 0;
  int action_line = 0;
  StringBuilder actionText = new StringBuilder();

  boolean isYYEOF;
  boolean notUnix;
  boolean caseless;
  boolean inclusive_states;

  int nextState;

  boolean macroDefinition;

  Timer t = new Timer();

  @Override
  protected int lexLine() {
    return yyline;
  }

  @Override
  protected int lexColumn() {
    return yycolumn;
  }

  @Override
  protected String lexText() {
    return yytext();
  }

  @Override
  protected int lexLength() {
    return yylength();
  }

  @Override
  protected void lexPushback(int n) {
    yypushback(n);
  }

  @Override
  protected void lexPushStream(File f) throws IOException {
    // yypushStream in skeleton.nested
    yypushStream(Files.newBufferedReader(f.toPath(), Options.encoding));
  }
%}

%init{
  states.insert("YYINITIAL", true);
%init}


Digit      = [0-9]
HexDigit   = [0-9a-fA-F]
OctDigit   = [0-7]

Number     = {Digit}+
HexNumber  = \\ x {HexDigit} {2}
OctNumber  = \\ [0-3]? {OctDigit} {1, 2}

// we want 099 to be an error, because it is an invalid octal literal
NumLiteral = [1-9]{Digit}* | "0x" {HexDigit}+ | "0" {OctDigit}+

// Unicode4 can encode chars only in the BMP with the 16 bits provided by its
// 4 hex digits.
// Match and warn for Unicode escapes with too many digits -- it's legal syntax,
// but likely a typo.
Unicode4  = \\ u {HexDigit} {4} {HexDigit}*

// Unicode6 can encode all Unicode chars, both in the BMP and in the
// supplementary planes -- only 21 bits are required as of Unicode 5.0,
// but its six hex digits provide 24 bits.
// Match and warn for Unicode escapes with too many digits -- it's legal syntax,
// but likely a typo.
Unicode6  = \\ U {HexDigit} {6} {HexDigit}*

// see http://www.unicode.org/unicode/reports/tr18/
WSP        = [ \t\b]
WSPNL      = [\u2028\u2029\u000A\u000B\u000C\u000D\u0085\t\b\ ]
NWSPNL     = [^\u2028\u2029\u000A\u000B\u000C\u000D\u0085\t\b\ ]
NL         = [\u2028\u2029\u000A\u000B\u000C\u000D\u0085] | \u000D\u000A
NNL        = [^\u2028\u2029\u000A\u000B\u000C\u000D\u0085]

Ident      = {IdentStart} {IdentPart}*
QualIdent  = {Ident} ( {WSP}* "." {WSP}* {Ident} )*
QUIL       = {QualIdent} ( {WSP}* "," {WSP}* {QualIdent} )*
Array      = "[" {WSP}* "]"
ParamPart  = {IdentStart}|{IdentPart}|"<"|">"|","|{WSP}|"&"|"?"|"."
GenParam   = "<" {ParamPart}+ ">"
ClassT     = {Ident} ({WSP}* {GenParam})?
QClassT    = {QualIdent} ({WSP}* {GenParam})?
ArrType    = ({GenParam} {WSP}*)? {QClassT} ({WSP}* {Array})*

IdentStart = [:jletter:]
IdentPart  = [:jletterdigit:]

WarningIdent = [a-zA-Z_\-]+

JFlexCommentChar = [^*/]|"/"+[^*/]|"*"+[^*/]
JFlexComment = {JFlexCommentChar}+

/* Java comments */
JavaComment = {TraditionalComment}|{EndOfLineComment}
TraditionalComment = "/*"{CommentContent}\*+"/"
EndOfLineComment = "//".*{NL}

CommentContent = ([^*]|\*+[^*/])*

StringCharacter = [^\u2028\u2029\u000A\u000B\u000C\u000D\u0085\"\\]

CharLiteral = \'([^\u2028\u2029\u000A\u000B\u000C\u000D\u0085\'\\]|{EscapeSequence})\'
StringLiteral = \"({StringCharacter}|{EscapeSequence})*\"

EscapeSequence = \\[^\u2028\u2029\u000A\u000B\u000C\u000D\u0085]|\\+u{HexDigit}{4}|\\[0-3]?{OctDigit}{1,2}

/* \\(b|t|n|f|r|\"|\'|\\|[0-3]?{OctDigit}{1,2}|u{HexDigit}{4}) */

JavaRest = [^\{\}\"\'/]|"/"[^*/]
JavaCode = ({JavaRest}|{StringLiteral}|{CharLiteral}|{JavaComment})+

DottedVersion =  [1-9][0-9]*(\.[0-9]+){0,2}

%%

<YYINITIAL> {
  "%%".*{NL}?              {
                             t.start();
                             yybegin(MACROS);
                             macroDefinition = true;
                             return symbol(sym.USERCODE,userCode);
                           }
  .*{NL} | .+              { userCode.append(yytext()); }
  <<EOF>>                  { return symbol(sym.EOF); }
}

<MACROS>   ("%{"|"%init{"|"%initthrow{"|"%eof{"|"%eofthrow{"|"%yylexthrow{"|"%eofval{").*{NL}
                                     { string.setLength(0); yybegin(COPY); }
<COPY> {
  "%}".*{NL}                    { classCode = conc(classCode,string);  yybegin(MACROS);  }
  "%init}".*{NL}                { initCode = conc(initCode,string);    yybegin(MACROS);  }
  "%initthrow}".*{NL}           { initThrow = concExc(initThrow,string);  yybegin(MACROS); }
  "%eof}".*{NL}                 { eofCode = conc(eofCode,string); yybegin(MACROS); }
  "%eofthrow}".*{NL}            { eofThrow = concExc(eofThrow,string); yybegin(MACROS); }
  "%yylexthrow}".*{NL}          { lexThrow.add(string.toString()); yybegin(MACROS); }
  "%eofval}".*{NL}              { eofVal = string.toString(); yybegin(MACROS); }

  .*{NL}                        { string.append(yytext()); }

  <<EOF>>                       { throw new ScannerException(file,ErrorMessages.EOF_IN_MACROS); }
}


<MACROS> ^"%s" ("tate" "s"?)? {WSP}+   { inclusive_states = true; yybegin(STATELIST); }
<MACROS> ^"%x" ("state" "s"?)? {WSP}+  { inclusive_states = false; yybegin(STATELIST); }
<STATELIST> {
  {Ident}                             { states.insert(yytext(),inclusive_states); }
  ([\ \t]*","[\ \t]*)|([\ \t]+)       { }
  {NL}                                { yybegin(MACROS);  }
  <<EOF>>                       { throw new ScannerException(file,ErrorMessages.EOF_IN_MACROS); }
}

<MACROS> {
  "%char"                     { charCount = true;  }
  "%line"                     { lineCount = true;  }
  "%column"                   { columnCount = true; }
  "%byaccj"                   { isInteger = true;
                                if (eofVal == null)
                                  eofVal = "return 0;";
                                eofclose = true;
                              }
  "%cup2"                     { cup2Compatible = true;
                                isImplementing = concExc(isImplementing, "Scanner");
                                lineCount = true;
                                columnCount = true;
                                if (functionName == null)
                                  functionName = "readNextTerminal";
                                if (tokenType == null)
                                  tokenType = "ScannerToken<? extends Object>";
                                if (eofVal == null)
                                  eofVal = "return token(SpecialTerminals.EndOfInputStream);";
                                if (!Options.jlex) eofclose = true;
                                // %unicode:
                                initCharClasses(CharSetSize.UNICODE);
                              }
  "%cup"                      { cupCompatible = true;
                                isImplementing = concExc(isImplementing, "java_cup.runtime.Scanner");
                                if (functionName == null)
                                  functionName = "next_token";
                                if (tokenType == null)
                                  tokenType = "java_cup.runtime.Symbol";
                                if (eofVal == null)
                                  eofVal = "return new java_cup.runtime.Symbol("+cupSymbol+".EOF);";
                                if (!Options.jlex) eofclose = true;
                              }
  "%cupsym"{WSP}+{QualIdent} {WSP}*  { cupSymbol = yytext().substring(8).trim();
                                if (cupCompatible) Out.warning(ErrorMessages.CUPSYM_AFTER_CUP, yyline); }
  "%cupsym"{WSP}+{NNL}*       { throw new ScannerException(file,ErrorMessages.QUIL_CUPSYM, yyline); }
  "%cupdebug"                 { cupDebug = true; }
  "%eofclose"({WSP}+"true")?  { eofclose = true; }
  "%eofclose"({WSP}+"false")  { eofclose = false; }
  "%class"{WSP}+{ClassT} {WSP}*     { className = yytext().substring(7).trim();  }
  "%ctorarg"{WSP}+{ArrType}{WSP}+   { yybegin(CTOR_ARG); ctorTypes.add(yytext().substring(8).trim()); }
  "%function"{WSP}+{Ident} {WSP}*   { functionName = yytext().substring(10).trim(); }
  "%type"{WSP}+{ArrType} {WSP}*     { tokenType = yytext().substring(6).trim(); }
  "%integer"|"%int"           { isInteger = true;  }
  "%intwrap"                  { isIntWrap = true;  }
  "%yyeof"                    { isYYEOF = true;  }
  "%notunix"                  { notUnix = true;  }
  "%7bit"                     { initCharClasses(CharSetSize.SEVEN_BIT); }
  "%full"|"%8bit"             { initCharClasses(CharSetSize.EIGHT_BIT); }
  "%16bit"                    { initCharClasses(CharSetSize.UNICODE); }
  "%unicode"({WSP}+{DottedVersion})? { String v = yytext().substring(8).trim();
                                       initCharClasses(CharSetSize.UNICODE, v);
                                     }

  "%caseless"|"%ignorecase"   { caseless = true; }
  "%implements"{WSP}+.*       { isImplementing = concExc(isImplementing, yytext().substring(12).trim());  }
  "%extends"{WSP}+{QClassT}{WSP}* { isExtending = yytext().substring(9).trim(); }
  "%public"                   { isPublic = true; }
  "%apiprivate"               { visibility = "private"; Skeleton.makePrivate(); }
  "%final"                    { isFinal = true; }
  "%abstract"                 { isAbstract = true; }
  "%debug"                    { debugOption = true; }
  "%standalone"               { standalone = true; isInteger = true; }
  "%pack"                     { /* no-op - this is the only generation method */ }
  "%include" {WSP}+ .*        { includeFile(yytext().substring(9).trim()); }
  "%buffer" {WSP}+ {Number} {WSP}*   { bufferSize = Integer.parseInt(yytext().substring(8).trim()); }
  "%buffer" {WSP}+ {NNL}*     { throw new ScannerException(file,ErrorMessages.NO_BUFFER_SIZE, yyline); }
  "%initthrow" {WSP}+ {QUIL} {WSP}* { initThrow = concExc(initThrow,yytext().substring(11).trim()); }
  "%initthrow" {WSP}+ {NNL}*  { throw new ScannerException(file,ErrorMessages.QUIL_INITTHROW, yyline); }
  "%eofthrow"  {WSP}+ {QUIL} {WSP}*  { eofThrow = concExc(eofThrow,yytext().substring(10).trim()); }
  "%eofthrow"  {WSP}+ {NNL}*  { throw new ScannerException(file,ErrorMessages.QUIL_EOFTHROW, yyline); }
  "%yylexthrow"{WSP}+ {QUIL} {WSP}* {JavaComment}? { lexThrow.add(yytext().substring(12).trim()); }
  "%throws"    {WSP}+ {QUIL} {WSP}*  { lexThrow.add(yytext().substring(8).trim()); }
  "%yylexthrow"{WSP}+ {NNL}*  { throw new ScannerException(file,ErrorMessages.QUIL_YYLEXTHROW, yyline); }
  "%throws"    {WSP}+ {NNL}*  { throw new ScannerException(file,ErrorMessages.QUIL_THROW, yyline); }
  "%scanerror" {WSP}+ {QualIdent} {WSP}* { scanErrorException = yytext().substring(11).trim(); }
  "%scanerror" {WSP}+ {NNL}*  { throw new ScannerException(file,ErrorMessages.QUIL_SCANERROR, yyline); }

  // Java-ish style
  "%suppress" {WSP}+ "all" {WSP}* { OptionUtils.suppressAllWarnings(); }
  "%suppress" {WSP}+ {WarningIdent} {WSP}* { OptionUtils.suppressWarning(yytext().substring(10).trim()); }
  "%suppress" {WSP}+ {NNL}* { throw new ScannerException(file, ErrorMessages.NOT_A_WARNING_ID, yyline); }

  // CLI style
  "%no-warn" {WSP}+ "all" {WSP}* { OptionUtils.suppressAllWarnings(); }
  "%no-warn" {WSP}+ {WarningIdent} {WSP}* { OptionUtils.suppressWarning(yytext().substring(9).trim()); }
  "%no-warn" {WSP}+ {NNL}* { throw new ScannerException(file, ErrorMessages.NOT_A_WARNING_ID, yyline); }

  "%warn" {WSP}+ "all" {WSP}* { OptionUtils.enableAllWarnings(); }
  "%warn" {WSP}+ {WarningIdent} {WSP}* { OptionUtils.enableWarning(yytext().substring(6).trim()); }
  "%warn" {WSP}+ {NNL}* { throw new ScannerException(file, ErrorMessages.NOT_A_WARNING_ID, yyline); }

  "%no_suppress_warnings" {WSP}* { noSuppressWarnings = true; }

  "%token_size_limit" {WSP}+ ({NumLiteral} | {QualIdent}) {WSP}* {
                                tokenSizeLimit = yytext().substring(18).trim();
                              }
  "%token_size_limit" {WSP}* {NNL}* {
                                 throw new ScannerException(file, ErrorMessages.TOKEN_SIZE_LIMIT, yyline);
                              }

  {Ident}                     { return symbol(sym.IDENT, yytext()); }
  "="{WSP}*                   { yybegin(REGEXP);
                                return symbol(sym.EQUALS);
                              }

  "/*"                        { nextState = MACROS; yybegin(COMMENT); }

  {EndOfLineComment}          { }

  ^"%%" {NNL}*                { macroDefinition = false;
                                yybegin(REGEXPSTART);
                                return symbol(sym.DELIMITER);
                              }
  "%"{Ident}                  { throw new ScannerException(file,ErrorMessages.UNKNOWN_OPTION, yyline, yycolumn); }
  "%"                         { throw new ScannerException(file,ErrorMessages.UNKNOWN_OPTION, yyline, yycolumn); }
  ^{WSP}+"%"                  { Out.warning(ErrorMessages.NOT_AT_BOL, yyline); yypushback(1); }

  {WSP}+                      { }
  {NL}+                       { }
  <<EOF>>                     { if ( yymoreStreams() ) {
                                  file = popFile();
                                  yypopStream();
                                }
                                else
                                  throw new ScannerException(file,ErrorMessages.EOF_IN_MACROS);
                              }
}

<CTOR_ARG> {
  {Ident} {WSP}*   { yybegin(MACROS); ctorArgs.add(yytext().trim()); }
  [^]              { throw new ScannerException(file,ErrorMessages.CTOR_ARG,yyline,yycolumn); }
}

<REGEXPSTART> {
  ^ {WSP}* "%include" {WSP}+ .*  { includeFile(yytext().trim().substring(9).trim()); }
  {WSP}* "/*"                    { nextState = REGEXPSTART; yybegin(COMMENT); }
  {WSP}* "<"                     { yybegin(STATES); return symbol_countUpdate(sym.LESSTHAN, null); }
  {WSP}* "}"                     { return symbol_countUpdate(sym.RBRACE, null); }
  {WSP}* "//" {NNL}*             { }
  {WSP}* "<<EOF>>" {WSPNL}* "{"  { actionText.setLength(0); yybegin(JAVA_CODE);
                                   Symbol s = symbol_countUpdate(sym.EOFRULE, null);
                                   action_line = s.left+1;
                                   return s;
                                 }
  {WSP}* "<<EOF>>" {WSPNL}*/"|"  { yybegin(REGEXP); return symbol(sym.EOFRULE); }
  ^ {WSP}* {NWSPNL}              { yypushback(yylength()); yybegin(REGEXP); }
  {WSP} | {NL}                   { }
}

<STATES> {
  {Ident}                     { return symbol(sym.IDENT, yytext()); }
  ","                         { return symbol(sym.COMMA); }
  {WSPNL}+                    { }

  // "{" will be caught in REGEXP
  ">"{WSPNL}*                 { yybegin(REGEXP); return symbol(sym.MORETHAN); }

  <<EOF>>                     { throw new ScannerException(file,ErrorMessages.EOF_IN_STATES); }
}


<REGEXP> {
  "<<EOF>>" {WSPNL}+ "{"  { actionText.setLength(0);
                            yybegin(JAVA_CODE);
                            action_line = yyline+1;
                            return symbol(sym.EOFRULE);
                          }
  "<<EOF>>"               { throw new ScannerException(file,ErrorMessages.EOF_WO_ACTION); }

  {WSPNL}*"|"{WSP}*$      { if (macroDefinition) {
                              yybegin(EATWSPNL);
                              return symbol(sym.BAR);
                            }
                            else {
                              yybegin(REGEXPSTART);
                              return symbol(sym.NOACTION);
                            }
                          }

  // stategroup
  "{"            { yybegin(REGEXPSTART); return symbol(sym.LBRACE); }

  {WSPNL}*"|"    { return symbol(sym.BAR); }

  {WSPNL}*\"     { string.setLength(0); nextState = REGEXP; yybegin(STRING_CONTENT); }
  {WSPNL}*"\\u{" { string.setLength(0); yybegin(REGEXP_CODEPOINT_SEQUENCE); }
  {WSPNL}*"!"    { return symbol(sym.BANG); }
  {WSPNL}*"~"    { return symbol(sym.TILDE); }
  {WSPNL}*"("    { return symbol(sym.OPENBRACKET); }
  {WSPNL}*")"    { return symbol(sym.CLOSEBRACKET); }
  {WSPNL}*"*"    { return symbol(sym.STAR); }
  {WSPNL}*"+"    { return symbol(sym.PLUS); }
  {WSPNL}*"?"    { return symbol(sym.QUESTION); }
  {WSPNL}*"$"    { return symbol(sym.DOLLAR); }
  {WSPNL}*"^"    { bolUsed = true; return symbol(sym.HAT); }
  {WSPNL}*"."    { return symbol(sym.POINT); }
  {WSPNL}*"\\R"  { return symbol(sym.NEWLINE); }
  {WSPNL}*"["    { yybegin(CHARCLASS); return symbol(sym.OPENCLASS); }
  {WSPNL}*"/"    { return symbol(sym.LOOKAHEAD); }

  {WSPNL}* "{" {WSP}* {Ident} {WSP}* "}" { return symbol_countUpdate(sym.MACROUSE, makeMacroIdent()); }
  {WSPNL}* "{" {WSP}* {Number}   { yybegin(REPEATEXP);
                                   return symbol(sym.REPEAT,
                                                 Integer.valueOf(yytext().trim().substring(1).trim()));
                                 }

  {WSPNL}+ "{"    { actionText.setLength(0); yybegin(JAVA_CODE); action_line = yyline+1; return symbol(sym.REGEXPEND); }
  {NL}            { if (macroDefinition) { yybegin(MACROS); } return symbol(sym.REGEXPEND); }

  {WSPNL}*"/*"    { nextState = REGEXP; yybegin(COMMENT); }

  {WSPNL}*"//"{NNL}*  { }

  {WSP}+          { }

  <CHARCLASS> {
    {WSPNL}*"[:jletter:]"      { return symbol(sym.JLETTERCLASS); }
    {WSPNL}*"[:jletterdigit:]" { return symbol(sym.JLETTERDIGITCLASS); }
    {WSPNL}*"[:letter:]"       { return symbol(sym.LETTERCLASS); }
    {WSPNL}*"[:uppercase:]"    { return symbol(sym.UPPERCLASS); }
    {WSPNL}*"[:lowercase:]"    { return symbol(sym.LOWERCLASS); }
    {WSPNL}*"[:digit:]"        { return symbol(sym.DIGITCLASS); }
    {WSPNL}*"\\d"              { return symbol(sym.DIGITCLASS); }
    {WSPNL}*"\\D"              { return symbol(sym.DIGITCLASSNOT); }
    {WSPNL}*"\\s"              { return symbol(sym.WHITESPACECLASS); }
    {WSPNL}*"\\S"              { return symbol(sym.WHITESPACECLASSNOT); }
    {WSPNL}*"\\w"              { return symbol(sym.WORDCLASS); }
    {WSPNL}*"\\W"              { return symbol(sym.WORDCLASSNOT); }
    {WSPNL}*"\\p{"[^}]*"}"     { String trimmedText = yytext().trim();
                                 String propertyValue = trimmedText.substring(3,trimmedText.length()-1);
                                 IntCharSet set = getIntCharSet(propertyValue);
                                 if (null == set) {
                                   throw new ScannerException(file,ErrorMessages.INVALID_UNICODE_PROPERTY, yyline, yycolumn + 3);
                                 }
                                 return symbol(sym.UNIPROPCCLASS, propertyValue);
                               }
    {WSPNL}*"\\P{"[^}]*"}"     { String trimmedText = yytext().trim();
                                 String propertyValue = trimmedText.substring(3,trimmedText.length()-1);
                                 IntCharSet set = getIntCharSet(propertyValue);
                                 if (null == set) {
                                   throw new ScannerException(file,ErrorMessages.INVALID_UNICODE_PROPERTY, yyline, yycolumn + 3);
                                 }
                                 return symbol(sym.UNIPROPCCLASSNOT, propertyValue);
                               }
  }

  . { return symbol(sym.CHAR, yytext().codePointAt(0)); }
}

<EATWSPNL> {WSPNL}+  { yybegin(REGEXP); }


<REPEATEXP> {
  "}"          { yybegin(REGEXP); return symbol(sym.RBRACE); }
  "," {WSP}* {Number}  { return symbol(sym.REPEAT, Integer.valueOf(yytext().substring(1).trim())); }
  {WSP}+       { }

  <<EOF>>                 { throw new ScannerException(file,ErrorMessages.EOF_IN_REGEXP); }
}

<CHARCLASS> {
  "{"{Ident}"}" { return symbol(sym.MACROUSE, yytext().substring(1,yylength()-1)); }
  "["     { balance++; return symbol(sym.OPENCLASS); }
  "]"     { if (balance > 0) balance--; else yybegin(REGEXP); return symbol(sym.CLOSECLASS); }
  "^"     { return symbol(sym.HAT); }
  "-"     { return symbol(sym.DASH); }
  "--"    { return symbol(sym.DIFFERENCE); }
  "&&"    { return symbol(sym.INTERSECTION); }
  "||"    { /* union is the default operation - '||' can be ignored */ }
  "~~"    { return symbol(sym.SYMMETRICDIFFERENCE); }
  "\\u{"  { yybegin(CHARCLASS_CODEPOINT); }

  // this is a hack to keep JLex compatibilty with char class
  // expressions like [+-]
  "-]"    { yypushback(1); yycolumn--; return symbol(sym.CHAR, (int)'-'); }

  \"      { string.setLength(0); nextState = CHARCLASS; yybegin(STRING_CONTENT); }

  .       { return symbol(sym.CHAR, yytext().codePointAt(0)); }

  \n      { throw new ScannerException(file,ErrorMessages.EOL_IN_CHARCLASS,yyline,yycolumn); }

  <<EOF>> { throw new ScannerException(file,ErrorMessages.EOF_IN_REGEXP); }
}

<STRING_CONTENT> {
  \"       { yybegin(nextState); return symbol(sym.STRING, string.toString()); }
  \\\"     { string.append('\"'); }
  [^\"\\\u2028\u2029\u000A\u000B\u000C\u000D\u0085]+ { string.append(yytext()); }

  {NL}     { throw new ScannerException(file,ErrorMessages.UNTERMINATED_STR, yyline, yycolumn); }

  {HexNumber} { string.append( (char) Integer.parseInt(yytext().substring(2,yylength()), 16)); }
  {OctNumber} { string.append( (char) Integer.parseInt(yytext().substring(1,yylength()), 8)); }
  {Unicode4}  { maybeWarnUnicodeMatch(4);
                string.append( (char) Integer.parseInt(yytext().substring(2,6), 16));
              }
  {Unicode6}  { maybeWarnUnicodeMatch(6);
                int codePoint = Integer.parseInt(yytext().substring(2,8), 16);
                if (codePoint <= getMaximumCodePoint()) {
                  string.append(Character.toChars(codePoint));
                } else {
                  throw new ScannerException(file,ErrorMessages.CODEPOINT_OUT_OF_RANGE, yyline, yycolumn+2);
                }
              }

  "\\u{"      { yybegin(STRING_CODEPOINT_SEQUENCE); }

  \\b { string.append('\b'); }
  \\n { string.append('\n'); }
  \\t { string.append('\t'); }
  \\f { string.append('\f'); }
  \\r { string.append('\r'); }

  \\. { string.append(yytext().substring(1, yytext().offsetByCodePoints(1, 1))); }

  <<EOF>>     { throw new ScannerException(file,ErrorMessages.EOF_IN_STRING); }
}


<REGEXP, CHARCLASS> {
  {HexNumber} { return symbol(sym.CHAR, Integer.parseInt(yytext().substring(2,yylength()), 16)); }
  {OctNumber} { return symbol(sym.CHAR, Integer.parseInt(yytext().substring(1,yylength()), 8)); }
  {Unicode4}  { maybeWarnUnicodeMatch(4);
                return symbol(sym.CHAR, Integer.parseInt(yytext().substring(2,6), 16));
              }
  {Unicode6}  { maybeWarnUnicodeMatch(6);
                int codePoint = Integer.parseInt(yytext().substring(2,8), 16);
                if (codePoint <= getMaximumCodePoint()) {
                  return symbol(sym.CHAR, codePoint);
                } else {
                  throw new ScannerException(file,ErrorMessages.CODEPOINT_OUT_OF_RANGE, yyline, yycolumn+2);
                }
              }

  \\b { return symbol(sym.CHAR, (int)'\b'); }
  \\n { return symbol(sym.CHAR, (int)'\n'); }
  \\t { return symbol(sym.CHAR, (int)'\t'); }
  \\f { return symbol(sym.CHAR, (int)'\f'); }
  \\r { return symbol(sym.CHAR, (int)'\r'); }

  \\. { return symbol(sym.CHAR, yytext().codePointAt(1)); }
}


<JAVA_CODE> {
  "{"        { balance++; actionText.append('{'); }
  "}"        { if (balance > 0) {
                 balance--;
                 actionText.append('}');
               }
               else {
                 yybegin(REGEXPSTART);
                 Action a = new Action(actionText.toString(), action_line);
                 actions.add(a);
                 return symbol(sym.ACTION, a);
               }
             }

  {JavaCode}     { actionText.append(yytext()); }

  <<EOF>>     { throw new ScannerException(file,ErrorMessages.EOF_IN_ACTION, action_line-1); }
}

<COMMENT> {

  "/"+ "*"  { commentbalance++; }
  "*"+ "/"  { if (commentbalance > 0)
                commentbalance--;
              else
                yybegin(nextState);
            }

  {JFlexComment} { /* ignore */ }

  <<EOF>>     { throw new ScannerException(file,ErrorMessages.EOF_IN_COMMENT); }
}

<REGEXP_CODEPOINT_SEQUENCE> {
  "}"             { yybegin(REGEXP); return symbol(sym.STRING, string.toString()); }
  {HexDigit}{1,6} { int codePoint = Integer.parseInt(yytext(), 16);
                    if (codePoint <= getMaximumCodePoint()) {
                      string.append(Character.toChars(codePoint));
                    } else {
                      throw new ScannerException(file,ErrorMessages.CODEPOINT_OUT_OF_RANGE, yyline, yycolumn);
                    }
                  }
  {WSPNL}+        { }
  <<EOF>>         { throw new ScannerException(file,ErrorMessages.EOF_IN_REGEXP); }
}

<STRING_CODEPOINT_SEQUENCE> { // Specialized form: newlines disallowed, and doesn't return a symbol
  "}"             { yybegin(STRING_CONTENT); }
  {HexDigit}{1,6} { int codePoint = Integer.parseInt(yytext(), 16);
                    if (codePoint <= getMaximumCodePoint()) {
                      string.append(Character.toChars(codePoint));
                    } else {
                      throw new ScannerException(file, ErrorMessages.CODEPOINT_OUT_OF_RANGE, yyline, yycolumn);
                    }
                  }
  {NL}            { throw new ScannerException(file,ErrorMessages.UNTERMINATED_STR, yyline, yycolumn); }
  {WSP}+          { }
  <<EOF>>         { throw new ScannerException(file,ErrorMessages.EOF_IN_STRING); }
}

<CHARCLASS_CODEPOINT> { // Specialized form: only one codepoint allowed, no whitespace allowed
  {HexDigit}{1,6} "}" { int codePoint = Integer.parseInt(yytext().substring(0, yylength() - 1), 16);
                        if (codePoint <= getMaximumCodePoint()) {
                          yybegin(CHARCLASS);
                          return symbol(sym.CHAR, codePoint);
                        } else {
                          throw new ScannerException(file, ErrorMessages.CODEPOINT_OUT_OF_RANGE, yyline, yycolumn);
                        }
                      }
  <<EOF>>             { throw new ScannerException(file,ErrorMessages.EOF_IN_REGEXP); }
}

.  { throw new ScannerException(file,ErrorMessages.UNEXPECTED_CHAR, yyline, yycolumn); }
\R { throw new ScannerException(file,ErrorMessages.UNEXPECTED_NL, yyline, yycolumn); }

<<EOF>>  { if ( yymoreStreams() ) {
             file = popFile();
             yypopStream();
           }
           else {
             return symbol(sym.EOF);
           }
         }
