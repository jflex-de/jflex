/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.charclass;

%%

BadRange = [b-a]

%%

{BadRange}	 		{ }
[^] { }
