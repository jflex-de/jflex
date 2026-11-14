/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include_in_rules;

%%

%unicode
%public
%class IncludeInRulesScanner
%type int

%debug

LETTER = [a-z]

%%

"abc" { return 1; }
%include extra-jflex-rules.inc.jflex
"def" { /* never matches */ return 4; }

[^] { return 0; }

<<EOF>> { return -1; }
