/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eof;

import java.io.*;

%%

%public
%class Eof
%type String

%{
  /** Store end-of-file output for later test access. */
  public final java.util.List<String> events = new java.util.ArrayList<>();
%}

%eof{
  events.add("EOF");

  // should never throw, but the compiler won't know
  if (System.out == null) {
    throw new java.io.IOException("testing");
  }
%eof}

%eofthrow{
   java.io.IOException
%eofthrow}

%%

.+  { return "line"; }
\n  { return "newline"; }

<<EOF>> { events.add("<<EOF>>"); return ""; }
