/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class Negation2
%integer

%line
%column

%unicode

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

// starts with a
X = a [ab]*
// ends with b
Y = [ab]* b

K = "kl" | m

%%

// intersection starts with a, ends with b
!(!{X}|!{Y})                      { return 1; }

"(" ![c] ")"                      { return 2; }
"<" [^c] ">"                      { return 3; }

// should all be the same match modulo the */+/- difference
"/*" ~"*/"                        { return 4; }
"/+" ([^+] | \++ [^+/])* \++ "/"  { return 5; }
"/-" !([^]* "-/" [^]*) "-/"       { return 6; }

"[" !{K} "]"                      { return 7; }

[^]                               { return 0; }
