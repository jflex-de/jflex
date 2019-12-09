package edu.tum.cup2.precedences;

import java.util.List;

import edu.tum.cup2.grammar.Terminal;


/**
 * This class defines the associativity of terminals,
 * needed for defining {@link Precedences}.
 * 
 * The more left an item stands, the higher its priority.
 * 
 * @author Andreas Wenger
 */
public abstract class Associativity
{
	
	protected List<Terminal> terminals;
	
	
	/**
	 * Sets the terminals of this associativity.
	 */
	public Associativity(List<Terminal> terminals)
	{
		this.terminals = terminals;
	}
	
	
	/**
	 * Returns true, when the given terminal is part of this list.
	 */
	public boolean contains(Terminal terminal)
	{
		return terminals.contains(terminal);
	}
	

}
