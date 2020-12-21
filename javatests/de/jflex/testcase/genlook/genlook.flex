package de.jflex.testcase.genlook;

%%

%public
%class Genlook
%integer
%debug

%line
%column

%unicode

%states YYINITIAL, END

%%

<YYINITIAL> {    
  /* normal case */
  "aa"|"a"/"a"+    { /* normal */ }

  /* lookahead may be empty */
  "bb"|"b"/"b"*    { /* empty-look */ }

  "c"              { /* blah */ }

  "c"?             { yybegin(END); /* should not fire, "c" or EOF should always precede */ }

  [^]              { }
}

<END> {
  [^]              { /* END, should never be matched */ }
}
