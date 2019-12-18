package edu.tum.cup2.parser.tables;

import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.KTreeMap;


/**
 * This class realises the association between a set of {@link ITerminalSeq} (first/follow sets of a grammar) and a
 * generic parameter V
 * 
 * @author Gero
 * @param <V>
 */
public class TerminalTreeMap<V> extends KTreeMap<ITerminalSeq, Terminal, V>
{
	private static final TerminalEnumerator TERMINAL_ENUMERATOR = new TerminalEnumerator();
	
	
	/**
	 * @param pureTerminalsSize Number of grammars terminals w/o EndOfInputStream!
	 */
	public TerminalTreeMap(Grammar gr)
	{
		this(terminalsSize(gr));
	}
	
	
	/**
	 * @param terminalsSize (w/o {@link SpecialTerminals#EndOfInputStream}!!!)
	 */
	public TerminalTreeMap(int terminalsSize)
	{
		super(terminalsSize + 2, TERMINAL_ENUMERATOR);
	}
	
	
	private static int terminalsSize(Grammar gr)
	{
		if (gr.getTerminals().contains(SpecialTerminals.EndOfInputStream))
		{
			return gr.getTerminals().size() - 1;
		} else {
			return gr.getTerminals().size();
		}
	}
	
	
	private static class TerminalEnumerator implements IEnumerator<Terminal>
	{
		public int ordinal(Terminal keypart, int maxOrdinal)
		{
			if (keypart == SpecialTerminals.Epsilon)
			{
				return maxOrdinal;	// max = place for Epsilon
			} else if (keypart == SpecialTerminals.EndOfInputStream)
			{
				return maxOrdinal - 1;	// max - 1 = place for EndOfInputStream
			} else
			{
				return keypart.ordinal();
			}
		}
	}
}
