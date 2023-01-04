/*
 * Copyright (C) 2014 Steve Rowe <sarowe@gmail.com>
 * Copyright (C) 2021 Google, LLC.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package de.jflex.testcase.unicode_codepoint_escapes;
%%

%unicode

%%

"\u{FFFFFF}" { }
[^] { }
