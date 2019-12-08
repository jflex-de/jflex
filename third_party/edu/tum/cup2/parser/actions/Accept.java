package edu.tum.cup2.parser.actions;

import edu.tum.cup2.semantics.Action;
import java.io.Serializable;

/**
 * LR parser action for "accept".
 * 
 * @author Andreas Wenger
 */
public class Accept 
	implements LRAction, Serializable
{
	private static final long serialVersionUID = 1L;

	public Action getAction()
	{
		return null;
	}
	
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof Accept)
		{
			return true;
		}
		return false;
	}

	
	@Override public String toString()
	{
		return "acc";
	}
	
	
}
