/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

%%

%public
%class RefillBol
%unicode
%type String

// small buffer, so that short inputs reach the end of the buffer
%buffer 4

%%

// ends in \r and is matched without reading the next character
"b"* \r   { return yytext(); }

// to decide if this is the beginning of a line after seeing \r, the scanner
// needs to look at the character after \r, which may need a refill
^"x"      { return "^x"; }

[^]       { return yytext(); }
