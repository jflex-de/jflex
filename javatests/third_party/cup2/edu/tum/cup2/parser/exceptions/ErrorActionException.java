package edu.tum.cup2.parser.exceptions;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.ErrorAction;
import edu.tum.cup2.parser.states.LRParserState;


/**
 * This exception is thrown when the parser tries
 * to perform an action from the action table,
 * but if the corresponding cell has an {@link ErrorAction}
 * (i.e. "is empty" like in most books about parsing).
 * 
 * @author Andreas Wenger
 */
public final class ErrorActionException
	extends LRParserException
{
	/**  */
	private static final long serialVersionUID = -4128108282482359853L;
	
	private final LRParserState state;
	private final Terminal terminal;
	
	
	/**
	 * Creates a new {@link ErrorActionException}.
	 * @param state     the current state of the parser
	 * @param terminal  the terminal that was read
	 */
	public ErrorActionException(LRParserState state, Terminal terminal)
	{
		super("Error action in action table. Table not compliant to this version of CUP2! State: " + state.getID() + ", Terminal: " + terminal);
		this.state = state;
		this.terminal = terminal;
	}
	
	
	/**
	 * Gets the current state the parser was in when the exception was created.
	 */
	public LRParserState getState()
	{
		return state;
	}


	/**
	 * Gets the terminal that was read when the exception was created.
	 */
	public Terminal getTerminal()
	{
		return terminal;
	}


}
