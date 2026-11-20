/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

%%

%class Ctorarg

%function test
%int

%ctorarg int x
%ctorarg Object y

%standalone

%{
  /** The values of the %ctorarg arguments, stored by the %init code. */
  int x;
  Object y;
%}

%init{
  this.x = x;
  this.y = y;
%init}

%%

"some rule" { /* without content */ }
