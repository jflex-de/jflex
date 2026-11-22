/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_esc;

%%

%public
%class Ccl
%type Token

%line
%column
%counter_methods

// Spec test for bug #467469: strings in character classes, as in ["-_."]

%state IN_ATTRIBUTE
%state IN_TAG
%state IN_DATA
%state IN_CDATA

ALPHA=[A-Za-z]
DIGIT=[0-9]
ALPHA_NUMERIC={ALPHA}|{DIGIT}
WHITE_SPACE=([\ \n\r\t\f])+

%%

<YYINITIAL> {WHITE_SPACE} { }
<YYINITIAL> "<" { yybegin(IN_TAG); return Token.FIRST_TAG_OPEN; }
<IN_TAG> {WHITE_SPACE} { }
<IN_TAG> "![CDATA[" { yybegin(IN_CDATA); return Token.CDATA_START; }
<IN_TAG> ({ALPHA_NUMERIC}|["-_."])+ { return Token.NAME; }
<IN_TAG> "=" { return Token.EQ; }
<IN_TAG> "\"" { yybegin(IN_ATTRIBUTE); return Token.QUOTE_OPEN; }
<IN_TAG> "/" { return Token.SLASH; }
<IN_TAG> ">" { yybegin(IN_DATA); return Token.TAG_CLOSE; }

<IN_ATTRIBUTE> ([^\"])* { return Token.VALUE; }
<IN_ATTRIBUTE> "\"" { yybegin(IN_TAG); return Token.QUOTE_CLOSE; }

<IN_DATA> ([^<])* { return Token.DATA; }
<IN_DATA> "<" { yybegin(IN_TAG); return Token.TAG_OPEN; }

<IN_CDATA> "]]>" { yybegin(IN_DATA); return Token.CDATA_END; }
<IN_CDATA> ([^"]>"]|"]"[^"]"]|[^"]"]">"|"]]"[^">"]|[^"]"]"]>")* { return Token.CDATA; }

// Shows what the rules above do not match
[^] { return Token.OTHER; }
