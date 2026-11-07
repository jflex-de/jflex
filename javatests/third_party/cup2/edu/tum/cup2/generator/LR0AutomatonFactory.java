package edu.tum.cup2.generator;

import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.*;
import edu.tum.cup2.generator.states.*;
import edu.tum.cup2.grammar.Symbol;

/**
 * AutomatonFactory is a Factory for an Automaton that creates
 * an LR1Automaton
 * 
 * @author Daniel Altmann
 * @author Michael Hausmann
 * 
 */
public class LR0AutomatonFactory extends AutomatonFactory<LR0Item, LR0State>
{
	public Automaton<LR0Item, LR0State> createAutomaton(
				LRGenerator<LR0Item, LR0State> generator, 
				GrammarInfo grammarInfo)
	throws GeneratorException
	{
		this.generator = generator;
		this.grammarInfo = grammarInfo;
		initCreation();
		
		//for all states, find their edges to other (possibly new) states
		while (!queue.isEmpty())
			//Appel says: "until E and T did not change in this iteration".
			//but: do we really need E here? I ignored it
		{
			//handle next state in queue
			stateKernel = queue.remove(0);
		
			//debug messages
			printDebugMessages();
			state = stateKernel.closure(grammarInfo); //unpack state (from kernel to closure)
			
			for (LR0Item item : state.getItems())
			{
				
				if (item.isShiftable())
				{
					Symbol symbol = item.getNextSymbol();
					if (symbol == EndOfInputStream)
					{
						//$-symbol: here we accept
						dfaEdges.add(Edge.createAcceptEdge(stateKernel, symbol));
					}
					else
					{
						//terminal or non-terminal
						LR0State shiftedState = (LR0State) state.goTo(symbol);
						//new state?
						if (!dfaStates.contains(shiftedState))
						{
							dfaStates.add(shiftedState);
							queue.add(shiftedState);

						} 
						//add the edge
						dfaEdges.add(new Edge(stateKernel, symbol, shiftedState, item));
						
					} /*end else*/
				} /*end if*/
			} /*end for*/
		} /*end while*/
		
		//TODO
		
		printDebugResult();
		
		return ret;
	}

} /*end of AutomatonFactory*/
