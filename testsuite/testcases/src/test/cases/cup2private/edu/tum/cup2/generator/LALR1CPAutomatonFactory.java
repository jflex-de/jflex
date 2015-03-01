package edu.tum.cup2.generator;

import static edu.tum.cup2.generator.Edge.createAcceptEdge;
import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;
import static edu.tum.cup2.grammar.SpecialTerminals.Placeholder;
import static edu.tum.cup2.util.CollectionTools.map;
import static edu.tum.cup2.util.CollectionTools.set;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.CPGoToLink;
import edu.tum.cup2.generator.items.LALR1CPItem;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.states.LALR1CPState;
import edu.tum.cup2.generator.states.LR1State;
import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.Tuple2;


/**
 * Factory for a LALR(1) automaton, that works without side effects
 * (unlike {@link LALR1CPAutomatonFactory}).
 * 
 * @author Andreas Wenger
 * @author Daniel Altmann
 * @author Michael Hausmann
 */
public class LALR1CPAutomatonFactory extends AutomatonFactory<LR1Item, LR1State>
{

	
	/**
	 * Create an LALR(1)-Automaton.
	 */
	public Automaton<LR1Item, LR1State> createAutomaton(LRGenerator<LR1Item, LR1State> generator, 
		GrammarInfo grammarInfo)
		throws GeneratorException
	{

		this.generator = generator;
		this.grammarInfo = grammarInfo;
		initCreation();
		
//		long time0 = System.currentTimeMillis();
		
		//create the start state (start production with dot at position 0 and "Placeholder" as lookahead)
		LR0Item startStateKernelItem = this.queue.remove(0).getLR0Kernel().getFirstItem();
		Set<LR0Item> startStateItemKernel = set();
		startStateItemKernel.add(startStateKernelItem);
		Set<LALR1CPItem> startStateItem = set();
		startStateItem.add(new LALR1CPItem(startStateKernelItem, grammarInfo.getTerminalSet(Placeholder)));
		LALR1CPState startStateKernel = new LALR1CPState(
			startStateItem);
		
		//hashmap which matches a kernel of a state
		//to its complete (with closure) state
		Map<LALR1CPState, LALR1CPState> kernel2closure = map();
		
		//collected go-to context propagation links
		//(key: source item, value contains target state *closure*)
		Map<LALR1CPItem, CPGoToLink> goToLinks = map();
		
		//which items belong to which state (closure)
		Map<LALR1CPItem, LALR1CPState> itemStates = map();
		
		//set of edges
		Set<Edge> lr0Edges = set();
		
		//TODO: own function
		{
			
			//initialize queue, which consists of states
			Set<LALR1CPState> queue = set();
			queue.add(startStateKernel);
			kernel2closure.put(startStateKernel, startStateKernel.closure(grammarInfo));
			
			//for all states, find their edges to other (possibly new) states
			while (!queue.isEmpty())
				//Appel says: "until E and T did not change in this iteration".
				//but: do we really need E here? I ignored it
			{
				//handle next state in queue
				LALR1CPState stateKernel = queue.iterator().next();
				queue.remove(stateKernel);
			
				//debug messages
				printDebugMessages();
				
				//first, create a LALR(1)-with-CP-Links automaton and remember the closures
				//for performance reasons
				LALR1CPState state = kernel2closure.get(stateKernel);
				Set<Symbol> shiftedSymbols = new HashSet<Symbol>();
				for (LALR1CPItem item : state.getItems())
				{
					//remember the state this item belongs to
					itemStates.put(item, state);
					//try to shift
					if (item.isShiftable())
					{
						Symbol symbol = item.getNextSymbol();
						if (symbol == EndOfInputStream)
						{
							//$-symbol: here we accept
							lr0Edges.add(createAcceptEdge(stateKernel, symbol)); //GOON: with or without closure?
						}
						else if (shiftedSymbols.add(symbol)) //shift each symbol only once
						{
							//terminal or non-terminal
							
							//shift to other state
							Tuple2<LALR1CPState, List<CPGoToLink>> s = state.goToCP(symbol);
							LALR1CPState shiftedStateKernel = s.get1();
							List<CPGoToLink> shiftedStateCPLinks = s.get2();
							
							//we try to find out if there is already some state which has an equal
							//kernel to the shifted state (LALR1CPState equals on kernel)
							LALR1CPState equalStateLALR1CP = kernel2closure.get(shiftedStateKernel);
							LALR1CPState gotoLinkTargetState = equalStateLALR1CP;
							if (equalStateLALR1CP == null)
							{
								//add new state
								LALR1CPState shiftedState = shiftedStateKernel.closure(grammarInfo);
								kernel2closure.put(shiftedStateKernel, shiftedState);
								queue.add(shiftedStateKernel);
								gotoLinkTargetState = shiftedState;
								
							}	
							
							//remember CP links (cp link contains closure of target state, not only kernel)
							for (CPGoToLink link : shiftedStateCPLinks)
							{
								LALR1CPItem todoItem = link.getSource();
								if (goToLinks.containsKey(todoItem))
									throw new RuntimeException("Double gotoLink!");
								goToLinks.put(todoItem, link.withTargetState(gotoLinkTargetState));
							}
							
							//add edge
							lr0Edges.add(new Edge(stateKernel, symbol, shiftedStateKernel, item.getLR0Item()));
						}
					}
				}
			}
		}
		
		Map<LALR1CPItem, EfficientTerminalSet> lookaheads = map();
		//TODO: own function
		{
			//now, since we have built the LALR(1)-CP automaton, we compute
			//all lookaheads by just following the CP links. Therefore, we just save the lookaheads
			//for each LALR1CPItem in a hashmap

			
			//initialize queue (consisting of kernels) with the start item kernel
			Set<LALR1CPItem> queue = set();
			LALR1CPState st = kernel2closure.get(startStateKernel);
			LALR1CPItem firstItem = st.getItemWithLookaheadByLR0Item(startStateKernelItem);
			queue.add(firstItem);
			lookaheads.put(firstItem, firstItem.getLookaheads());

			EfficientTerminalSet empty = firstItem.getLookaheads().empty();
			for (LALR1CPState sta: kernel2closure.values()){
				for (LALR1CPItem ite : sta.getItems()){
					if (ite.getPosition()==0) {
						queue.add(ite);
						lookaheads.put(ite,ite.getLookaheads());
					}
					else
						lookaheads.put(ite,empty);
				}
			}
			
			while (!queue.isEmpty())
			{
				LALR1CPItem item = queue.iterator().next();
				queue.remove(item);
				EfficientTerminalSet sourceItemLookaheads = lookaheads.get(item);
				
				//go-to-links: propagate lookaheads to all target items
				CPGoToLink gotoLink = goToLinks.get(item);
				if (gotoLink != null)
				{
					LALR1CPState targetState = gotoLink.getTargetState();
					LALR1CPItem targetItem = targetState.getItemWithLookaheadByLR0Item(gotoLink.getTargetItem());
					//add lookaheads to target item
					//if new lookaheads were found, add target item to the queue
					EfficientTerminalSet before = lookaheads.get(targetItem);
					EfficientTerminalSet after = before.plusAll(sourceItemLookaheads);
					if (!before.equals(after))
					{
						lookaheads.put(targetItem, after);
						queue.add(targetItem);
					}
				}
				
				//closure-links
				for (LR0Item closureLink : item.getClosureLinks())
				{
					LALR1CPState targetState = itemStates.get(item); //same state as current item
					LALR1CPItem targetItem = targetState.getItemWithLookaheadByLR0Item(closureLink);
					//add lookaheads to target item
					//if new lookaheads were found, add target item to the queue
					EfficientTerminalSet before = lookaheads.get(targetItem);
					EfficientTerminalSet after = before.plusAll(sourceItemLookaheads);
					after=after.plusAll(targetItem.getLookaheads());
					if (!before.equals(after))
					{
						lookaheads.put(targetItem, after);
						queue.add(targetItem);
					}
				}
				
			}
		}
		
		//create states and edges from collected information
		Map<LALR1CPState, LR1State> lalr1CPToLR1Map = map(); 
		for (LALR1CPState state : kernel2closure.keySet())
		{
			HashSet<LR1Item> lr1Items = new HashSet<LR1Item>();
			LALR1CPState stateWithClosure = kernel2closure.get(state);
			
			for (LR0Item strippedItem : state.getStrippedItems()){
				LALR1CPItem item = stateWithClosure.getItemWithLookaheadByLR0Item(strippedItem);
				EfficientTerminalSet terminals = lookaheads.get(item);
				lr1Items.add(new LR1Item(strippedItem, terminals));
			}
			LR1State lr1State = new LR1State(lr1Items);
			lalr1CPToLR1Map.put(state, lr1State);
			dfaStates.add(lr1State);
		}
		
		//fill dfaEdges
		for (Edge edge : lr0Edges)
		{
			this.dfaEdges.add(new Edge(lalr1CPToLR1Map.get(edge.getSrc()),
				edge.getSymbol(), lalr1CPToLR1Map.get(edge.getDest()), edge.getSrcItem()));
		}
		
		printDebugResult();
		
		return ret;
	}
	

}
