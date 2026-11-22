/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;

%%

%public
%class LookInMacro
%int

%line
%column
%counter_methods

letters = [a-z]+
end     = "stop"

/* the spec in issue #1088. Lookahead of fixed length */
stopped = {letters} / {end}

/* base of fixed length, in parentheses */
fixedBase = ("ab" / [0-9]+)

/* lookahead with a finite choice of fixed lengths */
choice = [0-9]+ / ("x" | "yy")

/* general lookahead, used through a second macro */
general = [A-Z]+ / [A-Z]* "!"
viaMacro = {general}

/* end of line */
eol = "#"+ $

/* lookahead expression with end of line */
lookEol = "@"+ / ";" $

%%

{stopped}       { return 1; }
({fixedBase})   { return 2; }
{choice}        { return 3; }
{viaMacro}      { return 4; }
{eol}           { return 5; }
( ({lookEol}) ) { return 6; }
\R              { return 7; }
[^]             { return 0; }
