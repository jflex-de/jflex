/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class Negation
%integer
%debug

%line
%column

%unicode

%%

([^]* [^a]) | !([^]* [^a])  { /* all */ }
aba   { /* should not match */ }
