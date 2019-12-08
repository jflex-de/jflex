package edu.tum.cup2.generator.exceptions;


/**
 * Base class for all exceptions thrown by a
 * parser generator.
 * 
 * @author Andreas Wenger
 */
public class GeneratorException extends Exception
{
	/**  */
	private static final long serialVersionUID = 1540402270436552083L;
	
	
	public GeneratorException()
	{
	}
	
	
	public GeneratorException(String message)
	{
		super(message);
	}
	
	
	public GeneratorException(Throwable cause)
	{
		super(cause);
	}
	
	
	public GeneratorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
