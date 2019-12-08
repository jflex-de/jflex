package edu.tum.cup2.parser.exceptions;

import edu.tum.cup2.parser.LLkParser;

/**
 * The base exception for every thing which should NOT happen while running the {@link LLkParser}
 * 
 * @author Gero
 * 
 */
public class LLkParserException extends ParserException
{

	/**  */
	private static final long serialVersionUID = 2675367100120865598L;

	/**
	 * @param message
	 */
	public LLkParserException(String message)
	{
		super(message);
	}
}
