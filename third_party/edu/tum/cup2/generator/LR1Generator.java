package edu.tum.cup2.generator;

import static edu.tum.cup2.grammar.SpecialTerminals.Placeholder;
import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.states.LR1State;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRActionTable;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.util.ArrayTools;


/**
 * LR(1) parser generator.
 * 
 * Given a {@link Grammar}, this class computes the {@link LRParsingTable}
 * that is needed for a {@link LRParser} to parse an input stream.
 * 
 * @author Andreas Wenger
 */
public class LR1Generator
	extends LRGenerator<LR1Item, LR1State>
{
	/**
	 * Computes a {@link LRParsingTable} for the given LR(1) specification.
	 */
	public LR1Generator(CUP2Specification spec)
		throws GeneratorException
	{
		this(spec, Verbosity.None);
	}
	/**
	 * Computes a {@link LRParsingTable} for the given LR(1) specification.
	 */
	public LR1Generator(CUP2Specification spec, Verbosity verbosity)
		throws GeneratorException
	{
		super(spec, verbosity, true);
	}
	
	
	
	/**
	 * Computes a {@link LRParsingTable} for the given LR(1) specification.
	 * (which is only extended by an auxiliary start production if requested).
	 * The given verbosity tells the generator how many debug messages are requested.
	 */
	public LR1Generator(CUP2Specification spec, Verbosity verbosity, boolean extendGrammar)
		throws GeneratorException
	{
		super(spec, verbosity, extendGrammar);
	}
	
	
	/**
	 * Creates and returns the start state.
	 * This is the item, which consists of the start production at position 0.
	 */
	@Override protected LR1State createStartState()
	{
		return new LR1State(ArrayTools.toHashSet(new LR1Item(grammar.getStartProduction(), 0,
			grammarInfo.getTerminalSet(Placeholder))));
	}
	
	
	/**
	 * Fills the given {@link LRActionTable} with reduce actions, using the
	 * given non-shiftable item and its parent parser state.
	 */
	@Override protected void createReduceActions(LRActionTable actionTable, LR1Item item, LRParserState state)
		throws GeneratorException
	{
		//in LR(1), reduce the production for each lookahead terminal
		Reduce reduce = new Reduce(item.getProduction());
		for (Terminal lookahead : item.getLookaheads().getTerminals())
		{
			setReduceAction(actionTable, reduce, state, lookahead);
		}
	}
	
	/**
	 * Creates the DFA of the parser, consisting of states and edges
	 * (for shift and goto) connecting them.
	 */
	public Automaton<LR1Item, LR1State> createAutomaton() throws GeneratorException
	{
		return new LR1AutomatonFactory().createAutomaton(this, grammarInfo);
	}
	
}
