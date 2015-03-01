package edu.tum.cup2.generator.items;

import java.util.List;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;


/**
 * An LR(0)-Item is a {@link Production} combined
 * with a position in its right hand side.
 * 
 * In many compiler books, this position is visualized
 * as a dot within the right hand side, e.g. for the
 * production "E → (B)" with the dot at position 1:
 * "E → (.B)".
 * 
 * @author Andreas Wenger
 */
public class LR0Item implements Item
{
	
	protected final Production production;
	protected final int position;
	
	// cache
	protected final Symbol nextSymbol;
	protected final int hashCode;
	private final boolean shiftable;
	
	
	/**
	 * Creates a new {@link LR0Item}, using the given {@link Production} and
	 * position within this production.
	 * If the position is before an epsilon, it is automatically shifted
	 * on to the first non-epsilon symbol (or end).
	 */
	public LR0Item(Production production, int position)
	{
		this.production = production;
		// correct position if epsilons are following
		List<Symbol> rhs = production.getRHS();
		int correctedPosition = position;
		if (position == 0 && rhs.get(0) == SpecialTerminals.Epsilon)
		{
			correctedPosition = 1; // immediately shift over epsilon
		}
		this.position = correctedPosition;
		// shiftable
		this.shiftable = (this.position < rhs.size());
		// next symbol
		if (this.shiftable)
			this.nextSymbol = rhs.get(this.position);
		else
			this.nextSymbol = null;
		// hash code
		this.hashCode = production.hashCode() + this.position;
	}
	
	
	public Production getProduction()
	{
		return production;
	}
	
	
	public int getPosition()
	{
		return position;
	}
	
	
	/**
	 * Gets the {@link Symbol} right behind the current position,
	 * or <code>null</code> if there is none left.
	 */
	public Symbol getNextSymbol()
	{
		return nextSymbol;
	}
	
	
	public boolean isShiftable()
	{
		return shiftable;
	}
	
	
	public boolean isComplete()
	{
		return !shiftable;
	}
	
	
	/**
	 * Returns this item with the position shifted one symbol further.
	 */
	public LR0Item shift()
	{
		if (!isShiftable())
			throw new RuntimeException("Shifting not possible: Item already closed: " + this);
		
		// return shifted item
		return new LR0Item(production, position + 1);
	}
	
	
	/**
	 * @return A new {@link LR0Item} with the same production but shifted until it is complete
	 */
	public LR0Item complete()
	{
		if (isComplete())
		{
			return this;
		} else
		{
			return new LR0Item(production, production.getRHS().size());
		}
	}
	
	
	@Override
	public String toString()
	{
		return production.toString(position);
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof LR0Item)
		{
			LR0Item l = (LR0Item) obj;
			// if (production != l.production) //OBSOLETE:
			if (!production.equals(l.production))
				return false;
			if (position != l.position)
				return false;
			return true;
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	
}
