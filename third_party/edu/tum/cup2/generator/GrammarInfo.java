package edu.tum.cup2.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Terminal;


/**
 * This class contains a {@link Grammar} extended by more information,
 * like the set of nullable nonterminals and the FIRST sets.
 * 
 * These values are cached for performance reasons to speed up the
 * parser generation process.
 * 
 * @author Andreas Wenger
 */
public final class GrammarInfo
{
	
	private final Grammar grammar;
	
	private final NullableSet nullableSet;
	private final FirstSets firstSets;
	private final HashMap<NonTerminal, List<Production>> productionsStartingWith;
	private final Map<NonTerminal, List<Production>> constProductionsStartingWith;
	
	private final EfficientTerminalSet emptyTerminalsSet;
	
	
	/**
	 * Creates a {@link GrammarInfo} from the given {@link Grammar}.
	 */
	public GrammarInfo(Grammar grammar)
	{
		this.grammar = grammar;
		//compute additional information about the grammar
		this.nullableSet = new NullableSet(grammar.getProductions());
		this.firstSets = new FirstSets(grammar, this.nullableSet);
		this.productionsStartingWith = new HashMap<NonTerminal, List<Production>>();
		for (NonTerminal nonTerminal : grammar.getNonTerminals())
		{
			List<Production> list = new LinkedList<Production>();
			for (Production p : grammar.getProductions())
			{
				if (p.getLHS() == nonTerminal)
					list.add(p);
			}
			this.productionsStartingWith.put(nonTerminal, Collections.unmodifiableList(list));
		}
		this.constProductionsStartingWith = Collections.unmodifiableMap(this.productionsStartingWith);
		//empty set of terminals
		this.emptyTerminalsSet = new EfficientTerminalSet(grammar.getTerminals());
	}
	
	
	/**
	 * Gets the grammar.
	 */
	public Grammar getGrammar()
	{
		return grammar;
	}
	
	
	/**
	 * Gets all productions beginning with the given {@link NonTerminal}
	 */
	public List<Production> getProductionsFrom(NonTerminal lhs)
	{
		return productionsStartingWith.get(lhs);
	}
	
	
	/**
	 * Gets all productions beginning with the given {@link NonTerminal}
	 */
	public Map<NonTerminal, List<Production>> getProductionsFrom()
	{
		return constProductionsStartingWith;
	}


	/**
	 * Gets the set of all nullable non-terminals, that means the
	 * non-terminals that can derive the empty string.
	 */
	public NullableSet getNullableSet()
	{
		return nullableSet;
	}


	/**
	 * Gets the sets of terminals, one for each symbol, that can be
	 * the first terminal when deriving from those symbol.
	 */
	public FirstSets getFirstSets()
	{
		return firstSets;
	}
	
	
	/**
	 * Gets an empty set of terminals.
	 */
	public EfficientTerminalSet getEmptyTerminalSet()
	{
		return emptyTerminalsSet;
	}
	
	
	/**
	 * Gets a set of terminals that contains only the given terminal.
	 */
	public EfficientTerminalSet getTerminalSet(Terminal terminal)
	{
		return emptyTerminalsSet.plus(terminal);
	}
	

}
