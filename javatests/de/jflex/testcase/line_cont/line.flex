/*
 * Copyright 2020, Gerwin Klein <lsf@jflex.de>
 * SPDX-License-Identifier: BSD-3-Clause
 */

package de.jflex.testcase.line_cont;

%%

%class Line
%int

macro1 = a | b*

macro2 = b 
       | a* |
         c
         ?

%%

{macro2}    |

{macro1}
| {macro2}  { ; }

x
*  { ;; }
