/*
 * Copyright (C) 2017 Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2009 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2018-2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.migration.testcase;

import de.jflex.migration.testcase.TestCase;
import java.util.*;

%%

%unicode
%class TestSpecScanner

%function load
%type TestCase

// %debug 

%state DESCR JFLEXCMD JAVAC_FILES LINELIST VERSION

%{
  private StringBuilder buffer = new StringBuilder();
  private TestCase test = new TestCase();
  private List<String> cmdLine;
  private List<Integer> lineList;
%}

NL = \r | \n | \r\n
DIGIT = [0-9]

%%

<YYINITIAL> {
  "name: " [^\r\n]*   { test.setTestName(yytext().substring(6).trim()); }

  "description:"      { yybegin(DESCR); }

  "jflex: "           { cmdLine = new ArrayList<String>(); yybegin(JFLEXCMD); }
  "javac-files: "     { cmdLine = new ArrayList<String>(); yybegin(JAVAC_FILES); }

  "jflex-fail:" " "+ "true"  { test.setExpectJFlexFail(true); }
  "jflex-fail:" " "+ "false" { test.setExpectJFlexFail(false); }

  "jflex-diff:" " "+  { lineList = new ArrayList<Integer>(); 
                        test.setJFlexDiff(lineList); 
                        yybegin(LINELIST); 
                      }

  "javac-fail:" " "+ "true"  { test.setExpectJavacFail(true); }
  "javac-fail:" " "+ "false" { test.setExpectJavacFail(false); }

  "javac-encoding:" [^\r\n]* { test.setJavacEncoding(yytext().substring(15).trim()); }
    
  "input-file-encoding:" [^\r\n]* { test.setInputFileEncoding(yytext().substring(20).trim()); }
  "output-file-encoding:" [^\r\n]* { test.setOutputFileEncoding(yytext().substring(21).trim()); }
  
  "common-input-file:"  [^\r\n]* { test.setCommonInputFile(yytext().substring(18).trim()); }

  "jdk:" " "*         { yybegin(VERSION); }

  {NL} | [ \t]+       { /* ignore newline and whitespace */ }
  "#" [^\r\n]*        { /* ignore comments */ }
}


<VERSION> {
  {DIGIT}+ ("." {DIGIT}+)* { test.setJavaVersion(yytext()); yybegin(YYINITIAL); }
}

<DESCR> {
  [^\r\n]+ | {NL}     { buffer.append(yytext()); }

  {NL}/[^\r\n ]*": "  { test.setDescription(buffer.toString()); yybegin(YYINITIAL); }
}


<JFLEXCMD, JAVAC_FILES> {
  [^ \t\r\n]+         { cmdLine.add(yytext()); }
  \" ~\"              { cmdLine.add(yytext().substring(1,yylength()-1)); 
                        /* quoted cmdline options */ } 
  [ \t]+              { /* ignore whitespace */ }
  \\[ \t]+{NL}        { /* allow line continuation with \ */ }
}

<JFLEXCMD>
  {NL}                { test.setJflexCmdln(cmdLine); yybegin(YYINITIAL); }

<JAVAC_FILES>
  {NL}                { test.setJavacFiles(cmdLine); yybegin(YYINITIAL); }

<LINELIST> {
  [0-9]+              { lineList.add(Integer.valueOf(yytext())); }
  [ \t]+              { }
  {NL}                { yybegin(YYINITIAL); }
}

<<EOF>>               { return test; }

[^]   { throw new RuntimeException("Illegal character: ["+yytext()+"]"); }
