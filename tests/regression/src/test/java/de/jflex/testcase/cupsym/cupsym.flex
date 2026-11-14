/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.cupsym;

%%

%public
%class Cupsym

%cupsym sym2
%cup
%cupdebug

%%

[a-z]+ { return new java_cup.runtime.Symbol(sym2.WORD, yytext()); }

[^] { /* do nothing */ }
