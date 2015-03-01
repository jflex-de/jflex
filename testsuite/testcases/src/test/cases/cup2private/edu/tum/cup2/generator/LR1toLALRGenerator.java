package edu.tum.cup2.generator;

import java.util.HashMap;
import java.util.LinkedList;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.exceptions.ReduceReduceConflict;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.states.LR1State;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.spec.CUP2Specification;


/**
 * LALR(1) parser generator, that is based on a LR(1) parser generator.
 * 
 * This parser is not efficient, but shows how a LALR(1) automaton
 * can be constructed given a complete LR(1) automaton.
 * 
 * @author Andreas Wenger
 */
public class LR1toLALRGenerator
	extends LR1Generator
{
	Automaton<LR1Item, LR1State> dfaLALR = null; //cache LALR1 automaton
	
	/**
	 * Computes a {@link LRParsingTable} for the given LALR(1) grammar.
	 * The given verbosity tells the generator how many debug messages are requested.
	 */
	public LR1toLALRGenerator(CUP2Specification spec, Verbosity verbosity)
		throws GeneratorException
	{
		super(spec, verbosity);
	}
	
	
	/**
	 * Computes a {@link LRParsingTable} for the given LALR(1) grammar.
	 */
	public LR1toLALRGenerator(CUP2Specification spec)
		throws GeneratorException
	{
		this(spec, Verbosity.None);
	}
	
	
	/**
	 * Creates the DFA of the parser, consisting of states and edges
	 * (for shift and goto) connecting them.
	 */
	@Override public Automaton<LR1Item, LR1State> createAutomaton()
		throws GeneratorException
	{
		//create LR(1) automaton
		Automaton<LR1Item, LR1State> dfaLR1 = super.createAutomaton();
		//list for LALR(1) states, which we will fill now
		LR1State startState = dfaLR1.getStartState();
		LinkedList<LR1State> statesLALR = new LinkedList<LR1State>();
		statesLALR.add(startState);
		//find all states that have the same kernel, that means, all
		//states that have equal items except the lookahead sets
		HashMap<LR1State, LR1State> equalStates = new HashMap<LR1State, LR1State>(); //<old state, equal state>
		for (LR1State state : dfaLR1.getStates())
		{
			if (state != startState) //the start state was already handled
			{
				//find equal state in already created LALR states
				boolean found = false;
				for (LR1State candidate : statesLALR)
				{
					if (state.equalsLR0(candidate)) //TODO: use hashmap with state.getLR0Kernel()-key instead
					{
						//equal state found. can be merged
						equalStates.put(state, candidate);
						found = true;
						break;
					}
				}
				//if no equal state was found, add it to the LALR states
				if (!found)
				{
					statesLALR.add(state);
				}
			}
		}
		//now we know the equal states and we can merge them.
		//begin with the start state
		LR1State startStateLALR = mergeStates(startState, equalStates);
		equalStates.put(startState, startStateLALR);
		statesLALR.removeFirst(); //remove start state from queue
		
		this.dfaLALR = new Automaton<LR1Item, LR1State>(startStateLALR);
		//merge the other states
		while (!statesLALR.isEmpty())
		{
			LR1State originalState = statesLALR.removeFirst();
			LR1State mergedState = mergeStates(originalState, equalStates);
			dfaLALR.getStates().add(mergedState);
			equalStates.put(originalState, mergedState);
		}
		//now merge the edges. this can lead to reduce-reduce conflicts,
		//see ASU example 4.44 for an example.
		LinkedList<Edge> edges = new LinkedList<Edge>();
		for (Edge edge : dfaLR1.getEdges())
		{
			Edge newEdge = new Edge(getLALRState((LR1State) edge.getSrc(), equalStates),
				edge.getSymbol(), getLALRState((LR1State) edge.getDest(), equalStates), edge.getSrcItem());
			boolean found = false;
			for (Edge oldEdge : edges)
			{
				if (newEdge.getSrc() == oldEdge.getSrc() && newEdge.getSymbol() == oldEdge.getSymbol())
				{
					//the two edges start from the same state using the same symbol. if they
					//have different destinations, we have a reduce-reduce conflict!
					if (newEdge.getDest() != oldEdge.getDest())
					{
						throw new ReduceReduceConflict("Reduce-Reduce-Konflikt"); //TODO
					}
					else
					{
						found = true;
						break;
					}
				}
			}
			//new edge? then add it
			if (!found)
			{
				edges.add(newEdge);
			}
		}
		dfaLALR.getEdges().addAll(edges);
		return dfaLALR;
	}
	
	
	/**
	 * Merges the given state with the states of the given map, which have
	 * the given state as the value. The mappings will be updated to the
	 * merged state.
	 */
	private LR1State mergeStates(LR1State state, HashMap<LR1State, LR1State> equalStates)
	{
		LR1State ret = state;
		//merge states
		for (LR1State s : equalStates.keySet())
		{
			if (equalStates.get(s) == state)
				ret = ret.merge(s);
		}
		//update mappings
		for (LR1State s : equalStates.keySet())
		{
			if (equalStates.get(s) == state)
				equalStates.put(s, ret);
		}
		return ret;
	}
	
	
	/**
	 * Gets the LALR state which was created from the given LR(1) state.
	 * The given map is used for lookup. If it does not contain the state,
	 * the given LR(1) state is returned.
	 */
	private LR1State getLALRState(LR1State state, HashMap<LR1State, LR1State> equalStates)
	{
		LR1State ret = equalStates.get(state);
		return (ret != null ? ret : state);
	}
	
	/**
	 * return LALR1 automaton for tests
	 */
	protected Automaton<LR1Item, LR1State> getLALRAutomaton()
	{
		return this.dfaLALR;
	}
}
