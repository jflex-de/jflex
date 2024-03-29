/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.nevermatch;

%%

%class Never
%integer

%states A B

%%

<A,B> .    { /* action 1 */ }
a|b      { /* action 2 */ }

<A> .    { /* action */ }

<<EOF>>  { /* action 3 */ }

<A,B> <<EOF>> {  /* action 4 */ }

<A> <<EOF>>  { /* action 4 */ }