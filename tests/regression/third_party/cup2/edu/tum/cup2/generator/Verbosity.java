package edu.tum.cup2.generator;


/**
 * Number of debug messages printed to the debug output stream.
 * 
 * @author Andreas Wenger
 */
public enum Verbosity
{
	
	/**
	 * No debug messages at all. Only exceptions are thrown if something fails.
	 */
	None(0),
	/**
	 * Only a few messages, like "Number of terminals: ..." or
	 * "Number of states exceeding 1000..." for example.
	 */
	Sparse(1000),
	/**
	 * More messages, like "Number of states exceeding 100..." for example.
	 */
	Verbose(100),
	/**
	 * Very many messages, like "Current state has 156 items" for example.
	 */
	Detailled(1);
	
	
	private final int statesCounterStep;
	
	
	private Verbosity(int statesCounterStep)
	{
		this.statesCounterStep = statesCounterStep;
	}
	
	
	/**
	 * Gets the number of states between messages in the form
	 * "Number of states exceeding...".
	 * 0 means, that no such messages are requested.
	 */
	public int getStatesCounterStep()
	{
		return statesCounterStep;
	}
	
}