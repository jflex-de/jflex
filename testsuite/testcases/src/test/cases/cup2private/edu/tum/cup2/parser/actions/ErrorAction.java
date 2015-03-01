package edu.tum.cup2.parser.actions;

/**
 * LR parser action for "error".
 * 
 * @author Andreas Wenger
 */
public class ErrorAction
	implements LRAction
	//remark: ErrorAction does not need to be serializable as Error Action objects are not stored in the action table
{
	
	private static ErrorAction instance = new ErrorAction();
	
	public static ErrorAction getInstance()
	{
		return instance;
	}
	
	
	@Override public boolean equals(Object obj)
	{
		return (obj instanceof ErrorAction);
	}
	
	
	@Override public String toString()
	{
		return "";
	}
	

}
