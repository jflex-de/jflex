package edu.tum.cup2.parser.exceptions;

import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.scanner.ScannerToken;
import edu.tum.cup2.semantics.ErrorInformation;


/**
 * 
 * This exception indicates, that an error was encountered in the input
 * but error-recovery failed due to missing error-recovery productions.
 * 
 **/

public class MissingErrorRecoveryException extends LRParserException
{
	/**  */
	private static final long serialVersionUID = 5586558157177385458L;
	
	private final LRParserState state;
	private final ScannerToken<? extends Object> token;
	private ErrorInformation errInf;
	
	
	/**
	 * Creates a new {@link MissingErrorRecoveryException}.
	 * @param message a message.
	 * @param state the current state of the parser
	 * @param errInf
	 * @param terminal the terminal that was read
	 */
	public MissingErrorRecoveryException(String message, LRParserState state, ScannerToken<? extends Object> token,
			ErrorInformation errInf)
	{
		super(message + " State: " + state.getID() + ", Token: " + token + " at " + "\n Error is " + errInf);
		this.state = state;
		this.token = token;
		this.errInf = errInf;
	}
	
	
	public ErrorInformation getErrorInformation()
	{
		return errInf;
	}
	
	
	public LRParserState getState()
	{
		return state;
	}
	
	
	public ScannerToken<? extends Object> getToken()
	{
		return token;
	}
}
