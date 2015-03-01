package edu.tum.cup2.spec;

import static edu.tum.cup2.spec.WeakCompatibleLR1TestSpec.Terminals.*;
import static edu.tum.cup2.spec.WeakCompatibleLR1TestSpec.NonTerminals.*;


/**
 * Das ist eine grammtik zu der Bekant ist, wie der zugehörige Item-Automat
 * auszusehen hat. Sie sollte unter schwacher Kompatibilität einen Automaten
 * erzeugen, der nicht ganz so klein wie ein LALR1 Automat ist, aber auch nicht
 * ganz so groß wie ein LR1 Automat.
 * 
 * @author Daniel Altmann
 */
public class WeakCompatibleLR1TestSpec extends CUP2Specification
{


	public enum Terminals implements edu.tum.cup2.grammar.Terminal
	{
		a, b, c, d, e;
	}
	
	
	public enum NonTerminals implements edu.tum.cup2.grammar.NonTerminal
	{
		S_tick, A, B, C, D, E, F, X, Y, Z;
	}

	public WeakCompatibleLR1TestSpec() {
		grammar(
			prod(S_tick, rhs(a, X, b)),
			prod(S_tick, rhs(a, Y, d)),
			prod(S_tick, rhs(a, Z, a)),
			prod(S_tick, rhs(b, X, d)),
			prod(S_tick, rhs(b, Y, a)),
			prod(S_tick, rhs(b, Z, c)),
			prod(X, rhs(a, A, E)),
			prod(Y, rhs(a, B)),
			prod(Z, rhs(a, C)),
			prod(A, rhs(a, D, F)),
			prod(B, rhs(b)),
			prod(C, rhs(a, D, F)),
			prod(D, rhs(d)),
			prod(E, rhs(e)),
			prod(F, rhs(e))
		);
	}

}
