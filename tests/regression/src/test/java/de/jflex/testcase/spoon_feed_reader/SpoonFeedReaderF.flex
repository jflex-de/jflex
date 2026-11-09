/*
 * Copyright (C) 2019 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2020 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.spoon_feed_reader;

%%

%unicode
%public
%class SpoonFeedReaderF
%type String

%%
// Recognize any sequence of code points; exclude surrogate chars
[^\uD800-\uDFFF]+             { System.out.println(yytext()); return yytext(); }

[^]     { System.out.printf("char: \\u%04X", yytext().charAt(0)); return null; }
