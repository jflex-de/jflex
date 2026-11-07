package edu.tum.cup2.generator.items;

import static edu.tum.cup2.grammar.SpecialTerminals.Epsilon;
import static edu.tum.cup2.util.Tuple2.t;

import java.util.HashSet;
import java.util.Set;

import edu.tum.cup2.generator.FirstSets;
import edu.tum.cup2.generator.NullableSet;
import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.Tuple2;


/**
 * An LALR(1)-CP-Item is an LR(0)-Item, combined
 * with a set of lookahead terminals and context propagation links
 * to other items of the same state (closure CP links)
 * and/or an item of another state (goto CP link).
 * 
 * @author Andreas Wenger
 */
public final class LALR1CPItem
	implements Item
{
	
	private final LR0Item stripped;
	private final EfficientTerminalSet lookaheads;
	
	//closure CP links
	private final Set<LR0Item> closureLinks; //links to (LR(0) kernels of other items (targets) of the same state

	//cache
	private final int hashCode;
	
	
	/**
	 * Creates a new {@link LALR1Item}, using the LR(0) kernel and a set of lookaheads (may not be null).
	 */
	public LALR1CPItem(LR0Item stripped, EfficientTerminalSet lookaheads)
	{
		this(stripped, lookaheads, new HashSet<LR0Item>());
	}
	
	
	/**
	 * Creates a new {@link LALR1Item}, using the given kernel, a set of lookaheads (may not be null)
	 * and a set of closure context propagation links.
	 */
	private LALR1CPItem(LR0Item stripped, EfficientTerminalSet lookaheads, Set<LR0Item> closureLinks)
	{
		this.stripped = stripped;
		this.lookaheads = lookaheads;
		this.closureLinks = closureLinks;
		//compute hashcode
		this.hashCode = (this.stripped.hashCode() + lookaheads.hashCode() * 100);
	}
	
	
	public Production getProduction()
	{
		return stripped.getProduction();
	}
	
	
	public int getPosition()
	{
		return stripped.getPosition();
	}
	
	
	/**
	 * Gets the {@link Symbol} right behind the current position,
	 * or <code>null</code> if there is none left.
	 */
	public Symbol getNextSymbol()
	{
		return stripped.getNextSymbol();
	}
	
	
	public boolean isShiftable()
	{
		return stripped.isShiftable();
	}
	
	
	public boolean isComplete()
	{
		return stripped.isComplete();
	}
	
	
	/**
	 * Returns this item with the position shifted one symbol further.
	 * The already known lookaheads stay the same (it would also be ok to use
	 * an empty list, since a context propagation link must point at the
	 * returned item anway).
	 */
	public LALR1CPItem shift()
	{
		if (stripped.position >= stripped.getProduction().getRHS().size())
			throw new RuntimeException(
				"Shifting not possible: Item already closed: " + stripped.production.toString(stripped.position));
		return new LALR1CPItem(new LR0Item(stripped.production, stripped.position + 1), lookaheads);
	}
	
	
	/**
	 * Gets the lookahead terminals assigned to this item.
	 */
	public EfficientTerminalSet getLookaheads()
	{
		return lookaheads;
	}
	
	
	/**
	 * Gets the FIRST set of β (where this item has the following form:
	 * "A → α.Xβ with lookahead z" with X being the next symbol), using
	 * the given precomputed nullable set and FIRST sets for all symbols.
	 * As the second return value, true is returned when β is nullable,
	 * otherwise false.
	 */
	public Tuple2<EfficientTerminalSet, Boolean> getNextLookaheadsAndCP(FirstSets firstSets, NullableSet nullableSet)
	{
		EfficientTerminalSet ret = lookaheads.empty();
		//while the symbols of β (if any) are nullable, collect their FIRST sets.
		//when not nullable, collect the symbols and stop.
		for (int i = stripped.position + 1; i < stripped.production.getRHS().size(); i++)
		{
			Symbol symbol = stripped.production.getRHS().get(i);
			ret = ret.plusAll(firstSets.get(symbol));
			if (!(symbol == Epsilon || nullableSet.contains(symbol))) //TODO: why "symbol == Epsilon"? redundant to nullableSet
				return t(ret, false);
		}
		//since we still did not return, all symbols of β (if any) were nullable,
		//so we return true as the second return value
		return t(ret, true);
	}
	
	
	public Set<LR0Item> getClosureLinks()
	{
		return closureLinks;
	}
	
	
	@Override public String toString()
	{
		StringBuilder s = new StringBuilder(stripped.production.toString(stripped.position));
		s.append(" [");
		s.append(lookaheads.toString());
		s.append("] CP: <");
		s.append(">");
		return s.toString();
	}
	
	
	/**
	 * {@link LALR1CPItem}s are only equal if they are the same instance.
	 * This allows using them in a global hashtable, which includes many states
	 * that may contain {@link LALR1CPItem}s with "equal content".
	 * To find equal {@link LALR1CPItem}s within a single state, use their
	 * LR(0) kernels as a hashing key instead.
	 */
	@Override public boolean equals(Object obj)
	{
		return this == obj;
	}
	
	
	public LALR1CPItem plusLookaheads(EfficientTerminalSet lookaheads)
	{
		return new LALR1CPItem(stripped, this.lookaheads.plusAll(lookaheads), closureLinks);
	}
	
	
	public LALR1CPItem plusClosureCPLink(LR0Item targetItem)
	{
		//GOON: this is destructive. can we use a persistent data structure instead?
		closureLinks.add(targetItem);
		return new LALR1CPItem(stripped, lookaheads, closureLinks);
	}
	
	
	@Override public int hashCode()
	{
		return hashCode;
	}
	
	
	public LR0Item getLR0Item()
	{
		return stripped;
	}
	

}
