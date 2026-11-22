/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.negation;

%%

%public
%class Negation
%integer

%line
%column
%counter_methods

%unicode

%%

([^]* [^a]) | !([^]* [^a])  { /* all */ return 1; }
aba   { /* should not match */ return 2; }
