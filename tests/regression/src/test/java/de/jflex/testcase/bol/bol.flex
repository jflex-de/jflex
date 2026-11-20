/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.bol;

%%

%public
%class BolScanner
%type State

%line
%column

%unicode

%char
%{
  State state = State.INITIAL;

  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }

  /** Column of the current match, counted from 0. */
  public int column() {
    return yycolumn;
  }

  /** Number of characters before the current match. */
  public long offset() {
    return yychar;
  }
%}

%%

^"hello"$  { return State.HELLO_AT_BOL_AND_EOL; }
^"hello"   { return State.HELLO_AT_BOL; }
"hello"$   { return State.HELLO_AT_EOL; }
"hello"    { return State.HELLO_SIMPLY; }

\n         { return State.LINE_FEED; }

" "        { return State.SPACE; }
[^]        { return State.OTHER; }

<<EOF>>    { return State.END_OF_FILE; }
