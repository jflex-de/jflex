package edu.tum.cup2.parser.tables;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.tum.cup2.generator.exceptions.LLkGeneratorException;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.generator.terminals.ITerminalSeqSet;
import edu.tum.cup2.generator.terminals.TerminalSeqSetf;
import edu.tum.cup2.generator.terminals.TerminalSeqf;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.parser.states.LLkErrorState;


/**
 * This is a sub-table of the {@link LLkParsingTable} for one kind of possbile transitions - expand, shift or reduce.
 * Here are the {@link LLkTransition}s stored, accessible by {@link LLkState}, current {@link Symbol} and
 * lookahead of length k.<br/>
 * Requests to this table are handled in three stages. The ordering is:<br/>
 * {@link LLkState} -> {@link Symbol} -> Lookahead ({@link ITerminalSeqSet}) -> {@link LLkTransition}
 * 
 * @see ALLkTransitionTable
 * 
 * @author Gero
 * 
 */
public class LLkTransitionTable extends ALLkTransitionTable implements Serializable
{
	private static final long serialVersionUID = -9090843052314048545L;
	
	private final Grammar grammar;
	private final int k;
	
	// TODO Gero: Evaluate usage of a different structure... maybe merge from and symbol???
	/** The core of this table, 173 characters of field definition: Madness? This is SPAR..JAVA! */
	private final Map<LLkState, Map<Symbol, TerminalTreeMap<LLkTransition>>> transitionsTable = new HashMap<LLkState, Map<Symbol, TerminalTreeMap<LLkTransition>>>();
	
	
	/**
	 * @param k Size of lookahead
	 */
	public LLkTransitionTable(Grammar grammar, int k)
	{
		this.grammar = grammar;
		this.k = k;
	}
	
	
	@Override
	public void set(LLkState from, Symbol symbol, LLkState to) throws LLkGeneratorException
	{
		// 1. Use map state->symbols
		final Map<Symbol, TerminalTreeMap<LLkTransition>> symbolMap = getMapFromMap(transitionsTable, from);
		
		
		// 2. Use map symbol->lookaheads
		TerminalTreeMap<LLkTransition> lookaheadMap = symbolMap.get(symbol);
		if (lookaheadMap == null)
		{
			lookaheadMap = new TerminalTreeMap<LLkTransition>(grammar);
			symbolMap.put(symbol, lookaheadMap);
		}
		
		
		// 3. Store new assignment lookahead->LLkTransition
		final ITerminalSeqSet sealedSet;
		if (to.getLookahead().size() > 0)
		{
			sealedSet = to.getLookahead().seal(); // guarantuees that a immutable, caching instance is used
		} else
		{
			// If empty lookahead, store as only alternative
			sealedSet = new TerminalSeqSetf(new TerminalSeqf());
		}
		
		// Sanity check: is lookahead longer then allowed?
		if (sealedSet.getMaxLength() > k)
		{
			throw new LLkGeneratorException("The lookahead of item '" + from + "' is longer then its supposed to! ("
					+ sealedSet.getMaxLength() + " > " + k + " k)");
		}
		
		final LLkTransition newTransition = new LLkTransition(from, symbol, to);
		for (ITerminalSeq seq : sealedSet)
		{
			if (!lookaheadMap.put(seq, newTransition))
			{
				throw new LLkGeneratorException("Given combination of LLkItems, symbol '" + symbol + "' and lookahead '"
						+ sealedSet + "' is not unique!!!");
			}
		}
	}
	
	
	private <K1, K2, V2> Map<K2, V2> getMapFromMap(Map<K1, Map<K2, V2>> map, K1 key)
	{
		Map<K2, V2> result = map.get(key);
		if (result == null)
		{
			result = new HashMap<K2, V2>();
			map.put(key, result);
		}
		return result;
	}
	
	
	@Override
	public LLkState getWithNull(LLkState from, Symbol symbol, ITerminalSeq lookahead)
	{
		// 1. Use map state->symbols
		final Map<Symbol, TerminalTreeMap<LLkTransition>> symbolMap = transitionsTable.get(from);
		if (symbolMap == null || symbolMap.isEmpty())
		{
			return null;
		}
		
		// 2. Use map symbol->lookaheads
		final TerminalTreeMap<LLkTransition> lookaheadMap = symbolMap.get(symbol);
		if (lookaheadMap == null || lookaheadMap.isEmpty())
		{
			return null;
		}
		
		// 3. Check for existing transition in map lookahead->transition
		LLkTransition transition = lookaheadMap.getValue(lookahead);
		if (transition == null)
		{
			return null;
		}
		return transition.getTo();
	}
	
	
	@Override
	public LLkState get(LLkState from, Symbol symbol, ITerminalSeq lookahead)
	{
		// 1. Use map state->symbols
		final Map<Symbol, TerminalTreeMap<LLkTransition>> symbolMap = transitionsTable.get(from);
		if (symbolMap == null || symbolMap.isEmpty())
		{
			return new LLkErrorState("No transition from this state '" + from + "'!");
		}
		
		// 2. Use map symbol->lookaheads
		final TerminalTreeMap<LLkTransition> lookaheadMap = symbolMap.get(symbol);
		if (lookaheadMap == null || lookaheadMap.isEmpty())
		{
			return new LLkErrorState("No transition from this state '" + from + "' with this symbol '" + symbol + "'!");
		}
		
		// 3. Check for existing transition in map lookahead->transition
		LLkTransition transition = lookaheadMap.getValue(lookahead);
		if (transition == null)
		{
			return new LLkErrorState("No transition from this state '" + from + "' with this symbol '" + symbol
					+ "' and lookahead '" + lookahead + "'!");
		}
		return transition.getTo();
	}
}
