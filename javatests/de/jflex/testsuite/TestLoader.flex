/*
 * Copyright 2019, Gerwin Klein, Régis Décamps, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testsuite;

import java.util.ArrayList;
import java.util.List;

%%

%unicode
%class TestLoader

%function load
%throws LoadException
%type SuiteCase

%state DESCR JFLEXCMD

%{
  private StringBuilder buffer = new StringBuilder();
  private SuiteCase test = new SuiteCase();
  private List<String> cmdLine;
%}

NL = \r | \n | \r\n

%%

<YYINITIAL> {
  "name: " [^\r\n]*   { test.name = yytext().substring(6).trim(); }

  "description:"      { yybegin(DESCR); }

  "jflex: "           { cmdLine = new ArrayList<String>(); yybegin(JFLEXCMD); }

  "jflex-fail:" " "+ "true"  { test.expectJFlexFail = true; }
  "jflex-fail:" " "+ "false" { test.expectJFlexFail = false; }

  "input-file-encoding:" [^\r\n]* { test.inputFileEncoding = yytext().substring(20).trim(); }
  "output-file-encoding:" [^\r\n]* { test.outputFileEncoding = yytext().substring(21).trim(); }

  "common-input-file:"  [^\r\n]* { test.commonInputFile = yytext().substring(18).trim(); }

  {NL} | [ \t]+       { /* ignore newline and whitespace */ }
  "#" [^\r\n]*        { /* ignore comments */ }
}

<DESCR> {
  [^\r\n]+ | {NL}     { buffer.append(yytext()); }

  {NL}/[^\r\n ]*": "  { test.description = buffer.toString(); yybegin(YYINITIAL); }
}

<JFLEXCMD> {
  [^ \t\r\n]+         { cmdLine.add(yytext()); }
  \" ~\"              { cmdLine.add(yytext().substring(1,yylength()-1));
                        /* quoted cmdline options */ }
  [ \t]+              { /* ignore whitespace */ }
  \\[ \t]+{NL}        { /* allow line continuation with \ */ }
  {NL}                { test.jflexArgs = cmdLine; yybegin(YYINITIAL); }
}

<<EOF>>               { return test; }

[^]   { throw new LoadException("Illegal character: ["+yytext()+"]"); }
