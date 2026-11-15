/*
 * Copyright 2026, Gerwin Klein
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.refill;

%%

%public
%class RefillLimit
%unicode
%type String

// the buffer has this size from the start and cannot grow
%token_size_limit 4

%%

// same rules as refill.flex

"a"       { return yytext(); }

"b"+      { return yytext(); }

"b"* \r   { return yytext(); }

[^]       { return yytext(); }
