package edu.tum.cup2.grammar;

import static edu.tum.cup2.grammar.SpecialNonTerminals.StartLHS;
import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;

import java.io.Serializable;
import java.util.LinkedList;

import edu.tum.cup2.util.ArrayTools;

/**
 * A grammar consists of a list of {@link Production}s.
 * 
 * @author Andreas Wenger
 * @author Michael Hausmann
 */
public class Grammar implements IGrammar, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final LinkedList<Terminal> terminals;
	private final LinkedList<NonTerminal> nonTerminals;
	private final LinkedList<Production> productions;
	
	/**
	 * Creates a new {@link Grammar}, using the given list of
	 * {@link Production}s. The first production is the start
	 * production of the grammar. 
	 */
	public Grammar(LinkedList<Terminal> terminals, LinkedList<NonTerminal> nonTerminals, LinkedList<Production> productions)
	{
		this.terminals = terminals;
		this.nonTerminals = nonTerminals;
		this.productions = productions;
	}
	
	/**
	 * Creates a new {@link Grammar}, using the given list of
	 * {@link Production}s. The first production is the start
	 * production of the grammar. 
	 */
	public Grammar(Terminal[] terminals, NonTerminal[] nonTerminals, Production[] productions)
	{
		this(ArrayTools.toLinkedListT(terminals),
			ArrayTools.toLinkedListT(nonTerminals),
			ArrayTools.toLinkedListT(productions));
	}
	
	
	/**
	 * Returns a new grammar, that is the same as this grammar, but with an auxiliary
	 * "S' → S$" production, where S is the old start production, S' is the new one
	 * and $ is the EndOfInputStream terminal.
	 * If the grammar already contains a $ terminal, it is returned unmodified.
	 */
	public Grammar extendByAuxStartProduction()
	{
		//$ already there?
		if (terminals.contains(EndOfInputStream))
		{
			//yes. return unmodified grammar
			return this;
		}
		else
		{
			//no. add it to the terminals list
			LinkedList<Terminal> terminals = new LinkedList<Terminal>();
			terminals.addAll(this.terminals);
			terminals.add(EndOfInputStream);
			//add StartLHS to the non-terminals list
			LinkedList<NonTerminal> nonTerminals = new LinkedList<NonTerminal>();
			nonTerminals.addAll(this.nonTerminals);
			nonTerminals.add(StartLHS);
			//add start production to productions list
			Production startProduction = new Production(0, StartLHS, productions.getFirst().getLHS(), EndOfInputStream);
			LinkedList<Production> productions = new LinkedList<Production>();
			productions.add(startProduction);
			productions.addAll(this.productions);
			//create and return grammar
			return new Grammar(terminals, nonTerminals, productions);
		}
	}
	
	
	/**
	 * Gets the terminals.
	 */
	public LinkedList<Terminal> getTerminals()
	{
		return terminals;
	}

	
	/**
	 * Gets the non-terminals.
	 */
	public LinkedList<NonTerminal>  getNonTerminals()
	{
		return nonTerminals;
	}
	
	
	/**
	 * Gets the number of productions.
	 */
	public int getProductionCount()
	{
		return productions.size();
	}
	
	
	/**
	 * Gets the start production.
	 */
	public Production getStartProduction()
	{
		return productions.getFirst();
	}
	
	
	/**
	 * Gets the production with the given index.
	 */
	public Production getProductionAt(int index)
	{
		return productions.get(index);
	}
	
	
	/**
	 * Gets the productions. TIDY
	 */
	public LinkedList<Production> getProductions()
	{
		return productions;
	}

}
