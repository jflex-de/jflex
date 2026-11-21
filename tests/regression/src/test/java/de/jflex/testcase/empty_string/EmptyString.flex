/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.empty_string;

%%

%public
%class EmptyString
%integer

%unicode

%%

// "" as the first operand of an alternative
"<" ("" | a* b) ">"     { return 1; }

// "" as the first operand of a concatenation, directly before a negation
"(" ("" !(c+)) ")"      { return 2; }

// "" as the content of a negation
"[" !"" "]"             { return 3; }

// "" after another expression
"{" (d | "") "" "}"     { return 4; }

[^]                     { return 0; }
