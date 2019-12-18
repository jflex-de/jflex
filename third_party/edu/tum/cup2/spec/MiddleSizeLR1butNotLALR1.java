package edu.tum.cup2.spec;

import static edu.tum.cup2.spec.MiddleSizeLR1butNotLALR1.Terminals.*;
import static edu.tum.cup2.spec.MiddleSizeLR1butNotLALR1.NonTerminals.*;


/**
 * Das ist eine Grammtik, die zwar LR1, aber nicht LALR1 ist.
 * 
 * @author Daniel Altmann
 */
public class MiddleSizeLR1butNotLALR1 extends CUP2Specification
{


	public enum Terminals implements edu.tum.cup2.grammar.Terminal
	{
		a, b, c, d;
	}
	
	
	public enum NonTerminals implements edu.tum.cup2.grammar.NonTerminal
	{
		S, A, B, C, D, E;
	}

	public MiddleSizeLR1butNotLALR1() {
		grammar(
			prod(S, rhs(a, A)),
			prod(A, rhs(C, a)),
			prod(B, rhs(C, c)),
			prod(C, rhs(E, E)),
			prod(S, rhs(b, B)),
			prod(A, rhs(D, b)),
			prod(B, rhs(D, a)),
			prod(D, rhs(E)),
			prod(E, rhs())
		);
	}

}