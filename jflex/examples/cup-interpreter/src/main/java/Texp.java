/*
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * AST node for an integer expression.
 *
 * <p>The non terminal exp is the sum of multiple variants and therefore modelled as an abstract
 * class.
 *
 * <p>The interpretation function {@code interpret} is called with valuations of input variables
 * {@code in} and parameters {@code par}. Before interpret can be called, pointers and variable
 * indices must be set with {@code prepInterp}.
 */
abstract class Texp implements AST {
  // test context conditions (DefFun,DefVar,Arity)
  public abstract void checkcontext(SymTab st);

  // set pointers and indices for variables and functions
  public abstract void prepInterp(SymTab st);

  // interpretation
  public abstract int interpret(int[] in, int[] par);
}
