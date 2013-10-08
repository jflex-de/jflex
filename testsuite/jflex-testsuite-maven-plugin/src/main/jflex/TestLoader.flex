package jflextest;

import java.util.*;
import jflex.sym;

%%

%unicode
%class TestLoader

%function load
%throws LoadException
%type TestCase

// %debug 

%state DESCR JFLEXCMD JAVAC_EXTRA_FILES LINELIST

%{
  private StringBuilder buffer = new StringBuilder();
  private TestCase test = new TestCase();
  private List<String> cmdLine;
  private List<Integer> lineList;

  // public TestCase getTestCase() { return test; }
%}

NL = \r | \n | \r\n

%%

<YYINITIAL> {
  "name: " [^\r\n]*   { test.setTestName(yytext().substring(6).trim()); }

  "description:"      { yybegin(DESCR); }

  "jflex: "           { cmdLine = new ArrayList<String>(); yybegin(JFLEXCMD); }
  "javac-extra-files: " { cmdLine = new ArrayList<String>(); yybegin(JAVAC_EXTRA_FILES); }

  "jflex-fail:" " "+ "true"  { test.setExpectJFlexFail(true); }
  "jflex-fail:" " "+ "false" { test.setExpectJFlexFail(false); }

  "jflex-diff:" " "+  { lineList = new ArrayList<Integer>(); 
                        test.setJFlexDiff(lineList); 
                        yybegin(LINELIST); 
                      }

  "javac-fail:" " "+ "true"  { test.setExpectJavacFail(true); }
  "javac-fail:" " "+ "false" { test.setExpectJavacFail(false); }
    
  "input-file-encoding:" [^\r\n]* { test.setInputFileEncoding(yytext().substring(20).trim()); }
  "output-file-encoding:" [^\r\n]* { test.setOutputFileEncoding(yytext().substring(21).trim()); }
  
  "common-input-file:"  [^\r\n]* { test.setCommonInputFile(yytext().substring(18).trim()); }

  {NL} | [ \t]+       { /* ignore newline and whitespace */ }
  "#" [^\r\n]*        { /* ignore comments */ }
}


<DESCR> {
  [^\r\n]+ | {NL}     { buffer.append(yytext()); }

  {NL}/[^\r\n ]*": "  { test.setDescription(buffer.toString()); yybegin(YYINITIAL); }
}


<JFLEXCMD, JAVAC_EXTRA_FILES> {
  [^ \t\r\n]+         { cmdLine.add(yytext()); }
  \" ~\"              { cmdLine.add(yytext().substring(1,yylength()-1)); 
                        /* quoted cmdline options */ } 
  [ \t]+              { /* ignore whitespace */ }
  \\[ \t]+{NL}        { /* allow line continuation with \ */ }
}

<JFLEXCMD>
  {NL}                { test.setJflexCmdln(cmdLine); yybegin(YYINITIAL); }

<JAVAC_EXTRA_FILES>
  {NL}                { test.setJavacExtraFiles(cmdLine); yybegin(YYINITIAL); }

<LINELIST> {
  [0-9]+              { lineList.add(new Integer(yytext())); }
  [ \t]+              { }
  {NL}                { yybegin(YYINITIAL); }
}

<<EOF>>               { return test; }

[^]   { throw new LoadException("Illegal character: ["+yytext()+"]"); }
