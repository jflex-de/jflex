/*
 * Copyright 2023, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.unicode_escape_warning;

%%

%public
%class Warnings
%int

// %unicode // default

%%

// no warnings
\u0030        { return 0; }
\U000031      { return 1; }
\u{000032}    { return 2; }

"\u0033"      { return 3; }
"\U000034"    { return 4; }
"\u{000035}"  { return 5; }

// warning, and match extra characters
\u00302       { return 6; }
\u003021      { return 7; }
\U000030ab    { return 8; }

"\u00312"     { return 9; }
"\u003120"    { return 10; }
"\U000031ab"  { return 11; }
