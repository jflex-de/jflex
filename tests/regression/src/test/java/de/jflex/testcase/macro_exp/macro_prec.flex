/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp;

%%

%class MacroPrec
%integer

%line
%column
%counter_methods

// No parentheses around the macro bodies and usages
A = a|b
D = d|e
G = g|h
P = p|q
U = u|v
S = xy

%%

{A}c          { return 1; }
{D}+          { return 2; }
{G}{2}        { return 3; }
"<" !{P} ">"  { return 4; }
"[" ~{U}      { return 5; }
{S}+          { return 6; }

[^]           { return 0; }
