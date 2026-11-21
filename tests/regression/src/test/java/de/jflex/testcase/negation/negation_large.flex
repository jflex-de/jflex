/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class NegationLarge
%integer

%unicode

// "abcdefgh" 6000 times. In nested macros, because a repetition is a chain of that length in the
// expression, and one of 6000 can be too deep for the stack.
A = "abcdefgh"{10}
B = {A}{10}
C = {B}{10}
D = {C}{6}

%%

"<" !{D} ">"    { return 1; }

[^]             { return 0; }
