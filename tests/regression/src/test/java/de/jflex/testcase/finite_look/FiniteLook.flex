/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.finite_look;

%%

%public
%class FiniteLook
%integer

%unicode

%%

// The lookahead of each rule is a choice of expressions with fixed lengths.

"a"+ / ("" | "a")             { return 1; }
"b"+ / ("b" | "")             { return 2; }
"c"+ / ("c" | "cc")           { return 3; }
"d"+ / ("dd" | "d")           { return 4; }
"e"+ / ("ee" | "" | "e")      { return 5; }

// earlier rule has priority even if lookahead is shorter, because total match length is equal
"x"+ / ("yy" | "y")  { return 6; }
"x"+ "y" / ("" | "y" | "yy")  { return 7; }

// end of line is \r | \n | \r\n and more single characters
[f\r]+$                       { return 8; }
"g"+$                         { return 9; }

// should get warning, never matches (subset of rule 7)
"x"+ "y" / ("" | "y")         { return 10; }

[^]                           { return 0; }
