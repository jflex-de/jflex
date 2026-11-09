/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.macro_exp2;

%%

MACRO = "using some " {UNDEFINED} " macro"

%%

{MACRO}  { /* some action */ }
