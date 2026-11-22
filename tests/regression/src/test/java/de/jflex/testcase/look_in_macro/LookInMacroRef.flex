/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;

%%

%public
%class LookInMacroRef
%int

%line
%column
%counter_methods

/* Same rules as in LookInMacro.flex but without lookahead macros. */

letters = [a-z]+
end     = "stop"

%%

{letters} / {end}           { return 1; }
"ab" / [0-9]+               { return 2; }
[0-9]+ / ("x" | "yy")       { return 3; }
[A-Z]+ / [A-Z]* "!"         { return 4; }
"#"+ $                      { return 5; }
"@"+ / ";" $                { return 6; }
\R                          { return 7; }
[^]                         { return 0; }
