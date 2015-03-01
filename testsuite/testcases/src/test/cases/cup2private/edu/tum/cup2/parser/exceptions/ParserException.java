package edu.tum.cup2.parser.exceptions;

/**
 * Base class for exceptions thrown by a parser implementation.
 * 
 * @author Gero
 * 
 */
public class ParserException extends Exception
{
	/**  */
	private static final long serialVersionUID = -1120814031891174447L;

	public ParserException(String message)
	{
		super(message);
	}
}
