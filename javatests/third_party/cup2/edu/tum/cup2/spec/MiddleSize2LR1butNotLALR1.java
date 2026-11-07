package edu.tum.cup2.spec;

import static edu.tum.cup2.spec.MiddleSize2LR1butNotLALR1.Terminals.*;
import static edu.tum.cup2.spec.MiddleSize2LR1butNotLALR1.NonTerminals.*;


/**
 * Das ist eine Grammtik, die zwar LR1, aber nicht LALR1 ist.
 * 
 * @author Daniel Altmann
 */
public class MiddleSize2LR1butNotLALR1 extends CUP2Specification
{


	public enum Terminals implements edu.tum.cup2.grammar.Terminal
	{
		a, b, c, d, e;
	}
	
	
	public enum NonTerminals implements edu.tum.cup2.grammar.NonTerminal
	{
		Z, S, A, B;
	}

	public MiddleSize2LR1butNotLALR1() {
		grammar(
			prod(Z, rhs(S)),
			prod(S, rhs(a, A, d)),
			prod(S, rhs(b, A, e)),
			prod(S, rhs(b, B, d)),
			prod(S, rhs(a, B, e)),
			prod(A, rhs(a)),
			prod(B, rhs(a))
		);
	}

}