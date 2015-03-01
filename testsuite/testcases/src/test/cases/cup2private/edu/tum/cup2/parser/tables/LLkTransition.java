package edu.tum.cup2.parser.tables;

import java.io.Serializable;

import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.grammar.Symbol;


/**
 * This defines a transition of a LL(k) pushdown automaton. It consists of an {@link LLkParserState} ({@link #from}), a
 * consumed symbol ({@link #symbol}) and a {@link LLkParserState} ({@link #to}) it transitions into.
 * 
 * @author Gero
 */
public class LLkTransition implements Serializable
{
	private static final long serialVersionUID = -2382455334586821834L;
	
	private final LLkState from;
	private final Symbol symbol;
	private final LLkState to;
	
	private final LLkTransitionId id;
	
	
	/**
	 * @param from
	 * @param symbol
	 * @param to
	 */
	public LLkTransition(LLkState from, Symbol symbol, LLkState to)
	{
		super();
		if (from == null || to == null)
		{
			throw new IllegalArgumentException("Passed states may not be null!");
		}
		
		this.from = from;
		this.symbol = symbol;
		this.to = to;
		
		this.id = new LLkTransitionId();
	}
	
	
	public LLkState getFrom()
	{
		return from;
	}
	
	
	public LLkState getTo()
	{
		return to;
	}
	
	
	public Symbol getSymbol()
	{
		return symbol;
	}
	
	
	@Override
	public String toString()
	{
		return "[ LLkTransition: \n   " + from.toString() + ", \n   Symbol: " + symbol + "\n   " + to.toString() + " ]";
	}
	
	
	@Override
	public int hashCode()
	{
		return id.getBase();
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
		LLkTransition other = (LLkTransition) obj;
		return this.id.getBase() == other.id.getBase();
	}
	
	
	private static class LLkTransitionId implements Serializable
	{
		private static final long serialVersionUID = 3853005922113484319L;
		
		private static int nextBase = 0;
		private final int base;
		
		
		public LLkTransitionId()
		{
			super();
			this.base = nextBase++;
		}
		
		
		public int getBase()
		{
			return base;
		}
		
		
		@Override
		public int hashCode()
		{
			return base;
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
			LLkTransitionId other = (LLkTransitionId) obj;
			return this.base == other.getBase();
		}
	}
}
