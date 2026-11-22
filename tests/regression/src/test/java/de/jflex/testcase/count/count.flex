/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.count;

%%

%public
%class Count
%integer

%char
%line
%column
%counter_methods

%{
  /** Number of characters that the next action pushes back. Set by the test class. */
  public int pushback;
%}

%%

[^] | [a-z]{2,3}  { yypushback(pushback); pushback = 0; return 1; }
