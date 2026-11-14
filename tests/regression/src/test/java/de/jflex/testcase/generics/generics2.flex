/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.generics;

import java.util.Vector;
import java.util.Map;

%%

%class Generics2

%type Vector<Integer>
%ctorarg Map<Vector<Integer>, String> arg1

%init{
  names = arg1;
%init}

%{
  private final Map<Vector<Integer>, String> names;

  /** The constructor argument. */
  Map<Vector<Integer>, String> names() {
    return names;
  }

  /** The first code point of the matched text. */
  private Vector<Integer> codePoint() {
    Vector<Integer> result = new Vector<>();
    result.add(yytext().codePointAt(0));
    return result;
  }
%}

%%

[^] { return codePoint(); }
