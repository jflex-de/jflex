package edu.tum.cup2.generator.states;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import edu.tum.cup2.generator.items.LLkItem;
import edu.tum.cup2.generator.terminals.ITerminalSeqSet;
import edu.tum.cup2.parser.LLkParser;


/**
 * This class holds the state of a item pushdown automaton for a {@link LLkParser}. LR counterpart: {@link LRState}
 * 
 * @author Gero
 * 
 */
public class LLkState extends State<LLkItem> implements Serializable
{
	private static final long serialVersionUID = 9051334334450461555L;
	
	/** The top-most {@link LLkItem} on the parsers stack */
	protected final LLkItem item0;
	
	/** The second top-most {@link LLkItem} on the parsers stack */
	protected final LLkItem item1;
	
	/** Cached for performance-reasons */
	private final int hashCode;
	
	
	/**
	 * @param items
	 */
	public LLkState(Collection<LLkItem> items)
	{
		super(items);
		final Iterator<LLkItem> it = items.iterator();
		this.item0 = it.next();
		if (it.hasNext())
		{
			this.item1 = it.next();
		} else
		{
			this.item1 = null;
		}
		hashCode = calcHashCode();
	}
	
	
	public LLkState(LLkItem item0)
	{
		super(Arrays.asList(item0));
		this.item0 = item0;
		this.item1 = null;
		hashCode = calcHashCode();
	}
	
	
	public LLkState(LLkItem item1, LLkItem item0)
	{
		super(Arrays.asList(item0, item1));
		this.item0 = item0;
		this.item1 = item1;
		hashCode = calcHashCode();
	}
	
	
	public LLkItem getSecondItem()
	{
		return this.item1;
	}
	
	
	public boolean isTopItemComplete()
	{
		return this.item0.isComplete();
	}
	
	
	public ITerminalSeqSet getLookahead()
	{
		return this.item0.getLookaheads();
	}
	
	
	@Override
	public String toString()
	{
		final Iterator<LLkItem> it = this.items.iterator();
		String result = "[ LLkState: \n   " + it.next().toString();
		while (it.hasNext())
		{
			result += " ,\n   " + it.next().toString();
		}
		result += "\n]";
		return result;
	}
	
	
	private int calcHashCode()
	{
		final int prime = 31;
		int result = 1;
		for (LLkItem item : this.items)
		{
			result = prime * result + ((item == null) ? 0 : item.hashCode());
		}
		return result;
	}
	
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LLkState other = (LLkState) obj;
		return this.items.equals(other.items);
	}
}
