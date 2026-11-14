/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

%%

%class Generics<T>

%type T

%{
  /** Returned by the rule below. Set by the test class. */
  T token;
%}

%%

[^] { return token; }
