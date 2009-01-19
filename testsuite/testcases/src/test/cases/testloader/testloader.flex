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
  private StringBuilder buffer = new StringBuilder();
  private List<String> cmdLine;
  private List<Integer> lineList;
%}

NL = \r | \n | \r\n

%%

<YYINITIAL> {
  "name: " [^\r\n]*   { /* test.setTestName(yytext().substring(6).trim()); */ }

  "description:"      { yybegin(DESCR); }

  "jflex: "           { cmdLine = new ArrayList<String>(); yybegin(JFLEXCMD); }
  "javac: "           { cmdLine = new ArrayList<String>(); yybegin(JAVACCMD); }

  "jflex-fail:" " "+ "true"  { /* test.setExpectJFlexFail(true); */ }
  "jflex-fail:" " "+ "false" { /* test.setExpectJFlexFail(false); */ }

  "jflex-diff:" " "+  { lineList = new ArrayList<Integer>(); 
                        /* test.setJFlexDiff(lineList); */
                        /* yybegin(LINELIST); */
                      }

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
  [^ \t\r\n]+         { cmdLine.add(yytext()); }
  \" ~\"              { cmdLine.add(yytext().substring(1,yylength()-1)); 
                        /* quoted cmdline options */ } 
  [ \t]+              { /* ignore whitespace */ }
  \\[ \t]+{NL}        { /* allow line continuation with \ */ }
}

<JFLEXCMD>
  {NL}                { /* test.setJflexCmdln(cmdLine); */ yybegin(YYINITIAL); }

<JAVACCMD>
  {NL}                { /* test.setJavacCmdln(cmdLine); */ yybegin(YYINITIAL); }

<LINELIST> {
  [0-9]+              { /* lineList.add(new Integer(yytext())); */ }
  [ \t]+              { }
  {NL}                { yybegin(YYINITIAL); }
}

<<EOF>>               { return 1; }

[^]   { throw new Error("Illegal character: ["+yytext()+"]"); }
