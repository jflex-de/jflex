package edu.tum.cup2.generator.states;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import edu.tum.cup2.generator.FirstSets;
import edu.tum.cup2.generator.GrammarInfo;
import edu.tum.cup2.generator.NullableSet;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.terminals.TerminalSet;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;


/**
 * LR(1) state (immutable, no side effects possible), consisting of a list of
 * LR(1) items.
 * 
 * Methods related to the nucleus were removed (since the author of this
 * class doesn't know anything about them).
 * 
 * @author Andreas Wenger
 */
public class LR1State
	extends LRState<LR1Item>
{


	private final LR0State kernel;
	private final HashMap<LR0Item, LR1Item> itemsWithKernels;
	private final int hashCode;
	
	//cache
	private LR1State closureCache = null;


	public LR1State(HashSet<LR1Item> items)
	{
		super(items);
		//hash code
		int sum = 0;
		//create map with LR(0) kernels as key
		itemsWithKernels = new HashMap<LR0Item, LR1Item>();
		for (LR1Item item : items)
		{
			itemsWithKernels.put(item.getLR0Kernel(), item);
			sum += item.hashCode();
		}
                kernel = new LR0State(itemsWithKernels.keySet());
		this.hashCode = sum;
	}


	/**
	 * Returns the closure of this {@link LR1State} as another {@link LR1State}.
	 */
	@Override public LR1State closure(GrammarInfo grammarInfo)
	{
		if (closureCache == null)
			closureCache = new LR1State(closureItems(grammarInfo));
		return closureCache;
	}


	/**
	 * Returns the closure of the {@link LR1Item}s of this state as set of
	 * {@link LR1Item}s. For a description of this algorithm, see Appel's book,
	 * page 63.
	 * 
	 * TODO: optimization possible by using persistent data structures?
	 */
	private HashSet<LR1Item> closureItems(GrammarInfo grammarInfo)
	{
		//the closure contains all items of the source...
		//(we use a map with the LR(0)-part as the key for very fast access,
		//since we will probably often add new lookahead symbols to existing LR1Items)
		HashMap<LR0Item, TerminalSet> items = new HashMap<LR0Item, TerminalSet>();
		for (LR1Item item : this.items)
		{
			items.put(item.getLR0Kernel(), item.getLookaheads());
		}
		// ... and the following ones: for any item "A → α.Xβ with lookahead z",
		// any "X → γ" and any w ∈ FIRST(βz), add "X → .γ with lookahead w"
		LinkedList<LR1Item> queue = new LinkedList<LR1Item>();
		queue.addAll(this.items);
		FirstSets firstSets = grammarInfo.getFirstSets();
		NullableSet nullableSet = grammarInfo.getNullableSet();
		while (!queue.isEmpty())
		{
			LR1Item item = queue.removeFirst(); //item is A → α.Xβ with lookahead z"
			Symbol nextSymbol = item.getNextSymbol();
			if (nextSymbol instanceof NonTerminal) //nextSymbol is "X"
			{
				TerminalSet firstSet = item.getNextLookaheads(firstSets, nullableSet); //all "w"s
				for (Production p : grammarInfo.getProductionsFrom((NonTerminal) nextSymbol)) //p is each "X → γ"
				{
					LR0Item newItemLR0 = new LR0Item(p, 0);
					//look, if there is already a LR(1) item with that LR(0) kernel. if so, add the
					//new lookahead symbols there. If not, add the LR(1) item to the result set.
					TerminalSet sameKernelItemLookaheads = items.get(newItemLR0);
					if (sameKernelItemLookaheads != null)
					{
						//add to existing LR1Item
						TerminalSet newLookaheads = sameKernelItemLookaheads.plusAll(firstSet);
						//if new lookahead was found, add again to queue
						if (!newLookaheads.equals(sameKernelItemLookaheads))
						{
							items.put(newItemLR0, newLookaheads);
							queue.add(new LR1Item(newItemLR0, newLookaheads));
						}
					}
					else
					{
						//new item
						items.put(newItemLR0, firstSet);
						queue.add(new LR1Item(newItemLR0, firstSet));
					}
				}
			}
		}
		//collect resulting LR(1) items
		HashSet<LR1Item> ret = new HashSet<LR1Item>();
		for (LR0Item itemLR0 : items.keySet())
		{
			ret.add(new LR1Item(itemLR0.getProduction(), itemLR0.getPosition(), items.get(itemLR0)));
		}
		return ret;
	}


	/**
	 * Moves the position of the items one step further when the given symbol
	 * follows and returns them as a new state (only the kernel, without
	 * closure). For a description of this algorithm, see Appel's book, page 62.
	 */
	@Override public LR1State goTo(Symbol symbol)
	{
		HashSet<LR1Item> ret = new HashSet<LR1Item>();
		//find all items where the given symbol follows and add them shifted
		for (LR1Item item : items)
		{
			if (item.getNextSymbol() == symbol)
			{
				ret.add(item.shift());
			}
		}
		return new LR1State(ret);
	}


	/**
	 * Gets the number of items (when each item has only one lookahead symbol,
	 * e.g. an item with 3 lookahead symbols counts as 3 items).
	 * Useful for debugging and testing.
	 */
	int getSimpleItemsCount()
	{
		int ret = 0;
		for (LR1Item item : items)
		{
			ret += item.getLookaheads().getTerminals().size();
		}
		return ret;
	}


	/**
	 * Returns true, if this state contains an item with the given production,
	 * position and lookahead symbol. Useful for debugging and testing.
	 */
	boolean containsSimpleItem(Production production, int position, Terminal lookahead)
	{
		for (LR1Item item : items)
		{
			if (item.getProduction() == production && item.getPosition() == position
				&& item.getLookaheads().contains(lookahead))
				return true;
		}
		return false;
	}
	
	
	/**
	 * Gets the item of this state which has the given LR(0) kernel,
	 * or <code>null</code>, if there is no such item.
	 */
	public LR1Item getItemByLR0Kernel(LR0Item kernel)
	{
		return itemsWithKernels.get(kernel);
	}


	/**
	 * Merges this state with the given one and returns the result.
	 * Only works, if both states have equal LR(0) kernels.
	 */
	public LR1State merge(LR1State state)
	{
		HashSet<LR1Item> items = new HashSet<LR1Item>();
		for (LR1Item item1 : this.items)
		{
			LR1Item newItem = item1;
			
			//find lr0-equal item and merge them
			LR1Item item2 = state.getItemByLR0Kernel(item1.getLR0Kernel());
			newItem = newItem.merge(item2);
			
			items.add(newItem);
		}
		return new LR1State(items);
	}


	@Override public boolean equals(Object obj)
	{
		if (obj instanceof LR1State)
		{
			LR1State s = (LR1State) obj;
			if (items.size() != s.items.size())
				return false;
			for (LR1Item item : items)
			{
				if (!s.items.contains(item))
					return false;
			}
			return true;
		}
		return false;
	}


	/**
	 * Gets the LR(0) kernel of this state.
	 */
	public LR0State getLR0Kernel()
	{
		return kernel;
	}


	@Override public int hashCode()
	{
		return hashCode;
	}
	
	
	/**
	 * Returns true, when the LR(0) kernel of the state
	 * is equal to the LR(0) kernel of the given state.
	 */
	public boolean equalsLR0(LR1State state)
	{
		//first check for same hash code for performance reasons
		return kernel.hashCode() == state.kernel.hashCode() &&
			kernel.equals(state.kernel);
	}


}
