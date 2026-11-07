package edu.tum.cup2.generator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.tum.cup2.generator.NFA.Edge;
import edu.tum.cup2.generator.exceptions.LLkGeneratorException;
import edu.tum.cup2.generator.items.LLkItem;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.generator.states.LR0State;
import edu.tum.cup2.generator.terminals.ITerminalSeqSet;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.parser.tables.LLkParsingTable;
import edu.tum.cup2.precedences.Precedences;
import edu.tum.cup2.util.It;


/**
 * This class implements a {@link Generator} for LL(k) {@link Automaton}s and {@link LLkParsingTable}s. Both are
 * generated on construction.
 * 
 * @author Gero
 * 
 */
public class LLkGenerator extends Generator
{
	protected final GrammarInfo grammarInfo;
	protected final int k;
	
	protected final NFA<LLkItem, LLkState> automaton;
	protected final LLkParsingTable table;
	
	
	/**
	 * @param grammar
	 * @param precedences
	 * @param verbosity
	 * @param k Length of lookahead
	 * @throws LLkGeneratorException
	 */
	public LLkGenerator(Grammar grammar, Precedences precedences, Verbosity verbosity, int k)
			throws LLkGeneratorException
	{
		super(grammar.extendByAuxStartProduction(), precedences, verbosity);
		
		// Additional infos
		this.grammarInfo = new GrammarInfo(grammar);
		this.k = k;
		
		
		// Create pushdown automaton
		this.automaton = createAutomaton();
		
		
		// Construct parsing table
		this.table = new LLkParsingTable(grammar, k);
		this.table.setStartState(this.automaton.getStartState());
		if (automaton.getEndStates().size() != 1)
		{
			System.out.println("Error: Created automaton should contain exactlly one end state!");
		} else
		{
			this.table.setEndState(automaton.getEndStates().iterator().next());
		}
		
		// Simply add all edges as transitions to the table. Lookaheads are handled by the parser
		for (Edge<LLkState> edge : automaton.getEdges())
		{
			this.table.set(edge.getSrc(), edge.getSymbol(), edge.getDest());
		}
	}
	
	
	/**
	 * Constructs LL(k) pushdown automaton
	 * @throws LLkGeneratorException
	 */
	private NFA<LLkItem, LLkState> createAutomaton() throws LLkGeneratorException
	{
		// This uses LR0Items/-State as lookup-keys to avoid unnecessary lookahead construction for LLkItems
		// Stuff we gonna need:
		final LookaheadGenerator laGen = new LookaheadGenerator(grammarInfo);
		
		// Start with start... :-P
		final Production startProduction = grammar.getStartProduction();
		final LR0Item startItem = new LR0Item(startProduction, 0);
		final LR0State startState = new LR0State(startItem);
		
		// Create automaton instance
		final ITerminalSeqSet startLookahead = laGen.calcLookahead(startItem, k);
		final LLkItem llkStartItem = new LLkItem(startProduction, 0, startLookahead);
		final LLkState llkStartState = new LLkState(llkStartItem);
		final NFA<LLkItem, LLkState> automaton = new NFA<LLkItem, LLkState>(llkStartState);
		
		
		// Prepare creation
		final Queue<LR0State> todo = new LinkedList<LR0State>();
		todo.add(startState);
		final Map<LR0Item, LLkItem> createdItems = new HashMap<LR0Item, LLkItem>();
		createdItems.put(startItem, llkStartItem);
		final Map<LR0State, LLkState> createdStates = new HashMap<LR0State, LLkState>();
		createdStates.put(startState, llkStartState);
		
		// Walk all items of the grammar and construct the needed edges and state for the automaton
		while (!todo.isEmpty())
		{
			final LR0State currentState = todo.poll();
			
			// What to do with the current state?
			final LR0Item currentItem = currentState.getFirstItem();
			if (currentItem.isShiftable())
			{
				// ### Push/Expand, Shift/Consume or Accept...
				final Symbol nextSymbol = currentItem.getNextSymbol();
				
				// Accept?
				if (nextSymbol == SpecialTerminals.EndOfInputStream)
				{
					// This is the end state, we are done here
				} else
				{
					// ### Push/Expand or Shift/Consume...
					if (nextSymbol instanceof NonTerminal)
					{
						// => Push/Expand!
						final NonTerminal nonTerminal = (NonTerminal) nextSymbol;
						
						final LR0State preExpandState = new LR0State(currentItem);
						final LLkState fromState = createState(preExpandState, createdStates, createdItems, laGen, k, todo);
						
						for (Production nextProduction : grammarInfo.getProductionsFrom(nonTerminal))
						{
							// PUSH the new item on top of the current top item
							final LR0Item newTopItem = new LR0Item(nextProduction, 0);
							final LR0State postExpandedState = new LR0State(newTopItem, currentItem);
							
							final LLkState toState = createState(postExpandedState, createdStates, createdItems, laGen, k,
									todo, false); // We don't want to check the exact created state as it is uninteresting
							automaton.addEdge(fromState, SpecialTerminals.Epsilon, toState);
							
							if (newTopItem.isComplete())
							{
								// Okay, we ARE interested in the expanded state as it already is complete and we got to reduce
								// it eventually..
								todo.add(postExpandedState);
							} else
							{
								// We are interested in two things:
								// 1. What happens with our fresh expanded state when it is complete...? This we will have to
								// reduce eventually..
								final LR0Item topItemShifted = newTopItem.complete();
								final LR0State interestingState = new LR0State(topItemShifted, currentItem);
								// Create and adde to todo
								createState(interestingState, createdStates, createdItems, laGen, k, todo);
								
								// 2. And what to do when we come across our newly create top item?
								final LR0State newTopState = new LR0State(newTopItem);
								createState(newTopState, createdStates, createdItems, laGen, k, todo);
							}
						}
					} else
					{
						// => Shift/Consume!
						final LR0State preShiftState = new LR0State(currentItem);
						final LR0Item newShiftedItem = currentItem.shift();
						final LR0State postShiftState = new LR0State(newShiftedItem);
						
						final LLkState fromState = createState(preShiftState, createdStates, createdItems, laGen, k, todo);
						final LLkState toState = createState(postShiftState, createdStates, createdItems, laGen, k, todo,
								!newShiftedItem.isComplete());
						automaton.addEdge(fromState, nextSymbol, toState);
					}
				}
			} else
			{
				// => Reduce
				final LLkState fromState = createState(currentState, createdStates, createdItems, laGen, k, todo);
				
				// Pop top most state and shift second top most
				final int itemsCount = currentState.getItemsCount();
				
				final It<LR0Item> it = currentState.getItems();
				final LR0Item reducedItem;
				final boolean completed;
				if (itemsCount == 2)
				{
					it.next(); // 1.
					reducedItem = it.next().shift(); // 2.
					completed = false;
				} else if (itemsCount == 1)
				{
					reducedItem = it.next();
					completed = true;
				} else
				{
					throw new RuntimeException("LLkState to REDUCE consists not of 2 items!!!");
				}
				final LR0State newReducedState = new LR0State(reducedItem);
				
				if (reducedItem.getNextSymbol() == SpecialTerminals.EndOfInputStream)
				{
					// This means we completed the auxiliary start production => ACCEPT
					final LLkState endState = createState(newReducedState, createdStates, createdItems, laGen, k, todo,
							false); // We're not interested in the end state
					automaton.addAcceptingEdge(fromState, SpecialTerminals.Epsilon, endState);
				} else
				{
					final LLkState toState = createState(newReducedState, createdStates, createdItems, laGen, k, todo,
							!completed);
					automaton.addEdge(fromState, SpecialTerminals.Epsilon, toState);
				}
			}
		}
		
		return automaton;
	}
	
	
	private static LLkState createState(LR0State kernel, Map<LR0State, LLkState> createdStates,
			Map<LR0Item, LLkItem> createdItems, LookaheadGenerator laGen, int laLength, Queue<LR0State> todo)
			throws LLkGeneratorException
	{
		return createState(kernel, createdStates, createdItems, laGen, laLength, todo, true);
	}
	
	
	/**
	 * This methods checks if the {@link LLkState} for the given {@link LR0State} already exists. If yes, it is returned.
	 * It not, it is created (while looking for existing items in the same manner) and added to the given todo-set for
	 * further inspection.
	 * 
	 * @param kernel The {@link LR0State} for which a {@link LLkState} should be created
	 * @param createdStates Already existing {@link LLkState}s
	 * @param createdItems Already existing {@link LLkItem}s
	 * @param laGen {@link LookaheadGenerator} in case we have to create new {@link LLkItem}s
	 * @param laLength Length of the lookahead for new items
	 * @param todo A set of newly created {@link LLkState}s
	 * @param addToTodo
	 * @return The corresponding {@link LLkState} for the given {@link LR0State}
	 * @throws LLkGeneratorException
	 */
	private static LLkState createState(LR0State kernel, Map<LR0State, LLkState> createdStates,
			Map<LR0Item, LLkItem> createdItems, LookaheadGenerator laGen, int laLength, Queue<LR0State> todo,
			boolean addToTodo) throws LLkGeneratorException
	{
		LLkState result = createdStates.get(kernel);
		// Check if state already exists
		if (result == null)
		{
			// Okay, first checks for needed items
			final List<LLkItem> items = new LinkedList<LLkItem>();
			for (LR0Item baseItem : kernel.getItemsAsCollection())
			{
				LLkItem item = createdItems.get(baseItem);
				if (item == null)
				{
					// New, need to create the item
					final ITerminalSeqSet lookahead = laGen.calcLookahead(baseItem, laLength);
					item = new LLkItem(baseItem, lookahead);
					createdItems.put(baseItem, item);
				}
				items.add(item);
			}
			
			// Now that we have all items, build LLkState
			result = new LLkState(items);
			createdStates.put(kernel, result);
			
			// And finally: If wanted, add it to the "new" queue
			if (addToTodo)
			{
				todo.add(kernel);
			}
		}
		
		return result;
	}
	
	
	public NFA<LLkItem, LLkState> getAutomaton()
	{
		return automaton;
	}
	
	
	public LLkParsingTable getParsingTable()
	{
		return table;
	}
}
