/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;
// file contributed by Stephen Ostermiller
%%
%class Macro
%integer

%line
%column

%{
  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }

  /** Column of the current match, counted from 0. */
  public int column() {
    return yycolumn;
  }
%}
operatorChar=([\+\-\*\/\<\>\=\~\!\@\#\%\^\&\|\`\?\$])
illegalOperator=(({operatorChar}*("--"|"/*"){operatorChar}*)|({operatorChar}+[\-\/]))
operator=(!(!({operatorChar}+)|{illegalOperator}))
%%
<YYINITIAL> (("/*"~"*/")|("--"[^\n]*)) { return 1; }
/* Separates the operators from the comments but
 * operators are only one character each
<YYINITIAL> ({operatorChar}) {
	System.out.println("Operator: " + yytext());
} */
/* Operarators are a better length but
 * comments are not found.
<YYINITIAL> ({operatorChar}+) {
	System.out.println("Operator: " + yytext());
} */
/* I get a macro error.*/
<YYINITIAL> ({operator}) { return 2; }
<YYINITIAL> [^] { return 0; }
