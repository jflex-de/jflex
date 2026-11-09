package edu.tum.cup2.parser.exceptions;


/**
 * This exception is thrown when an error
 * occurs while parsing.
 * 
 * @author Andreas Wenger
 */
public class LRParserException extends ParserException
{
	/**  */
	private static final long serialVersionUID = 5821534198543313926L;

	public LRParserException(String message)
	{
		super(message);
	}
}
