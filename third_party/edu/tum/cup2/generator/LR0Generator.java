package edu.tum.cup2.generator;

import static edu.tum.cup2.util.ArrayTools.toHashSet;
import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.states.LR0State;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRActionTable;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.spec.CUP2Specification;


/**
 * LR(0) parser generator.
 * 
 * Given a {@link Grammar}, this class computes the {@link LRParsingTable}
 * that is needed for a {@link LRParser} to parse an input stream.
 * 
 * @author Andreas Wenger
 */
public class LR0Generator
	extends LRGenerator<LR0Item, LR0State>
{
	
	
	/**
	 * Computes a {@link LRParsingTable} for the given LR(0) grammar.
	 * The given verbosity tells the generator how many debug messages are requested.
	 */
	public LR0Generator(CUP2Specification spec, Verbosity verbosity)
		throws GeneratorException
	{
		super(spec, verbosity, true);
	}
	
	
	/**
	 * Computes a {@link LRParsingTable} for the given LR(0) grammar.
	 */
	public LR0Generator(CUP2Specification spec)
		throws GeneratorException
	{
		this(spec, Verbosity.None);
	}
	
	
	/**
	 * Creates and returns the start state.
	 * This is the item, which consists of the start production at position 0.
	 */
	@Override protected LR0State createStartState()
	{
		return new LR0State(
			toHashSet(new LR0Item(grammar.getStartProduction(), 0)));
	}
	
	
	/**
	 * Fills the given {@link LRActionTable} with reduce actions, using the
	 * given non-shiftable item and its parent parser state.
	 */
	@Override protected void createReduceActions(LRActionTable actionTable, LR0Item item, LRParserState state)
		throws GeneratorException
	{
		//easy in LR(0): just reduce the production, for each existing terminal
		Reduce reduce = new Reduce(item.getProduction());
		setReduceAction(actionTable, reduce, state, SpecialTerminals.WholeRow);
	}

	
	/**
	 * Creates the DFA of the parser, consisting of states and edges
	 * (for shift and goto) connecting them.
	 */
	public Automaton<LR0Item, LR0State> createAutomaton() throws GeneratorException
	{
		return new LR0AutomatonFactory().createAutomaton(this, grammarInfo);
	}
	

}
