/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_in_macro;
%%

%class LookRuleNeg
%int

m = a / b
n = {m}
e = a $

%%

{m}*       { return 1; }
x {m}      { return 2; }
{m} | c    { return 3; }
{m} / c    { return 4; }
c / {m}    { return 5; }
(a / b)*   { return 6; }
{n}        |
{e}+       { return 7; }
[{m}]      { return 8; }
!(a $)     { return 9; }
{m}        { return 10; }
(x / y)    { return 11; }
