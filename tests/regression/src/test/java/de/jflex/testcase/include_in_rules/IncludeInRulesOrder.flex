/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include_in_rules;

%%

%unicode
%public
%class IncludeInRulesOrderScanner
%type int

LETTER = [a-z]

%%

"abc" { return 1; }
%include order-rules.inc.jflex
"def" { /* never matches */ return 4; }

[^] { return 0; }

<<EOF>> { return -1; }
