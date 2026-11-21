/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class NegationNested
%integer

%unicode

%%

// 30 negations. An even number, so this is "<" "ab" ">".
"<" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"ab" ">"    { return 1; }

[^]                                           { return 0; }
