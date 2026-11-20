/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.eofclose;

%%

%public
%class EofNoClose
%int

// %eofclose false turns the effect of %eofclose off again
%eofclose
%eofclose false

%%

.+  { /* ignore */ }
\n  { /* that too */ }
