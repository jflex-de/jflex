/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.ccl_neg;

%%

%public
%class CCLNeg
%type int

%%

// negated char class with overlapping content
[^\n\s]+   { return 1; }

// something that intersects with the negated char class
[\n]+      { return 2; }

// default
.          { return 0; }

<<EOF>>    { return -1; }
