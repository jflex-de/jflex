package edu.tum.cup2.spec;

import static edu.tum.cup2.spec.LR1butNotLALR1.Terminals.*;
import static edu.tum.cup2.spec.LR1butNotLALR1.NonTerminals.*;


/**
 * Das ist eine Grammtik, die zwar LR1, aber nicht LALR1 ist.
 * 
 * @author Daniel Altmann
 */
public class LR1butNotLALR1 extends CUP2Specification
{


	public enum Terminals implements edu.tum.cup2.grammar.Terminal
	{
		a, b, e;
	}
	
	
	public enum NonTerminals implements edu.tum.cup2.grammar.NonTerminal
	{
		S, E, F;
	}

	public LR1butNotLALR1() {
		grammar(
			prod(S, rhs(a, E, a)),
			prod(S, rhs(b, E, b)),
			prod(S, rhs(a, F, b)),
			prod(S, rhs(b, F, a)),
			prod(E, rhs(e)),
			prod(F, rhs(e))
		);
	}

}