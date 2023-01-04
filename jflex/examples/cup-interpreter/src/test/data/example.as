// Copyright (C) 2001, Gerwin Klein <lsf@jflex.de>, Bernhard Rumpe <rumpe@in.tum.de>
// SPDX-License-Identifier: BSD-3-Clause

// example program for interpreter testing
// contains division and modulo functions

input a,b
functions div(x,y) = if x < y
                     then 0
                     else div(x-y,y)+1
                     fi,
          mod(x,y) = if x < y
                     then x
                     else mod(x-y,y)
                     fi
output div(a,b), mod(a,b)
arguments 324, 17
end
