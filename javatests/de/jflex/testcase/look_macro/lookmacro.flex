/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.look_macro;
%%
%public
%class Lookmacro
%int
%debug 

foo="<foo>"|":"
bar=([:letter:])+ 

%%

{bar}/{foo} { return 1; }
(.) { return 0; }

