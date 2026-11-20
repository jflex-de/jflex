/*
 * Copyright 2026, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ctorarg;

%%

%class CtorargCupdebug

%cup
%cupdebug

%ctorarg int x
%ctorarg Object y

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
