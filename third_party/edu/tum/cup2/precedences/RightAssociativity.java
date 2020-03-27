package edu.tum.cup2.precedences;

import java.util.List;

import edu.tum.cup2.grammar.Terminal;


/**
 * Right associativity of terminals.
 * 
 * @author Andreas Wenger
 */
public class RightAssociativity
	extends Associativity
{
	
	
	/**
	 * Creates a right associativity for the given list of {@link Terminal}s.
	 */
	public RightAssociativity(List<Terminal> terminals)
	{
		super(terminals);
	}
	

}
