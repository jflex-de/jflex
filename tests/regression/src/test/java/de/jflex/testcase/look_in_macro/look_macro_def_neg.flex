/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;
%%

%class LookMacroDefNeg
%int

star = (a / b)*
ok = (a / b)
concat = c (a $)
base = (a / b) / c
look = a / (b $)
unused = ((a / b $))
bar = a | (b / c)

%%

{ok}  { return 1; }
