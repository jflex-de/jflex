package edu.tum.cup2.spec.exceptions;


/**
 * Exception used to report an illegal definition of a
 * grammar or specification in general.
 * 
 * @author Andreas Wenger
 */
public class IllegalSpecException
	extends RuntimeException
{
	
	public IllegalSpecException(String message)
	{
		super(message);
	}
	
}
