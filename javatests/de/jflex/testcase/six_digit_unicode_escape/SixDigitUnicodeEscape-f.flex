/*
 * Copyright 2014, Steve Rowe
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.six_digit_unicode_escape;
%%

%unicode

%%

\UFFFFFF { }
[^] { }
