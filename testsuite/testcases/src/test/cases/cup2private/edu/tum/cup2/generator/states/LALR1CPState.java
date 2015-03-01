package edu.tum.cup2.generator.states;

import static edu.tum.cup2.util.CollectionTools.llist;
import static edu.tum.cup2.util.CollectionTools.map;
import static edu.tum.cup2.util.CollectionTools.set;
import static edu.tum.cup2.util.Tuple2.t;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.tum.cup2.generator.FirstSets;
import edu.tum.cup2.generator.GrammarInfo;
import edu.tum.cup2.generator.NullableSet;
import edu.tum.cup2.generator.items.CPGoToLink;
import edu.tum.cup2.generator.items.LALR1CPItem;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.Tuple2;


/**
 * LALR(1) state, consisting of a list of LALR(1)-CP items
 * (LR(0) items with some lookaheads and with context propagation
 * links).
 * 
 * @author Andreas Wenger
 */
public final class LALR1CPState
	extends LRState<LALR1CPItem>
{
	
	//cache
        private final int hashCode;
	private final Map<LR0Item,LALR1CPItem> stripped2Full = map();
        
	
	/**
	 * Creates a new {@link LALR1CPState} with the given items (and their kernels,
	 * needed for performance reasons).
	 */
        //TODO: Parameter strippedItems is redundant
        public LALR1CPState(Collection<LALR1CPItem> items)
	{
		super(items);
		//compute hashcode
		int sum = 0;
                for (LALR1CPItem it: items){
                    LR0Item item = it.getLR0Item();
                    sum+=item.hashCode();
                    stripped2Full.put(item,it);
                }
		this.hashCode = sum;
	}
	
	
	/**
	 * Returns the closure of this {@link LALR1CPState} as another
	 * {@link LALR1CPState}.
	 */
	@Override public LALR1CPState closure(GrammarInfo grammarInfo)
	{
		//the closure contains all items of the source... (here indexed by LR(0) kernel)
		Map<LR0Item, LALR1CPItem> retItems = map();
		for (LALR1CPItem item : this.items)
			retItems.put(item.getLR0Item(), item);
		//... and the following ones: for any item "A → α.Xβ with lookahead z",
		//any "X → γ" and any w ∈ FIRST(β), add "X → .γ with lookahead w". if "ɛ ∈ w" is
		//nullable, add a context propagation link from the source item to the target item.
		LinkedList<LR0Item> queue = new LinkedList<LR0Item>();
		queue.addAll(retItems.keySet());
		FirstSets firstSets = grammarInfo.getFirstSets();
		NullableSet nullableSet = grammarInfo.getNullableSet();
		while (!queue.isEmpty())
		{
			LR0Item sourceKernel = queue.removeFirst(); //itemKernel is "A → α.Xβ"
			LALR1CPItem source = retItems.get(sourceKernel); //item is "A → α.Xβ with lookahead z and some CP links"
			Symbol nextSymbol = source.getNextSymbol();
			if (nextSymbol instanceof NonTerminal) //nextSymbol is "X"
			{
				Tuple2<EfficientTerminalSet, Boolean> next = source.getNextLookaheadsAndCP(firstSets, nullableSet);
				EfficientTerminalSet firstSet = next.get1(); //all "w"s
				Boolean needCP = next.get2(); //true if CP link is needed
				for (Production p : grammarInfo.getProductionsFrom((NonTerminal) nextSymbol)) //p is each "X → γ"
				{
					LR0Item destKernel = new LR0Item(p, 0);
					//look, if there is already a LALR(1)-CP item with that LR(0) kernel. if so, add the
					//new lookahead symbols there. If not, add the LALR(1)-CP item to the result set.
					LALR1CPItem sameKernelItem = retItems.get(destKernel);
					if (sameKernelItem != null)
					{
						//add lookaheads to existing LALR1CPItem
						retItems.put(destKernel, sameKernelItem.plusLookaheads(firstSet));
					}
					else
					{
						//new item
						retItems.put(destKernel, new LALR1CPItem(new LR0Item(p, 0), firstSet));
						queue.add(destKernel);
					}
					//if required, add CP link to source item
					if (needCP)
					{
						source = source.plusClosureCPLink(destKernel);
						retItems.put(sourceKernel, source);
					}
				}
			}
		}
		return new LALR1CPState(retItems.values());
	}
	
	
	/**
	 * Use {@link #goToCP(Symbol)} instead.
	 */
	@Deprecated @Override public LALR1CPState goTo(Symbol symbol)
	{
		throw new RuntimeException();
	}
	
	
	/**
	 * Moves the position of the items one step further when
	 * the given symbol follows and returns them as a new state
	 * (only the kernel, without closure).
	 * As a second return value, the go-to CP links are returned,
	 * but the target state is still null (because we do not now
	 * yet, if the target state is not already existing).
	 * For a description of this algorithm, see Appel's book, page 60.
	 */
	public Tuple2<LALR1CPState, List<CPGoToLink>> goToCP(Symbol symbol)
	{
		Set<LALR1CPItem> targetItems = set();
		List<CPGoToLink> cpLinks = llist();
		//find all items where the given symbol follows and add them shifted
		for (LALR1CPItem source : items)
		{
			if (source.getNextSymbol() == symbol)
			{
				LALR1CPItem target = source.shift();
				targetItems.add(target);
				//leave the target state null for now, will be set later
				cpLinks.add(new CPGoToLink(source, null, target.getLR0Item()));
			}
		}
		LALR1CPState targetState = new LALR1CPState(targetItems);
		return t(targetState, cpLinks);
	}
	
	
	/**
	 * Gets the LALR1CPItem which belongs to the given LR(0) kernel.
	 */
	public LALR1CPItem getItemWithLookaheadByLR0Item(LR0Item stripped)
	{
            LALR1CPItem ret = stripped2Full.get(stripped);
            if (ret==null) throw new IllegalArgumentException("Unknown kernel " + stripped);
            return ret;
	}
	
	
	public Set<LR0Item> getStrippedItems()
	{
		return stripped2Full.keySet();
	}
	
	
	/**
	 * Returns true, if the given object is a {@link LALR1CPState} and if
	 * it has exactly the same LR(0) items.
	 */
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof LALR1CPState)
		{
			LALR1CPState s = (LALR1CPState) obj;
			return s.stripped2Full.keySet().equals(stripped2Full.keySet());
		}
		return false;
	}
	
	
	@Override public int hashCode()
	{
		return hashCode;
	}
	
	
	
	
}
