package edu.tum.cup2.parser.actions;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.semantics.Action;
import java.io.Serializable;

/**
 * LR reduce parser action.
 * 
 * @author Andreas Wenger
 */
public final class Reduce
	implements LRAction, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final Production production;
	
	
	public Reduce(Production production)
	{
		this.production = production;
	}
	
	
	public Production getProduction()
	{
		return production;
	}
	
	
	/**
	 * Gets the semantic action assigned to this reduce action, or null.
	 */
	public Action getAction()
	{
		return production.getReduceAction();
	}
	
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof Reduce)
		{
			Reduce a = (Reduce) obj;
			return this.production.equals(a.production);
		}
		return false;
	}
	
	
	@Override public String toString()
	{
		if (production.getID() > -1)
			return "r" + production.getID(); //r<production index>
		else
			return "r(#" + production.getRHS().size() + ")"; //r(#<number of rhs symbols>)
	}

}
