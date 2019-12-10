package edu.tum.cup2.precedences;

import java.util.List;

import edu.tum.cup2.grammar.Terminal;


/**
 * Left associativity of terminals.
 * 
 * @author Andreas Wenger
 */
public class LeftAssociativity
	extends Associativity
{
	

	/**
	 * Creates a left associativity for the given list of {@link Terminal}s.
	 */
	public LeftAssociativity(List<Terminal> terminals)
	{
		super(terminals);
	}
	

}
