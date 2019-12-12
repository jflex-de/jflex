package edu.tum.cup2.parser.tables;

import java.io.Serializable;

import edu.tum.cup2.generator.exceptions.LLkGeneratorException;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Symbol;


/**
 * Instead of implementing a big table with all transitions in it the {@link LLkParsingTable} consists of three
 * sub-tables ({@link LLkTransitionTable}) for the expand, shift and reduce actions. This segmentation reduces the
 * search costs enormously (at least
 * it should :-P)
 * 
 * @author Gero
 */
public class LLkParsingTable extends ALLkTransitionTable implements Serializable
{
	private static final long serialVersionUID = -3154164637457469125L;
	
	private final int k;
	
	private final LLkTransitionTable expandTable;
	private final LLkTransitionTable shiftTable;
	private final LLkTransitionTable reduceTable;
	
	private LLkState startState = null;
	private LLkState endState = null;
	
	
	public LLkParsingTable(Grammar grammar, int k)
	{
		this.k = k;
		
		expandTable = new LLkTransitionTable(grammar, k);
		shiftTable = new LLkTransitionTable(grammar, k);
		reduceTable = new LLkTransitionTable(grammar, k);
	}
	
	
	/**
	 * @return the k
	 */
	public int getK()
	{
		return k;
	}
	
	
	@Override
	public void set(LLkState from, Symbol symbol, LLkState to) throws LLkGeneratorException
	{
		getTableFor(from).set(from, symbol, to);
		return;
	}
	
	
	@Override
	public LLkState get(LLkState from, Symbol symbol, ITerminalSeq lookahead)
	{
		return getTableFor(from).get(from, symbol, lookahead);
	}
	
	
	@Override
	public LLkState getWithNull(LLkState from, Symbol symbol, ITerminalSeq lookahead)
	{
		return getTableFor(from).getWithNull(from, symbol, lookahead);
	}
	
	
	/**
	 * Accept-action is in the Reduce-table!!!
	 * 
	 * @param from
	 * @return Returns the proper {@link LLkTransitionTable} for the given state
	 */
	private LLkTransitionTable getTableFor(LLkState from)
	{
		// Which table to choose?
		if (!from.isTopItemComplete())
		{
			// Push/Expand or Shift/Consume...
			if (from.getFirstItem().getNextSymbol() instanceof NonTerminal)
			{
				// -> Push/Expand!
				return expandTable;
			} else
			{
				// -> Shift/Consume!
				return shiftTable;
			}
		} else
		{
			// -> Reduce (and Accept!)
			return reduceTable;
		}
	}
	
	
	public void setStartState(LLkState startState)
	{
		this.startState = startState;
	}
	
	
	public LLkState getStartState()
	{
		return startState;
	}
	
	
	public void setEndState(LLkState endState)
	{
		this.endState = endState;
	}
	
	
	public LLkState getEndState()
	{
		return endState;
	}
}
