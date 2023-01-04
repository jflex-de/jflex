/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_macros;

%%

%public
%class Cclmacros
%int
%debug

A = [A]
NCD = [^CD]
C = [BC--{NCD}]
X = [{A}{C}]

%%

{X} { /* X */ }
[{NCD}] { /* NCD */ }

[^] { /* default */ }
