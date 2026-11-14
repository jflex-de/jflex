/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.genlook;

%%

%public
%class Genlook
%integer

%line
%column

%unicode

%states YYINITIAL, END

%{
  public int line() {
    return yyline;
  }

  public int column() {
    return yycolumn;
  }
%}

%%

<YYINITIAL> {
  /* normal case */
  "aa"|"a"/"a"+    { /* normal */ return 1; }

  /* lookahead may be empty */
  "bb"|"b"/"b"*    { /* empty-look */ return 2; }

  "c"              { /* blah */ return 3; }

  "c"?             { yybegin(END); /* should not fire, "c" or EOF should always precede */ return 4; }

  [^]              { return 0; }
}

<END> {
  [^]              { /* END, should never be matched */ return 5; }
}
