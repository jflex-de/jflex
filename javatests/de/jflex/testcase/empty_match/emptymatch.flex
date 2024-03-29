/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.empty_match;

%%

%public
%class Emptymatch

%int
%debug
%state A, B

%%

<YYINITIAL> {
    [a-zA-Z]+ "["    { yybegin(A); return 1; }
    [^]              { return 2; }
}

<A> {
    ""               { yybegin(B); return 3; }
}

<B> {
    "]"              { yybegin(YYINITIAL); return 4; }
    [^]              { return 5; }
}
