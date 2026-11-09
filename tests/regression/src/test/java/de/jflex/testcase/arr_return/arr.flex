/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.arr_return;

%%

%class Arr
%type int []

%%

"blub" { return new int[2]; }

