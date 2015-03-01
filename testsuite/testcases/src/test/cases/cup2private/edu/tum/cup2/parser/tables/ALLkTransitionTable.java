package edu.tum.cup2.parser.tables;

import edu.tum.cup2.generator.exceptions.LLkGeneratorException;
import edu.tum.cup2.generator.items.LLkItem;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.grammar.Symbol;


/**
 * Base class that contains some convenience methods for {@link LLkTransitionTable} and {@link LLkParsingTable}
 * 
 * @author Gero
 * 
 */
public abstract class ALLkTransitionTable
{
	/**
	 * Get the {@link LLkParserState} associated with the given state and symbol. Returns <code>null</code> if no
	 * appropritate rule is present!
	 * 
	 * @param from The state...
	 * @param symbol ... the symbol, and...
	 * @param lookahead ... the lookahead this method should search the {@link LLkParserState} for
	 * @return The {@link LLkParserState} associated with the given state and symbol. If theres none, <code>null</code>
	 *         is returned!
	 */
	public abstract LLkState getWithNull(LLkState from, Symbol symbol, ITerminalSeq lookahead);
	
	
	/**
	 * Get the {@link LLkState} associated with the given state and symbol. Returns
	 * 
	 * @param from The state, ...
	 * @param symbol ... the symbol, and...
	 * @param lookahead ... the lookahead this method should search the {@link LLkState} for
	 * @return The {@link LLkState} associated with the given state and symbol. If theres none, an error state is
	 *         returned!
	 */
	public abstract LLkState get(LLkState from, Symbol symbol, ITerminalSeq lookahead);
	
	
	/**
	 * Set a new transition rule in thhis table, consisting of:
	 * 
	 * @param from A source {@link LLkState}
	 * @param symbol A symbol which is consumed by this rule
	 * @param to A target {@link LLkState}
	 * @throws LLkGeneratorException 
	 */
	public abstract void set(LLkState from, Symbol symbol, LLkState to) throws LLkGeneratorException;
	
	
	/**
	 * Convenience method, see {@link ALLkTransitionTable#set(LLkState, Symbol, LLkState)}
	 * 
	 * @param from0
	 * @param symbol
	 * @param to0
	 * @throws LLkGeneratorException 
	 */
	public void set(LLkItem from0, Symbol symbol, LLkItem to0) throws LLkGeneratorException
	{
		final LLkState fromState = new LLkState(from0);
		final LLkState toState = new LLkState(to0);
		set(fromState, symbol, toState);
	}
	
	
	/**
	 * Convenience method, see {@link ALLkTransitionTable#set(LLkState, Symbol, LLkState)}
	 * 
	 * @param from0
	 * @param from1
	 * @param symbol
	 * @param to0
	 * @throws LLkGeneratorException 
	 */
	public void set(LLkItem from0, LLkItem from1, Symbol symbol, LLkItem to0) throws LLkGeneratorException
	{
		final LLkState fromState = new LLkState(from1, from0);
		final LLkState toState = new LLkState(to0);
		set(fromState, symbol, toState);
	}
	
	
	/**
	 * Convenience method, see {@link ALLkTransitionTable#set(LLkState, Symbol, LLkState)}
	 * 
	 * @param from0
	 * @param symbol
	 * @param to0
	 * @param to1
	 * @throws LLkGeneratorException 
	 */
	public void set(LLkItem from0, Symbol symbol, LLkItem to0, LLkItem to1) throws LLkGeneratorException
	{
		final LLkState fromState = new LLkState(from0);
		final LLkState toState = new LLkState(to1, to0);
		set(fromState, symbol, toState);
	}
	
	
	/**
	 * Convenience method, see {@link ALLkTransitionTable#set(LLkState, Symbol, LLkState)}
	 * 
	 * @param from0
	 * @param from1
	 * @param symbol
	 * @param to0
	 * @param to1
	 * @throws LLkGeneratorException 
	 */
	public void set(LLkItem from0, LLkItem from1, Symbol symbol, LLkItem to0, LLkItem to1) throws LLkGeneratorException
	{
		final LLkState fromState = new LLkState(from1, from0);
		final LLkState toState = new LLkState(to1, to0);
		set(fromState, symbol, toState);
	}
}
