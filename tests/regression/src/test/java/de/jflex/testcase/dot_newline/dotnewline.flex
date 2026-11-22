/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.dot_newline;

%%

%public
%class Dotnewline
%integer
%line
%column
%counter_methods
%unicode

%%

.   { return 1; }

\R  { return 2; }

<<EOF>> { return YYEOF; }

[\uD800-\uDFFF] { /* unpaired surrogate */ return 3; }

[^] { /* should never get here: every char is matched by dot (.), (\R) or the surrogates */ return 0; }
