package edu.tum.cup2.generator.items;

import static edu.tum.cup2.grammar.SpecialTerminals.Epsilon;
import edu.tum.cup2.generator.FirstSets;
import edu.tum.cup2.generator.NullableSet;
import edu.tum.cup2.generator.terminals.TerminalSet;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;


/**
 * An LR(1)-Item is an LR(0)-Item, combined
 * with a set of lookahead terminals.
 * 
 * @author Andreas Wenger
 */
public final class LR1Item
	implements Item
{
	
	private final LR0Item kernel;
	private final TerminalSet lookaheads;
	
	//cache
	private TerminalSet nextLookaheads = null; //compute on demand
	private final int hashCode;
	
	
	/**
	 * Creates a new {@link LR1Item}, using the given {@link Production},
	 * position within this production and set of possible lookahead terminals.
	 */
	public LR1Item(Production production, int position, TerminalSet lookaheads)
	{
		this.kernel = new LR0Item(production, position);
		this.lookaheads = lookaheads;
		//compute hashcode
		this.hashCode = this.kernel.hashCode() + lookaheads.hashCode();
	}
	
	
	/**
	 * Creates a new {@link LR1Item}, using the given {@link LR0Item}
	 * and set of possible lookahead terminals.
	 */
	public LR1Item(LR0Item kernel, TerminalSet lookaheads)
	{
		this.kernel = kernel;
		this.lookaheads = lookaheads;
		//compute hashcode
		this.hashCode = this.kernel.hashCode() + lookaheads.hashCode();
	}
	
	
	public Production getProduction()
	{
		return kernel.getProduction();
	}
	
	
	public int getPosition()
	{
		return kernel.getPosition();
	}
	
	
	/**
	 * Gets the {@link Symbol} right behind the current position,
	 * or <code>null</code> if there is none left.
	 */
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
		return kernel.isComplete();
	}
	
	
	/**
	 * Returns a new {@link LR1Item}, having the same LR(0) kernel as this
	 * one and containing the union of the lookahead symbols of this item
	 * and the given ones. If no new lookaheads are given, the original
	 * LR1Item (<code>this</code>) is returned.
	 */
	public LR1Item createUnion(TerminalSet lookaheads)
	{
		if (this.lookaheads.equals(lookaheads))
		{
			return this;
		}
		else
		{
			return new LR1Item(kernel.production, kernel.position,
				this.lookaheads.plusAll(lookaheads));
		}
	}
	
	
	/**
	 * Returns this item with the position shifted one symbol further.
	 */
	public LR1Item shift()
	{
		if (kernel.position >= kernel.getProduction().getRHS().size())
			throw new RuntimeException(
				"Shifting not possible: Item already closed: " + kernel.production.toString(kernel.position));
		return new LR1Item(kernel.production, kernel.position + 1, lookaheads);
	}
	
	
	/**
	 * Gets the lookahead terminals assigned to this item.
	 */
	public TerminalSet getLookaheads()
	{
		return lookaheads;
	}
	
	
	/**
	 * Gets the FIRST set of βa (where this item has the following form:
	 * "A → α.Xβ with lookahead a" with X being the next symbol), using
	 * the given precomputed nullable set and FIRST sets for all symbols.
	 */
	public TerminalSet getNextLookaheads(FirstSets firstSets, NullableSet nullableSet)
	{
		if (nextLookaheads == null)
		{
			nextLookaheads = computeNextLookaheadSymbols(firstSets, nullableSet);
		}
		return nextLookaheads;
	}
	
	
	/**
	 * Gets the FIRST set of βa (where this item has the following form:
	 * "A → α.Xβ with lookahead a" with X being the next symbol). See
	 * </code>getNextLookaheads</code>.
	 */
	TerminalSet computeNextLookaheadSymbols(FirstSets firstSets, NullableSet nullableSet)
	{
		TerminalSet ret = lookaheads.empty();
		//while the symbols of β (if any) are nullable, collect their FIRST sets.
		//when not nullable, collect the symbols and stop.
		for (int i = kernel.position + 1; i < kernel.production.getRHS().size(); i++)
		{
			Symbol symbol = kernel.production.getRHS().get(i);
			ret = ret.plusAll(firstSets.get(symbol));
			if (!(symbol == Epsilon || nullableSet.contains(symbol))) //TODO: why "symbol == Epsilon"? redundant to nullableSet
				return ret;
		}
		//since we still did not return, all symbols of β (if any) were nullable,
		//so we add all our lookaheads to the result
		ret = ret.plusAll(lookaheads);
		return ret;
	}
	
	
	/**
	 * Merges this item with the given one and returns the result.
	 */
	public LR1Item merge(LR1Item item)
	{
		if (!kernel.equals(item.kernel))
			throw new IllegalArgumentException("Only items with equal LR(0) kernel can be merged!");
		return new LR1Item(kernel.production, kernel.position, lookaheads.plusAll(item.lookaheads));
	}
	
	
	@Override public String toString()
	{
		StringBuilder s = new StringBuilder(kernel.production.toString(kernel.position));
		s.append(" [");
		s.append(lookaheads.toString());
		s.append("]");
		return s.toString();
	}
	
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof LR1Item)
		{
			LR1Item l = (LR1Item) obj;
			if (!kernel.equals(l.kernel))
				return false;
			if (!lookaheads.equals(l.lookaheads))
				return false;
			return true;
		}
		return false;
	}
	
	
	public boolean equalsLR0(LR1Item item)
	{
		return kernel.equals(item.kernel);
	}
	
	
	@Override public int hashCode()
	{
		return hashCode;
	}
	
	
	public LR0Item getLR0Kernel()
	{
		return kernel;
	}
	

}
