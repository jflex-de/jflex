/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

%%

%public
%class Refill
%unicode
%type String

// the line count looks at the character after a \r, which may need a refill
%line

// small buffer, so that short inputs reach the end of the buffer
%buffer 4

%{
  /** Line of the current match, counted from 0. */
  public int line() {
    return yyline;
  }
%}

%%

// matched without reading the next character
"a"       { return yytext(); }

// needs the next character to find the end of the match
"b"+      { return yytext(); }

// ends in \r and is matched without reading the next character
"b"* \r   { return yytext(); }

[^]       { return yytext(); }
