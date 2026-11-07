package edu.tum.cup2.generator.terminals;

import java.util.Set;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;


/**
 * Interface for a set of terminals.
 * 
 * For educational purposes, straightforward implementations may be created.
 * When performance matters, optimized solutions (like bit fields) may be used.
 * 
 * @author Andreas Wenger
 */
public interface TerminalSet
{
	
	
	/**
	 * Returns an empty {@link TerminalSet} for the same terminals as this one.
	 */
	public TerminalSet empty();
	
	
	/**
	 * Returns this set of terminals, with the given one added.
	 */
	public TerminalSet plus(Terminal terminal);
	
	
	/**
	 * Returns this set of terminals merged with the given one.
	 */
	public TerminalSet plusAll(TerminalSet terminals);
	
	
	/**
	 * Returns this set of terminals merged with the given one
	 * (but {@link SpecialTerminals#Epsilon} is ignored).
	 */
	public TerminalSet plusAllExceptEpsilon(TerminalSet terminals);
	
	
	/**
	 * Returns true, if the given terminal is element of this set,
	 * otherwise false.
	 */
	public boolean contains(Terminal terminal);

	
	/**
	 * Gets the set of terminals.
	 */
	public Set<Terminal> getTerminals();


}
