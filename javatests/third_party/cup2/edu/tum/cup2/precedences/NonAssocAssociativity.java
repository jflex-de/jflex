package edu.tum.cup2.precedences;

import java.util.List;

import edu.tum.cup2.grammar.Terminal;

/**
 * NonAssocAssociativity
 * represents the non associative associativity in cup2.
 * Just like in cup, if a terminal is declared as nonassoc, 
 * then two consecutive occurrences of equal precedence non-associative 
 * terminals generates an error.
 * 
 * @author Michael Hausmann
 */
public class NonAssocAssociativity extends Associativity
{

	/**
	 * creates a nonassociative associativity for the given list of {@link Terminal}s.
	 * @param terminals
	 */
	public NonAssocAssociativity(List<Terminal> terminals) {
		super(terminals);
	}

}
