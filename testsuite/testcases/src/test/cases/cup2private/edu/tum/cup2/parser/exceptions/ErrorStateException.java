package edu.tum.cup2.parser.exceptions;

import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.parser.states.ErrorState;
import edu.tum.cup2.parser.states.LRParserState;


/**
 * This exception is thrown when the parser tries
 * to set a new current state from the goto table,
 * but if the corresponding cell has an {@link ErrorState} (i.e. "is empty" like in most books about parsing).
 * 
 * @author Andreas Wenger
 */
public final class ErrorStateException extends LRParserException
{
	/**  */
	private static final long serialVersionUID = -6827529790345681925L;
	
	private final LRParserState state;
	private final NonTerminal nonterminal;
	
	
	/**
	 * Creates a new {@link ErrorStateException}.
	 * @param state the current state of the parser
	 * @param nonterminal the non-terminal that was reduced to
	 */
	public ErrorStateException(LRParserState state, NonTerminal nonterminal)
	{
		super("Error state in goto table. State: " + state.getID() + ", Non-Terminal: " + nonterminal);
		this.state = state;
		this.nonterminal = nonterminal;
	}
	
	
	/**
	 * Gets the current state the parser was in when the exception was created.
	 */
	public LRParserState getState()
	{
		return state;
	}
	
	
	/**
	 * Gets the non-terminal that was reduced to when the exception was created.
	 */
	public NonTerminal getNonTerminal()
	{
		return nonterminal;
	}
	
	
}
