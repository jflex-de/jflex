import java.util.*;

// actions cut down to avoid compile errors

%%

%unicode
%public
%class Testloader

%function load
%throws Exception
%type int

%debug 

%state DESCR JFLEXCMD JAVACCMD LINELIST

%{
  private StringBuffer buffer = new StringBuffer();
  private Vector cmdLine;
  private Vector lineList;
%}

NL = \r | \n | \r\n

%%

<YYINITIAL> {
  "name: " [^\r\n]*   { /* test.setTestName(yytext().substring(6).trim()); */ }

  "description:"      { yybegin(DESCR); }

  "jflex: "           { cmdLine = new Vector(); yybegin(JFLEXCMD); }
  "javac: "           { cmdLine = new Vector(); yybegin(JAVACCMD); }

  "jflex-fail:" " "+ "true"  { /* test.setExpectJFlexFail(true); */ }
  "jflex-fail:" " "+ "false" { /* test.setExpectJFlexFail(false); */ }

  "jflex-diff:" " "+  { lineList = new Vector(); /* test.setJFlexDiff(lineList); */ yybegin(LINELIST); }

  "javac-fail:" " "+ "true"  { /* test.setExpectJavacFail(true); */ }
  "javac-fail:" " "+ "false" { /* test.setExpectJavacFail(false); */ }  

  {NL} | [ \t]+       { /* ignore newline and whitespace */ }
  "#" [^\r\n]*        { /* ignore comments */ }
}


<DESCR> {
  [^\r\n]+ | {NL}     { buffer.append(yytext()); }

  {NL}/[^\r\n]*": "   { /* test.setDescription(buffer.toString()); */ yybegin(YYINITIAL); }
}


<JFLEXCMD, JAVACCMD> {
  [^ \t\r\n]+         { cmdLine.addElement(yytext()); }
  \" ~\"              { cmdLine.addElement(yytext().substring(1,yylength()-1)); 
                        /* quoted cmdline options */ } 
  [ \t]+              { /* ignore whitespace */ }
  \\[ \t]+{NL}        { /* allow line continuation with \ */ }
}

<JFLEXCMD>
  {NL}                { /* test.setJflexCmdln(cmdLine); */ yybegin(YYINITIAL); }

<JAVACCMD>
  {NL}                { /* test.setJavacCmdln(cmdLine); */ yybegin(YYINITIAL); }

<LINELIST> {
  [0-9]+              { /* lineList.addElement(new Integer(yytext())); */ }
  [ \t]+              { }
  {NL}                { yybegin(YYINITIAL); }
}

<<EOF>>               { return 1; }

[^]   { throw new Error("Illegal character: ["+yytext()+"]"); }
