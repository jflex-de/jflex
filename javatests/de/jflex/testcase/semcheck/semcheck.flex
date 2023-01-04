/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.semcheck;
%%

%class Semcheck
%ignorecase

%%

"foo" / [A-Z]	{  }
~"foo" / "bar"  {  }
"bar" / !"foo"  {  }
!"foo" / "bar"  {  }

