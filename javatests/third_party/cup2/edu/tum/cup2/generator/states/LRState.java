package edu.tum.cup2.generator.states;

import java.util.Collection;

import edu.tum.cup2.generator.GrammarInfo;
import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.grammar.Symbol;


/**
 * Specialization of {@link State} for LR parsers. LL counterpart: {@link LLkState}
 * 
 * @author Gero
 * 
 */
public abstract class LRState<T extends Item> extends State<T>
{
	/**
	 * @param items
	 */
	public LRState(Collection<T> items)
	{
		super(items);
	}
	
	
	/**
	 * Moves the position if the items one step further when
	 * the given symbol follows and returns them as a new state
	 * (only the kernel, without closure).
	 * For a description of this algorithm, see Appel's book, page 62.
	 */
	public abstract State<T> goTo(Symbol symbol);
	
	
	/**
	 * Returns the closure of this {@link State} as another {@link State}.
	 * For a description of this algorithm, see Appel's book, page 62.
	 */
	public abstract LRState<T> closure(GrammarInfo grammarInfo);
}
