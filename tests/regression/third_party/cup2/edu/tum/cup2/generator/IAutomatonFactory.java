package edu.tum.cup2.generator;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.states.LRState;

/**
 * IAutomatonFactory is an interface for a factory for an automaton that creates
 * an automaton (used in an LRGenerator)
 * 
 * @author Michael Hausmann
 * 
 */
public interface IAutomatonFactory<I extends Item, S extends LRState<I>>
{
	/**
	 * Method for creating an Automaton - to be overridden in subclasses
	 * @throws GeneratorException 
	 */
	public Automaton<I, S> createAutomaton ( 
			LRGenerator<I, S> generator, 
			GrammarInfo grammarInfo) 
		throws GeneratorException;
	
}
