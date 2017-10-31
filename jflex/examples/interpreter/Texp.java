/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001       Gerwin Klein <lsf@jflex.de>                    *
 * Copyright (C) 2001       Bernhard Rumpe <rumpe@in.tum.de>               *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


/**
 * AST node for an integer expression.
 * 
 * The non terminal exp is the sum of multiple variants and
 * therefore modelled as an abstract class.
 * 
 * The interpretation function <tt>interpret</tt> is called with
 * valuations of input variables <tt>in</tt> and parameters
 * <tt>par</tt>. Before interpret can be called, pointers
 * and variable indices must be set with <tt>prepInterp</tt>.
 */ 
abstract class Texp implements AST {
  // test context conditions (DefFun,DefVar,Arity)
  abstract public void checkcontext(SymTab st);
  
  // set pointers and indices for variables and functions
  abstract public void prepInterp(SymTab st);
  
  // interpretation
  abstract public int interpret(int[] in, int[] par);
}

