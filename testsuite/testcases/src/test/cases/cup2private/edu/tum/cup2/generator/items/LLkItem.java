package edu.tum.cup2.generator.items;

import java.io.Serializable;
import java.util.Collection;

import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.generator.terminals.ITerminalSeqSet;
import edu.tum.cup2.generator.terminals.TerminalSeqSet;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialNonTerminals;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;


/**
 * The {@link Item}-implementation of an LL(k)-item cellar automaton.
 * 
 * @author Gero
 */
public class LLkItem implements Item, Serializable
{
	private static final long serialVersionUID = 7301061753367113925L;
	
	public static final LLkItem FAKE_ITEM = new LLkItem(new LR0Item(new Production(-1, SpecialNonTerminals.StartLHS,
			SpecialTerminals.EndOfInputStream), 0), ITerminalSeqSet.EMPTY);
	
	protected final LR0Item kernel;
	
	protected final ITerminalSeqSet lookahead;
	
	/** Cached for performance reasons */
	private final int hashCode;
	
	
	/**
	 * @param production
	 * @param position
	 * @param lookaheads
	 */
	public LLkItem(Production production, int position, ITerminalSeqSet lookaheads)
	{
		this(new LR0Item(production, position), lookaheads);
	}
	
	
	/**
	 * @param production
	 * @param position
	 * @param lookaheads
	 */
	public LLkItem(Production production, int position, ITerminalSeq... lookaheads)
	{
		this(new LR0Item(production, position), new TerminalSeqSet(lookaheads));
	}
	
	
	/**
	 * @param production
	 * @param position
	 * @param lookaheads
	 */
	public LLkItem(Production production, int position, Collection<ITerminalSeq> lookaheads)
	{
		this(new LR0Item(production, position), new TerminalSeqSet(lookaheads));
	}
	
	
	public LLkItem(LR0Item kernel, ITerminalSeqSet lookahead)
	{
		super();
		this.kernel = kernel;
		this.hashCode = calcHashCode();
		this.lookahead = lookahead;
	}
	
	
	public ITerminalSeqSet getLookaheads()
	{
		return lookahead;
	}
	
	
	public Production getProduction()
	{
		return kernel.getProduction();
	}
	
	
	public int getPosition()
	{
		return kernel.getPosition();
	}
	
	
	public Symbol getNextSymbol()
	{
		return kernel.getNextSymbol();
	}
	
	
	public boolean isShiftable()
	{
		return kernel.isShiftable();
	}
	
	
	public boolean isComplete()
	{
		return !kernel.isShiftable();
	}
	
	
	public LLkItem shift()
	{
		if (!isShiftable())
		{
			throw new RuntimeException("Shifting not possible: Item already closed: " + this);
		}
		
		return new LLkItem(getProduction(), getPosition() + 1, getLookaheads());
	}
	
	
	@Override
	public String toString()
	{
		return "[ LLkItem: " + kernel.toString() + ", Lookahead: " + lookahead.toString() + " ]";
	}
	
	
	private int calcHashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kernel == null) ? 0 : kernel.hashCode());
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
		LLkItem other = (LLkItem) obj;
		if (kernel == null)
		{
			if (other.kernel != null)
				return false;
		} else if (!kernel.equals(other.kernel))
			return false;
		return true;
	}
}
