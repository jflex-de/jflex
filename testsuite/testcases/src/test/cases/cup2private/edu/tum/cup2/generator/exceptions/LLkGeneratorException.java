package edu.tum.cup2.generator.exceptions;

import edu.tum.cup2.generator.LLkGenerator;
import edu.tum.cup2.generator.LookaheadGenerator;


/**
 * Represents an error which occured while running a {@link LLkGenerator}/ {@link LookaheadGenerator}
 * 
 * @author Gero
 * 
 */
public class LLkGeneratorException extends GeneratorException
{
	private static final long serialVersionUID = 6656096538308869468L;
	
	
	public LLkGeneratorException()
	{
		
	}
	
	public LLkGeneratorException(String msg)
	{
		super(msg);
	}
	
	
	public LLkGeneratorException(Throwable cause)
	{
		super(cause);
	}
	
	
	public LLkGeneratorException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
