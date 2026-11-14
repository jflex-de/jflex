/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.include2;
import java.io.*;

%%

%public
%class Include2
%integer
%char

%{
  /** Return value of the fallback rule. */
  public static final int OTHER = 0;
  public static final int INCLUDE = 1;
  public static final int TOKEN = 2;
  public static final int NEWLINE = 3;
  /** Return value of {@code <<EOF>>} for an included stream. */
  public static final int STREAM_END = 4;

  /** The file name of the last include, without the quotes. */
  public String included;

  /** Number of characters before the current match in the current stream. */
  public long charPos() {
    return yychar;
  }
%}

WSP   = [ \t\b]
ALPHA = [a-zA-Z]

NAMEPART = ({ALPHA}|[0-9])+

FILENAME = {NAMEPART} ("/" {NAMEPART})*

%%

"%include" {WSP}+ \" {FILENAME} \" {
  String quotedFilename = yytext().substring(9).trim();
  included = quotedFilename.substring(1, quotedFilename.length() - 1);
  yypushStream(new FileReader(included));
  return INCLUDE;
}

^ "token" {WSP}+ .    { return TOKEN; }

\n|\r\n|\r          { return NEWLINE; }

[^]                 { return OTHER; }

<<EOF>>  { if (yymoreStreams()) { yypopStream(); return STREAM_END; } else return YYEOF; }
