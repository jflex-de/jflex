package edu.tum.cup2.generator.states;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import edu.tum.cup2.generator.GrammarInfo;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;


/**
 * LR(0) state, consisting of a list of LR(0) items.
 * 
 * @author Andreas Wenger
 */
public final class LR0State
	extends LRState<LR0Item>
{
	
	//cache
	private final int hashCode;
	
	
	/**
	 * Creates a new {@link LR0State} with the given items.
	 */
	public LR0State(Collection<LR0Item> items)
	{
		super(items);
		//compute hashcode
		int sum = 0;
		for (LR0Item item : items)
		{
			sum += item.hashCode();
		}
		this.hashCode = sum;
	}
	
	
	public LR0State(LR0Item... items)
	{
		this(Arrays.asList(items));
	}
	
	
	/**
	 * Returns the closure of this {@link LR0State} as another
	 * {@link LR0State}.
	 */
	@Override public LR0State closure(GrammarInfo grammarInfo)
	{
		return new LR0State(closure(items, grammarInfo));
	}
	
	
	/**
	 * Returns the closure of the given {@link LR0Item}s as another
	 * set of {@link LR0Item}s.
	 * For a description of this algorithm, see Appel's book, page 60,
	 * or ASU, page 271.
	 */
	private HashSet<LR0Item> closure(Collection<LR0Item> items, GrammarInfo grammarInfo)
	{
		//the closure contains all items of the source...
		HashSet<LR0Item> ret = new HashSet<LR0Item>();
		ret.addAll(items);
		//... and the following ones (for any item "A → α.Xβ" and any "X → γ" add "X → .γ")
		LinkedList<LR0Item> queue = new LinkedList<LR0Item>();
		queue.addAll(items);
		while (!queue.isEmpty())
		{
			LR0Item it = queue.removeFirst();
			Symbol nextSymbol = it.getNextSymbol();
			if (nextSymbol instanceof NonTerminal)
			{
				for (Production p : grammarInfo.getProductionsFrom((NonTerminal) nextSymbol))
				{
					LR0Item newitem = new LR0Item(p, 0);
					if (!ret.contains(newitem))
					{
						ret.add(newitem);
						queue.addLast(newitem);
					}
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * Moves the position of the items one step further when
	 * the given symbol follows and returns them as a new state
	 * (only the kernel, without closure).
	 * For a description of this algorithm, see Appel's book, page 60.
	 */
	@Override public LR0State goTo(Symbol symbol)
	{
		HashSet<LR0Item> ret = new HashSet<LR0Item>();
		//find all items where the given symbol follows and add them shifted
		for (LR0Item item : items)
		{
			if (item.getNextSymbol() == symbol)
			{
				ret.add(item.shift());
			}
		}
		return new LR0State(ret);
	}
	
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof LR0State)
		{
			LR0State s = (LR0State) obj;
			if (items.size() != s.items.size())
				return false;
			for (LR0Item item : items)
			{
				if (!s.items.contains(item))
					return false;
			}
			return true;
		}
		return false;
	}
	
	
	@Override public int hashCode()
	{
		return hashCode;
	}	
	
}
