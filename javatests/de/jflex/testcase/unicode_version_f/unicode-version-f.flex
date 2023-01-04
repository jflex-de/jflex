/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode_version_f;
%%
%unicode 1.29.4

%public
%class UnicodeVersionF

%%

. {
  System.out.println("Character: <" + yytext() + ">");
}

