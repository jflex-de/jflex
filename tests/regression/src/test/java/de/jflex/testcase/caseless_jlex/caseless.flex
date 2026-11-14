/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.caseless_jlex;

%%

%public
%class Caseless

%type State

%line
%column

%unicode

%ignorecase

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

NL = \r|\n|\r\n

// Cyrillic small letters ie, zhe, ze
CYRILLIC = [\u0435-\u0437]

%%

a          { return State.A; }
[a-z]+     { return State.WORD;  }
"hello"    { return State.HELLO;   }

// à to þ
[\u00E0-\u00FE]+  { return State.LATIN1; }

// class from a macro
{CYRILLIC}+       { return State.CYRILLIC; }

// negated class: everything but δ
"#" [^\u03B4]     { return State.NOT_DELTA; }

{NL}       { return State.NEW_LINE; }
.          { return State.OTHER; }

<<EOF>>    { return State.END_OF_FILE; }
