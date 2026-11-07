package edu.tum.cup2.parser.exceptions;

import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.scanner.ScannerToken;


/**
 * 
 * This exception indicates, that the input-stream ended too early.
 * 
 * It occurs, if the {@link edu.tum.cup2.scanner.Scanner Scanner} returns the special token EndOfInputStream, while the
 * parser expects
 * some token as defined in the grammar.
 * 
 **/

public class EndOfInputstreamException extends LRParserException
{
	
	/**  */
	private static final long serialVersionUID = -3222445215359651671L;
	
	private final LRParserState state;
	private final ScannerToken<? extends Object> token;
	
	
	/**
	 * Creates a new {@link EndOfInputstreamException}.
	 * @param message a message
	 * @param state the current state of the parser
	 * @param terminal the terminal that was read
	 */
	public EndOfInputstreamException(String message, LRParserState state, ScannerToken<? extends Object> token)
	{
		super(message + " State: " + state.getID() + ", Token: " + token);
		this.state = state;
		this.token = token;
	}
	
	
	public ScannerToken<? extends Object> getToken()
	{
		return token;
	}
	
	
	public LRParserState getState()
	{
		return state;
	}
}
